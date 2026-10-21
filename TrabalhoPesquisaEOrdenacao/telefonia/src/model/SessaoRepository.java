package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MODEL: guarda as sessões em um HashMap (chave = número do celular).
 *
 * Depois de carregar(), o mapa só é LIDO. Várias threads podem fazer get()
 * ao mesmo tempo em um HashMap sem problema, desde que ninguém escreva nele.
 */
public class SessaoRepository {

    // "chave": "texto"  ou  "chave": numero
    private static final Pattern CAMPO =
        Pattern.compile("\"(\\w+)\"\\s*:\\s*(?:\"([^\"]*)\"|(-?[0-9.]+))");

    private final Map<String, Sessao> sessoes = new HashMap<>();
    private int linhasInvalidas = 0;

    /** Lê o arquivo, separa cada linha, cria a Sessao e faz put no HashMap. */
    public void carregar(Path arquivo) throws IOException {
        sessoes.clear();
        linhasInvalidas = 0;

        try (BufferedReader br = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;

                // limite 2: separa só no primeiro ';' (número ; json)
                String[] partes = linha.split(";", 2);
                if (partes.length < 2) { linhasInvalidas++; continue; }

                try {
                    String numero = partes[0].trim();
                    sessoes.put(numero, criarSessao(partes[1]));
                } catch (RuntimeException e) {
                    linhasInvalidas++;
                }
            }
        }
    }

    /** Busca O(1) em média. Retorna null se o número não existe. */
    public Sessao buscar(String numero) {
        return sessoes.get(numero);
    }

    public int tamanho()            { return sessoes.size(); }
    public int getLinhasInvalidas() { return linhasInvalidas; }

    /** Cópia da lista de números (usada para sortear números no teste). */
    public String[] numeros() {
        return sessoes.keySet().toArray(new String[0]);
    }

    // ---- parser simples do JSON (sem bibliotecas externas) ----
    private Sessao criarSessao(String json) {
        Map<String, String> c = new HashMap<>();
        Matcher m = CAMPO.matcher(json);
        while (m.find()) {
            c.put(m.group(1), m.group(2) != null ? m.group(2) : m.group(3));
        }
        return new Sessao(
            c.get("session_id"), c.get("imsi"), c.get("imei"), c.get("mcc"), c.get("mnc"),
            Integer.parseInt(c.get("cell_id")), Integer.parseInt(c.get("lac")),
            c.get("bearer"), c.get("start_time"), c.get("end_time"),
            Integer.parseInt(c.get("duration_s")), c.get("ip_addr"),
            c.get("sip_call_id"), c.get("codec"),
            Long.parseLong(c.get("bytes_up")), Long.parseLong(c.get("bytes_down")),
            Double.parseDouble(c.get("lat")), Double.parseDouble(c.get("lon")),
            c.get("encryption"));
    }
}
