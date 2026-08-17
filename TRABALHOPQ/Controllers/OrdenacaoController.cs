using System.Collections.Generic;
using ComparacaoOrdenacao.Models;
using ComparacaoOrdenacao.Views;

namespace ComparacaoOrdenacao.Controllers
{
    public class OrdenacaoController
    {
        private readonly OrdenacaoModel model;
        private readonly OrdenacaoView view;

        public OrdenacaoController()
        {
            model = new OrdenacaoModel();
            view = new OrdenacaoView();
        }

        public void Executar()
        {
            int quantidade = 100000;

            view.ExibirTitulo(quantidade);

            // Gera apenas UMA lista original
            List<int> listaOriginal =
                model.GerarLista(quantidade);

            // As duas recebem exatamente os mesmos números
            List<int> listaAgitacao =
                new List<int>(listaOriginal);

            List<int> listaSort =
                new List<int>(listaOriginal);

            ResultadoOrdenacao resultadoAgitacao =
                model.OrdenarPorAgitacao(listaAgitacao);

            ResultadoOrdenacao resultadoSort =
                model.OrdenarPorSortNativo(listaSort);

            view.ExibirResultado(resultadoAgitacao);
            view.ExibirResultado(resultadoSort);

            view.ExibirVencedor(
                resultadoAgitacao,
                resultadoSort
            );
        }
    }
}