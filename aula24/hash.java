import java.util.HashSet;

public class hash {
    public static void main(String[] args) {
        HashSet<String> tabelaHashString = new HashSet<>();

        tabelaHashString.add("henrique");
        tabelaHashString.add("alexandre");
        tabelaHashString.add("davi");
        tabelaHashString.add("wilson");
        tabelaHashString.add("allan");

        for (String item : tabelaHashString) {
            System.out.println(item + " -> " + item.hashCode());
        }

        System.out.println(tabelaHashString.contains("allan") + " -> " + "allan".hashCode());
    }

}
