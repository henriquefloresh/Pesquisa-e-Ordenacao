using ComparacaoOrdenacao.Controllers;

namespace ComparacaoOrdenacao
{
    public class Program
    {
        public static void Main(string[] args)
        {
            OrdenacaoController controller =
                new OrdenacaoController();

            controller.Executar();
        }
    }
}
