package view;

/*
 * Trabalho Prático - Pesquisa e Ordenação
 * Universidade Franciscana (UFN) - Ciência da Computação
 * Professor: Alexandre Zamberlan
 *
 * Autor: Henrique Flores Hiemer

 */

import controller.ArquivoController;
import controller.BuscaController;
import controller.OrdenacaoController;
import controller.RelatorioController;
import controller.Tempo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import model.Catalogo;
import model.Produto;

public class JFramePrincipal extends JFrame {

    private static final long LIMITE_NANOS = 120000000000L; // 2 minutos
    private static final int REPETICOES = 1000;

    private static final String[] CAMPOS = {"Preço", "Nome", "Id"};
    private static final String[] ALGORITMOS = {
        "Insertion Sort (simples)", "Merge Sort (eficiente)", "Collections.sort (referência)"};
    private static final String[] TECNICAS = {
        "Id - busca linear", "Id - busca binária", "Id - HashMap", "Categoria - HashMap"};

    // controllers
    private final ArquivoController arquivoController = new ArquivoController();
    private final OrdenacaoController ordenacaoController = new OrdenacaoController();
    private final BuscaController buscaController = new BuscaController();
    private final RelatorioController relatorioController = new RelatorioController();

    // dados em memória
    private Catalogo catalogo;

    // componentes
    private final JComboBox<String> cbArquivo = new JComboBox<>();
    private final JComboBox<String> cbCampo = new JComboBox<>(CAMPOS);
    private final JComboBox<String> cbAlgoritmo = new JComboBox<>(ALGORITMOS);
    private final JComboBox<String> cbTecnica = new JComboBox<>(TECNICAS);
    private final JTextField txtBusca = new JTextField(14);

    private final JButton btnProcurar = new JButton("Procurar...");
    private final JButton btnCarregar = new JButton("Carregar");
    private final JButton btnExportar = new JButton("Exportar lista...");
    private final JButton btnRelatorio = new JButton("Gerar relatorio.txt");
    private final JButton btnOrdenar = new JButton("Ordenar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnCompleta = new JButton("Mostrar lista completa");

    private final JLabel lblTempo = new JLabel("Tempo da última operação: -");
    private final JLabel lblInfo = new JLabel(" ");

    private final ProdutoTableModel modeloTabela = new ProdutoTableModel();

    public JFramePrincipal() {
        super("Catálogo de Produtos - Pesquisa e Ordenação");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        montarTela();
        listarArquivos();
        configurarEventos();
        setSize(980, 640);
        setLocationRelativeTo(null);

        if (cbArquivo.getItemCount() > 0) {
            cbArquivo.setSelectedIndex(0);
            carregar();
        }
    }


    private void montarTela() {
        JPanel pArquivo = novoPainel("Arquivo");
        pArquivo.add(new JLabel("Arquivo:"));
        pArquivo.add(cbArquivo);
        pArquivo.add(btnProcurar);
        pArquivo.add(btnCarregar);
        pArquivo.add(btnExportar);
        pArquivo.add(btnRelatorio);

        JPanel pOrdenacao = novoPainel("Ordenação");
        pOrdenacao.add(new JLabel("Campo:"));
        pOrdenacao.add(cbCampo);
        pOrdenacao.add(new JLabel("Algoritmo:"));
        pOrdenacao.add(cbAlgoritmo);
        pOrdenacao.add(btnOrdenar);

        JPanel pBusca = novoPainel("Busca");
        pBusca.add(new JLabel("Técnica:"));
        pBusca.add(cbTecnica);
        pBusca.add(new JLabel("Id ou categoria:"));
        pBusca.add(txtBusca);
        pBusca.add(btnBuscar);
        pBusca.add(btnCompleta);

        JPanel topo = new JPanel(new GridLayout(3, 1));
        topo.add(pArquivo);
        topo.add(pOrdenacao);
        topo.add(pBusca);

        JPanel rodape = new JPanel(new GridLayout(2, 1));
        rodape.add(lblTempo);
        rodape.add(lblInfo);

        getContentPane().add(topo, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(new JTable(modeloTabela)), BorderLayout.CENTER);
        getContentPane().add(rodape, BorderLayout.SOUTH);
    }

    private JPanel novoPainel(String titulo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painel.setBorder(new TitledBorder(titulo));
        return painel;
    }

    private void listarArquivos() {
        File[] arquivos = new File(".").listFiles();
        if (arquivos == null) {
            return;
        }
        List<File> produtos = new ArrayList<>();
        for (File f : arquivos) {
            if (f.getName().matches("produtos_\\d+\\.txt")) {
                produtos.add(f);
            }
        }
        // ordena pelo número do nome (1000, 10000, 50000)
        Collections.sort(produtos, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                return Integer.compare(numero(a), numero(b));
            }
        });
        for (File f : produtos) {
            cbArquivo.addItem(f.getName());
        }
    }

    private int numero(File f) {
        return Integer.parseInt(f.getName().replaceAll("\\D", ""));
    }

    private void configurarEventos() {
        btnProcurar.addActionListener(e -> procurarArquivo());
        btnCarregar.addActionListener(e -> carregar());
        btnExportar.addActionListener(e -> exportar());
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        btnOrdenar.addActionListener(e -> ordenar());
        btnBuscar.addActionListener(e -> buscar());
        txtBusca.addActionListener(e -> buscar());
        btnCompleta.addActionListener(e -> mostrarCompleta());
    }


    private void bloquear(boolean bloqueado, String mensagem) {
        btnProcurar.setEnabled(!bloqueado);
        btnCarregar.setEnabled(!bloqueado);
        btnExportar.setEnabled(!bloqueado);
        btnRelatorio.setEnabled(!bloqueado);
        btnOrdenar.setEnabled(!bloqueado);
        btnBuscar.setEnabled(!bloqueado);
        btnCompleta.setEnabled(!bloqueado);
        lblInfo.setText(mensagem);
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void exibir(List<Produto> dados) {
        modeloTabela.setDados(dados);
        lblInfo.setText(dados.size() + " produto(s) exibido(s) de " + catalogo.getLista().size() + " carregado(s).");
    }

    private boolean semDados() {
        if (catalogo == null) {
            aviso("Carregue um arquivo primeiro.");
            return true;
        }
        return false;
    }



    private void procurarArquivo() {
        JFileChooser fc = new JFileChooser(new File("."));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String caminho = fc.getSelectedFile().getAbsolutePath();
            cbArquivo.addItem(caminho);
            cbArquivo.setSelectedItem(caminho);
            carregar();
        }
    }

    private void carregar() {
        String nome = (String) cbArquivo.getSelectedItem();
        if (nome == null) {
            aviso("Escolha um arquivo de produtos.");
            return;
        }
        File arquivo = new File(nome);
        bloquear(true, "Carregando " + arquivo.getName() + "...");

        new Thread(() -> {
            try {
                long t0 = System.nanoTime();
                ArrayList<Produto> lista = arquivoController.lerArquivo(arquivo);
                long t1 = System.nanoTime();
                HashMap<Integer, Produto> mapa = buscaController.montarMapaPorId(lista);
                long t2 = System.nanoTime();
                HashMap<String, List<Produto>> indice = buscaController.montarIndiceCategorias(lista);
                long t3 = System.nanoTime();

                Catalogo novo = new Catalogo(lista, mapa, indice);
                String tempos = "Leitura: " + Tempo.formatar(t1 - t0)
                        + " | HashMap por id: " + Tempo.formatar(t2 - t1)
                        + " | Índice por categoria: " + Tempo.formatar(t3 - t2);

                SwingUtilities.invokeLater(() -> {
                    catalogo = novo;
                    exibir(catalogo.getLista());
                    lblTempo.setText(tempos);
                    bloquear(false, lblInfo.getText());
                });
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    bloquear(false, " ");
                    erro("Não foi possível carregar o arquivo:\n" + ex.getMessage());
                });
            }
        }).start();
    }



    private void ordenar() {
        if (semDados()) {
            return;
        }
        Comparator<Produto> comparador;
        String campo;
        if (cbCampo.getSelectedIndex() == 0) {
            comparador = OrdenacaoController.POR_PRECO;
            campo = "preço";
        } else if (cbCampo.getSelectedIndex() == 1) {
            comparador = OrdenacaoController.POR_NOME;
            campo = "nome";
        } else {
            comparador = OrdenacaoController.POR_ID;
            campo = "id";
        }
        int algoritmo = cbAlgoritmo.getSelectedIndex();
        String nomeAlgoritmo = ALGORITMOS[algoritmo];

        ArrayList<Produto> copia = new ArrayList<>(catalogo.getLista());
        bloquear(true, "Ordenando por " + campo + " com " + nomeAlgoritmo + "...");

        new Thread(() -> {
            boolean concluiu = true;
            long inicio = System.nanoTime();
            if (algoritmo == 0) {
                concluiu = ordenacaoController.insertionSort(copia, comparador, LIMITE_NANOS);
            } else if (algoritmo == 1) {
                ordenacaoController.mergeSort(copia, comparador);
            } else {
                ordenacaoController.ordenarComCollections(copia, comparador);
            }
            long tempo = System.nanoTime() - inicio;
            boolean ok = concluiu;

            SwingUtilities.invokeLater(() -> {
                if (ok) {
                    catalogo.setLista(copia);
                    exibir(copia);
                    lblTempo.setText("Ordenação por " + campo + " (" + nomeAlgoritmo + "): " + Tempo.formatar(tempo));
                    bloquear(false, lblInfo.getText());
                } else {
                    lblTempo.setText("Ordenação por " + campo + " (" + nomeAlgoritmo + "): " + Tempo.formatar(-1));
                    bloquear(false, "Ordenação interrompida; a lista continua como estava.");
                }
            });
        }).start();
    }

    private void buscar() {
        if (semDados()) {
            return;
        }
        String texto = txtBusca.getText().trim();
        if (texto.isEmpty()) {
            aviso("Informe o id ou a categoria a buscar.");
            return;
        }
        int tecnica = cbTecnica.getSelectedIndex();
        if (tecnica == 3) {
            buscarPorCategoria(texto);
        } else {
            buscarPorId(texto, tecnica);
        }
    }

    private void buscarPorId(String texto, int tecnica) {
        int id;
        try {
            id = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            aviso("O id deve ser um número inteiro.");
            return;
        }

        ArrayList<Produto> lista = catalogo.getLista();
        if (tecnica == 1 && !buscaController.estaOrdenadaPorId(lista)) {
            aviso("A busca binária só funciona com a lista ordenada pelo mesmo campo buscado.\n"
                    + "Ordene por Id e tente novamente.");
            return;
        }

        // repete a mesma busca 1.000 vezes para calcular o tempo médio
        int[] ids = new int[REPETICOES];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = id;
        }

        Produto achado;
        double media;
        if (tecnica == 0) {
            achado = buscaController.buscaLinear(lista, id);
            media = buscaController.tempoMedioLinear(lista, ids);
        } else if (tecnica == 1) {
            achado = buscaController.buscaBinaria(lista, id);
            media = buscaController.tempoMedioBinaria(lista, ids);
        } else {
            achado = catalogo.getMapaPorId().get(id);
            media = buscaController.tempoMedioHash(catalogo.getMapaPorId(), ids);
        }

        List<Produto> resultado = new ArrayList<>();
        if (achado != null) {
            resultado.add(achado);
        }
        exibir(resultado);
        lblTempo.setText(TECNICAS[tecnica] + ": média de " + Tempo.formatar(media) + " em " + REPETICOES
                + " buscas (" + (achado == null ? "id não encontrado" : "encontrado") + ")");
    }

    private void buscarPorCategoria(String texto) {
        HashMap<String, List<Produto>> indice = catalogo.getIndicePorCategoria();

        // procura o nome da categoria ignorando maiúsculas/minúsculas
        String chave = null;
        for (String categoria : indice.keySet()) {
            if (categoria.equalsIgnoreCase(texto)) {
                chave = categoria;
            }
        }
        if (chave == null) {
            List<String> todas = new ArrayList<>(indice.keySet());
            Collections.sort(todas);
            aviso("Categoria não encontrada.\nCategorias disponíveis: " + String.join(", ", todas));
            return;
        }

        double media = buscaController.tempoMedioCategoria(indice, chave, REPETICOES);
        exibir(indice.get(chave));
        lblTempo.setText(TECNICAS[3] + " (" + chave + "): média de " + Tempo.formatar(media)
                + " em " + REPETICOES + " buscas");
    }

    private void mostrarCompleta() {
        if (semDados()) {
            return;
        }
        exibir(catalogo.getLista());
    }


    private void exportar() {
        if (semDados()) {
            return;
        }
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File("produtos_exportados.txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                arquivoController.exportarArquivo(catalogo.getLista(), fc.getSelectedFile());
                lblInfo.setText("Lista exportada para " + fc.getSelectedFile().getName());
            } catch (IOException e) {
                erro("Não foi possível exportar:\n" + e.getMessage());
            }
        }
    }

    private void gerarRelatorio() {
        int r = JOptionPane.showConfirmDialog(this,
                "Gerar relatorio.txt com 1.000, 10.000 e 50.000 registros?\n"
                + "Pode levar alguns minutos.", "Gerar relatório", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        bloquear(true, "Gerando relatório...");

        new Thread(() -> {
            try {
                relatorioController.gerar(new File("."), new File("relatorio.txt"),
                        msg -> SwingUtilities.invokeLater(() -> lblInfo.setText(msg)));
                SwingUtilities.invokeLater(() -> {
                    lblTempo.setText("Relatório gerado em relatorio.txt");
                    bloquear(false, " ");
                    JOptionPane.showMessageDialog(this, "relatorio.txt gerado na pasta do projeto.");
                });
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    bloquear(false, " ");
                    erro("Falha ao gerar o relatório:\n" + ex.getMessage());
                });
            }
        }).start();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        SwingUtilities.invokeLater(() -> new JFramePrincipal().setVisible(true));
    }
}
