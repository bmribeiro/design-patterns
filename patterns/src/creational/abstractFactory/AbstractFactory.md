
**Abstract Factory** é um padrão de projeto criacional que resolve o problema de criar famílias inteiras de produtos sem especificar suas classes concretas.

### Exemplos nas Java Core Libraries
- [`javax.xml.parsers.DocumentBuilderFactory#newInstance()`](http://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html#newInstance--)
- [`javax.xml.transform.TransformerFactory#newInstance()`](http://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerFactory.html#newInstance--)
- [`javax.xml.xpath.XPathFactory#newInstance()`](http://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/XPathFactory.html#newInstance--)

**Identification:** The pattern is easy to recognize by methods, which return a factory object. Then, the factory is used for creating specific sub-components.


Problema:
Simulador de loja de móveis.
- Família de produtos relacionados: Chair + Sofá + CoffeeTable
- Variantes: Modern + Victorian + ArtDeco

Não vai querer alterar o código existente ao adicionar novos produtos ou famílias de produtos. Os fornecedores atualizam catálogos com muita frequência.

Solução

O padrão Abstract Factory sugere é declarar interfaces para cada produto distinto da família de produtos ( cadeira, sofá , mesa ). De seguida, pode fazer com que todas as variantes dos produtos sigam essas interfaces.

![[Pasted image 20260821164911.png]]


Próximo passo é declarar Fábrica Abstrata - uma interface com uma lista de métodos de criação para todos os produtos (ex. createChair, createSofa and createCoffeeTable )

![[Pasted image 20260821165748.png]]


Para cada variante de uma família de produtos, criamos uma classe de fábrica separada com base na interface `AbstractFactory`. Uma fábrica devolve produtos de um tipo especifico. Ex. `ModernFurnitureFactory` só pode criar objetos do tipo `ModernChair`, `ModernSofa` e `ModernCoffeeTable`.

O Código do cliente precisa de funcionar tanto com fábricas quanto com produtos por meio das suas interfaces abstratas.

O Cliente quer uma fábrica que produza uma cadeira. Não precisa saber a classe da fábrica. Usa interface abstrata `Chair`. A Única coisa que o cliente sabe é que implementa o método `sitOn`. Qualquer que seja a variante da cadeira devolvida, corresponderá ao tipo de sofá ou mesa produzida na mesma fábrica.

## Estrutura

![[Pasted image 20260821171231.png]]


1. Resumo: `Os produtos` declaram interfaces para um conjunto de produtos distintos, porém relaconados, que compõem uma família de produtos.
2. `Os Produtos concretos` são diversas implementações de produtos abstratos, agrupados por variantes. Cada produto abstrato ( cadeira / sofá ) deve ser implementado em todas as variantes fornecidas ( vitoriana / moderna ).
3. A Interface `AbstractFactory` declara um conjunto de métodos para criar cada um dos produtos abstratos.
4. As fábricas concretas implementam os métodos de criação da fábrica abstrata. Cada fábrica concreta corresponde a uma variante específica de proutos e cria apenas essa variante.
5. Embora as fábricas concretas instanciem produtos concretos, as assinaturas de seus métodos de criação devem retornar os produtos _abstratos_ correspondentes . Dessa forma, o código do cliente que utiliza uma fábrica não fica acoplado à variante específica do produto que recebe da fábrica. O **cliente** pode trabalhar com qualquer variante concreta de fábrica/produto, desde que se comunique com seus objetos por meio de interfaces abstratas.

## Pseudocódigo

![[Pasted image 20260821172421.png]]


## Aplicabilidade

Problem: use abstract factory quando o código precisar de funcionar com várias famílias de produtos relacionados, mas não quer que dependa das classes concretas desses produtos. Podem ser desconhecidas antemão ou é desejado permitir extensibilidade futura.

Solução: Abstract Factory fornece uma interface para criar objetos de classe da família de produtos.

1. Primeiro: o que é uma "família de produtos"?

Imagina uma aplicação gráfica que suporta dois sistemas operativos:

Windows
├── WindowsButton
└── WindowsCheckbox

macOS
├── MacButton
└── MacCheckbox

Temos dois tipos de produto
- `Button`
- `Checkbox`

Mas cada um tem duas implementações:

Button
├── WindowsButton
└── MacButton

Checkbox
├── WindowsCheckbox
└── MacCheckbox

Estas implementações relacionadas formam **famílias**:

```` text
             Windows Family
             ┌─────────────┐
             │             │
       WindowsButton   WindowsCheckbox


              macOS Family
             ┌─────────────┐
             │             │
         MacButton      MacCheckbox
````

2. Primeiro Problem do teu texto

Tens:

> use abstract factory quando o código precisar de funcionar com várias famílias de produtos relacionados, mas não quer que dependa das classes concretas desses produtos.

Sem Abstract Factory poderias ter:

Button button = new WindowsButton();
Checkbox checkbox = new WindowsCheckbox();

O código conhece diretamente:

- WindowsButton
- WindowsCheckbox

Portanto está acoplado às classes concretas.
O objetivo é passar a:

- Button button = factory.createButton();
- Checkbox checkbox = factory.createCheckbox();

Agora o código conhece apenas:

- Button
- Checkbox
- GUIFactory

3. Criar as interfaces dos produtos

````

interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}
````

```` text
          Button             Checkbox
         <<interface>>      <<interface>>
````

4. Criar os produtos concretos

Família Windows

````

class WindowsButton implements Button {

    @Override
    public void paint() {
        System.out.println("Windows button");
    }
}

class WindowsCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("Windows checkbox");
    }
}
````

Família macOS

````

class MacButton implements Button {

    @Override
    public void paint() {
        System.out.println("macOS button");
    }
}

class MacCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("macOS checkbox");
    }
}
````

```` text
Button
 ├── WindowsButton
 └── MacButton

Checkbox
 ├── WindowsCheckbox
 └── MacCheckbox
````

5. Agora aparece a Abstract Factory

```` 
interface GUIFactory {

    Button createButton();

    Checkbox createCheckbox();
}
````

No Factory Method tínhamos:

````
createTransport()
````

Aqui temos vários métodos:

````
createButton()
createCheckbox()
````

Porque precisamos de criar **vários produtos relacionados**.

6. Criar a Windows Factory

```` 
class WindowsFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}
````

Esta factory sabe criar a família Windows:

````
WindowsFactory
      |
      ├── createButton()
      │       ↓
      │   WindowsButton
      │
      └── createCheckbox()
              ↓
          WindowsCheckbox
````

7. Criar a Mac Factory

````
class MacFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}
````

````
MacFactory
      |
      ├── createButton()
      │       ↓
      │    MacButton
      │
      └── createCheckbox()
              ↓
          MacCheckbox
````

8. O código cliente deixa de conhecer as classes concretas

````
class Application {

    private final Button button;
    private final Checkbox checkbox;

    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void paint() {
        button.paint();
        checkbox.paint();
    }
}
````

9. É aqui que aparece a ideia de "família"

Se fizermos:

````
GUIFactory factory = new WindowsFactory();
````

````
WindowsFactory
      │
      ├── WindowsButton
      └── WindowsCheckbox
````

A factory garante que os produtos pertencem à **mesma família**.

----

Problem: Considere implementar a Fábrica Abstrata quando tiver uma classe com um conjunto de métodos de fábrica que obscurecem sua responsabilidade principal.

Solução: Cada classe é responsável apenas por uma coisa. Quando uma classe lida com múltiplos tipos de produto, pode valer a pena extrair seus métodos de fábrica para uma classe de fábrica independente ou para uma implementação completa de Fábrica Abstrata.

````
class Application {

    public void run() {
        // lógica da aplicação
    }

    public Button createButton() {
        return new WindowsButton();
    }

    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}
````



A estrutura completa

```` text
                         CLIENT
                           |
                           ↓
                      GUIFactory
                     <<interface>>
                           |
             ┌─────────────┴─────────────┐
             ↓                           ↓
      WindowsFactory                 MacFactory
             |                           |
       ┌─────┴─────┐               ┌─────┴─────┐
       ↓           ↓               ↓           ↓
WindowsButton WindowsCheckbox   MacButton  MacCheckbox
       ↑           ↑               ↑           ↑
       └─────┐     │               └─────┐     │
             ↓     ↓                     ↓     ↓
           Button Checkbox              Button Checkbox
````

Diferença para Factory Method

==Factory Method==
Normalmente tens **um produto**:

```` text
Creator
   |
   +-- createTransport()
             |
             ├── Truck
             └── Ship
````

A criação é normalmente delegada através de **subclasses**.

"Qual produto devo criar?"

````
createTransport()
       ↓
     Truck
       ou
      Ship
````

==Abstract Factory==
Tens **uma família de vários produtos relacionados**:

```` text
              GUIFactory
             /          \
            /            \
 WindowsFactory        MacFactory
      |                    |
      ├── Button           ├── Button
      └── Checkbox         └── Checkbox
````

A Abstract Factory fornece uma **interface para criar a família inteira**.

"Qual família de produtos devo criar?"

````
WindowsFactory
   ↓
WindowsButton
WindowsCheckbox
WindowsMenu
````


