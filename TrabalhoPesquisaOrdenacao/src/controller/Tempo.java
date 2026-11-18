package controller;

import java.util.Locale;

public class Tempo {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    // Valor negativo significa "não concluído" (passou de 2 minutos). 
    public static String formatar(double ns) {
        if (ns < 0) {
            return "não concluído (> 2 min)";
        } else if (ns < 1000) {
            return String.format(PT_BR, "%.0f ns", ns);
        } else if (ns < 1000000) {
            return String.format(PT_BR, "%.2f \u00b5s", ns / 1000.0);
        } else if (ns < 1000000000) {
            return String.format(PT_BR, "%.3f ms", ns / 1000000.0);
        } else {
            return String.format(PT_BR, "%.3f s", ns / 1000000000.0);
        }
    }
}
