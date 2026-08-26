É um padrão comportamental que define o esqueleto de um algoritmo na superclasse, mas permite que as subclasses sobrescrevam etapas específicas do algoritmo sem alterar a sua estrutura.

**Exemplos de uso:** O padrão Template Method é bastante comum em frameworks Java. Os desenvolvedores frequentemente o utilizam para fornecer aos usuários do framework um meio simples de estender a funcionalidade padrão por meio de herança.

### Exemplos nas Java Core Libraries

- Todos os métodos não abstratos de [`java.io.InputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), [`java.io.OutputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html), [`java.io.Reader`](http://docs.oracle.com/javase/8/docs/api/java/io/Reader.html)e [`java.io.Writer`](http://docs.oracle.com/javase/8/docs/api/java/io/Writer.html).

- Todos os métodos não abstratos de [`java.util.AbstractList`](http://docs.oracle.com/javase/8/docs/api/java/util/AbstractList.html), [`java.util.AbstractSet`](http://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html)e [`java.util.AbstractMap`](http://docs.oracle.com/javase/8/docs/api/java/util/AbstractMap.html).

- Na [`javax.servlet.http.HttpServlet`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html)aula, todos os `doXXX()`métodos enviam o erro HTTP 405 "Método não permitido" por padrão. No entanto, você pode sobrescrever qualquer um desses métodos para enviar uma resposta diferente.


**Identificação:** Um método modelo pode ser reconhecido se você observar um método na classe base que chama vários outros métodos que são abstratos ou vazios.

## Problema

Aplicação que analisa documentos corporativos. Vários formatos (PDF, DOC, CSV) e tenta extrair dados relevantes. A primeira versão apenas com DOC, na versão seguinte CSV, depois PDF.

![[Template Method 1.png]]

As três classes têm código semelhante. Embora o código para lidar com vários formatos seja diferente. O Código para processamento e análise é quase idêntico. Seria ótimo eliminar a duplicação mantendo a estrutura do algoritmo.

Outro problema com o código do cliente que utilizava estas classes. Continha muitas condicionais que escolhiam a ação apropriada dependendo da classe do objeto. Se as três classes de processamento tiverem uma interface comum ou classe base seria possível eliminar as condicionais no cliente e usar polimorfismo ao chamar métodos em um objeto de processamento.

## Solução

`Template Method` sugere que decomponha um algoritmo numa série de etapas, transforme essas etapas em métodos e coloque uma série de chamadas a esses métodos dentro de um único modelo.

As etapas podem ser `abstract` ou ter alguma implementação padrão.
Para utilizar o algoritmo, o cliente deve forneceder sua própria subclasse, implementar todas as etapas abstratas.

![[Template Method 2.png]]

Inicialmente, podemos declarar todas as etapas `abstract`, forçando as subclasses a fornecerem as suas implementações.

Abrir/Fechar ficheiros, extrair/analisar dados é diferente entre vários formatos.
Analisar dados e gerar relatórios é semelhante, podendo ser incorporada na classe base, onde as subclasses podem partilhar esse código.

Como você pode ver, temos dois tipos de degraus:

- _Etapas abstratas_ devem ser implementadas por cada subclasse.
- _As etapas opcionais_ já possuem alguma implementação padrão, mas ainda podem ser substituídas, se necessário.

Chamada de ganho, ou `hook` com corpo vazio.
Os ganhos são colocados antes e depois de etapas cruciais de algoritmos, fornecendo ás subclasses pontos de extensão adicionais para um algoritmo.


## Analogia com o mundo real

![[Template Method 3.png]]

`Template Method` pode ser usada na construção de habitações. A construção de uma casa padrão pode conter diversos pontos de extensão que permitiam ao potencial proprietário ajustar alguns detalhes da casa resultante.

Cada etapa:
- lançamento da fundação, estrutura, construção de paredes, instalação da rede hidráulica, pode ser ligeiramente alterada

## Estrutura

![[Template Method 4.png]]

1. `Classe Abstrata` declara métodos que atuam como etapas de um algoritmo, bem como o método modelo que chama esses métodos numa ordem. As etapas pode ser `abstract` ou ter alguma implementação.

2. `Classes concretas` podem sobrescrever todas as etapas, mas não o próprio método do modelo.


## Pseudocódigo

`Template Method` fornece um "esqueleto" para vários ramos da AI

![[Template Method 5.png]]

Todas as raças do jogo possuem os mesmos tipos de unidades e construções. Pode reutilizar a mesma estrutura de IA para várias raças, podendo modificar alguns detalhes.


## Aplicabilidade

problema: `template method` para permitir que os clientes estendam apenas etapas específicas de um algoritmo, e não o algoritmo todo ou sua estrutura.

solução: `template method` permite transformar um algoritmo monolítico numa série de etapas individuais que podem ser estendidas por subclasses, mantendo intacta a estrutura definida na superclasse

---

problema: `template method` quando tiver várias classes que contêm algoritmos quase idênticos com algumas pequenas diferenteças. Consequentemente, pode precisar modificar todas as classes quando o algoritmo for alterado.

solução: `template method` pode incorporar as etapas com implementações diferentes numa superclasse, eliminando duplicação de código.


A ideia central é:

> **Definir a estrutura de um algoritmo numa classe base, deixando que as subclasses implementem ou alterem determinados passos.**

Ou seja, no **Strategy** usamos principalmente **composição**; no **Template Method**, usamos **herança**.

Vou usar um exemplo simples: **processar um ficheiro**.

### 1. Primeiro: perceber o problema

Imagina que temos dois tipos de ficheiros:

```
CSV
JSON
```

O processo geral para ambos é:

```
1. Abrir ficheiro
2. Ler dados
3. Processar dados
4. Fechar ficheiro
```

A estrutura é igual.

Mas alguns passos são diferentes:

```
CSV
 ├── abrir
 ├── ler CSV
 ├── processar
 └── fechar


JSON
 ├── abrir
 ├── ler JSON
 ├── processar
 └── fechar
```

Podíamos começar por fazer algo assim:

```
public void process(String type) {

    if (type.equals("CSV")) {
        // abrir CSV
        // ler CSV
        // processar CSV
        // fechar CSV

    } else if (type.equals("JSON")) {
        // abrir JSON
        // ler JSON
        // processar JSON
        // fechar JSON
    }
}
```

Temos novamente um problema semelhante ao Strategy:

**há um algoritmo com partes que variam.**

Mas há uma diferença importante:

No Strategy, queremos poder **trocar completamente o algoritmo**.

No Template Method, queremos manter **a mesma estrutura do algoritmo**, alterando apenas determinados passos.


### 2. Passo 1 — Dividir o algoritmo em etapas

O primeiro passo diz:

> "Analise o algoritmo alvo para verificar se é possível dividi-lo em etapas."

Pegamos no nosso algoritmo:

```
Processar ficheiro
       │
       ├── Abrir
       ├── Ler
       ├── Processar
       └── Fechar
```

Agora perguntamos:

### Quais etapas são comuns?

Por exemplo:

```
Abrir
Processar
Fechar
```

podem seguir a mesma estrutura.

### Quais etapas variam?

A leitura:

```
CSV → ler CSV
JSON → ler JSON
```

Portanto podemos separar:

```
Algoritmo

1. open()
2. read()
3. process()
4. close()
```

E dizer:

```
open()     → comum
read()     → varia
process()  → comum
close()    → comum
```

É exatamente aqui que identificamos os **pontos de variação**.


### 3. Passo 2 — Criar a classe abstrata

Agora criamos uma classe base:

```
public abstract class FileProcessor {
}
```

Esta classe vai conter o algoritmo comum.

Criamos então o chamado **Template Method**:

```
public final void process() {
    open();
    read();
    processData();
    close();
}
```

A classe completa começa a ficar:

```
public abstract class FileProcessor {

    public final void process() {
        open();
        read();
        processData();
        close();
    }
}
```

Repara no:

```
final
```

Isto é importante.

Estamos a dizer:

> "As subclasses podem alterar os passos, mas não podem alterar a ordem do algoritmo."

Portanto isto:

```
open()
   ↓
read()
   ↓
processData()
   ↓
close()
```

fica protegido.

Uma subclasse não pode fazer:

```
read()
   ↓
open()
   ↓
close()
   ↓
processData()
```

porque o método `process()` é `final`.

### 4. O que é exatamente o Template Method?

Neste exemplo:

```
public final void process() {
    open();
    read();
    processData();
    close();
}
```

**este método é o Template Method.**

Ele funciona como um **molde** do algoritmo.

Pensa nele como uma receita:

```
RECEITA

1. Abrir
2. Ler
3. Processar
4. Fechar
```

A receita define a ordem.

Mas não necessariamente define todos os detalhes de cada passo.

### 5. Passo 2 — Declarar os passos abstratos

Agora precisamos dizer quais passos as subclasses terão de implementar.

Por exemplo:

```
protected abstract void read();
```

Podemos criar:

```
public abstract class FileProcessor {

    public final void process() {
        open();
        read();
        processData();
        close();
    }

    protected abstract void read();
}
```

Agora qualquer subclasse **é obrigada** a implementar:

```
read()
```

Porque cada tipo de ficheiro precisa de uma forma diferente de leitura.

### 6. Passo 3 — Implementações padrão

O passo 3 diz:

> "Algumas etapas podem beneficiar de uma implementação padrão."

Isto significa que **nem tudo precisa de ser `abstract`**.

Por exemplo, podemos ter:

```
protected void open() {
    System.out.println("Abrindo ficheiro...");
}
```

E:

```
protected void close() {
    System.out.println("Fechando ficheiro...");
}
```

Então:

```
public abstract class FileProcessor {

    public final void process() {
        open();
        read();
        processData();
        close();
    }

    protected void open() {
        System.out.println("Abrindo ficheiro...");
    }

    protected abstract void read();

    protected void processData() {
        System.out.println("Processando dados...");
    }

    protected void close() {
        System.out.println("Fechando ficheiro...");
    }
}
```

Agora temos três tipos diferentes de métodos:

### 1. Template Method

```
public final void process()
```

Define a estrutura.

### 2. Método abstrato

```
protected abstract void read();
```

A subclasse **tem de implementar**.

### 3. Método concreto

```
protected void open()
```

Já possui uma implementação padrão.

A subclasse **não precisa de fazer nada**.

### 7. Passo 4 — Criar pontos de conexão

Aqui entra um conceito muito importante do Template Method:

## Hooks

Um **hook** é um ponto opcional de extensão.

Por exemplo:

```
protected boolean shouldProcess() {
    return true;
}
```

Podemos então alterar o Template Method:

```
public final void process() {

    open();

    read();

    if (shouldProcess()) {
        processData();
    }

    close();
}
```

Agora temos:

```
open()
  ↓
read()
  ↓
shouldProcess()
  │
  ├── true  → processData()
  │
  └── false → não processa
  ↓
close()
```

A implementação padrão é:

```
protected boolean shouldProcess() {
    return true;
}
```

Mas uma subclasse pode alterar:

```
@Override
protected boolean shouldProcess() {
    return false;
}
```

Isto permite alterar **um detalhe do algoritmo sem alterar a sua estrutura**.

Esse é o conceito de **hook**.

### 8. Passo 5 — Criar as subclasses

Agora criamos as diferentes variações.

## CSV

```
public class CSVProcessor extends FileProcessor {

    @Override
    protected void read() {
        System.out.println("Lendo ficheiro CSV...");
    }
}
```

Repara que não precisamos implementar:

```
open()
processData()
close()
```

porque a classe base já fornece implementações.

---

## JSON

```
public class JSONProcessor extends FileProcessor {

    @Override
    protected void read() {
        System.out.println("Lendo ficheiro JSON...");
    }
}
```

Temos:

```
FileProcessor
      │
      ├── CSVProcessor
      │       └── read()
      │
      └── JSONProcessor
              └── read()
```


### 9. Como executar?

Podemos fazer:

```
public class Main {

    public static void main(String[] args) {

        FileProcessor csv = new CSVProcessor();

        csv.process();
    }
}
```

O Java executa:

```
process()
   │
   ├── open()
   │
   ├── read()
   │      └── CSVProcessor.read()
   │
   ├── processData()
   │
   └── close()
```

Resultado:

```
Abrindo ficheiro...
Lendo ficheiro CSV...
Processando dados...
Fechando ficheiro...
```

Se fizermos:

```
FileProcessor json = new JSONProcessor();

json.process();
```

a estrutura continua exatamente igual:

```
Abrindo ficheiro...
Lendo ficheiro JSON...
Processando dados...
Fechando ficheiro...
```

A única coisa que mudou foi:

```
read()
```



### 10. Agora vamos relacionar com os 5 passos

## Passo 1 — Dividir o algoritmo

Temos:

```
process()
    ↓
    ├── open()
    ├── read()
    ├── processData()
    └── close()
```

Identificamos:

```
Comum:
    open()
    processData()
    close()

Variável:
    read()
```

---

## Passo 2 — Criar a classe base e o Template Method

Criamos:

```
public abstract class FileProcessor {

    public final void process() {
        open();
        read();
        processData();
        close();
    }
}
```

O:

```
process()
```

é o **Template Method**.

Ele define:

> **a ordem dos passos.**

---

## Passo 3 — Criar implementações padrão

Podemos ter:

```
protected void open() {
    System.out.println("Abrindo...");
}
```

e:

```
protected void close() {
    System.out.println("Fechando...");
}
```

As subclasses herdam essas implementações.

---

## Passo 4 — Adicionar hooks

Por exemplo:

```
protected boolean shouldProcess() {
    return true;
}
```

A subclasse pode sobrescrever se precisar de comportamento diferente.

---

## Passo 5 — Criar subclasses

```
class CSVProcessor extends FileProcessor {
    
    @Override
    protected void read() {
        // ler CSV
    }
}
```

e:

```
class JSONProcessor extends FileProcessor {

    @Override
    protected void read() {
        // ler JSON
    }
}
```

Cada subclasse implementa **a parte variável**, enquanto a classe base mantém **a estrutura**.

# A arquitetura final

Visualmente:

```
                  FileProcessor
                       │
                       │
              ┌────────▼────────┐
              │    process()    │ ← Template Method
              │     final       │
              └────────┬────────┘
                       │
          ┌────────────┼────────────┐
          │            │            │
          ▼            ▼            ▼
       open()       read()      processData()
       comum        variável       comum
          │            │            │
          │            │            │
          │      ┌─────┴─────┐      │
          │      │           │      │
          │      ▼           ▼      │
          │     CSV         JSON     │
          │                           │
          └───────────┬───────────────┘
                      ▼
                    close()
```

---

# A diferença fundamental para Strategy

Como estás a estudar **Strategy → Template Method**, esta comparação é essencial.

## Strategy

Usa **composição**:

```
Context
   │
   │ possui
   ▼
Strategy
   ├── StrategyA
   ├── StrategyB
   └── StrategyC
```

O cliente pode trocar a estratégia:

```
context.setStrategy(new StrategyA());
context.setStrategy(new StrategyB());
```

A ideia é:

> **"Quero escolher qual algoritmo utilizar."**

---

## Template Method

Usa **herança**:

```
AbstractClass
      │
      ├── ConcreteClassA
      └── ConcreteClassB
```

A estrutura é definida pela classe base:

```
public final void process() {
    step1();
    step2();
    step3();
}
```

As subclasses alteram determinados passos.

A ideia é:

> **"Quero manter a estrutura do algoritmo, mas permitir que subclasses alterem determinadas etapas."**

---

# Uma forma muito boa de memorizar

### Strategy

```
"QUAL algoritmo?"
```

O objeto escolhe:

```
Strategy A
Strategy B
Strategy C
```

### Template Method

```
"COMO é estruturado o algoritmo?"
```

A classe base determina:

```
1 → 2 → 3 → 4
```

e as subclasses determinam **o conteúdo de alguns passos**.

---

## E o `final` é especialmente importante

Quando escreves:

```
public final void process() {
    open();
    read();
    close();
}
```

estás a proteger o **template**.

A subclasse pode fazer:

```
@Override
protected void read() {
    // implementação diferente
}
```

Mas não pode fazer:

```
@Override
public void process() {
    // alterar a ordem
}
```

porque `process()` é `final`.

Portanto:

> **A classe base controla a estrutura; as subclasses controlam os detalhes.**

Essa é provavelmente a frase mais importante para guardares sobre o **Template Method**. ([refactoring.guru](https://refactoring.guru/design-patterns/template-method))

## Prós

- Você pode permitir que os clientes substituam apenas certas partes de um algoritmo grande, tornando-os menos afetados por mudanças que ocorram em outras partes do algoritmo.
- Você pode incorporar o código duplicado em uma superclasse.

## Contras

- Alguns clientes podem ficar limitados pela estrutura básica do algoritmo fornecida.
- Você pode violar o _Princípio da Substituição de Liskov_ ao suprimir a implementação de uma etapa padrão por meio de uma subclasse.
- Os métodos baseados em modelos tendem a ser mais difíceis de manter quanto mais etapas eles tiverem.