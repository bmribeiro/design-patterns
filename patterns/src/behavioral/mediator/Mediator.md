Permite reduzir dependências caóticas entre objetos. `Mediator` restringe a comunicação direta entre os objetos e os força a colaborar apenas por meio de um objeto mediador.

**Exemplos de uso:** O uso mais comum do padrão Mediator em código Java é facilitar a comunicação entre os componentes da interface gráfica de um aplicativo. O sinônimo de Mediator é o componente Controller do padrão MVC.

### Exemplos nas Java Core Libraries

- [`java.util.Timer`](http://docs.oracle.com/javase/8/docs/api/java/util/Timer.html)(todos `scheduleXXX()`os métodos)
- [`java.util.concurrent.Executor#execute()`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executor.html#execute-java.lang.Runnable-)
- [`java.util.concurrent.ExecutorService`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)( `invokeXXX()`e `submit()`métodos)
- [`java.util.concurrent.ScheduledExecutorService`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html)(todos `scheduleXXX()`os métodos)
- [`java.lang.reflect.Method#invoke()`](http://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html#invoke-java.lang.Object-java.lang.Object...-)



![[Mediator 1.png]]


## Problema

Caixa de diálogo para criar e editar perfis de clientes. Ela consiste em vários controles de formulário, como campos de texto, caixas de seleção, botões, etc.

![[Mediator 2.png]]

Alguns elementos podem interagir entre si. Selecionar a `Checkbox` pode revelar um campo de texto oculto. Outro, é o botão de envio que precisa validar valores de todos os campos.

![[Mediator 3.png]]

Ao implementar lógica diretamente no código dos elementos, torna mais difícil reutilizar as classes desses elements noutros formulários.

## Solução

`Mediator` sugere que devem colaborar indiretamente chamando um objeto `Mediator` que redireciona as chamadas para os componentes apropriados. Os componentes dependem apenas de uma única classe mediadora.

No formulário edição de perfil, a própria classe `Dialog` pode atuar como `Mediator`. A `Dialog` já conhece todos os seus subelementos não sendo necessário introduzir novas dependências.

![[Mediator 4.png]]

A mudança mais significativa ocorre nos próprios elementos. Ex. Botão `Enviar`, antes cada vez que o utilizador clicava, precisava de validar os valores de todos os elementos individuais do formulário. Agora, a única função é notificar a `Dialog` sobre o click. Ao receber essa notificação, a `Dialog` realiza as validações ou passa a tarefa para os elementos individuais.

Ir mais além, tornar a dependência mais fléxivel extraindo uma interface comum para todos os `Dialogs`. A interface declara o método de notificação que todos os elementos do formulário podem usar.

O `Mediator` permite encapsular uma rede de relações entre vários objetos dentro de um único mediador. Quanto menos dependências uma classe tiver, mais fácil será modificá-la, estendê-la ou reutilizá-la.


## Analogia com o mundo real

![[Mediator 5.png]]

Os pilotos de aeronaves não se comunicam diretamente entre si para decidir quem irá pousar o avião em seguida. Toda a comunicação é feita através da torre de controle.

Sem o controlador de tráfego aéreo, os pilotos precisariam estar cientes de todas as aeronaves.

## Estrutura

![[Mediator 6.png]]

1. `Components` são várias classes que contém alguma lógica de negócio. Cada componente possui uma referência a um `Mediator`. O `Component` não tem conhecimento da classe real do `Mediator`, por isso pode reutilizar o `Component` em outros programas vinculando-o a um `Mediator` diferente.

2. Interface `Mediator` declara os métodos de comunicação com componentes, um único método de notificação. Os `Component` podem passar qualquer contexto como argumentos para esse método, incluindo seus próprios objetos, mas somente de forma que não ocorra acoplamento entre o componente receptor e a classe do remetente.

3. `Mediator Concreto` encapsulam relações entre vários componentes. Frequentemente, mantém referências a todos os componentes que gerenciam e, ás vezes, até o ciclo de vida.

4. Os componentes não devem ter conhecimento uns dos outros, devem apenas notificar o `Mediator`. Quando o `Mediator` recebe a notificação, ele pode identificar o remetente, o que pode ser suficiente para decidir qual `Component` deve ser acionado em resposta.

## Pseudocódigo

![[Mediator 8.png]]

Um elemento, acionado por um usuário, não se comunica diretamente com outros elementos, mesmo que pareça que deveria. Em vez disso, o elemento só precisa informar seu mediador sobre o evento, passando qualquer informação contextual junto com a notificação.

## Aplicabilidade

problema: `Mediator` quando for difícil alterar algumas classes porque elas estão fortemente acopladas a várias outras classes

solução: `Mediator` permite extrair todos os relacionamentos entre as classes para uma classe separada, isolando quaisquer alterações num componente específico dos demais componentes

---

problema: `Mediator` quando não for possível reutilizar um componente num programa diferente porque ele depende muito de outros componentes.

solução: com `Mediator` os componentes individuais tornam-se independentes uns dos outros. Ainda podem comunicar entre si, embora indiretamente, por meio de um `Mediator`. Para reutilizar um componente numa aplicação diferente, precisa fornecer nova classe `Mediator`.

---

problema: usar o `Mediator` quando for necessário criar inúmeras subclasses de componentes apenas para reutilizar comportamentos básicos em diversos contextos

problema: como todas as relações entre componentes estão contidas no `Mediator`, é fácil definir maneiras completamente novas para esses componentes colaborarem, introduzindo novas classes de `Mediator` sem precisar de alterar os próprios componentes.

## Como implementar


O **Mediator (Mediador)** serve para evitar que várias classes precisem conhecer e chamar diretamente umas às outras.

A ideia central é:

> **Em vez de os objetos conversarem diretamente entre si, eles conversam através de um objeto Mediador.**

# Mediator — Como implementar passo a passo

Imagine que temos vários usuários em um sistema de chat:

```
UserA ───────► UserB
  │              │
  ├────────────► UserC
  │              │
  └────────────► UserD
```

Se cada usuário precisar conhecer todos os outros, teremos muito acoplamento.

Com Mediator:

```
UserA ──►
UserB ──►  ChatMediator  ◄── UserC
UserD ──►
```

Agora os usuários **não precisam conhecer uns aos outros**.
#### 1. Identifique as classes fortemente acopladas

Primeiro, procure classes que ficam dependendo diretamente umas das outras.

Por exemplo:

```
class UserA {
    private UserB userB;
    private UserC userC;

    public void sendMessage(String message) {
        userB.receive(message);
        userC.receive(message);
    }
}
```

E talvez:

```
class UserB {
    private UserA userA;
    private UserC userC;
}
```

O problema é que as classes começam a conhecer umas às outras.

Imagine 20 usuários:

```
User1 ──► User2
       ├─► User3
       ├─► User4
       └─► User5

User2 ──► User1
       ├─► User3
       ├─► User4
       └─► User5
```

O número de dependências cresce bastante.

### O que queremos?

Centralizar essa comunicação:

```
User1 ──┐
User2 ──┤
User3 ──┼──► ChatMediator
User4 ──┤
User5 ──┘
```

Agora cada usuário só precisa conhecer o **Mediator**.



#### 2. Declare a interface do Mediator

Agora criamos uma interface que define como os componentes vão conversar com o mediador.

Por exemplo:

```
interface ChatMediator {

    void sendMessage(String message, User sender);

    void addUser(User user);
}
```

Temos aqui dois conceitos importantes.

### `sendMessage()`

É usado pelo componente para avisar o mediador:

```
mediator.sendMessage("Olá!", this);
```

O usuário está dizendo:

> "Mediator, eu quero enviar esta mensagem."

Ele **não precisa saber para quem a mensagem será enviada**.

---

### `addUser()`

Permite registrar componentes no mediador:

```
mediator.addUser(user);
```

O Mediator passa a conhecer os usuários.

---

## Por que usar uma interface?

Porque o componente pode depender de:

```
ChatMediator
```

em vez de:

```
ChatMediatorImpl
```

Por exemplo:

```
class User {

    private ChatMediator mediator;

    public User(ChatMediator mediator) {
        this.mediator = mediator;
    }
}
```

Isso permite trocar a implementação:

```
User
 │
 ▼
ChatMediator
 │
 ├── ChatMediatorImpl
 ├── AnotherChatMediator
 └── TestChatMediator
```

O `User` não precisa saber qual implementação está sendo utilizada.

#### 3. Implemente o Mediator concreto

Agora criamos a implementação real:

```
class ChatMediatorImpl implements ChatMediator {

    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void sendMessage(String message, User sender) {

        for (User user : users) {

            if (user != sender) {
                user.receive(message);
            }
        }
    }
}
```

Aqui está a parte mais importante do padrão.

O Mediator possui:

```
private List<User> users;
```

Portanto ele conhece os componentes.

Quando recebe:

```
sendMessage(...)
```

ele decide o que fazer.

Nesse caso:

```
for (User user : users) {
    if (user != sender) {
        user.receive(message);
    }
}
```

Ou seja:

> "O User A me avisou que quer enviar uma mensagem. Vou decidir quais usuários devem recebê-la."


#### 4. O Mediator pode criar os componentes

Esse passo é **opcional**.

O Mediator pode também ser responsável por criar os componentes.

Por exemplo:

```
class ChatMediatorImpl implements ChatMediator {

    private List<User> users = new ArrayList<>();

    public User createUser(String name) {
        User user = new User(this, name);
        users.add(user);
        return user;
    }

    // ...
}
```

Agora temos:

```
ChatMediator
      │
      ├── cria User
      ├── registra User
      └── coordena User
```

Por isso o Mediator pode começar a parecer uma:

- **Factory**
- **Facade**

Mas cuidado:

> **Mediator não é obrigatoriamente uma Factory ou Facade.**

Ele pode simplesmente coordenar a comunicação.

#### 5. Os componentes armazenam uma referência ao Mediator

Agora modificamos o `User`.

```
class User {

    private ChatMediator mediator;
    private String name;

    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
}
```

O usuário possui uma referência para o Mediator:

```
User
 │
 └────► ChatMediator
```

Mas observe uma coisa importante:

O User **não possui referências aos outros usuários**.

Não temos:

```
private User userA;
private User userB;
private User userC;
```

Temos apenas:

```
private ChatMediator mediator;
```

Essa é justamente a redução do acoplamento.

#### 6. Os componentes avisam o Mediator

Agora chegamos à parte fundamental.

Antes, poderíamos ter algo assim:

```
public void sendMessage(String message) {

    userA.receive(message);
    userB.receive(message);
    userC.receive(message);
}
```

O componente está controlando outros componentes diretamente.

Com Mediator:

```
public void sendMessage(String message) {

    mediator.sendMessage(message, this);
}
```

Agora o `User` simplesmente diz:

> "Mediator, quero enviar esta mensagem."

Ele não sabe o que acontecerá depois.

---

# Fluxo completo

Vamos juntar tudo.

## Mediator

```
interface ChatMediator {

    void sendMessage(String message, User sender);

    void addUser(User user);
}
```

---

## Mediator concreto

```
class ChatMediatorImpl implements ChatMediator {

    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void sendMessage(String message, User sender) {

        for (User user : users) {

            if (user != sender) {
                user.receive(message);
            }
        }
    }
}
```

---

## Componente

```
class User {

    private ChatMediator mediator;
    private String name;

    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public void send(String message) {
        mediator.sendMessage(message, this);
    }

    public void receive(String message) {
        System.out.println(name + " recebeu: " + message);
    }
}
```

---

## Cliente

```
public class Main {

    public static void main(String[] args) {

        ChatMediator mediator = new ChatMediatorImpl();

        User bruno = new User(mediator, "Bruno");
        User joao = new User(mediator, "João");
        User maria = new User(mediator, "Maria");

        mediator.addUser(bruno);
        mediator.addUser(joao);
        mediator.addUser(maria);

        bruno.send("Olá pessoal!");
    }
}
```

O resultado seria aproximadamente:

```
João recebeu: Olá pessoal!
Maria recebeu: Olá pessoal!
```

---

# O que aconteceu?

Quando fazemos:

```
bruno.send("Olá pessoal!");
```

o `User` executa:

```
mediator.sendMessage("Olá pessoal!", this);
```

Então:

```
Bruno
  │
  │ sendMessage()
  ▼
ChatMediator
  │
  │ percorre os usuários
  ├──────────────► João
  │
  └──────────────► Maria
```

O Bruno **não chamou João nem Maria diretamente**.

Quem fez isso foi o Mediator.

---

#### Comparando antes e depois

##### Sem Mediator

```
        ┌────► User B
        │
User A ─┼────► User C
        │
        └────► User D
```

Os componentes conhecem uns aos outros.

Isso gera:

**alto acoplamento**.

---

##### Com Mediator

```
User A ──┐
User B ──┤
User C ──┼──► Mediator
User D ──┘
```

Os componentes conhecem apenas o Mediator.

Isso gera:

**menor acoplamento entre componentes**.

---

#### A ideia mais importante do passo 6

Este é provavelmente o ponto que mais importa entender.

Imagine que temos:

```
class Button {
    private TextBox textBox;
    private CheckBox checkBox;
    private Label label;
}
```

O `Button` começa a controlar tudo:

```
textBox.clear();
checkBox.uncheck();
label.setText("...");
```

Com Mediator:

```
class Button {

    private Mediator mediator;

    public void click() {
        mediator.notify(this, "click");
    }
}
```

E então:

```
class ConcreteMediator implements Mediator {

    public void notify(Component sender, String event) {

        if (sender instanceof Button) {
            // coordena os outros componentes
        }
    }
}
```

A lógica de coordenação sai do `Button` e vai para o Mediator.

Portanto:

> **O componente sabe que algo aconteceu; o Mediator decide o que fazer em resposta.**

---

#### Resumindo os 6 passos

|Passo|O que fazemos|
|---|---|
|**1**|Encontramos classes que estão muito dependentes umas das outras|
|**2**|Criamos a interface `Mediator`|
|**3**|Criamos o `ConcreteMediator`, que coordena os componentes|
|**4**|Opcionalmente, o Mediator também pode criar/destruir componentes|
|**5**|Cada componente recebe uma referência para o Mediator|
|**6**|Componentes deixam de chamar outros componentes diretamente e notificam o Mediator|

### Decore esta frase:

> **Componentes não conversam diretamente entre si; eles notificam o Mediator, e o Mediator coordena a comunicação.**

Essa é a essência do **Mediator**.
## Prós

-  _Princípio da Responsabilidade Única_ . Você pode centralizar as comunicações entre vários componentes em um único local, facilitando a compreensão e a manutenção.
-  _Princípio Aberto/Fechado_ . Você pode introduzir novos mediadores sem precisar alterar os componentes originais.
- É possível reduzir o acoplamento entre os diversos componentes de um programa.
- Você pode reutilizar componentes individuais com mais facilidade.
## Contras

- Com o tempo, um mediador pode evoluir para um [Objeto de Deus](https://refactoring.guru/antipatterns/god-object) .