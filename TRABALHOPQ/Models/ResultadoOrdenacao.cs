namespace ComparacaoOrdenacao.Models
{
    public class ResultadoOrdenacao
    {
        public string Nome { get; set; }
        public long TempoMs { get; set; }
        public long Comparacoes { get; set; }
        public long Trocas { get; set; }

        public ResultadoOrdenacao(
            string nome,
            long tempoMs,
            long comparacoes = 0,
            long trocas = 0)
        {
            Nome = nome;
            TempoMs = tempoMs;
            Comparacoes = comparacoes;
            Trocas = trocas;
        }
    }
}