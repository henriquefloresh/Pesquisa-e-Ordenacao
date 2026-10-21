# Resumo 
## AULA 24

1) o universo de ordenação
    - algoritmos
    - conceitos
2) o universo de pesquisa
    - conceitos
    - algoritmos
        - sequencial 0(n)
        - binário 0(log n)
        - digital ?
3) HASH
    - função de empilhamento, ou seja, um dado é armazenado ou pesquisado na estrutura via um código de endereçamento
    - conceitos
        - tabela hash: estrutura de dados que armazena os dados
        - função hash: cálculo de endereçamento para armazenar e buscar elementos na estrutura - 0(1)
        - tipos de tratamento de colisões
            - listas encadeadas
            - open addresing ou closed hashing

Qual a Diferença e me de exemplos de hashmap e hashset em java?
O HashMap serve para associar chaves a valores, enquanto o HashSet serve para armazenar elementos únicos e testar sua existência. A escolha entre eles depende de o problema exigir ou não uma informação associada a cada elemento.

Exemplo HashMap:
import java.util.HashMap;
import java.util.Map;

public class ExemploHashMap {
    public static void main(String[] args) {
        Map<Integer, String> alunos = new HashMap<>();

        alunos.put(101, "Ana");
        alunos.put(102, "Bruno");
        alunos.put(103, "Carla");
        alunos.put(102, "Bruna"); // mesma chave: SUBSTITUI o valor

        System.out.println(alunos.get(102));          // Bruna
        System.out.println(alunos.containsKey(101));  // true
        System.out.println(alunos.getOrDefault(999, "Não encontrado"));

        alunos.remove(103);

        for (Map.Entry<Integer, String> e : alunos.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}
Quando usar: quando você precisa associar algo a uma chave, como matrícula → aluno, CPF → cliente ou palavra → contagem.

Exemplo HashSet:
import java.util.HashSet;
import java.util.Set;

public class ExemploHashSet {
    public static void main(String[] args) {
        Set<String> linguagens = new HashSet<>();

        System.out.println(linguagens.add("Java"));    // true
        System.out.println(linguagens.add("Python"));  // true
        System.out.println(linguagens.add("Java"));    // false (já existe)

        System.out.println(linguagens.contains("C"));  // false
        System.out.println(linguagens.size());         // 2

        linguagens.remove("Python");

        for (String l : linguagens) {
            System.out.println(l);
        }
    }
}
Quando usar: quando você só precisa saber se algo existe ou quer eliminar duplicatas.

java
List<Integer> lista = List.of(1, 2, 2, 3, 3, 3);
Set<Integer> unicos = new HashSet<>(lista); // [1, 2, 3]
