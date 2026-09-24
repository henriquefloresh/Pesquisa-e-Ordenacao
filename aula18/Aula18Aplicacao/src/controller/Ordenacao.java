package controller;

import java.util.ArrayList;

public class Ordenacao {

    public static ArrayList bolha(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long qtdComparacoes = 0;
        long qtdTrocas = 0;
        int aux;
        boolean houveTroca;
        int i;
        do {
            houveTroca = false;
            for (i = 0; i < lista.size() - 1; i++) {
                qtdComparacoes++;
                if (lista.get(i) > lista.get(i + 1)) {
                    houveTroca = true;
                    aux = lista.get(i);
                    lista.set(i, lista.get(i + 1));
                    lista.set(i + 1, aux);
                    qtdTrocas++;
                }
            }
        } while (houveTroca);
        metricas.add((float) qtdComparacoes);
        metricas.add((float) qtdTrocas);
        return metricas;
    }

    public static ArrayList selecao(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long qtdComparacoes = 0;
        long qtdTrocas = 0;
        int i, j, posMenor, aux;
        posMenor = 0;

        for (i = 0; i < lista.size(); i++) {
            posMenor = i;
            for (j = i + 1; j < lista.size(); j++) {
                qtdComparacoes++;
                if (lista.get(j) < lista.get(posMenor)) {
                    posMenor = j;
                }
            }
            if (posMenor != i) {
                aux = lista.get(i);
                lista.set(i, lista.get(posMenor));
                lista.set(posMenor, aux);
                qtdTrocas++;
            }
        }

        metricas.add((float) qtdComparacoes);
        metricas.add((float) qtdTrocas);
        return metricas;
    }

    public static ArrayList insercao(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long qtdComparacoes = 0;
        long qtdTrocas = 0;
        int i, j, aux;

        for (i = 1; i < lista.size(); i++) {
            aux = lista.get(i);
            for (j = i - 1; j > 0 && aux < lista.get(j); j--, qtdComparacoes++) {
                qtdTrocas++;
                lista.set(j + 1, lista.get(j));
            }
            lista.set(j + 1, aux);
        }
        metricas.add((float) qtdComparacoes);
        metricas.add((float) qtdTrocas);
        return metricas;
    }

    public static ArrayList shell(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long qtdComparacoes = 0;
        long qtdTrocas = 0;
        int i, j, aux, gap;
        int n = lista.size();

        for (gap = n / 2; gap > 0; gap /= 2) {
            for (i = gap; i < n; i++) {
                aux = lista.get(i);
                j = i;
                qtdComparacoes++;
                while (j >= gap && lista.get(j - gap) > aux) {
                    lista.set(j, lista.get(j - gap));
                    j -= gap;
                    qtdTrocas++;
                    if (j >= gap) {
                        qtdComparacoes++;
                    }
                }
                lista.set(j, aux);
            }
        }

        metricas.add((float) qtdComparacoes);
        metricas.add((float) qtdTrocas);
        return metricas;
    }

    public static ArrayList quick(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long[] contadores = new long[2]; // [0] = comparações, [1] = trocas
        quickSort(lista, 0, lista.size() - 1, contadores);
        metricas.add((float) contadores[0]);
        metricas.add((float) contadores[1]);
        return metricas;
    }

    private static void quickSort(ArrayList<Integer> lista, int inicio, int fim, long[] contadores) {
        if (inicio < fim) {
            int posPivo = particionar(lista, inicio, fim, contadores);
            quickSort(lista, inicio, posPivo - 1, contadores);
            quickSort(lista, posPivo + 1, fim, contadores);
        }
    }

    private static int particionar(ArrayList<Integer> lista, int inicio, int fim, long[] contadores) {
        int pivo = lista.get(fim);
        int i = inicio - 1;
        int aux;
        for (int j = inicio; j < fim; j++) {
            contadores[0]++;
            if (lista.get(j) <= pivo) {
                i++;
                aux = lista.get(i);
                lista.set(i, lista.get(j));
                lista.set(j, aux);
                contadores[1]++;
            }
        }
        aux = lista.get(i + 1);
        lista.set(i + 1, lista.get(fim));
        lista.set(fim, aux);
        contadores[1]++;
        return i + 1;
    }

    public static ArrayList merge(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long[] contadores = new long[2]; // [0] = comparações, [1] = trocas (movimentações)
        mergeSort(lista, 0, lista.size() - 1, contadores);
        metricas.add((float) contadores[0]);
        metricas.add((float) contadores[1]);
        return metricas;
    }

    private static void mergeSort(ArrayList<Integer> lista, int inicio, int fim, long[] contadores) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSort(lista, inicio, meio, contadores);
            mergeSort(lista, meio + 1, fim, contadores);
            intercalar(lista, inicio, meio, fim, contadores);
        }
    }

    private static void intercalar(ArrayList<Integer> lista, int inicio, int meio, int fim, long[] contadores) {
        ArrayList<Integer> esquerda = new ArrayList<>(lista.subList(inicio, meio + 1));
        ArrayList<Integer> direita = new ArrayList<>(lista.subList(meio + 1, fim + 1));
        int i = 0, j = 0, k = inicio;
        while (i < esquerda.size() && j < direita.size()) {
            contadores[0]++;
            if (esquerda.get(i) <= direita.get(j)) {
                lista.set(k, esquerda.get(i));
                i++;
            } else {
                lista.set(k, direita.get(j));
                j++;
            }
            contadores[1]++;
            k++;
        }
        while (i < esquerda.size()) {
            lista.set(k, esquerda.get(i));
            i++;
            k++;
            contadores[1]++;
        }
        while (j < direita.size()) {
            lista.set(k, direita.get(j));
            j++;
            k++;
            contadores[1]++;
        }
    }

    public static ArrayList pente(ArrayList<Integer> lista) {
        ArrayList<Float> metricas = new ArrayList<>();
        long qtdComparacoes = 0;
        long qtdTrocas = 0;
        int aux;
        boolean houveTroca;
        int i;
        int distancia = lista.size();
        do {
            distancia = (int) (distancia / 1.3);
            if (distancia <= 0) {
                distancia = 1;
            }
            houveTroca = false;
            for (i = 0; i + distancia < lista.size(); i++) {
                qtdComparacoes++;
                if (lista.get(i) > lista.get(i + distancia)) {
                    houveTroca = true;
                    aux = lista.get(i);
                    lista.set(i, lista.get(i + distancia));
                    lista.set(i + distancia, aux);
                    qtdTrocas++;
                }
            }
        } while (distancia > 1 || houveTroca);
        metricas.add((float) qtdComparacoes);
        metricas.add((float) qtdTrocas);
        return metricas;
    }
}