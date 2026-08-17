using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace ComparacaoOrdenacao.Models
{
    public class OrdenacaoModel
    {
        public List<int> GerarLista(int quantidade)
        {
            Random gerador = new Random();
            List<int> lista = new List<int>();

            for (int i = 0; i < quantidade; i++)
            {
                lista.Add(gerador.Next(100000));
            }

            return lista;
        }

        public ResultadoOrdenacao OrdenarPorAgitacao(List<int> lista)
        {
            bool houveTroca;
            int temporario;
            int inicio = 0;
            int fim = lista.Count;

            long qtdComparacoes = 0;
            long qtdTrocas = 0;

            Stopwatch sw = Stopwatch.StartNew();

            do
            {
                houveTroca = false;

                // Percorre da esquerda para a direita
                for (int i = inicio; i < fim - 1; i++)
                {
                    qtdComparacoes++;

                    if (lista[i] > lista[i + 1])
                    {
                        qtdTrocas++;

                        temporario = lista[i];
                        lista[i] = lista[i + 1];
                        lista[i + 1] = temporario;

                        houveTroca = true;
                    }
                }

                if (!houveTroca)
                {
                    break;
                }

                fim--;

                houveTroca = false;

                // Percorre da direita para a esquerda
                for (int i = fim; i > inicio; i--)
                {
                    qtdComparacoes++;

                    if (lista[i] < lista[i - 1])
                    {
                        qtdTrocas++;

                        temporario = lista[i];
                        lista[i] = lista[i - 1];
                        lista[i - 1] = temporario;

                        houveTroca = true;
                    }
                }

                inicio++;

            } while (houveTroca);

            sw.Stop();

            return new ResultadoOrdenacao(
                "Ordenação por Agitação",
                sw.ElapsedMilliseconds,
                qtdComparacoes,
                qtdTrocas
            );
        }

        public ResultadoOrdenacao OrdenarPorSortNativo(List<int> lista)
        {
            Stopwatch sw = Stopwatch.StartNew();

            lista.Sort();

            sw.Stop();

            return new ResultadoOrdenacao(
                "Sort nativo",
                sw.ElapsedMilliseconds
            );
        }
    }
}