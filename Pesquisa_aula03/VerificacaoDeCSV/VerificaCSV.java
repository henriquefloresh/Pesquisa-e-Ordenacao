import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VerificaCSV {

    public static boolean estaOrdenado(String dados) {

        try (BufferedReader leitor = new BufferedReader(new FileReader(dados))) {

            String linha;
            Integer valorAnterior = null;
            boolean primeiraLinha = true;

            while ((linha = leitor.readLine()) != null) {

                // Ignora linhas vazias
                if (linha.trim().isEmpty()) {
                    continue;
                }

                // Caso o CSV use vírgula, troque ";" por ","
                String[] colunas = linha.split(";");

                String primeiroValor = colunas[0].trim();

                // Ignora o cabeçalho
                if (primeiraLinha && !ehNumeroInteiro(primeiroValor)) {
                    primeiraLinha = false;
                    continue;
                }

                primeiraLinha = false;

                int valorAtual;

                try {
                    valorAtual = Integer.parseInt(primeiroValor);
                } catch (NumberFormatException erro) {
                    System.out.println("Valor inválido encontrado: " + primeiroValor);
                    return false;
                }

                if (valorAnterior != null && valorAnterior > valorAtual) {
                    return false;
                }

                valorAnterior = valorAtual;
            }

            return true;

        } catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo: " + erro.getMessage());
            return false;
        }
    }

    private static boolean ehNumeroInteiro(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException erro) {
            return false;
        }
    }

    public static void main(String[] args) {

        String dados = "dados.csv";

        boolean resultado = estaOrdenado(dados);

        if (resultado) {
            System.out.println("O arquivo CSV está ordenado em ordem crescente.");
        } else {
            System.out.println("O arquivo CSV não está ordenado em ordem crescente.");
        }
    }
}
