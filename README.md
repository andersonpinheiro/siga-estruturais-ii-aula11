# SIGA — Padrões Estruturais II (Decorator e Composite)

**Técnicas de Programação II (TP2) · Aula 11** — CST em Desenvolvimento de Software Multiplataforma · Fatec de Porto Ferreira

Este projeto contém a refatoração da atividade prática da Aula 11, aplicando os padrões estruturais **Decorator** (para relatórios combináveis em tempo de execução) e **Composite** (para representação e travessia uniforme da estrutura curricular hierárquica).

---

## 📊 Etapa 1: A Conta da Explosão Combinatória ($2^n$)

No código inicial, cada combinação de características opcionais (cabeçalho institucional, numeração de páginas e marca d'água) era representada por uma subclasse estática herdando de `RelatorioDeAlunos`:
- **Subclasses existentes no código inicial:** **4 subclasses** (`RelatorioComCabecalho`, `RelatorioComNumeracao`, `RelatorioComCabecalhoENumeracao`, `RelatorioComCabecalhoENumeracaoEMarcaDagua`), além da classe base `RelatorioDeAlunos` (totalizando 5 classes concretas, ainda incompletas pois faltavam combinações como "Cabeçalho + Marca d'água" ou "Apenas Marca d'água").
- **Raciocínio combinatório ($2^n$):** Como cada característica opcional é independente (pode estar **presente** ou **ausente**, 2 possibilidades), para $n$ características existem $2^n$ combinações possíveis (contando o relatório base sem opcionais) ou $2^n - 1$ subclasses especializadas:
  - Com **$n = 3$ características** (cabeçalho, numeração, marca d'água): $2^3 = \mathbf{8}$ classes no total (1 classe base + **7 subclasses** para cobrir todas as combinações).
  - Com **$n = 4$ características** (adicionando, por exemplo, um *rodapé com data de emissão*): $2^4 = \mathbf{16}$ classes no total (1 classe base + **15 subclasses**).

Com o padrão **Decorator**, o crescimento deixa de ser exponencial ($2^n$) e passa a ser **linear ($n$)**:
- Para 3 características opcionais, temos apenas **3 classes decoradoras** (`ComCabecalho`, `ComNumeracao`, `ComMarcaDagua`).
- Se uma 4ª característica for solicitada, basta criar **1 nova classe** (`ComRodape`), sem tocar nas existentes (respeitando o Princípio Aberto/Fechado — OCP) e mantendo o texto do cabeçalho em um único lugar (eliminando a duplicação).

---

## 🎨 Etapas 2 e 3: Padrão Decorator nos Relatórios

### Solução Aplicada
1. Criada a classe abstrata [`DecoradorRelatorio`](src/siga/DecoradorRelatorio.java), que implementa [`Relatorio`](src/siga/Relatorio.java) e mantém uma referência ao objeto `Relatorio` decorado via composição.
2. Criados os três decoradores concretos:
   - [`ComCabecalho`](src/siga/ComCabecalho.java): adiciona o cabeçalho institucional da Fatec (escrito em um único ponto do sistema).
   - [`ComNumeracao`](src/siga/ComNumeracao.java): numera sequencialmente as linhas do relatório recebido.
   - [`ComMarcaDagua`](src/siga/ComMarcaDagua.java): acrescenta o aviso de rascunho ao final.
3. No [`Main`](src/siga/Main.java), foram montadas pilhas distintas sobre a mesma instância de [`RelatorioDeAlunos`](src/siga/RelatorioDeAlunos.java):
   - **Pilha 1 (`new ComMarcaDagua(new ComCabecalho(new ComNumeracao(base)))`):** numera primeiro apenas os nomes dos alunos, adiciona o cabeçalho acima e a marca d'água ao final.
   - **Pilha 2 (`new ComNumeracao(new ComCabecalho(base))`):** ao inverter a ordem de empilhamento, o cabeçalho é colocado antes da numeração e, portanto, suas linhas também são numeradas (`1: FATEC DE PORTO FERREIRA...`).
   - **Pilha 3 (`new ComMarcaDagua(new ComNumeracao(base))`):** combinação de rascunho numerado sem cabeçalho, que antes sequer possuía classe correspondente.

---

## 🌳 Etapas 4 e 5: Padrão Composite na Estrutura Curricular

### Problema Anterior
`Disciplina` e `Modulo` não compartilhavam nenhuma abstração. Por isso, `Modulo` guardava uma `List<Object>` e a classe `GradeCurricular` precisava percorrer a árvore usando blocos `instanceof` e *downcasts* duplicados tanto em `calcularCarga` quanto em `imprimir`.

### Solução Aplicada
1. Criada a interface componente [`ItemCurricular`](src/siga/ItemCurricular.java) com as operações de domínio: `getNome()`, `getCargaHoraria()` e `imprimir(int nivel)`.
2. [`Disciplina`](src/siga/Disciplina.java) (**Folha / Leaf**) implementa `ItemCurricular`, retornando sua própria carga horária e imprimindo sua linha formatada.
3. [`Modulo`](src/siga/Modulo.java) (**Composto / Composite**) implementa `ItemCurricular` e mantém uma coleção tipada `List<ItemCurricular> filhos`, somando recursivamente a carga horária dos filhos e delegando a impressão com `nivel + 1`.
4. A palavra `instanceof` e a classe auxiliar `GradeCurricular` foram completamente eliminadas do projeto.
5. Na **Etapa 5**, foi adicionado um submódulo de **3º nível** (`Trilha de Especializacao em Arquitetura e Nuvem` dentro do `2o semestre`). O cliente (`Main`) continuou invocando exatamente `curso.imprimir(1)` e `curso.getCargaHoraria()`, provando a transparência da hierarquia recursiva (340h antes do 3º nível; 420h após o acréscimo da trilha).

---

## ⚖️ Justificativa da Decisão de Projeto (Segurança de Tipos vs. Uniformidade)

No padrão Composite, os métodos de gerenciamento de filhos (`adicionar` e `remover`) só fazem sentido semântico no **Composto** (`Modulo`), e não na **Folha** (`Disciplina`).

**Decisão tomada:** Mantivemos os métodos `adicionar(ItemCurricular)` e `remover(ItemCurricular)` **exclusivamente na classe `Modulo`** (abordagem de *Segurança de Tipos* / *Type Safety*), deixando na interface `ItemCurricular` apenas as operações comuns (`getNome`, `getCargaHoraria`, `imprimir`).

**Por que escolhemos esta alternativa:**
- Se colocássemos `adicionar` e `remover` na interface `ItemCurricular`, a classe folha `Disciplina` seria obrigada a implementá-los lançando `UnsupportedOperationException`. Isso violaria o **Princípio da Substituição de Liskov (LSP)**, pois uma chamada válida na interface quebraria em tempo de execução dependendo do subtipo concreto.
- Ao manter `adicionar` e `remover` apenas em `Modulo`, o próprio compilador Java impede que alguém tente adicionar uma disciplina dentro de outra disciplina, convertendo um potencial erro de execução em verificação em tempo de compilação.

---

## 🚀 Como Compilar e Executar

Na pasta `siga-estruturais-ii`:

```bash
# 1. Compilar o código-fonte para a pasta bin/
mkdir -p bin
javac -encoding UTF-8 -d bin src/siga/*.java

# 2. Executar o programa principal
java -cp bin siga.Main
```

### Saída Esperada da Execução

```text
=== SIGA - Aula 11: Decorator e Composite (Refatorado) ===

1) Relatorios combinaveis com Decorator (Etapas 2 e 3)
   --- Pilha 1: ComMarcaDagua(ComCabecalho(ComNumeracao(base))) ---
   FATEC DE PORTO FERREIRA
   Sistema de Gestao Academica
   ---
   1: Maria Silva
   2: Joao Souza
   3: Ana Pereira
   *** RASCUNHO — NAO E DOCUMENTO OFICIAL ***
   --- Pilha 2: ComNumeracao(ComCabecalho(base)) - ordem invertida ---
   1: FATEC DE PORTO FERREIRA
   2: Sistema de Gestao Academica
   3: ---
   4: Maria Silva
   5: Joao Souza
   6: Ana Pereira
   --- Pilha 3: ComMarcaDagua(ComNumeracao(base)) ---
   1: Maria Silva
   2: Joao Souza
   3: Ana Pereira
   *** RASCUNHO — NAO E DOCUMENTO OFICIAL ***

2) Estrutura curricular como Composite - 2 niveis (Etapa 4)
  [DSM - Desenvolvimento de Software Multiplataforma]
    [1o semestre]
      Algoritmos (80h)
      Banco de Dados I (60h)
    [2o semestre]
      Tecnicas de Programacao II (80h)
      Desenvolvimento Web II (80h)
    Projeto Integrador (40h)
   Carga total (antes do 3o nivel): 340h

3) Acrescimo de submodulo de terceiro nivel sem alterar o cliente (Etapa 5)
  [DSM - Desenvolvimento de Software Multiplataforma]
    [1o semestre]
      Algoritmos (80h)
      Banco de Dados I (60h)
    [2o semestre]
      Tecnicas de Programacao II (80h)
      Desenvolvimento Web II (80h)
      [Trilha de Especializacao em Arquitetura e Nuvem]
        Arquitetura de Microsservicos (40h)
        Computacao em Nuvem I (40h)
    Projeto Integrador (40h)
   Carga total (com 3o nivel): 420h
```
