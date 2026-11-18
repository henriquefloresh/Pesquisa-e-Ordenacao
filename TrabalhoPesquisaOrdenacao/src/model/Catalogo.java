package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Catalogo {

    private ArrayList<Produto> lista;
    private HashMap<Integer, Produto> mapaPorId;
    private HashMap<String, List<Produto>> indicePorCategoria;

    public Catalogo(ArrayList<Produto> lista, HashMap<Integer, Produto> mapaPorId,
                    HashMap<String, List<Produto>> indicePorCategoria) {
        this.lista = lista;
        this.mapaPorId = mapaPorId;
        this.indicePorCategoria = indicePorCategoria;
    }

    public ArrayList<Produto> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Produto> lista) {
        this.lista = lista;
    }

    public HashMap<Integer, Produto> getMapaPorId() {
        return mapaPorId;
    }

    public HashMap<String, List<Produto>> getIndicePorCategoria() {
        return indicePorCategoria;
    }
}
