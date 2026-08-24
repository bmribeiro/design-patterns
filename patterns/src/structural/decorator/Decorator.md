**Decorator** é um padrão de projeto estrutural que permite associar novos comportamentos a objetos, colocando esses objetos dentro de objetos encapsuladores especiais que contêm os comportamentos.

### Exemplos nas Java Core Libraries

- Todas as subclasses de [`java.io.InputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), [`OutputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html), [`Reader`](http://docs.oracle.com/javase/8/docs/api/java/io/Reader.html)e [`Writer`](http://docs.oracle.com/javase/8/docs/api/java/io/Writer.html)possuem construtores que aceitam objetos do seu próprio tipo.

- [`java.util.Collections`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html), métodos [`checkedXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#checkedCollection-java.util.Collection-java.lang.Class-)e [`synchronizedXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#synchronizedCollection-java.util.Collection-).[`unmodifiableXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#unmodifiableCollection-java.util.Collection-)

- [`javax.servlet.http.HttpServletRequestWrapper`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletRequestWrapper.html)e[`HttpServletResponseWrapper`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletResponseWrapper.html)

**Identificação:** Um decorador pode ser reconhecido por seus métodos de criação ou construtores que aceitam objetos da mesma classe ou interface que a classe atual.
## Problema

Biblioteca de notificações que permite que outros programas notifiquem seus utilizadores sobre eventos importantes.

Versão inicial da biblioteca era baseada numa classe `Notifier` que tinha aguns campos, um construtor e um único método `send`. O método recebia um argumento de mensagem de um cliente e enviar a mensagem para uma lista de e-mails passados no construtor.
Uma aplicação de terceiros, atuava como cliente, deveria criar e configurar o objeto notificador uma única vez e utilizá-lo sempre que algo acontecesse.

![[Pasted image 20260824163610.png]]

Utilizadores da biblioteca esperam mais do que apenas notificações por email. Gostariam de receber SMS, notificações FB, SLACK etc

![[Pasted image 20260824163701.png]]

Métodos de notificação adicionais em novas subclasses. O cliente deveria instancar a classe de notificação e usá-la.
Por que não utilizar notificação ao mesmo tempo? Subclasses especiais que combinavam vários métodos de notificação numa única classe.

## Solução

Estender uma classe é a primeira solução que vem à mente para alterar comportamento.
Existem ressalvas:
- A herança é estática. Não pode alterar o comportamento de um objeto existente em tempo de execução. Pode sim, substituir o objeto inteiro por outro criado a partir de uma subclasse diferente.
- Subclasses podem ter apenas uma classe pai. Não permite herança multipla.

Forma de superar as limitações é usand agregação ou composição.
Ambas funcionam de forma quase idêntica. Um objeto tem uma referência a outro e delega a ele algumas tarefas, enquanto que, com a herança, o próprio objeto é capaz de realizar essa tarefa, herdando o comportamento da superclasse.

Com essa abordagem, pode substituir o objeto "auxiliar" vinculado por outro, alterando o comportamento do container em tempo de execução. Um objeto pode usar o comportamento de várias classes, tendo referências a múltiplos objetos e delegando a eles todos os tipos de trabalho.

Agregação / composição é o princípio fundamental por trás de muitos padrões de projeto, incluindo o Decorator.

![[Pasted image 20260824164940.png]]

"Wrapper" é um apelido alternativo para o padrão Decorator.

Um wrapper é um objeto que pode ser vinculado a um objeto alvo. Contém o mesmo conjunto de métodos que o alvo e delega a ele todas as requisições que recebe. No entanto, pode alterar o resultado executando alguma ação antes ou depois de passar a requisição para o alvo.

Quando um wrapper se torna um decorator?
O Wrapper implementa a mesma interface que o objeto encapsulado. Por isso da perspetiva do cliente, esses objetos são idênticos. Faça com que o campo de referência do wrapper aceite qualquer objeto que siga essa interface. Isso permitirá que cubra um objeto com múltiplos wrappers, adicionando um comportamento combinado de todos eles.

![[Pasted image 20260824170525.png]]


O código do cliente precisaria encapsular um objeto notificador básico em um conjunto de decoradores que correspondam às preferências do cliente. Os objetos resultantes serão estruturados como uma pilha.

![[Pasted image 20260824170809.png]]

O último decorador na pilha seria o objeto com o qual o cliente realmente interage. Como todos os decoradores implementam a mesma interface que o notificador base, o restante do código do cliente não se importará se está trabalhando com o objeto notificador "puro" ou com o decorado.


## Analogia com o mundo real

![[Pasted image 20260824170829.png]]

Vestir é um exemplo de uso de elementos decorators.

## Estrutura

![[Pasted image 20260824170959.png]]

1. O `Componente` declara a interface comum para os elementos encapsulados
2. Um `componente concreto` é uma classe de objetos que está sendo encapsulada. Ele define o comportamento básico, que pode ser alterado por decoradores.
3. A classe `Decorator base` possui um campo para referenciar um objeto encapsulado. O tipo do campo de ser declarado como a interface do componente para que possa conter tanto componentes concretos quanto decoradores. O Decorador base delega todas as operações ao objeto encapsulado.
4. `Decoradores concretos` definem comportamentos extras que podem ser adicionados dinamicamente aos componentes. Decoradores concretos subscrevem os métodos do decorador base e executam seu comportamento antes ou depois da chamada do método pai.
5. O `Cliente` pode envolver componentes em múltiplas camadas de decoradores, desde que funcione com todos os objetos da interface do componente.

## Pseudocódigo

![[Pasted image 20260824172307.png]]

A aplicação envolve o objeto de fonte de dados com um par de decoradores. Ambos os decoradores alteram a forma como os dados são gravados e lidos do disco:

- Pouco antes dos dados serem **gravados em disco** , os decoradores os criptografam e compactam. A classe original grava os dados criptografados e protegidos no arquivo sem ter conhecimento da alteração.

- Logo após os dados serem **lidos do disco** , eles passam pelos mesmos decoradores, que os descompactam e decodificam.


## Aplicabilidade

Problema: Decorator quando precisar atribuir comportamentos extra a objetos em tempo de execução sem partir o código que utiliza esses objetos.

Solução: O decorator permite estruturar a lógica de negócios em camadas, criar um decorator para cada camada e compor objetos com várias combinações desse lógica em tempo de execução. O Código do cliente pode tratar todos esses objetos da mesma maneira, já que todos seguem uma interface comum.

---

Problema: Utilizar Decorator quando for complicado ou impossível estender o comportamento de um objeto usando herança

Solução: classes `final` pode ser impedidas para estender uma classe. A única maneira de reutilizar o comportamento existente seria envolver a classe com um wrapper próprio, utilizando o Decorator.

## Como implementar

1. Identificar o componente principal e as camadas opcionais

Primeiro pergunta:

> Existe um objeto principal ao qual posso adicionar funcionalidades opcionais?

Exemplo:

````text
Notificação
    │
    ├── Email
    ├── SMS
    └── Slack
````

Podemos ter:

````
Notificação básica
````

Ou

````
Notificação básica
      +
Email
````

Ou

````
Notificação básica
      +
Email
      +
SMS
````

Isto é importante porque **não queremos criar uma classe para cada combinação**:

````
NotificationWithEmail
NotificationWithSMS
NotificationWithEmailAndSMS
NotificationWithEmailAndSlack
NotificationWithEmailAndSMSAndSlack
...
````
Isso criaria uma explosão de subclasses.

O Decorator permite fazer:
````
Slack
  ↓
SMS
  ↓
Email
  ↓
Notification
````

2. Criar a interface comum

Agora precisamos descobrir:

> O que é que a notificação básica e os decorators têm em comum?

Exemplo:

````
interface Notification {

    void send(String message);
}
````

Temos:

````
Notification
     │
     └── send()
````

Esta interface é fundamental.

Porquê?

Porque queremos que o cliente possa trabalhar tanto com:

````
Notification
````

como com:

````
EmailDecorator
SMSDecorator
SlackDecorator
````

sem precisar de saber qual é a implementação concreta.

3. Criar o componente concreto

Agora criamos a implementação básica:

````
class BasicNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending notification: " + message);
    }
}
````

Notification
▲
│
│ implements
│
BasicNotification

Este objeto representa o **comportamento base**.

````
Notification notification = new BasicNotification();

notification.send("Hello");
````

Produz:
Sending notification: Hello

4. Criar o Decorator base

````
abstract class NotificationDecorator
        implements Notification {

    protected final Notification notification;

    public NotificationDecorator(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void send(String message) {
        notification.send(message);
    }
}
````

Primeiro:

````
implements Notification
````

O decorator também é uma `Notification`.

Notification
▲
│
NotificationDecorator


Segundo:

````
protected final Notification notification;
````

O decorator guarda uma referência para outro objeto `Notification`.

Isto é **composição**.

NotificationDecorator
│
│ contém
▼
Notification

E aqui está uma decisão muito importante:

`Notification` e não `BasicNotification`

Porquê?

Porque queremos poder colocar:

````
Decorator
   ↓
BasicNotification
````

Mas também:

````
Decorator
   ↓
Decorator
   ↓
BasicNotification
````

Como todos implementam `Notification`, isto é possível.

5. Fazer todos implementarem a mesma interface

````text
                    Notification
                    ▲          ▲
                    │          │
                    │          │
       BasicNotification   NotificationDecorator
                                  ▲
                                  │
                         ┌────────┼────────┐
                         │        │        │
                      Email      SMS     Slack
                    Decorator  Decorator Decorator
````

Todos são `Notification`.

Isto permite fazer:

````
Notification notification;
````

e colocar dentro:

````
new BasicNotification()
````

ou

````
new EmailDecorator(...)
````

ou

````
new SMSDecorator(...)
````

ou até:

````
new SMSDecorator(
    new EmailDecorator(
        new BasicNotification()
    )
);
````

6. Criar os decorators concretos


Agora criamos os decorators que realmente adicionam comportamento.

````
class EmailDecorator extends NotificationDecorator {

    public EmailDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("Sending email");
    }
}
````

Repara nesta parte:
````
super.send(message);
````

O decorator base vai delegar para:
````
notification.send(message);
````

Depois podemos adicionar o nosso comportamento:
````
System.out.println("Sending email");
````

7. O cliente compõe os decorators

O cliente decide quais funcionalidades quer.

Apenas notificação básica
````
Notification notification = new BasicNotification();
````

Notificação + Email
````
Notification notification =
    new EmailDecorator(
        new BasicNotification()
    );
````

Estrutura:

EmailDecorator
↓
BasicNotification

Notificação + Email + SMS

````
Notification notification =
    new SMSDecorator(
        new EmailDecorator(
            new BasicNotification()
        )
    );
````

Estrutura:

SMSDecorator
↓
EmailDecorator
↓
BasicNotification

8. O que acontece quando chamamos `send()`?

notification.send("Hello");

````
SMSDecorator
      ↓
EmailDecorator
      ↓
BasicNotification
````

A execução começa no `SMSDecorator`:

````
super.send(message);
````

O decorator base chama:

````
notification.send(message);
````

que chama o `EmailDecorator`.

O `EmailDecorator` faz novamente:

````
super.send(message);
````

que chega ao `BasicNotification`.

Portanto:

````
SMSDecorator
     │
     │ super.send()
     ▼
EmailDecorator
     │
     │ super.send()
     ▼
BasicNotification
````

O `BasicNotification` executa primeiro o comportamento base.

Depois a execução volta:

````
BasicNotification
       │
       ▼
EmailDecorator
       │
       ▼
SMSDecorator
````

9. Visualmente

````
Notification notification =
    new SMSDecorator(
        new EmailDecorator(
            new BasicNotification()
        )
    );
````

````
┌─────────────────────────┐
│     SMSDecorator        │
│                         │
│  ┌───────────────────┐  │
│  │ EmailDecorator    │  │
│  │                   │  │
│  │ ┌───────────────┐ │  │
│  │ │ Basic         │ │  │
│  │ │ Notification  │ │  │
│  │ └───────────────┘ │  │
│  └───────────────────┘  │
└─────────────────────────┘
````

É literalmente uma **camada sobre outra**.

É daí que vem o nome **Decorator**.

10. "Antes ou depois da chamada ao método pai"


O texto diz:

> "Um decorador concreto deve executar seu comportamento antes ou depois da chamada ao método pai."

Isto significa que tens liberdade para escolher a ordem.

Depois:

````
@Override
public void send(String message) {

    super.send(message);

    System.out.println("Sending email");
}
````

Resultado:
1. Notificação básica
2. Email

Antes:

````
@Override
public void send(String message) {

    System.out.println("Preparing email");

    super.send(message);
}
````

Resultado:
1. Preparar email
2. Notificação básica

Portanto o decorator pode **adicionar comportamento antes, depois ou até em torno** do comportamento original.


11. O ponto mais importante: composição

````
new SMSDecorator(
    new EmailDecorator(
        new BasicNotification()
    )
);
````

Não estamos a criar: NotificationWithEmailAndSMS

Estamos a **compor objetos**.

````
BasicNotification
        +
EmailDecorator
        +
SMSDecorator
````

E podemos trocar a combinação em runtime.

12. Os 7 passos resumidos

````
|Passo|O que fazemos|
|---|---|
|**1**|Identificamos um objeto base e funcionalidades opcionais|
|**2**|Criamos uma interface comum|
|**3**|Criamos o objeto base|
|**4**|Criamos um decorator que contém outro objeto da mesma interface|
|**5**|Garantimos que todos implementam a mesma interface|
|**6**|Criamos decorators que adicionam comportamento|
|**7**|O cliente decide como combinar os decorators|
````

Estrutura:

````
                Component
                   ▲
          ┌────────┴────────┐
          │                 │
   ConcreteComponent    Decorator
                            │
                            │ contains
                            ▼
                        Component
                            ▲
                            │
                   ConcreteDecorator
````

### A frase para memorizar

> **Decorator adiciona responsabilidades a um objeto através de composição, envolvendo-o com outros objetos que implementam a mesma interface.**

E a diferença para o **Adapter**, que acabaste de estudar, é especialmente importante:

````
Adapter
→ muda/adapta a interface

Decorator
→ mantém a interface e adiciona comportamento
````

Ou, de forma ainda mais curta:

**Adapter = tornar compatível.**  
**Decorator = adicionar funcionalidades.**


## Prós

- Você pode estender o comportamento de um objeto sem criar uma nova subclasse.
- Você pode adicionar ou remover responsabilidades de um objeto em tempo de execução.
- Você pode combinar vários comportamentos envolvendo um objeto em múltiplos decoradores.
-  _Princípio da Responsabilidade Única_ . Você pode dividir uma classe monolítica que implementa muitas variantes possíveis de comportamento em várias classes menores.
## Contras

- É difícil remover um wrapper específico da pilha de wrappers.
- É difícil implementar um decorador de forma que seu comportamento não dependa da ordem na pilha de decoradores.
- O código de configuração inicial das camadas pode parecer bastante complexo.