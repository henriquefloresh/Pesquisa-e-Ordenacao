package src.view;

import src.model.ResultadoMedicao;

import java.util.List;

public class MedicaoTempoView {

    public void exibirResultado(ResultadoMedicao resultado) {
        System.out.println(resultado.getDescricao());

        System.out.printf(
                "Tempo: %.3f ms%n",
                resultado.getTempoMilissegundos()
        );

        System.out.println(
                "Total de registros: " + resultado.getLista().size()
        );

        System.out.println("--------------------------");
    }

    public void exibirLista(List<Integer> lista, String titulo) {
        System.out.println(titulo);

        for (Integer numero : lista) {
            System.out.println(numero);
        }

        System.out.println("--------------------------");
        System.out.println("Total de registros: " + lista.size());
    }

    public void exibirErro(String mensagem) {
        System.err.println("Erro: " + mensagem);
    }
}
