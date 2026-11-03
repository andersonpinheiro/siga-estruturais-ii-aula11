package siga;

/**
 * Classe base abstrata do padrão Decorator.
 * Implementa a mesma interface do componente decorado (Relatorio) e mantém uma
 * referência a ele por composição, permitindo empilhar comportamentos em tempo
 * de execução.
 */
public abstract class DecoradorRelatorio implements Relatorio {

    protected final Relatorio relatorio;

    public DecoradorRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("O relatório decorado não pode ser nulo.");
        }
        this.relatorio = relatorio;
    }

    @Override
    public String gerar() {
        return relatorio.gerar();
    }
}

