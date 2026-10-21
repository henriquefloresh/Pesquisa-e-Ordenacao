package view;

import controller.SessaoController;
import controller.SessaoController.ResultadoBusca;
import model.Sessao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * VIEW: só interage com o usuário (entrada e saída no console).
 * Toda a lógica é delegada ao Controller.
 */
public class SessaoView {

    private final SessaoController controller;
    private final Scanner in = new Scanner(System.in);

    public SessaoView(SessaoController controller) {
        this.controller = controller;
    }

    public void iniciar() {
        int opcao;
        do {
            menu();
            opcao = lerInt("Opção: ");
            switch (opcao) {
                case 1 -> buscarUm();
                case 2 -> buscarVarios();
                case 3 -> compararSequencialParalelo();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void menu() {
        System.out.println("\n===== SESSÕES DE TELEFONIA =====");
        System.out.println("Sessões carregadas: " + controller.totalSessoes());
        System.out.println("1 - Buscar um número");
        System.out.println("2 - Buscar vários números ao mesmo tempo (threads)");
        System.out.println("3 - Teste: sequencial x paralelo (números sorteados)");
        System.out.println("0 - Sair");
    }

    // ---------- item 2 ----------
    private void buscarUm() {
        System.out.print("Número do celular: ");
        String numero = in.nextLine().trim();
        mostrar(numero, controller.buscar(numero));
    }

    // ---------- item 3 ----------
    private void buscarVarios() {
        System.out.print("Números separados por espaço ou vírgula: ");
        List<String> numeros = new ArrayList<>(Arrays.asList(in.nextLine().trim().split("[,;\\s]+")));
        numeros.removeIf(String::isBlank);
        if (numeros.isEmpty()) {
            System.out.println("Nenhum número informado.");
            return;
        }

        int threads = lerInt("Quantas threads? ");
        try {
            long ini = System.nanoTime();
            List<ResultadoBusca> resultados = controller.buscarParalelo(numeros, threads, 0);
            long ms = (System.nanoTime() - ini) / 1_000_000;

            for (ResultadoBusca r : resultados) {
                System.out.println("\n[" + r.thread() + " | " + r.micros() + " µs]");
                mostrar(r.numero(), r.sessao());
            }
            System.out.println("\nTotal: " + numeros.size() + " buscas em " + ms + " ms");
        } catch (Exception e) {
            System.out.println("Erro na busca paralela: " + e.getMessage());
        }
    }

    /**
     * Cada busca no HashMap leva ~1 µs, então threads não mostram vantagem real.
     * Por isso o teste simula um atraso (ex.: 50 ms) para imitar banco/rede.
     */
    private void compararSequencialParalelo() {
        int n = lerInt("Quantos numeros sortear? ");
        int threads = lerInt("Quantas threads? ");
        int atraso = lerInt("Atraso simulado por busca em ms (0 = sem atraso): ");

        String[] todos = controller.todosNumeros();
        if (todos.length == 0 || n <= 0) {
            System.out.println("Nada para buscar.");
            return;
        }
        Random rnd = new Random();
        List<String> numeros = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numeros.add(todos[rnd.nextInt(todos.length)]);
        }

        try {
            long t1 = System.nanoTime();
            controller.buscarSequencial(numeros, atraso);
            long seqMs = (System.nanoTime() - t1) / 1_000_000;

            long t2 = System.nanoTime();
            List<ResultadoBusca> res = controller.buscarParalelo(numeros, threads, atraso);
            long parMs = (System.nanoTime() - t2) / 1_000_000;

            long achados = res.stream().filter(r -> r.sessao() != null).count();
            System.out.println("\nBuscas: " + n + " | encontradas: " + achados);
            System.out.println("Sequencial: " + seqMs + " ms");
            System.out.println("Paralelo (" + threads + " threads): " + parMs + " ms");
        } catch (Exception e) {
            System.out.println("Erro no teste: " + e.getMessage());
        }
    }

    // ---------- utilitários de tela ----------
    private void mostrar(String numero, Sessao s) {
        if (s == null) {
            System.out.println("Numero " + numero + " nao encontrado.");
            return;
        }
        System.out.println("Numero: " + numero);
        System.out.println("  session_id : " + s.getSessionId());
        System.out.println("  imsi / imei: " + s.getImsi() + " / " + s.getImei());
        System.out.println("  mcc / mnc  : " + s.getMcc() + " / " + s.getMnc());
        System.out.println("  cell / lac : " + s.getCellId() + " / " + s.getLac());
        System.out.println("  bearer     : " + s.getBearer());
        System.out.println("  início     : " + s.getStartTime());
        System.out.println("  fim        : " + s.getEndTime());
        System.out.println("  duracao    : " + s.getDurationS() + " s");
        System.out.println("  ip         : " + s.getIpAddr());
        System.out.println("  sip_call_id: " + s.getSipCallId());
        System.out.println("  codec      : " + s.getCodec());
        System.out.println("  bytes up/dn: " + s.getBytesUp() + " / " + s.getBytesDown());
        System.out.println("  lat / lon  : " + s.getLat() + " / " + s.getLon());
        System.out.println("  criptografia: " + s.getEncryption());
    }

    private int lerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro.");
            }
        }
    }

    public void mensagem(String texto) {
        System.out.println(texto);
    }
}
