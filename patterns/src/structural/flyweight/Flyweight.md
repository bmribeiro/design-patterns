Permite acomodar mais objetos na quantidade de RAM disponível, partilhando partes comuns do estado entre vários objetos, em vez de manter todos os dados em cada objeto.

![[Flyweight 1.png]]

## Problema

Jogo. Mapa para disparar um contra os outros. Foi implementado um sistema de partículas.
Grandes quantidades de balas, mísseis e estilhaços de explosões voariam pelo mapa.

Depois de compilado foi partilhado com um amigo. RAM insuficiente.
Cada partícula, como uma bala, míssil ou um estilhaço era representada por um objeto isolado contendo muitos dados. Novas particulas não cabiam na RAM, aplicação trava.

![[Flyweight 2.png]]


## Solução

Ao observar `Particle` existem campos que consomem mais memória que outros. Pior, armazenam dados idênticos.

![[Flyweight 3.png]]


Outras partes, coordenadas, vetor de movimento, velocidade são exclusivas, representam o contexto. Os dados de um objeto são chamados de estado intrínseco. Residem dentro do objeto; outros objetos podem ler, não alterar. O restante estado, alterado externamente é chamado extrínseco.

`Flyweight` sugere deixar de armazenar o estado extrínseco dentro do objeto. Em vez disso, passar esse estado para métodos específicos que dependem dele. Só o estado `intrínseco` permanece dentro do objeto, permitindo reutilização noutros contextos. Como resultado, precisaria menos objetos, já que eles diferem apenas no estado intrínseco.

![[Flyweight 4.png]]

Extraído o estado `entrínseco` da classe de `Particle`, 3 objetos diferentes seriam suficientes para representar as partículas do jogo, uma bala, um míssil e um estilhaço.

Um objeto que armazena apenas o estado intrínseco é chamado de `Flyweight`.

#### Armazenamento de estado extrínseco

É movido para o objeto `conteiner`, que agrega os objetos antes de aplicarmos o padrão.

Objeto principal `Game` armazena todas as partículas no campo `Particle`.
Para mover o estado extrínseco para essa classe, precisa de criar vários campos de array para armazenar as coordenadas, vetores e velocidade de cada particula individual.

Precisa de outro array para armazenar referências a `flyweight` que representa uma partícula.
Dados devem ser sincronizados para que possa aceder todos os dados usando o mesmo índice.

![[Flyweight 5.png]]


Solução mais elegante, seria uma classe de contexto separada que guardava o estado extrínseco com uma referência ao objeto `flyweight`. Exigiria apenas um único array no container.

#### Flyweight e imutabilidade

Como o objeto flyweight é usado em diferentes contextos, deve ser garantido que o estado não é modificado. O flyweight deve inicializar o estado uma única vez, via parâmetros do construtor. Não deve ter getter ou setter.
#### Fábrica Flyweight

Para facilitar o acesso a diferentes tipos de objetos `Flyweight`, pode-se criar um método de fábrica para gerir um conjunto de objetos `Flyweight`. O método recebe o está intrínseco do Flyweight de um cliente, procura um objeto que corresponda a esse estado e devolve se encontrado. Caso contrário, cria novo Flyweight e adiciona ao conjunto.

Várias opções de implementação. Ex. Container Flyweight. Em alternativa, nova classe de fábrica. Ou ainda tornar o método de fábrica estático e colocá-lo numa classe Flyweight.

## Estrutura

![[Flyweight 6.png]]


1. O padrão `Flyweight` é uma mera otimização. Antes de aplicá-lo, identificar problema consumo RAM devido ao elevado número de objetos.
2. A classe `Flyweight` contém a porção do estado do objeto original que pode ser partilhada entre múltiplos objetos. O mesmo objeto pode ser usado em diversos contextos diferentes. O estado armazenado dentro de um `Flyweight` é chamado de intrínseco. O estado passado para os métodos do `Flyweight` é chamado de extrínseco.
3. A classe `Context` contém o estado extrínseco, único entre todos os objetos originais. Quando um contexto é emparelhado com um dos objetos `Flyweight`, ele representa o estado completo do objeto original.
4. O comportamento do objeto original permanece na classe `Flyweight`. Se quer chamar um método de um `Flyweight` também deverá passar os bits apropriados do estado extrínseco para os parâmetros do método. Por outro lado, o comportamento pode ser movido para a classe de contexto, que usaria o flyweight vinculado meramente como um objeto de dados.
5. O `Cliente` cálcula ou armazena o estado extrínseco dos `flyweights`. Da perspetiva do cliente, um `flyweight` é um objeto modelo que pode ser configurado em tempo de execução, passando alguns dados contextuais para os parâmetros de seus métodos.
6. A `Flyweight Factory` gere um conjunto de `flyweights` existentes. Com a fábrica, os clientes não criam `flyweights` diretamente. Em vez disso, eles chamam a fábrica, passando partes do estado intrínseco do `flyweight` desejado. A fábrica devolve o `flyweight` que corresponde à pesquisa ou cria novo caso não seja encontrado.

## Pseudocódigo

O `Flyweight` ajuda a reduzir o uso de memória ao renderizar milhões de objetos em forma de árvore numa tela.
![[Flyweight 7.png]]

O padrão extrai o estado intrínseco repetitivo de uma `Tree`classe principal e o move para a classe flyweight `TreeType`.

Agora, em vez de armazenar os mesmos dados em vários objetos, eles são mantidos em apenas alguns objetos leves e vinculados a `Tree`objetos apropriados que atuam como contextos. O código do cliente cria novos objetos de árvore usando a fábrica de objetos leves, que encapsula a complexidade de buscar o objeto correto e reutilizá-lo, se necessário.

## Aplicabilidade

problema: utilizar `flyweight` só quando o programa precisar de suportar um número grande de objetos que mal cabem na RAM

solução: aplicar o `flyweight` depende de como e onde ele é utilizado. Útil quando:
- uma aplicação precisa gerer um grande número de objetos semelhantes
- esgota toda a RAM disponível
- objetos têm estados duplicados que podem ser extraídos e partilhados entre múltiplos objetos.

## Como implementar

Separar o que é partilhável e repetido (estado intrínseco) do que é específico de cada objeto (estado extrínseco), para evitar criar várias cópias iguais.

Imagine um editor de texto que precisa representar **100.000 caracteres**.

Uma implementação simples poderia criar:

```
class Character {
    private char symbol;
    private String font;
    private int fontSize;
    private int x;
    private int y;
}
```

Para cada carácter teríamos um objeto:

```
Character('A', "Arial", 12, 10, 20)
Character('A', "Arial", 12, 30, 20)
Character('A', "Arial", 12, 50, 20)
Character('B', "Arial", 12, 70, 20)
...
```

O problema é que estamos a repetir constantemente:

```
'A' + "Arial" + 12
```

O que muda é principalmente:

```
x + y
```

É aqui que entra o Flyweight.

## 1. Separar o estado intrínseco do estado extrínseco

Primeiro analisamos os campos da classe.

### Estado intrínseco

É a informação que:

- é comum a vários objetos;
- pode ser partilhada;
- não depende do contexto;
- idealmente é imutável.

No nosso exemplo:

```
private final char symbol;
private final String font;
private final int fontSize;
```

Por exemplo, todos estes caracteres podem partilhar exatamente o mesmo estado:

```
'A'
"Arial"
12
```

---

### Estado extrínseco

É a informação que:

- depende do contexto;
- é diferente para cada utilização;
- não deve ficar armazenada no Flyweight.

No exemplo:

```
private int x;
private int y;
```

Porque cada carácter pode aparecer numa posição diferente:

```
A → x=10, y=20
A → x=30, y=20
A → x=50, y=20
```

Portanto:

```
Flyweight
┌──────────────────────┐
│ symbol = 'A'         │
│ font = "Arial"       │
│ fontSize = 12        │
└──────────────────────┘
          ↑
          │ partilhado
          │
     ┌────┴────┐
     │         │
 contexto 1  contexto 2
 x=10,y=20   x=30,y=20
```


# 2. Manter o estado intrínseco no Flyweight

Agora criamos a classe que representa o Flyweight.

```
public class CharacterFlyweight {

    private final char symbol;
    private final String font;
    private final int fontSize;

    public CharacterFlyweight(
            char symbol,
            String font,
            int fontSize) {

        this.symbol = symbol;
        this.font = font;
        this.fontSize = fontSize;
    }
}
```

Repara numa coisa importante:

```
private final char symbol;
private final String font;
private final int fontSize;
```

São `final`.

Isto é importante porque o estado intrínseco deve ser **imutável**.

Depois de criar:

```
new CharacterFlyweight('A', "Arial", 12);
```

não queremos que alguém faça:

```
flyweight.setSymbol('B');
```

Porque o mesmo Flyweight pode estar a ser utilizado por centenas ou milhares de caracteres.


# 3. Identificar os métodos que utilizam estado extrínseco

Imaginemos que anteriormente tínhamos:

```
public class Character {

    private final char symbol;
    private final String font;
    private final int fontSize;

    private int x;
    private int y;

    public void draw() {
        // desenhar o carácter na posição x, y
    }
}
```

O método:

```
draw()
```

utiliza:

```
x
y
```

Mas `x` e `y` são estado extrínseco.

Portanto, eles **não devem permanecer dentro do Flyweight**.

# 4. Transformar o estado extrínseco em parâmetros

Em vez de:

```
public void draw() {
    // utiliza x e y
}
```

passamos:

```
public void draw(int x, int y) {
    // utiliza x e y
}
```

Agora a classe fica:

```
public class CharacterFlyweight {

    private final char symbol;
    private final String font;
    private final int fontSize;

    public CharacterFlyweight(
            char symbol,
            String font,
            int fontSize) {

        this.symbol = symbol;
        this.font = font;
        this.fontSize = fontSize;
    }

    public void draw(int x, int y) {
        System.out.println(
            "Desenhar " + symbol +
            " em (" + x + ", " + y + ")"
        );
    }
}
```

Este é um dos pontos **mais importantes do padrão Flyweight**.

O Flyweight conhece:

```
O QUE desenhar
```

mas não conhece:

```
ONDE desenhar
```

A posição é fornecida pelo contexto.

# 5. Criar a Flyweight Factory

Agora temos outro problema.

Se fizermos:

```
new CharacterFlyweight('A', "Arial", 12);
new CharacterFlyweight('A', "Arial", 12);
new CharacterFlyweight('A', "Arial", 12);
```

continuamos a criar três objetos iguais.

Precisamos de alguém que controle os Flyweights existentes.

É aí que aparece a **Factory**.

```
public class CharacterFactory {

    private final Map<String, CharacterFlyweight> flyweights
            = new HashMap<>();

    public CharacterFlyweight get(
            char symbol,
            String font,
            int fontSize) {

        String key = symbol + ":" + font + ":" + fontSize;

        if (!flyweights.containsKey(key)) {

            flyweights.put(
                key,
                new CharacterFlyweight(
                    symbol,
                    font,
                    fontSize
                )
            );
        }

        return flyweights.get(key);
    }
}
```

Agora:

```
CharacterFlyweight a1 =
    factory.get('A', "Arial", 12);

CharacterFlyweight a2 =
    factory.get('A', "Arial", 12);
```

`a1` e `a2` apontam para **o mesmo objeto**.

Conceptualmente:

```
factory
   │
   └── "A:Arial:12"
            │
            ▼
    CharacterFlyweight
    ┌─────────────────┐
    │ 'A'              │
    │ Arial            │
    │ 12               │
    └─────────────────┘
       ▲       ▲
       │       │
      A1      A2
```

Assim evitamos criar objetos duplicados.

# 6. O cliente pede Flyweights à Factory

Agora o cliente não deveria fazer:

```
new CharacterFlyweight(...);
```

Em vez disso:

```
CharacterFlyweight flyweight =
    factory.get('A', "Arial", 12);
```

A Factory verifica:

```
Já existe?
   │
   ├── SIM → devolver existente
   │
   └── NÃO → criar + guardar + devolver
```

Isto garante que existe apenas uma instância para cada combinação de estado intrínseco.

# 7. O cliente mantém o estado extrínseco

Agora precisamos de representar cada carácter no documento.

Por exemplo:

```
public class CharacterContext {

    private final CharacterFlyweight flyweight;

    private final int x;
    private final int y;

    public CharacterContext(
            CharacterFlyweight flyweight,
            int x,
            int y) {

        this.flyweight = flyweight;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        flyweight.draw(x, y);
    }
}
```

Aqui temos:

```
CharacterContext
├── flyweight  → estado intrínseco partilhado
├── x          → estado extrínseco
└── y          → estado extrínseco
```

Por exemplo:

```
CharacterContext c1 =
    new CharacterContext(
        factory.get('A', "Arial", 12),
        10,
        20
    );

CharacterContext c2 =
    new CharacterContext(
        factory.get('A', "Arial", 12),
        30,
        20
    );
```

Temos:

```
c1 ─────┐
        │
        ▼
     Flyweight A
     ┌─────────────┐
     │ 'A'         │
     │ Arial       │
     │ 12          │
     └─────────────┘
        ▲
        │
c2 ─────┘
```

Mas:

```
c1
├── x = 10
└── y = 20

c2
├── x = 30
└── y = 20
```

Ou seja, **o Flyweight é partilhado, mas o contexto é diferente**.

# 8. O cliente utiliza o contexto

Podemos agora fazer:

```
c1.draw();
c2.draw();
```

Internamente:

```
public void draw() {
    flyweight.draw(x, y);
}
```

Portanto:

```
c1.draw()
    ↓
flyweight.draw(10, 20)

c2.draw()
    ↓
flyweight.draw(30, 20)
```

O mesmo Flyweight é utilizado duas vezes, mas com diferentes estados extrínsecos.

---

# 9. Exemplo completo

Juntando tudo:

### Flyweight

```
public class CharacterFlyweight {

    private final char symbol;
    private final String font;
    private final int fontSize;

    public CharacterFlyweight(
            char symbol,
            String font,
            int fontSize) {

        this.symbol = symbol;
        this.font = font;
        this.fontSize = fontSize;
    }

    public void draw(int x, int y) {
        System.out.println(
            "Desenhar " + symbol +
            " [" + font + ", " + fontSize + "]" +
            " em (" + x + ", " + y + ")"
        );
    }
}
```

### Factory

```
public class CharacterFactory {

    private final Map<String, CharacterFlyweight> flyweights
            = new HashMap<>();

    public CharacterFlyweight get(
            char symbol,
            String font,
            int fontSize) {

        String key = symbol + ":" + font + ":" + fontSize;

        return flyweights.computeIfAbsent(
            key,
            k -> new CharacterFlyweight(
                symbol,
                font,
                fontSize
            )
        );
    }
}
```

### Context

```
public class CharacterContext {

    private final CharacterFlyweight flyweight;
    private final int x;
    private final int y;

    public CharacterContext(
            CharacterFlyweight flyweight,
            int x,
            int y) {

        this.flyweight = flyweight;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        flyweight.draw(x, y);
    }
}
```

### Cliente

```
CharacterFactory factory = new CharacterFactory();

CharacterContext c1 =
    new CharacterContext(
        factory.get('A', "Arial", 12),
        10,
        20
    );

CharacterContext c2 =
    new CharacterContext(
        factory.get('A', "Arial", 12),
        30,
        20
    );

CharacterContext c3 =
    new CharacterContext(
        factory.get('B', "Arial", 12),
        50,
        20
    );

c1.draw();
c2.draw();
c3.draw();
```

Temos potencialmente:

```
3 Characters
        │
        ▼
┌─────────────────────────┐
│ CharacterFactory        │
│                         │
│ A:Arial:12 ────────┐    │
│ B:Arial:12 ──────┐ │    │
└───────────────────│─│────┘
                    │ │
             ┌──────▼─┘
             │
      ┌──────▼─────────┐
      │ Flyweight A    │
      │ 'A' Arial 12   │
      └────────────────┘

      ┌────────────────┐
      │ Flyweight B    │
      │ 'B' Arial 12   │
      └────────────────┘
```

Os três contextos continuam a existir:

```
Context 1 → Flyweight A + (10,20)
Context 2 → Flyweight A + (30,20)
Context 3 → Flyweight B + (50,20)
```

Mas só existem **dois Flyweights**.

---

# 10. O que realmente ganhámos?

Sem Flyweight:

```
A + Arial + 12 + 10 + 20
A + Arial + 12 + 30 + 20
A + Arial + 12 + 50 + 20
B + Arial + 12 + 70 + 20
...
```

Com Flyweight:

```
Flyweight A
├── A
├── Arial
└── 12

Flyweight B
├── B
├── Arial
└── 12
```

E os contextos:

```
Context → Flyweight A + posição
Context → Flyweight A + posição
Context → Flyweight A + posição
Context → Flyweight B + posição
```

A grande vantagem aparece quando temos **muitos objetos com muito estado repetido**.

---

# 11. Resumindo os passos do Refactoring Guru

Podemos traduzir os 5 passos que tens no texto para isto:

### Passo 1 — Separar os estados

Pergunta:

> **O que é igual entre muitos objetos?**

→ **Estado intrínseco**

Pergunta:

> **O que é diferente para cada utilização?**

→ **Estado extrínseco**

---

### Passo 2 — Tornar o intrínseco imutável

```
private final char symbol;
private final String font;
private final int fontSize;
```

O Flyweight não deve alterar estes valores depois de criado.

---

### Passo 3 — Retirar o extrínseco dos métodos

Antes:

```
public void draw() {
    // usa x e y
}
```

Depois:

```
public void draw(int x, int y) {
    // usa x e y
}
```

O estado extrínseco passa a ser fornecido pelo cliente/contexto.

---

### Passo 4 — Criar a Factory

A Factory mantém:

```
Map<String, Flyweight>
```

e garante:

```
mesmo estado intrínseco
        ↓
mesmo Flyweight
```

---

### Passo 5 — Criar o Context

O Context guarda:

```
Flyweight + estado extrínseco
```

Assim:

```
             ┌─────────────────┐
             │     Context     │
             │                 │
             │ x = 10          │
             │ y = 20          │
             │       │         │
             └───────┼─────────┘
                     │
                     ▼
             ┌─────────────────┐
             │   Flyweight     │
             │                 │
             │ A               │
             │ Arial           │
             │ 12              │
             └─────────────────┘
                    ↑
             partilhado por
             vários Contexts
```

## A ideia que deves memorizar

O **Flyweight não é simplesmente "uma Factory que reutiliza objetos"**.

A parte fundamental é esta:

> **Flyweight = separar estado intrínseco partilhável de estado extrínseco contextual.**

A Factory entra depois para garantir a reutilização:

```
ESTADO INTRÍNSECO
        ↓
   Flyweight
        ↑
        │ reutilizado
        │
   Factory
        │
        ↓
ESTADO EXTRÍNSECO
        ↓
    Contexto
```

E uma boa regra mental para reconhecer o padrão é:

> **"Tenho milhares de objetos e muitos deles contêm exatamente os mesmos dados? Posso retirar esses dados dos objetos individuais e partilhá-los?"**

Se a resposta for **sim**, o Flyweight pode ser adequado.