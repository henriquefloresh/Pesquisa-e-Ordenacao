## Revisão das possíveis perguntas

Conceito de HD/Disco -> Memória secundária
RAM -> Memória Principal, não existe RAM se não falar de cache(cache está embutido na RAM)
A memória RAM serve justamente para ser um espaço de trabalho rápido onde os dados do armazenamento externo são otimizados para uso imediato

## Qual a diferença de ordenar por memória Principal e por Secundária?
  - RAM ordena ao mesmo tempo e HD em partes

  - Na memória principal(RAM) é mais rápido que na memória secundária(HD), O termo de I/O aplica-se operacionalmente ao HD

## Método instável e estável e exemplos:
  - Estável: Mantém a ordem relativa de itens iguais. Se dois itens iguais apareciam em uma posição original, eles continuam na mesma ordem após a ordenação.
  - Instável: Não garante a manutenção dessa ordem original. Elementos de mesmo valor podem trocar de posição entre si durante o processo.
  - Bolha(estável), Seleção(instável), Inserção(estável), Pente(instável).

Código em Comparable e CompareTo:
  - Comparable -> interface que define a ordem natural de uma classe. Obriga a implementar o método compareTo()
  - CompareTo -> método que compara this com outro e retorna um int

(Questão seguinte a essa é bolha ou pode ser inserção, questão que te da um vetor e é pra dizer qual é o tipo melhor de ser ordenado)

Método de agitação e método paint: 
  Agitação:
  - Variação do Bubble Sort que percorre o array nos dois sentidos alternadamente (ida e volta), "agitando" os elementos.
  - Uma passada da esquerda pra direita leva o maior valor pro final
  - Uma passada da direita pra esquerda leva o menor valor pro início
  - Repete até não haver mais trocas
    
  Paint:
  - Método usado em componentes gráficos (Java AWT/Swing) para desenhar o conteúdo de um componente na tela.
  - É chamado automaticamente pelo Java sempre que o componente precisa ser redesenhado (ex: ao abrir a janela, redimensionar, etc)
  - Recebe um objeto Graphics, usado para desenhar formas, texto e imagens
      
  A principal inovação do algoritmo paint: Usa recursão para propagar o preenchimento automaticamente por toda uma região conectada, sem precisar pintar célula por célula manualmente.

