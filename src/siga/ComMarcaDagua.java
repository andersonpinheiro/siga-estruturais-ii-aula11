package siga;

/**
 * Decorador concreto que acrescenta a marca d'água de rascunho ao final do relatório.
 */
public class ComMarcaDagua extends DecoradorRelatorio {

    public ComMarcaDagua(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        return super.gerar()
             + "\n*** RASCUNHO — NAO E DOCUMENTO OFICIAL ***";
    }
}

