package siga;

/**
 * Decorador concreto que numera cada linha produzida pelo relatório envolto.
 */
public class ComNumeracao extends DecoradorRelatorio {

    public ComNumeracao(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        String[] linhas = super.gerar().split("\n");
        StringBuilder numerado = new StringBuilder();
        for (int i = 0; i < linhas.length; i++) {
            numerado.append(i + 1).append(": ").append(linhas[i]).append("\n");
        }
        return numerado.toString().stripTrailing();
    }
}

