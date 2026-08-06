package src.controller;

import src.model.GeradorLista;
import src.model.ResultadoMedicao;
import src.view.MedicaoTempoView;

import java.util.List;

public class MedicaoTempoController {

    private final GeradorLista model;
    private final MedicaoTempoView view;

    public MedicaoTempoController(
            GeradorLista model,
            MedicaoTempoView view
    ) {
        this.model = model;
        this.view = view;
    }

    public void executar() {
        try {
            ResultadoMedicao resultadoAleatorio =
                    medirGeracaoAleatoria(
                            100_000,
                            100,
                            100_000
                    );

            view.exibirResultado(resultadoAleatorio);

            ResultadoMedicao resultadoSequencial =
                    medirGeracaoSequencial(
                            100_000,
                            1
                    );

            view.exibirResultado(resultadoSequencial);

            // Evite exibir 100 mil itens durante a medição,
            // pois a impressão no console é bastante lenta.
            //
            // view.exibirLista(
            //         resultadoAleatorio.getLista(),
            //         "Lista aleatória"
            // );
            //
            // view.exibirLista(
            //         resultadoSequencial.getLista(),
            //         "Lista sequencial"
            // );

        } catch (IllegalArgumentException exception) {
            view.exibirErro(exception.getMessage());
        }
    }

    private ResultadoMedicao medirGeracaoAleatoria(
            int quantidade,
            int inicio,
            int fim
    ) {
        long tempoInicio = System.nanoTime();

        List<Integer> lista =
                model.gerarListaAleatoria(
                        quantidade,
                        inicio,
                        fim
                );

        long tempoFim = System.nanoTime();

        return new ResultadoMedicao(
                "Rotina 1 — geração de números aleatórios",
                lista,
                tempoFim - tempoInicio
        );
    }

    private ResultadoMedicao medirGeracaoSequencial(
            int quantidade,
            int inicio
    ) {
        long tempoInicio = System.nanoTime();

        List<Integer> lista =
                model.gerarListaSequencial(
                        quantidade,
                        inicio
                );

        long tempoFim = System.nanoTime();

        return new ResultadoMedicao(
                "Rotina 2 — geração de números sequenciais",
                lista,
                tempoFim - tempoInicio
        );
    }
}
