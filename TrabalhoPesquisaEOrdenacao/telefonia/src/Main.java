import controller.SessaoController;
import model.SessaoRepository;
import view.SessaoView;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        // caminho do arquivo: argumento da linha de comando ou o padrão na pasta atual
        Path arquivo = Path.of(args.length > 0 ? args[0] : "telephony_sessions.txt");

        SessaoRepository repo = new SessaoRepository();
        SessaoController controller = new SessaoController(repo);
        SessaoView view = new SessaoView(controller);

        try {
            controller.carregar(arquivo);
        } catch (IOException e) {
            System.out.println("Nao foi possivel ler '" + arquivo + "': " + e.getMessage());
            return;
        }

        view.mensagem("Arquivo carregado: " + controller.totalSessoes() + " sessoes"
                + " (linhas invalidas: " + controller.linhasInvalidas() + ")");
        view.iniciar();
    }
}
