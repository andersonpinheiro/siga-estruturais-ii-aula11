package siga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composto (Composite) da estrutura curricular.
 * Armazena uma coleção de ItemCurricular, podendo conter tanto disciplinas
 * quanto submódulos de qualquer profundidade, delegando polimorficamente o
 * cálculo de carga horária e a impressão.
 */
public class Modulo implements ItemCurricular {

    private final String nome;
    private final List<ItemCurricular> filhos = new ArrayList<>();

    public Modulo(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public void adicionar(ItemCurricular filho) {
        filhos.add(filho);
    }

    public void remover(ItemCurricular filho) {
        filhos.remove(filho);
    }

    public List<ItemCurricular> getFilhos() {
        return Collections.unmodifiableList(filhos);
    }

    @Override
    public int getCargaHoraria() {
        int total = 0;
        for (ItemCurricular filho : filhos) {
            total += filho.getCargaHoraria();
        }
        return total;
    }

    @Override
    public void imprimir(int nivel) {
        String recuo = "  ".repeat(nivel);
        System.out.println(recuo + "[" + nome + "]");
        for (ItemCurricular filho : filhos) {
            filho.imprimir(nivel + 1);
        }
    }
}
