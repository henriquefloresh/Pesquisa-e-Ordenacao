package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Produto;


public class ProdutoTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {"Id", "Nome", "Categoria", "Preço", "Estoque"};
    private List<Produto> dados = new ArrayList<>();

    public void setDados(List<Produto> dados) {
        this.dados = dados;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return COLUNAS[coluna];
    }

    @Override
    public Class<?> getColumnClass(int coluna) {
        if (coluna == 0 || coluna == 4) {
            return Integer.class;
        } else if (coluna == 3) {
            return Double.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Produto p = dados.get(linha);
        switch (coluna) {
            case 0:
                return p.getId();
            case 1:
                return p.getNome();
            case 2:
                return p.getCategoria();
            case 3:
                return p.getPreco();
            default:
                return p.getEstoque();
        }
    }
}
