using System;
using ComparacaoOrdenacao.Models;

namespace ComparacaoOrdenacao.Views
{
    public class OrdenacaoView
    {
        public void ExibirTitulo(int quantidade)
        {
            Console.WriteLine("=====================================");
            Console.WriteLine("   COMPARAÇÃO DE ALGORITMOS");
            Console.WriteLine("=====================================");
            Console.WriteLine($"Quantidade de elementos: {quantidade:N0}");
            Console.WriteLine();
        }

        public void ExibirResultado(ResultadoOrdenacao resultado)
        {
            Console.WriteLine($"Algoritmo: {resultado.Nome}");
            Console.WriteLine($"Tempo: {resultado.TempoMs} ms");

            if (resultado.Comparacoes > 0)
            {
                Console.WriteLine(
                    $"Comparações: {resultado.Comparacoes:N0}"
                );
            }

            if (resultado.Trocas > 0)
            {
                Console.WriteLine(
                    $"Trocas: {resultado.Trocas:N0}"
                );
            }

            Console.WriteLine("-------------------------------------");
        }

        public void ExibirVencedor(
            ResultadoOrdenacao agitacao,
            ResultadoOrdenacao sort)
        {
            Console.WriteLine();

            if (agitacao.TempoMs < sort.TempoMs)
            {
                Console.WriteLine("Mais rápido: Ordenação por Agitação");
            }
            else if (sort.TempoMs < agitacao.TempoMs)
            {
                Console.WriteLine("Mais rápido: Sort nativo");
            }
            else
            {
                Console.WriteLine("Os dois tiveram o mesmo tempo.");
            }
        }
    }
}