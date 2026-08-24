
**Prototype** é um padrão de projeto criacional que permite copiar objetos existentes sem tornar seu código dependente de suas classes.

> **Em vez de criares um objeto complexo do zero, copias um objeto que já está configurado.**


### Exemplos nas Java Core Libraries
- [`java.lang.Object#clone()`](http://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#clone--)(a classe deve implementar a [`java.lang.Cloneable`](http://docs.oracle.com/javase/8/docs/api/java/lang/Cloneable.html)interface)

**Identificação:** O protótipo pode ser facilmente reconhecido por meio de um `clone`ou mais `copy`métodos, etc.

==Problema:==

Existe um objeto e é necessário criar uma cópia exata.
Primeiro é preciso criar um novo objeto. Percorrer todos os campos e copiar valores.

- Nem todos os campos podem ser copiados dessa forma. Alguns podem ser privados e não visíveis fora do próprio objeto.
- precisa conhecer a classe do objeto para criar um duplicado, torna o código dependente dessa classe.
- por vezes só se conhece a interface, não a sua classe concreta

==Solução:==

O padrão Prototype delega o processo de clonagem aos objetos reais que estão sendo clonados. Declara uma interface comum para todos os objetos que suportam clonagem.
Essa interface permite clonar um objeto sem acoplar seu código á classe desse objeto.
Tal interface contém apenas um método `clone`.

Um objeto que suporta clonagem é chamado de protótipo.

## Analogia com o mundo real

Os prótotipos são usados para realizar diversos testes antes de iniciar a produção em massa de um produto. Não participam de nenhuma produção real mas desempenham um papel passivo.

Exemplo: processo de divisão celular mitótica.
A célula original atua como um protótipo e desempenha um papel ativo na criação da cópia.

## Estrutura

![[Prototype 1.png]]

1. Interface Prototype declara os métodos de clonagem.
   Na maioria dos casos, um método `clone`.

2. Classe `Concrete Prototype` implementa o método de clonagem.
   Além de copiar os dados do objeto original para o clone, pode também lidar com casos extremos do processo de clonagem relacionados à clonagem de objetos vinculados, à resolução de dependências recursivas, etc

3. O cliente pode produzir uma cópia de qualquer objeto que siga a interface do protótipo.

![[Prototype 2.png]]

O **Registro de Protótipos** oferece uma maneira fácil de acessar protótipos usados ​​com frequência. Armazena um conjunto de objetos pré-construídos que estão prontos para ser copiados.

## Pseudocódigo

Produzir cópias exatas de objetos sem acoplar o código ás suas classes.

![[Pasted image 20260824113934.png]]

## Aplicabilidade

Problema:
Quando o código não precisa depender das classes concretas dos objetos que são preciso copiar.

Solução:
Quando o código trabalha com objetos passado por terceiros através de uma interface.
As classes concretas desse objeto são desconhecidas e não poderia depender delas.

O Prototype fornece ao código do cliente uma interface geral para trabalhar com todos os objetos que suportam clonagem. Essa interface torna o código do cliente independente das classes concretas dos objetos que clona.

---

Problema:
Quando é desejado reduzir o número de subclasses que diferem apenas na forma como inicializam seus respetivos objetos.

## Como implementar

1. Criar a interface Prototype

Primeiro, precisas de definir uma interface que diga:

> "Qualquer objeto que possa ser clonado deve saber criar uma cópia de si próprio."3

````text
interface Prototype {
    Prototype clone();
}
````

Uma classe concreta implementa essa interface:

````text
class Circle implements Prototype {

    @Override
    public Circle clone() {
        return new Circle(this);
    }
}
````

Estamos a dizer:

> Cria um novo `Circle` usando este `Circle` atual como modelo.

2. Criar o construtor de cópia

A classe deve ter um construtor que recebe **outro objeto da mesma classe**.

````text
class Circle implements Prototype {

    private int radius;
    private String color;

	// Construtor Normal
    public Circle(int radius, String color) {
        this.radius = radius;
        this.color = color;
    }

	// Utilizado para criar um novo círculo a partir de outro
    public Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }
}
````


3. Implementar `clone()`

````text
@Override
public Circle clone() {
    return new Circle(this);
}
````

````text
original                    copia
┌──────────────┐            ┌──────────────┐
│ radius = 50  │            │ radius = 50  │
│ color = red  │            │ color = red  │
└──────────────┘            └──────────────┘
       ↑                            ↑
       └──── objetos diferentes ────┘
````

4. Por que cada classe deve implementar o próprio `clone()`?

````text
class Shape {

    public Shape clone() {
        return new Shape(this);
    }
}
````

````text
Circle circle = ...;

Shape copy = circle.clone();
````

Resultado: Shape

O objetivo é:
circle.clone()

Resultado: Circle

Cada classe concreta implementa explicitamente.

````text
@Override
public Circle clone() {
    return new Circle(this);
}

@Override
public Rectangle clone() {
    return new Rectangle(this);
}
````

5. E se existir uma classe pai?

Aqui entra esta parte do texto:

> "Se você estiver alterando uma subclasse, deve chamar o construtor da classe pai..."

````text
class Shape {

    protected int x;
    protected int y;

    public Shape(Shape source) {
        this.x = source.x;
        this.y = source.y;
    }
}
````

````text
class Circle extends Shape {

    private int radius;

    public Circle(Circle source) {
        super(source);
        this.radius = source.radius;
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }
}
````

O
super(source);

Ele diz:

> "Pai, copia os teus campos."

````text
Circle
 │
 ├── x
 ├── y
 └── radius
````


Quando fazemos:
new Circle(this)

Acontece:
````text
Circle(Circle source)
       │
       ├── super(source)
       │      │
       │      ├── copia x
       │      └── copia y
       │
       └── copia radius
````

6. O que significa "copiar todos os campos"?

Aqui aparece uma questão importante: **shallow copy vs deep copy**.

````text
class Circle {

    private int radius;
    private Point position;
}
````

this.radius = source.radius; - OK

this.position = source.position; - Copia a **referência**.

````text
original                    clone
   │                           │
   │ position ─────────────┐   │
   │                       │   │
   ▼                       ▼   ▼
        Point
````

Os dois objetos podem estar a apontar para o mesmo `Point`.

Se quiseres uma cópia independente, precisas de clonar também esse objeto:
Isto já é **deep copy**.

Portanto, Prototype não significa automaticamente que todos os objetos internos sejam copiados profundamente. A implementação do construtor de cópia é que determina isso.

7. O registro de protótipos

````
Circle vermelho
Circle azul
Rectangle vermelho
Rectangle azul
````

Poderias guardá-los num catálogo:

Map<String, Shape> prototypes = new HashMap<>();

````text
"red-circle" → Circle vermelho 
"blue-circle" → Circle azul 
"red-rectangle" → Rectangle vermelho
````

Esse catálogo é o **Prototype Registry**.

8. Exemplo completo do Registry

````
class PrototypeRegistry {

    private Map<String, Prototype> prototypes = new HashMap<>();

    public void add(String key, Prototype prototype) {
        prototypes.put(key, prototype);
    }

    public Prototype get(String key) {
        return prototypes.get(key).clone();
    }
}
````

Registar Protótipos:

````
PrototypeRegistry registry = new PrototypeRegistry();

registry.add(
    "red-circle",
    new Circle(50, "red")
);

registry.add(
    "blue-circle",
    new Circle(100, "blue")
);
````

E depois pedir cópias:
Circle circle = (Circle) registry.get("red-circle");

9. Por que isto é útil?

Sem Registry:
````
Circle circle = new Circle(50, "red");
````

O cliente precisa conhecer:

- `Circle`
- os parâmetros necessários
- como configurar o objeto

Com Registry:

````
Prototype circle = registry.get("red-circle");
````

O cliente apenas diz:

> "Quero o protótipo `red-circle`."

Não precisa saber como ele é construído.


10. A arquitetura completa

````text
                    Prototype
                       │
                 ┌─────┴─────┐
                 │            │
              Circle       Rectangle
                 │            │
               clone()      clone()
                 │            │
                 └─────┬──────┘
                       │
                       ▼
              Prototype Registry
                       │
          ┌────────────┼────────────┐
          │            │            │
     red-circle   blue-circle   red-rectangle
          │            │            │
          ▼            ▼            ▼
        clone        clone        clone
          │            │            │
          ▼            ▼            ▼
       Circle        Circle      Rectangle
````

Se estiveres a estudar Design Patterns, eu resumiria toda esta implementação assim:

> **Prototype cria novos objetos através da cópia de objetos existentes, utilizando um método `clone()` e, normalmente, um construtor de cópia para duplicar o estado do objeto.**

````text
1. Prototype
      ↓
   define clone()

2. Copy Constructor
      ↓
   copia o estado

3. clone()
      ↓
   new Classe(this)

4. Registry (opcional)
      ↓
   guarda protótipos
   e devolve clones
````

A parte mais importante é perceber que **o Prototype não é simplesmente "usar `clone()`"**. O objetivo do padrão é **evitar reconstruir objetos complexos e reduzir dependências das classes concretas**, usando objetos já configurados como modelos.
