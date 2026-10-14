import java.util.HashSet;

class Aluno{
    int matricula;
    String nome;

    public Aluno(int matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Aluno [matricula=" + matricula + ", nome=" + nome + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + matricula;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Aluno other = (Aluno) obj;
        if (matricula != other.matricula)
            return false;
        return true;
    }
}

public class ExHashObj {
    public static void main(String[] args) {
        HashSet<Aluno> tabelaHashAluno = new HashSet<>();

        tabelaHashAluno.add(new Aluno(1, "henrique"));
        tabelaHashAluno.add(new Aluno(2, "alexandre"));
        tabelaHashAluno.add(new Aluno(1, "davi"));
        tabelaHashAluno.add(new Aluno(2, "henrique"));
        
        for (Aluno a : tabelaHashAluno){
            System.out.println(a);
        }
    }
}
