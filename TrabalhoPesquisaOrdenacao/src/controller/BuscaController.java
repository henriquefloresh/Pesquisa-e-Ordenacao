package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import model.Produto;


public class BuscaController {

    private volatile int totalAchados;

    public Produto buscaLinear(List<Produto> lista, int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                return lista.get(i);
            }
        }
        return null;
    }

    public Produto buscaBinaria(List<Produto> lista, int id) {
        int inicio = 0;
        int fim = lista.size() - 1;
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            Produto p = lista.get(meio);
            if (p.getId() == id) {
                return p;
            } else if (p.getId() < id) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return null;
    }
    
    public boolean estaOrdenadaPorId(List<Produto> lista) {
        for (int i = 1; i < lista.size(); i++) {
            if (lista.get(i - 1).getId() > lista.get(i).getId()) {
                return false;
            }
        }
        return true;
    }

    /** HashMap<Integer, Produto>: chave = id. */
    public HashMap<Integer, Produto> montarMapaPorId(List<Produto> lista) {
        HashMap<Integer, Produto> mapa = new HashMap<>();
        for (Produto p : lista) {
            mapa.put(p.getId(), p);
        }
        return mapa;
    }

    /** HashMap<String, List<Produto>>: chave = categoria. */
    public HashMap<String, List<Produto>> montarIndiceCategorias(List<Produto> lista) {
        HashMap<String, List<Produto>> indice = new HashMap<>();
        for (Produto p : lista) {
            List<Produto> daCategoria = indice.get(p.getCategoria());
            if (daCategoria == null) {
                daCategoria = new ArrayList<>();
                indice.put(p.getCategoria(), daCategoria);
            }
            daCategoria.add(p);
        }
        return indice;
    }

    public int[] sortearIds(List<Produto> lista, int quantidade) {
        Random rnd = new Random(42);
        int[] ids = new int[quantidade];
        for (int i = 0; i < quantidade; i++) {
            ids[i] = lista.get(rnd.nextInt(lista.size())).getId();
        }
        return ids;
    }


    public double tempoMedioLinear(List<Produto> lista, int[] ids) {
        int achados = 0;
        for (int i = 0; i < ids.length; i++) {
            if (buscaLinear(lista, ids[i]) != null) {
                achados++;
            }
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < ids.length; i++) {
            if (buscaLinear(lista, ids[i]) != null) {
                achados++;
            }
        }
        long fim = System.nanoTime();
        totalAchados = achados;
        return (double) (fim - inicio) / ids.length;
    }

    public double tempoMedioBinaria(List<Produto> lista, int[] ids) {
        int achados = 0;
        for (int i = 0; i < ids.length; i++) {
            if (buscaBinaria(lista, ids[i]) != null) {
                achados++;
            }
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < ids.length; i++) {
            if (buscaBinaria(lista, ids[i]) != null) {
                achados++;
            }
        }
        long fim = System.nanoTime();
        totalAchados = achados;
        return (double) (fim - inicio) / ids.length;
    }

    public double tempoMedioHash(HashMap<Integer, Produto> mapa, int[] ids) {
        int achados = 0;
        for (int i = 0; i < ids.length; i++) {
            if (mapa.get(ids[i]) != null) {
                achados++;
            }
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < ids.length; i++) {
            if (mapa.get(ids[i]) != null) {
                achados++;
            }
        }
        long fim = System.nanoTime();
        totalAchados = achados;
        return (double) (fim - inicio) / ids.length;
    }

    public double tempoMedioCategoria(HashMap<String, List<Produto>> indice, String categoria, int repeticoes) {
        int achados = 0;
        for (int i = 0; i < repeticoes; i++) {
            if (indice.get(categoria) != null) {
                achados++;
            }
        }
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticoes; i++) {
            if (indice.get(categoria) != null) {
                achados++;
            }
        }
        long fim = System.nanoTime();
        totalAchados = achados;
        return (double) (fim - inicio) / repeticoes;
    }
}
