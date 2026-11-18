package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import model.Produto;

/**

 * Para rodar sem a tela (na pasta do projeto): java -cp build/classes controller.RelatorioController
 */
public class RelatorioController {

    private static final int[] TAMANHOS = {1000, 10000, 50000};
    private static final long LIMITE_NANOS = 120000000000L; // 2 minutos
    private static final int REPETICOES_BUSCA = 1000;
    private static final double NAO_CONCLUIDO = -1;

    // linhas da tabela de resultados
    private static final int SIMPLES = 0;
    private static final int EFICIENTE = 1;
    private static final int LINEAR = 2;
    private static final int BINARIA = 3;
    private static final int HASH = 4;
    private static final int CONSTRUCAO_HASH = 5;

    private final ArquivoController arquivoController = new ArquivoController();
    private final OrdenacaoController ordenacao = new OrdenacaoController();
    private final BuscaController busca = new BuscaController();

    public static void main(String[] args) throws IOException {
        new RelatorioController().gerar(new File("."), new File("relatorio.txt"), null);
    }

    /**
     * @param pasta     pasta onde estão os arquivos produtos_*.txt
     * @param saida     arquivo do relatório
     * @param progresso recebe mensagens de andamento (pode ser null)
     */
    public void gerar(File pasta, File saida, Consumer<String> progresso) throws IOException {
        avisar(progresso, "Aquecendo a JVM...");
        aquecer(new File(pasta, "produtos_1000.txt"));

        // tempos[operação][tamanho]
        double[][] tempos = new double[6][TAMANHOS.length];
        for (int t = 0; t < TAMANHOS.length; t++) {
            avisar(progresso, "Medindo " + TAMANHOS[t] + " registros...");
            medir(new File(pasta, "produtos_" + TAMANHOS[t] + ".txt"), tempos, t);
        }

        avisar(progresso, "Escrevendo o relatório...");
        String texto = montarTexto(tempos);
        Files.write(saida.toPath(), texto.getBytes(StandardCharsets.UTF_8));
        avisar(progresso, "Relatório gerado: " + saida.getAbsolutePath());
    }

    private void avisar(Consumer<String> progresso, String mensagem) {
        if (progresso != null) {
            progresso.accept(mensagem);
        } else {
            System.out.println(mensagem);
        }
    }


    // Executa tudo algumas vezes antes de medir, para o JIT compilar o código.
    private void aquecer(File arquivo) throws IOException {
        ArrayList<Produto> original = arquivoController.lerArquivo(arquivo);
        for (int i = 0; i < 5; i++) {
            ArrayList<Produto> a = new ArrayList<>(original);
            ordenacao.insertionSort(a, OrdenacaoController.POR_PRECO, 0);
            ArrayList<Produto> b = new ArrayList<>(original);
            ordenacao.mergeSort(b, OrdenacaoController.POR_PRECO);
            busca.montarMapaPorId(original);
        }
    }

    private void medir(File arquivo, double[][] tempos, int t) throws IOException {
        ArrayList<Produto> original = arquivoController.lerArquivo(arquivo);
        int n = original.size();

        int repeticoesSimples = (n <= 1000) ? 5 : (n <= 10000 ? 3 : 1);
        tempos[SIMPLES][t] = medirOrdenacao(original, true, repeticoesSimples);
        tempos[EFICIENTE][t] = medirOrdenacao(original, false, 5);

        // --- buscas por id (as mesmas 1.000 chaves para as três técnicas)
        int[] ids = busca.sortearIds(original, REPETICOES_BUSCA);

        // a binária precisa da lista ordenada por id
        ArrayList<Produto> ordenadaPorId = new ArrayList<>(original);
        ordenacao.mergeSort(ordenadaPorId, OrdenacaoController.POR_ID);

        HashMap<Integer, Produto> mapa = busca.montarMapaPorId(original);

        double[] linear = new double[7];
        double[] binaria = new double[7];
        double[] hash = new double[7];
        for (int r = 0; r < 7; r++) {
            linear[r] = busca.tempoMedioLinear(original, ids);
            binaria[r] = busca.tempoMedioBinaria(ordenadaPorId, ids);
            hash[r] = busca.tempoMedioHash(mapa, ids);
        }
        tempos[LINEAR][t] = mediana(linear);
        tempos[BINARIA][t] = mediana(binaria);
        tempos[HASH][t] = mediana(hash);

        // --- construção do HashMap
        double[] construcao = new double[5];
        for (int r = 0; r < construcao.length; r++) {
            long inicio = System.nanoTime();
            busca.montarMapaPorId(original);
            construcao[r] = System.nanoTime() - inicio;
        }
        tempos[CONSTRUCAO_HASH][t] = mediana(construcao);
    }

    // Repete a ordenação e devolve a mediana dos tempos (ou NAO_CONCLUIDO se passar de 2 min)
    private double medirOrdenacao(ArrayList<Produto> original, boolean simples, int repeticoes) {
        double[] tempos = new double[repeticoes];
        for (int r = 0; r < repeticoes; r++) {
            ArrayList<Produto> copia = new ArrayList<>(original);
            long inicio = System.nanoTime();
            if (simples) {
                boolean concluiu = ordenacao.insertionSort(copia, OrdenacaoController.POR_PRECO, LIMITE_NANOS);
                if (!concluiu) {
                    return NAO_CONCLUIDO;
                }
            } else {
                ordenacao.mergeSort(copia, OrdenacaoController.POR_PRECO);
            }
            tempos[r] = System.nanoTime() - inicio;
        }
        return mediana(tempos);
    }

    private double mediana(double[] valores) {
        double[] copia = Arrays.copyOf(valores, valores.length);
        Arrays.sort(copia);
        return copia[copia.length / 2];
    }


    private String montarTexto(double[][] tempos) {
        String[] nomes = {
            "Ordenação simples (por preço) - Insertion Sort",
            "Ordenação eficiente (por preço) - Merge Sort",
            "Busca linear por id (média)",
            "Busca binária por id (média)",
            "Busca por HashMap (média)",
            "Construção do HashMap"};

        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE TEMPOS - PESQUISA E ORDENAÇÃO\n");
        sb.append("Gerado em ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(" | Java ").append(System.getProperty("java.version"))
                .append(" | ").append(System.getProperty("os.name"))
                .append(" | ").append(Runtime.getRuntime().availableProcessors()).append(" núcleos\n\n");
        sb.append("Ordenação: mediana de várias execuções (1 execução do algoritmo simples com 50.000).\n");
        sb.append("Busca: tempo médio de ").append(REPETICOES_BUSCA)
                .append(" buscas por ids sorteados, mediana de 7 rodadas.\n\n");

        // monta as células e calcula a largura de cada coluna
        String[] cabecalho = {"Operação", "1.000", "10.000", "50.000"};
        String[][] celulas = new String[nomes.length][4];
        int[] largura = new int[4];
        for (int c = 0; c < 4; c++) {
            largura[c] = cabecalho[c].length();
        }
        for (int l = 0; l < nomes.length; l++) {
            celulas[l][0] = nomes[l];
            for (int t = 0; t < TAMANHOS.length; t++) {
                celulas[l][t + 1] = Tempo.formatar(tempos[l][t]);
            }
            for (int c = 0; c < 4; c++) {
                largura[c] = Math.max(largura[c], celulas[l][c].length());
            }
        }

        sb.append(linhaTabela(cabecalho, largura));
        sb.append("|");
        for (int c = 0; c < 4; c++) {
            sb.append(repetir('-', largura[c] + 2)).append("|");
        }
        sb.append("\n");
        for (int l = 0; l < nomes.length; l++) {
            sb.append(linhaTabela(celulas[l], largura));
        }

        sb.append("\nAnálise (50.000 registros):\n");
        int ultimo = TAMANHOS.length - 1;
        sb.append(analise(tempos, ultimo));
        return sb.toString();
    }

    private String linhaTabela(String[] celulas, int[] largura) {
        StringBuilder sb = new StringBuilder("|");
        for (int c = 0; c < celulas.length; c++) {
            String espacos = repetir(' ', largura[c] - celulas[c].length());
            if (c == 0) {
                sb.append(" ").append(celulas[c]).append(espacos).append(" |");
            } else {
                sb.append(" ").append(espacos).append(celulas[c]).append(" |");
            }
        }
        return sb.append("\n").toString();
    }

    private String repetir(char caractere, int vezes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vezes; i++) {
            sb.append(caractere);
        }
        return sb.toString();
    }

    // Resposta (5 linhas) montada com os números medidos.
    private String analise(double[][] tempos, int t) {
        double linear = tempos[LINEAR][t];
        double binaria = tempos[BINARIA][t];
        double hash = tempos[HASH][t];
        double construcao = tempos[CONSTRUCAO_HASH][t];

        String[] nomes = {"busca linear", "busca binária", "HashMap"};
        String[] motivos = {
            "percorre a lista elemento a elemento",
            "descarta metade da lista a cada comparação, mas exige a lista ordenada por id",
            "calcula o hash da chave e acessa o elemento direto, sem percorrer a lista"};
        double[] valores = {linear, binaria, hash};
        int melhor = 0;
        for (int i = 1; i < valores.length; i++) {
            if (valores[i] < valores[melhor]) {
                melhor = i;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("1) Mais rápida: ").append(nomes[melhor]).append(" (")
                .append(Tempo.formatar(valores[melhor])).append(" por busca); linear ")
                .append(Tempo.formatar(linear)).append(", binária ").append(Tempo.formatar(binaria))
                .append(", HashMap ").append(Tempo.formatar(hash)).append(".\n");
        sb.append("2) Por quê: ").append(melhor == 2 ? "o " : "a ").append(nomes[melhor]).append(" ").append(motivos[melhor])
                .append(" (linear O(n), binária O(log n), HashMap O(1) em média).\n");
        sb.append("3) Custo do HashMap: construí-lo leva ").append(Tempo.formatar(construcao))
                .append(" (O(n) e memória extra), o equivalente a ~")
                .append(Math.round(construcao / linear)).append(" buscas lineares.\n");
        if (linear > hash) {
            long ponto = (long) Math.ceil(construcao / (linear - hash));
            sb.append("4) Vale a pena com muitas consultas sobre os mesmos dados: a partir de ~")
                    .append(ponto).append(" buscas o HashMap já compensa o custo de construção.\n");
        } else {
            sb.append("4) Nesta medição o HashMap não foi mais rápido que a busca linear, então não compensou.\n");
        }
        sb.append("5) Com poucas buscas ou dados que mudam sempre, a linear basta; a binária também paga o custo de ordenar (Merge Sort ~")
                .append(Tempo.formatar(tempos[EFICIENTE][t])).append(").\n");
        return sb.toString();
    }
}
