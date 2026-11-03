package siga;

/**
 * Decorador concreto que adiciona o cabeçalho institucional ao relatório.
 * O texto do cabeçalho passa a existir em um único ponto do sistema.
 */
public class ComCabecalho extends DecoradorRelatorio {

    public ComCabecalho(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        return "FATEC DE PORTO FERREIRA\n"
             + "Sistema de Gestao Academica\n"
             + "---\n"
             + super.gerar();
    }
}

