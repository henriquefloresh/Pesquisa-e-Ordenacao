import java.util.ArrayList;
import java.util.List;

public class VerificaOrdenacao {

    // Método que verifica se a lista está ordenada
    public static boolean estaOrdenada(List<Integer> lista) {

        if (lista == null || lista.size() <= 1) {
            return true;
        }

        for (int i = 0; i < lista.size() - 1; i++) {
            if (lista.get(i) > lista.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        List<Integer> lista1 = new ArrayList<>();
        lista1.add(10);
        lista1.add(20);
        lista1.add(30);
        lista1.add(40);
        lista1.add(50);

        List<Integer> lista2 = new ArrayList<>();
        lista2.add(10);
        lista2.add(30);
        lista2.add(20);
        lista2.add(50);
        lista2.add(40);

        System.out.println("Lista 1 ordenada? " + estaOrdenada(lista1));
        System.out.println("Lista 2 ordenada? " + estaOrdenada(lista2));
    }
}
