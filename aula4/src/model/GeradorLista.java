package src.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorLista {

    private final Random gerador;

    public GeradorLista() {
        this.gerador = new Random();
    }

    /**
     * Gera uma lista com números aleatórios.
     *
     * @param quantidade quantidade de números
     * @param inicio menor número possível
     * @param fim maior número possível
     * @return lista preenchida
     */
    public List<Integer> gerarListaAleatoria(
            int quantidade,
            int inicio,
            int fim
    ) {
        validarParametros(quantidade, inicio, fim);

        List<Integer> lista = new ArrayList<>(quantidade);

        for (int i = 0; i < quantidade; i++) {
            // O limite superior do nextInt é exclusivo.
            lista.add(gerador.nextInt(inicio, fim + 1));
        }

        return lista;
    }

    /**
     * Gera uma lista sequencial a partir de um valor inicial.
     *
     * Exemplo:
     * quantidade = 5 e inicio = 1
     * resultado = [1, 2, 3, 4, 5]
     *
     * @param quantidade quantidade de números
     * @param inicio primeiro número da sequência
     * @return lista preenchida
     */
    public List<Integer> gerarListaSequencial(
            int quantidade,
            int inicio
    ) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade não pode ser negativa."
            );
        }

        List<Integer> lista = new ArrayList<>(quantidade);

        for (int i = 0; i < quantidade; i++) {
            lista.add(inicio + i);
        }

        return lista;
    }

    private void validarParametros(
            int quantidade,
            int inicio,
            int fim
    ) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade não pode ser negativa."
            );
        }

        if (inicio > fim) {
            throw new IllegalArgumentException(
                    "O início não pode ser maior que o fim."
            );
        }

        if (fim == Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "O valor final deve ser menor que Integer.MAX_VALUE."
            );
        }
    }
}
