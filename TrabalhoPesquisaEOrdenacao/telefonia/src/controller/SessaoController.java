package controller;

import model.Sessao;
import model.SessaoRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * CONTROLLER: liga a View ao Model.
 * Contém a busca simples e a busca de N números em paralelo (threads).
 */
public class SessaoController {

    /** Resultado de uma busca: qual número, o que achou, qual thread e quanto demorou. */
    public record ResultadoBusca(String numero, Sessao sessao, String thread, long micros) { }

    private final SessaoRepository repo;

    public SessaoController(SessaoRepository repo) {
        this.repo = repo;
    }

    public void carregar(Path arquivo) throws IOException {
        repo.carregar(arquivo);
    }

    public int totalSessoes()       { return repo.tamanho(); }
    public int linhasInvalidas()    { return repo.getLinhasInvalidas(); }
    public String[] todosNumeros()  { return repo.numeros(); }

    /** Item 2: busca um único número. Retorna null se não existir. */
    public Sessao buscar(String numero) {
        return repo.buscar(numero.trim());
    }

    /** Uma busca "cronometrada". atrasoMs simula um custo de I/O (banco, rede...). */
    private ResultadoBusca buscarCronometrado(String numero, long atrasoMs) {
        long ini = System.nanoTime();
        if (atrasoMs > 0) {
            try {
                Thread.sleep(atrasoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Sessao s = repo.buscar(numero.trim());
        long micros = (System.nanoTime() - ini) / 1_000;
        return new ResultadoBusca(numero, s, Thread.currentThread().getName(), micros);
    }

    /** Busca N números um depois do outro, na thread principal (para comparar). */
    public List<ResultadoBusca> buscarSequencial(List<String> numeros, long atrasoMs) {
        List<ResultadoBusca> resultados = new ArrayList<>();
        for (String n : numeros) {
            resultados.add(buscarCronometrado(n, atrasoMs));
        }
        return resultados;
    }

    /**
     * Item 3 (desafio): busca N números AO MESMO TEMPO.
     * Cada número vira uma tarefa (Callable) executada por uma thread de um pool fixo.
     * invokeAll espera todas terminarem e devolve os resultados NA MESMA ORDEM da entrada.
     */
    public List<ResultadoBusca> buscarParalelo(List<String> numeros, int numThreads, long atrasoMs)
            throws InterruptedException, ExecutionException {

        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, numThreads));
        try {
            List<Callable<ResultadoBusca>> tarefas = new ArrayList<>();
            for (String n : numeros) {
                tarefas.add(() -> buscarCronometrado(n, atrasoMs));
            }

            List<ResultadoBusca> resultados = new ArrayList<>();
            for (Future<ResultadoBusca> f : pool.invokeAll(tarefas)) {
                resultados.add(f.get());
            }
            return resultados;
        } finally {
            pool.shutdown(); // sempre libera as threads
        }
    }
}
