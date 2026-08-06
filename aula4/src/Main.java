package src;

import src.controller.MedicaoTempoController;
import src.model.GeradorLista;
import src.view.MedicaoTempoView;

public class Main {

    public static void main(String[] args) {
        GeradorLista model = new GeradorLista();
        MedicaoTempoView view = new MedicaoTempoView();

        MedicaoTempoController controller =
                new MedicaoTempoController(model, view);

        controller.executar();
    }
}
