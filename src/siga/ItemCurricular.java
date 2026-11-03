package siga;

/**
 * Componente (Component) do padrão Composite.
 * Define o contrato uniforme tanto para itens individuais (Disciplina - folha)
 * quanto para agrupamentos hierárquicos (Modulo - composto).
 */
public interface ItemCurricular {

    String getNome();

    int getCargaHoraria();

    void imprimir(int nivel);
}

