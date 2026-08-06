package src.model;

import java.util.List;

public class ResultadoMedicao {

    private final String descricao;
    private final List<Integer> lista;
    private final long tempoNanossegundos;

    public ResultadoMedicao(
            String descricao,
            List<Integer> lista,
            long tempoNanossegundos
    ) {
        this.descricao = descricao;
        this.lista = lista;
        this.tempoNanossegundos = tempoNanossegundos;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Integer> getLista() {
        return lista;
    }

    public long getTempoNanossegundos() {
        return tempoNanossegundos;
    }

    public double getTempoMilissegundos() {
        return tempoNanossegundos / 1_000_000.0;
    }
}
