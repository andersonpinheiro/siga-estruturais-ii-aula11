package siga;

/**
 * Demonstra a aplicação dos padrões estruturais Decorator e Composite (Aula 11).
 *
 * - Decorator: substitui a explosão combinatória de subclasses por decoradores
 *   empilháveis em tempo de execução, mantendo cada característica opcional em
 *   uma única classe.
 * - Composite: unifica Disciplina (folha) e Modulo (composto) sob a interface
 *   ItemCurricular, eliminando todas as verificações de tipo na travessia.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== SIGA - Aula 11: Decorator e Composite (Refatorado) ===");

        System.out.println();
        System.out.println("1) Relatorios combinaveis com Decorator (Etapas 2 e 3)");

        Relatorio base = new RelatorioDeAlunos();

        // Pilha 1: numeração dos alunos, depois cabeçalho, depois marca d'água
        Relatorio pilhaCompleta = new ComMarcaDagua(new ComCabecalho(new ComNumeracao(base)));
        imprimirRelatorio("Pilha 1: ComMarcaDagua(ComCabecalho(ComNumeracao(base)))", pilhaCompleta);

        // Pilha 2: inversão da ordem entre ComCabecalho e ComNumeracao (numera também o cabeçalho)
        Relatorio pilhaOrdemInvertida = new ComNumeracao(new ComCabecalho(base));
        imprimirRelatorio("Pilha 2: ComNumeracao(ComCabecalho(base)) - ordem invertida", pilhaOrdemInvertida);

        // Pilha 3: combinação que antes nem tinha subclasse (apenas numeração + marca d'água)
        Relatorio pilhaRascunhoNumerado = new ComMarcaDagua(new ComNumeracao(base));
        imprimirRelatorio("Pilha 3: ComMarcaDagua(ComNumeracao(base))", pilhaRascunhoNumerado);

        System.out.println();
        System.out.println("2) Estrutura curricular como Composite - 2 niveis (Etapa 4)");
        Modulo curso = new Modulo("DSM - Desenvolvimento de Software Multiplataforma");

        Modulo primeiro = new Modulo("1o semestre");
        primeiro.adicionar(new Disciplina("Algoritmos", 80));
        primeiro.adicionar(new Disciplina("Banco de Dados I", 60));

        Modulo segundo = new Modulo("2o semestre");
        segundo.adicionar(new Disciplina("Tecnicas de Programacao II", 80));
        segundo.adicionar(new Disciplina("Desenvolvimento Web II", 80));

        curso.adicionar(primeiro);
        curso.adicionar(segundo);
        curso.adicionar(new Disciplina("Projeto Integrador", 40));

        curso.imprimir(1);
        System.out.println("   Carga total (antes do 3o nivel): " + curso.getCargaHoraria() + "h");

        System.out.println();
        System.out.println("3) Acrescimo de submodulo de terceiro nivel sem alterar o cliente (Etapa 5)");
        Modulo trilha = new Modulo("Trilha de Especializacao em Arquitetura e Nuvem");
        trilha.adicionar(new Disciplina("Arquitetura de Microsservicos", 40));
        trilha.adicionar(new Disciplina("Computacao em Nuvem I", 40));

        segundo.adicionar(trilha);

        curso.imprimir(1);
        System.out.println("   Carga total (com 3o nivel): " + curso.getCargaHoraria() + "h");
    }

    private static void imprimirRelatorio(String titulo, Relatorio relatorio) {
        System.out.println("   --- " + titulo + " ---");
        for (String linha : relatorio.gerar().split("\n")) {
            System.out.println("   " + linha);
        }
    }
}
