import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Principal{
    public static void main(String[] args) {
        ArrayList<Integer> lista = new ArrayList<>();
        Util.carregarArquivoEmLista("numeros.txt", lista);
        Ordenacao.pente(lista);
        Util.exibirLista(lista);
        System.out.println("Total de elementos na lista: "+lista.size());


        int numeroPesquisa = Integer.parseInt(JOptionPane.showInputDialog("Digite um numero inteiro para pesquisar:"));
        JOptionPane.showMessageDialog(null, "Resultado:  " + lista.contains(numeroPesquisa) + " " + Ordenacao.pesquisaBinaria(numeroPesquisa, lista));

        System.exit(1);
            
    }
}
