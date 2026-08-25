Permite compor objetos em estruturas de árvore e, em seguida, trabalhar com essas estruturas, como se fossem objetos individuais.

A ideia central é permitir tratar **objetos individuais e grupos de objetos da mesma forma**, organizando-os numa estrutura em árvore.

![[Pasted image 20260825112508.png]]

## Problema

Só faz sentido quando o modelo principal da aplicação pode ser representado como uma árvore.

Exemplo: 2 tipos de objetos: `Products`e `Boxes`. A `Box` pode conter vários `Products`.
Sistema de pedidos que utiliza essas classes. Os pedidos podem conter produtos simples sem embalagem, bem como caixas com produtos e outras caixas. Como determinaria o preço total de um pedido desse tipo?

![[Pasted image 20260825113539.png]]

Abordagem direta: desembrulhar todas as caixas, percorrer todos os produtos e calcular o total.
Num programa, precisa de conhecer as classes `Product` e `Boxes`, o nível de aninhamento das caixas e outros detalhes complexos.

## Solução

`Composite` sugere que trabalhe com `Product` e `Boxes` através de uma interface comum que declara um método para calcular o preço total.

Para um produto, devolve o preço. Para uma caixa, analisa cada item que contém, para cada um, devolve o total da caixa. Se um desses items fosse uma caixa menor, o cálculo também analisaria o conteúdo da caixa.

## Analogia com o mundo real

![[Pasted image 20260825114129.png]]

Exércitos são estruturados em hierarquias. Um exército consiste em várias divisões; uma divisão é um conjunto de brigadas, e uma brigada é composta por pelotões, que podem ser divididos em esquadrões. As ordes são dadas no topo e repassadas.

## Estrutura

![[Pasted image 20260825114319.png]]


1. A interface `Component` descreve operações que são comuns tanto a elementos simples quanto a elementos complexos da árvore.

2. A `Leaf` é um elemento básico de uma árvore que não possui subelementos.
   Acabam por realizar a maior parte do trabalho real, não tem a quem delegar essa tarefa.

3. O `Container` (também conhecido como componente) é um elemento que possui subelementos: `Leaf` ou  outros `Container` . Um `Container` não conhece as classes concretas de seus filhos. Ele interage com todos os subelementos somente através da interface do componente.

   Ao receber uma solicitação, um `container` delega o trabalho aos seus subelementos, processa os resultados intermediários e, em seguida, devolve o resultado final ao cliente.

3. `Cliente` interage com todos os elementos através da interface de componentes. O `Cliente` pode trabalhar da mesma forma tanto com elementos simples quanto com elementos complexos da árvore.

## Pseudocódigo

![[Pasted image 20260825115430.png]]


`CompoundGraphic` é um container, pode conter qualquer número de subformas, incluíndo outras formas compostas. Uma forma composta tem os mesmos métodos que uma forma simples.
No entanto, em vez de executar uma ação por conta própria, uma forma composta passa a solicitação recursivamente para os seus filhos e "soma" o resultado.

O `Cliente` funciona com todas as formas através de uma única interface comum a todas as classes. O `Cliente` não sabe se está trabalhando com uma forma simples ou composta.
O `Cliente` pode trabalhar com estruturas de objetos muito complexas sem estar acoplado a classes concretas que forma essa estrutura.

## Aplicabilidade

Problema: `Composite` quando precisar implementar uma estrutura de objetos em forma de árvore.

Solução: `Composite` oferece 2 tipos básicos de elementos que partilham uma interface comum: `Leaf` e `Container`. Um `Container` pode ser composto por `Leaf` ou outros `Container`.
Permite construir uma estrutura recursiva aninhada.

---

Problema: quando for necessário que o código do cliente trate elementos simples e complexos de maneira uniforme.

Solução: Todos os elementos definidos por `Composite` partilham uma interface comum. Usando essa interface, o cliente não precisa de se preocupar com a classe concreta dos objetos com os quais trabalha.

## Como implementar

Primeiro: perceber o problema

````
Pasta
├── ficheiro.txt
├── imagem.png
└── Documentos
    ├── contrato.pdf
    └── currículo.pdf
````

Temos dois tipos de objetos:

- **Folhas (Leaf)** → ficheiros individuais
- **Contentores (Composite)** → pastas que podem conter ficheiros e outras pastas

A característica importante é:

> Uma pasta pode conter tanto ficheiros como outras pastas.

É precisamente isto que o Composite pretende representar.

1. Representar o modelo como uma árvore

````
                 Pasta
              /    |    \
             /     |     \
        Ficheiro  Pasta  Ficheiro
                   / \
                  /   \
             Ficheiro Ficheiro
````

Elemento simples
Não contém outros elementos - Ficheiro

Contentor
Pode conter outros elementos - Pasta

````
Contentor
    ↓
pode conter
    ↓
Folha OU outro Contentor
````

É isto que torna a estrutura **recursiva**.

2. Criar a interface Component

Agora temos de encontrar operações que façam sentido **tanto para uma folha como para um contentor**.

````
interface Component {
    void operation();
}
````

Porquê uma interface?

Porque queremos que o cliente possa fazer:

````
Component component;
````

sem precisar de saber se `component` é:
- Leaf
- Composite

Ambos são tratados como `Component`.

3. Criar a Leaf

````
class File implements Component {

    @Override
    public void operation() {
        System.out.println("Processando ficheiro");
    }
}
````

O `File` é uma **Leaf** porque não contém outros `Component`.

File
└── operation()

4. Criar o Composite

Agora criamos o contentor.
````
class Folder implements Component {

    private List<Component> children = new ArrayList<>();

    @Override
    public void operation() {
        for (Component child : children) {
            child.operation();
        }
    }
}
````

Aqui está uma das partes **mais importantes do padrão**:

````
List<Component>
````

Folder
├── File
├── File
└── Folder
├── File
└── File


5. Adicionar e remover filhos

````
class Folder implements Component {

    private List<Component> children = new ArrayList<>();

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
    }

    @Override
    public void operation() {
        for (Component child : children) {
            child.operation();
        }
    }
}
````

Agora conseguimos construir a árvore.

Folder root = new Folder();

File file1 = new File();
File file2 = new File();

Folder documents = new Folder();

File file3 = new File();
File file4 = new File();

documents.add(file3);
documents.add(file4);

root.add(file1);
root.add(file2);
root.add(documents);

Estrutura:

````
root
├── file1
├── file2
└── documents
    ├── file3
    └── file4
````

6. A parte mais importante: a recursividade

````
root.operation();
````

````
for (Component child : children) {
    child.operation();
}
````

````
root.operation()
       │
       ├── file1.operation()
       │
       ├── file2.operation()
       │
       └── documents.operation()
                    │
                    ├── file3.operation()
                    └── file4.operation()
````

7. O que significa "delegar o trabalho"?

O texto diz:

> "um contêiner deve delegar a maior parte do trabalho aos subelementos."

````
@Override
public void operation() {
    for (Component child : children) {
        child.operation();
    }
}
````

A pasta **não executa diretamente a operação de cada elemento**.

Ela delega:

````
Folder
   ↓
"Tu és um Component, portanto executa a tua operação."
   ↓
child.operation()
````


8. E o problema do add/remove na interface?

````
interface Component {

    void operation();

    void add(Component component);

    void remove(Component component);
}
````

Uma `Folder` precisa de:

```
add()
remove()
```

Mas um `File` não precisa deles.

Então, teoricamente, `File` teria de fazer algo como:

```
@Override
public void add(Component component) {
    // não faz nada
}

@Override
public void remove(Component component) {
    // não faz nada
}
```

Isto não é particularmente elegante.

É exatamente o problema que o texto menciona relativamente ao **Interface Segregation Principle**.

9. Porque é que mesmo assim podemos colocar add/remove?

Porque existe uma vantagem muito grande.

O cliente pode trabalhar apenas com:

```
Component
```

Por exemplo:

```
Component component = ...;

component.operation();
component.add(...);
```

O cliente não precisa de saber se recebeu:

```
File
```

ou:

```
Folder
```

Isto torna o código cliente muito mais simples.

É uma escolha entre:

```
Interface mais limpa
        VS
Cliente mais simples
```

O Composite permite as duas abordagens.


10. A versão completa

Uma implementação simples ficaria assim:

```
interface Component {

    void operation();

    void add(Component component);

    void remove(Component component);
}
```

### Leaf

```
class File implements Component {

    @Override
    public void operation() {
        System.out.println("Processando ficheiro");
    }

    @Override
    public void add(Component component) {
        // Não aplicável
    }

    @Override
    public void remove(Component component) {
        // Não aplicável
    }
}
```

### Composite

```
class Folder implements Component {

    private List<Component> children = new ArrayList<>();

    @Override
    public void operation() {
        for (Component child : children) {
            child.operation();
        }
    }

    @Override
    public void add(Component component) {
        children.add(component);
    }

    @Override
    public void remove(Component component) {
        children.remove(component);
    }
}
````


11. O desenho mental que deves guardar

Para estudar Composite, eu guardaria principalmente esta estrutura:

```
                 Component
                /         \
               /           \
            Leaf         Composite
                           │
                           │
                    List<Component>
                           │
                ┌──────────┴──────────┐
                ↓                     ↓
              Leaf                Composite
                                      │
                                      ↓
                                    Leaf
```

A relação fundamental é:

```
Composite
    ↓
contém
    ↓
Component
```

E como `Composite` também é um `Component`:

```
Composite
    ↓
pode conter
    ↓
Composite
    ↓
pode conter
    ↓
Composite
...
```

É isso que cria a **árvore recursiva**.

### Resumindo os 5 passos

|Passo|O que fazemos|
|---|---|
|**1**|Identificamos uma estrutura em árvore|
|**2**|Criamos `Component`|
|**3**|Criamos `Leaf` para objetos simples|
|**4**|Criamos `Composite` para objetos que contêm outros|
|**5**|Adicionamos `add()` / `remove()` para construir a árvore|
A frase mais importante para memorizar é:

> **Composite permite tratar objetos individuais e composições de objetos através da mesma interface.**

No exemplo:

```
File       → objeto individual
Folder     → composição
Component  → interface comum
```

E é precisamente por `Folder` também ser um `Component` que conseguimos criar **árvores de profundidade arbitrária**.