package controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.Produto;

public class OrdenacaoController {


    public static final Comparator<Produto> POR_ID = new Comparator<Produto>() {
        @Override
        public int compare(Produto a, Produto b) {
            return Integer.compare(a.getId(), b.getId());
        }
    };

    public static final Comparator<Produto> POR_NOME = new Comparator<Produto>() {
        @Override
        public int compare(Produto a, Produto b) {
            int r = a.getNome().compareToIgnoreCase(b.getNome());
            if (r == 0) {
                r = Integer.compare(a.getId(), b.getId());
            }
            return r;
        }
    };

    public static final Comparator<Produto> POR_PRECO = new Comparator<Produto>() {
        @Override
        public int compare(Produto a, Produto b) {
            int r = Double.compare(a.getPreco(), b.getPreco());
            if (r == 0) {
                r = Integer.compare(a.getId(), b.getId());
            }
            return r;
        }
    };

    /**
     * Insertion Sort. Ordena uma cópia em vetor e só copia de volta para a lista
     * se terminar dentro do limite de tempo.
     *
     * @param limiteNanos tempo máximo em nanossegundos (0 = sem limite)
     * @return true se concluiu, false se foi interrompido por passar do limite
     */
    public boolean insertionSort(List<Produto> lista, Comparator<Produto> comp, long limiteNanos) {
        Produto[] v = lista.toArray(new Produto[0]);
        long inicio = System.nanoTime();

        for (int i = 1; i < v.length; i++) {
            Produto chave = v[i];
            int j = i - 1;
            while (j >= 0 && comp.compare(v[j], chave) > 0) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = chave;

            // a cada 256 elementos confere se estourou o tempo limite
            if (limiteNanos > 0 && i % 256 == 0 && System.nanoTime() - inicio > limiteNanos) {
                return false;
            }
        }
        copiarParaLista(v, lista);
        return true;
    }

    /** Merge Sort (divide ao meio, ordena cada metade e intercala). */
    public void mergeSort(List<Produto> lista, Comparator<Produto> comp) {
        Produto[] v = lista.toArray(new Produto[0]);
        Produto[] aux = new Produto[v.length];
        dividir(v, aux, 0, v.length - 1, comp);
        copiarParaLista(v, lista);
    }

    private void dividir(Produto[] v, Produto[] aux, int inicio, int fim, Comparator<Produto> comp) {
        if (inicio >= fim) {
            return;
        }
        int meio = (inicio + fim) / 2;
        dividir(v, aux, inicio, meio, comp);
        dividir(v, aux, meio + 1, fim, comp);
        intercalar(v, aux, inicio, meio, fim, comp);
    }

    private void intercalar(Produto[] v, Produto[] aux, int inicio, int meio, int fim,
                            Comparator<Produto> comp) {
        for (int k = inicio; k <= fim; k++) {
            aux[k] = v[k];
        }
        int i = inicio;
        int j = meio + 1;
        for (int k = inicio; k <= fim; k++) {
            if (i > meio) {
                v[k] = aux[j++];
            } else if (j > fim) {
                v[k] = aux[i++];
            } else if (comp.compare(aux[j], aux[i]) < 0) {
                v[k] = aux[j++];
            } else {
                v[k] = aux[i++];
            }
        }
    }

    public void ordenarComCollections(List<Produto> lista, Comparator<Produto> comp) {
        Collections.sort(lista, comp);
    }

    private void copiarParaLista(Produto[] v, List<Produto> lista) {
        for (int i = 0; i < v.length; i++) {
            lista.set(i, v[i]);
        }
    }
}
