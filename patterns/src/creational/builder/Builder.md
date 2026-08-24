Permite construir objetos complexos passo a passo.
Permite produzir diferentes tipos e representações de um objeto usando o mesmo código de construção.

O padrão Builder é um padrão bem conhecido no mundo Java. Ele é especialmente útil quando você precisa criar um objeto com muitas opções de configuração possíveis.

### Exemplos nas Java Core Libraries

- [`java.lang.StringBuilder#append()`](http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html#append-boolean-)( `unsynchronized`)
- [`java.lang.StringBuffer#append()`](http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuffer.html#append-boolean-)( `synchronized`)
- [`java.nio.ByteBuffer#put()`](http://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html#put-byte-)(também em [`CharBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/CharBuffer.html#put-char-), [`ShortBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/ShortBuffer.html#put-short-), [`IntBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/IntBuffer.html#put-int-), [`LongBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/LongBuffer.html#put-long-), [`FloatBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/FloatBuffer.html#put-float-)e [`DoubleBuffer`](http://docs.oracle.com/javase/8/docs/api/java/nio/DoubleBuffer.html#put-double-))
- [`javax.swing.GroupLayout.Group#addComponent()`](http://docs.oracle.com/javase/8/docs/api/javax/swing/GroupLayout.Group.html#addComponent-java.awt.Component-)
- Todas as implementações[`java.lang.Appendable`](http://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html)

**Identificação:** O padrão Builder pode ser reconhecido em uma classe que possui um único método de criação e vários métodos para configurar o objeto resultante. Os métodos Builder geralmente suportam encadeamento (por exemplo, `someBuilder.setValueA(1).setValueB(2).create()`).

## Problema

Inicialização trabalhosa e passo a passo de muitos campos e objetos aninhados.

![[Builder 1.png]]

Construir casa simples, 4 paredes e um piso, porta, janelas e telhado.
Casa mais complexa, solução mais simples é estender e criar conjunto de subclasses.
Construtor gigante.
## Solução

O builder organiza a construção de objetos em conjunto de etapas (buildWalls, buildDoor). Não precisa chamar todas as etapas.

#### Diretor

A classe `diretor` define a ordem em que as etapas de construção devem ser executadas, enquanto a classe `builder` fornece a implementação dessas etapas.

Classe diretor não é estritamente necessário. No entanto pode ser um bom lugar para colocar várias rotinas de construção para que possa reutilizá-las em todo o programa.

## Estrutura

![[Builder 2.png]]

Carro, objeto complexo pode ser construído centenas de maneiras diferentes.
Em vez de um construtor enorme, extrair código de montagem para uma classe separada. Tem um conjunto de métodos para configurar várias parted do carro.

## Aplicabilidade

Utilize o padrão Builder para eliminar um "construtor telescópico".
O padrão Builder permite construir objetos passo a passo, usando apenas as etapas realmente necessárias.

---
Utilize o padrão Builder quando desejar que seu código seja capaz de criar diferentes representações de um mesmo produto (por exemplo, casas de pedra e de madeira).

A interface do construtor base define todas as etapas de construção possíveis, e os construtores concretos implementam essas etapas para construir representações específicas do produto. Enquanto isso, a classe diretora orienta a ordem da construção.

---

Utilize o Construtor para criar árvores [compostas ou outros objetos complexos.](https://refactoring.guru/design-patterns/composite)

O padrão Builder permite construir produtos passo a passo. Você pode adiar a execução de algumas etapas sem comprometer o produto final. É possível até mesmo chamar etapas recursivamente, o que é útil quando você precisa construir uma árvore de objetos.

---

## Como implementar

1. Primeiro precisamos de perceber se diferentes representações de uma casa podem ser construídas através de passos semelhantes.

```` text
Construir casa
    ↓
Construir paredes
    ↓
Adicionar porta
    ↓
Adicionar janelas
    ↓
Adicionar telhado
````

Podemos ter vários tipos de casa:

````text

House
 ├── WoodenHouse
 └── StoneHouse
 
````

Apesar de diferentes, ambas podem ter operações equivalentes:

```` text
buildWalls()
buildDoor()
buildWindows()
buildRoof()
````

**Este é o requisito fundamental do Builder:** deve existir um conjunto de passos de construção que faça sentido para as diferentes representações.

2. Criar a interface do Builder

```` text
interface HouseBuilder {

    void buildWalls();

    void buildDoor();

    void buildWindows();

    void buildRoof();
}
````

A interface não diz como construir a casa.
Define quais os passos que podem ser executados.

```` text
             HouseBuilder
             <<interface>>
                   |
        ┌──────────┴──────────┐
        ↓                     ↓
 WoodenHouseBuilder    StoneHouseBuilder
````

3. Criar os Concrete Builders

==WoodenHouseBuilder==

````text
class WoodenHouseBuilder implements HouseBuilder {

    private House house;

    @Override
    public void buildWalls() {
        // construir paredes de madeira
    }

    @Override
    public void buildDoor() {
        // construir porta de madeira
    }

    @Override
    public void buildWindows() {
        // construir janelas
    }

    @Override
    public void buildRoof() {
        // construir telhado
    }

    public House getResult() {
        return house;
    }
}
````

==StoneHouseBuilder==

````text
class StoneHouseBuilder implements HouseBuilder {

    private House house;

    @Override
    public void buildWalls() {
        // construir paredes de pedra
    }

    @Override
    public void buildDoor() {
        // construir porta
    }

    @Override
    public void buildWindows() {
        // construir janelas
    }

    @Override
    public void buildRoof() {
        // construir telhado
    }

    public House getResult() {
        return house;
    }
}
````

A ideia importante é:

````text
HouseBuilder
      |
      +-------------------------+
      |                         |
      ↓                         ↓
WoodenHouseBuilder      StoneHouseBuilder
      |                         |
      ↓                         ↓
 Wooden House               Stone House
````

Cada builder sabe como construir a sua representação.


4. Por que `getResult()` não está na interface?

Imagina:

````text
WoodenHouseBuilder → House
CarBuilder         → Car
PizzaBuilder       → Pizza
````

Não existe interface comum

````text
House
Car
Pizza
````

Podiamos fazer:

````
interface Builder {

    void buildPart();

    ??? getResult();
}
````

Quando os produtos não têm uma hierarquia comum, cada Builder pode ter o seu próprio:

````text
House getResult();
````

Se todos os produtos tiverem uma interface comum:

````text
interface Product {
}
````

Podemos:

````text
interface Builder {

    void buildPart();

    Product getResult();
}
````

5. Criar o Director

Sabe a ordem dos passos necessários para criar o produto.
Não sabe como cada passo é implementado.

````text
class HouseDirector {

    private HouseBuilder builder;

    public HouseDirector(HouseBuilder builder) {
        this.builder = builder;
    }

    public void buildHouse() {
        builder.buildWalls();
        builder.buildDoor();
        builder.buildWindows();
        builder.buildRoof();
    }
}
````

Separação é fundamental

````text
Director
   |
   | sabe QUANDO executar
   ↓
buildWalls()
buildDoor()
buildWindows()
buildRoof()

Builder
   |
   | sabe COMO executar
   ↓
construção concreta
````

6. O cliente cria Builder e Director

````text
HouseBuilder builder = new WoodenHouseBuilder();

HouseDirector director =
        new HouseDirector(builder);

director.buildHouse();

House house = builder.getResult();
````

Fluxo

````text
CLIENTE
   |
   ├── cria Builder
   |       ↓
   |  WoodenHouseBuilder
   |
   └── cria Director
           ↓
      HouseDirector
           |
           ↓
     buildHouse()
           |
     ┌─────┴─────┐
     ↓     ↓     ↓
   Walls Door Windows Roof
           |
           ↓
        House
````


7. Podemos trocar o Builder

````text
HouseBuilder builder =
        new StoneHouseBuilder();
        
HouseDirector director =
        new HouseDirector(builder);

director.buildHouse();        
````

Temos:

````text
              HouseDirector
                    |
                    ↓
             HouseBuilder
                    ↑
          ┌─────────┴─────────┐
          │                   │
 WoodenHouseBuilder    StoneHouseBuilder
          │                   │
          ↓                   ↓
   Wooden House          Stone House
````

O Diretor não precisa de conhecer:

WoodenHouseBuilder
StoneHouseBuilder

Conhece apenas:

HouseBuilder

8. O Director pode ter diferentes processos de construção

````text
class HouseDirector {

    private HouseBuilder builder;

    public HouseDirector(HouseBuilder builder) {
        this.builder = builder;
    }

    public void buildSimpleHouse() {
        builder.buildWalls();
        builder.buildDoor();
        builder.buildRoof();
    }

    public void buildLuxuryHouse() {
        builder.buildWalls();
        builder.buildDoor();
        builder.buildWindows();
        builder.buildRoof();
    }
}
````

Temos:

````text
                    Director
                       |
             ┌─────────┴─────────┐
             ↓                   ↓
       buildSimpleHouse()   buildLuxuryHouse()
             |                   |
             └─────────┬─────────┘
                       ↓
                    Builder
````

9. O Director não é obrigatório

Não existe um Director

````text
HouseBuilder builder =
        new WoodenHouseBuilder();

builder.buildWalls();
builder.buildDoor();
builder.buildWindows();
builder.buildRoof();

House house = builder.getResult();
````

É útil quando queremos encapsular receitas/processos de construção

````text
Director
 ├── buildSimpleHouse()
 ├── buildLuxuryHouse()
 └── buildVilla()
````


10. A diferença fundamental entre Builder e Factory Method

### Factory Method

O objetivo principal é decidir:

> **Qual objeto concreto devo criar?**

````text
createTransport()
       ↓
   ┌───┴───┐
 Truck    Ship
````

Builder

O objetivo principal é controlar:

> **Como construir um objeto complexo passo a passo?**

````text
buildWalls()
     ↓
buildDoor()
     ↓
buildWindows()
     ↓
buildRoof()
     ↓
Product
````

Portanto:

```
Factory Method
      ↓
escolher implementação
```

enquanto:

````
Builder
      ↓
construir passo a passo
````

11. O fluxo completo do Builder

````text
                         CLIENTE
                            |
                            ↓
                    HouseBuilder
                            |
                 ┌──────────┴──────────┐
                 ↓                     ↓
      WoodenHouseBuilder      StoneHouseBuilder
                 ▲
                 |
                 | usa
                 |
             DIRECTOR
                 |
                 ↓
          buildHouse()
                 |
        ┌────────┼────────┐
        ↓        ↓        ↓
      Walls     Door    Windows
                 |
                 ↓
               Roof
                 |
                 ↓
              PRODUCT
````


A ideia fundamental é:

> **O Builder separa a construção de um objeto complexo da sua representação.**

Ou, de forma ainda mais prática:

> **O Director define a sequência de construção; o Builder define como cada etapa é executada; o Product é o resultado final.**


````text

### Papéis

|Papel|Responsabilidade|
|---|---|
|**Product**|Objeto complexo que queremos construir|
|**Builder**|Declara as etapas de construção|
|**Concrete Builder**|Implementa cada etapa e mantém o produto|
|**Director**|Define a ordem/receita de construção|
|**Client**|Escolhe o Builder e inicia a construção|

Director
→ sabe a ORDEM dos passos

Builder
→ sabe COMO executar cada passo

Product
→ é o RESULTADO
````
