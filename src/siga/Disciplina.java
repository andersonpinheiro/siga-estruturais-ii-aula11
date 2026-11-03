package siga;

/**
 * Folha (Leaf) do padrão Composite na estrutura curricular.
 * Implementa ItemCurricular respondendo diretamente com sua própria carga horária
 * e impressão, eliminando a necessidade de verificações de tipo no cliente.
 */
public class Disciplina implements ItemCurricular {

    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String nome, int cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public void imprimir(int nivel) {
        String recuo = "  ".repeat(nivel);
        System.out.println(recuo + nome + " (" + cargaHoraria + "h)");
    }
}
