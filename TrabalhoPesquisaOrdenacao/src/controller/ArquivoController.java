package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class ArquivoController {

    public ArrayList<Produto> lerArquivo(File arquivo) throws IOException {
        ArrayList<Produto> lista = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(arquivo.toPath(), StandardCharsets.UTF_8)) {
            br.readLine(); // pula o cabeçalho
            String linha;
            int numeroLinha = 1;
            while ((linha = br.readLine()) != null) {
                numeroLinha++;
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                if (campos.length != 5) {
                    throw new IOException("Linha " + numeroLinha + " inválida: " + linha);
                }
                try {
                    int id = Integer.parseInt(campos[0].trim());
                    String nome = campos[1].trim();
                    String categoria = campos[2].trim();
                    double preco = Double.parseDouble(campos[3].trim());
                    int estoque = Integer.parseInt(campos[4].trim());
                    lista.add(new Produto(id, nome, categoria, preco, estoque));
                } catch (NumberFormatException e) {
                    throw new IOException("Linha " + numeroLinha + " com número inválido: " + linha);
                }
            }
        }
        return lista;
    }

    public void exportarArquivo(List<Produto> lista, File destino) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(destino.toPath(), StandardCharsets.UTF_8)) {
            bw.write("id;nome;categoria;preco;estoque");
            bw.newLine();
            for (Produto p : lista) {
                bw.write(p.paraLinha());
                bw.newLine();
            }
        }
    }
}
