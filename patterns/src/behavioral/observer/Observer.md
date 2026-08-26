Permite definir um mecanismo de assinatura para notificar vários objetos sobre quaisquer eventos que ocorram com o objeto que estão a observar.

## Problema

Dois tipos de Objeto: `Customer` e `Store`. Cliente interessa numa marca de produto, que deverá estar disponível na loja em breve.

O Cliente podia visitar a loja todos os dias e verificar disponibilidade.

![[Observer 1.png]]

A loja poderia enviar emails para todos os clientes cada vez que o produto fosse lançado.

## Solução

O objeto que possui um estado relevante é frequentemente chamado de `sujeito` mas como também notifica outros objetos é publisher. Todos os objetos que desejam acompanhar as mudanças são `subscriber`.

`Observer` sugere adicionar um mecanismo de assinatura à classe `publisher` que os objetos individuais possam se inscrever ou cancelar.
Na realidade:
- 1. um campo array para guardar uma lista de referênciasa objetos assinantes
- 2. vários métodos públicos que permitem adicionar e remover assinantes

![[Observer 2.png]]

Sempre que um evento acontece, o editor percorre os assinantes e chama o método de notificação nos seus objetos.

Aplicações reais podem ter dezenas de classes de assinantes diferentes interessadas em rastrear eventos da mesma classe `publisher`.

É importante que todos os assinantes implementem a mesma interface e que o `publisher` se comunique com eles apenas através da interface. Essa interface deve declarar o método de notificação juntamente com um conjunto de parâmetros que o `publisher` pode usar para passar dados contextuais junto com a notificação.

![[Observer 3.png]]


Se a aplicação tiver vários tipos de editores e desejar que os assinantes sejam compatíveis com eles, fazer com que todos os editores sigam a mesma interface, descrevendo alguns métodos de assinatura. A interface permitiria que os assinantes observassem os estados dos editores sem se acoplarem ás suas classes concretas.

## Analogia com o mundo real

![[Observer 5.png]]

Se assina um jornal ou revista, não precisa de ir à banca para verificar se a próxima edição está disponível. A editor envia as novas edições para a caixa de correio.

A editora mantém uma lista de assinantes e sabe quais lhes interessam. Os assinantes pode sair da lista.

## Estrutura

![[Observer 6.png]]


1. `Publisher` emite eventos de interesse para outros objetos. Esses eventos ocorrem quando o `Publisher` altera o seu estado ou executa comportamentos. Os `Publishers` contém uma infraestrutura de assinatura que permite que novos assinantes entrem.

2. Quando ocorre um novo evento, o `Editor` percorre a lista de assinantes e chama o método de notificação da interface do assinante em cada objeto.

3. Interface `Subscriver` declara a interface de notificação. Método `update`. Método pode ter vários parâmetros que permitem ao `Publisher` passar detalhes do evento.

4. `Assinantes concretos` executam ações em resposta ás notificações emitidas pelo `Publisher`. Todas essas classes devem implementar a mesma interface para que o `publisher` não fique acomplado ás classes concretas.

5. Os `Subscribers` precisam de algumas informações contextuais para processar a atualização. Os `Editors` costumam passar alguns dados contextuais como argumentos do método de notificação.

6. O `Client` cria objetos de `Editor` e assinante separadamente, regista os `Subscribers` para receber atualizações do `Publisher`

## Pseudocódigo

**Observer** permite que o objeto editor de texto notifique outros objetos de serviço sobre mudanças em seu estado.

![[Observer 7.png]]

A lista de assinantes é compilada dinamicamente: Os objetos podem começar / parar de ouvir notificações, dependendo do comportamento desejado.

A classe `Editor` não mantém a lista de assinantes por si só. Deleva essa tarefa a um objeto auxiliar dedicado a isso. `Dispacther` de eventos, permitindo que qualquer objeto atue como um `Publisher`

## Aplicabilidade

problema: `Observer` quando as mudanças no estado de um objeto puderem exigir a alteração de outros objetos, e o conjunto real de objetos for desconhecido antecipadamente ou mudar dinamicamente

solução: `observer` permite que qualquer objeto que implemente a interface `subscriber` se inscreva para receber notificações de eventos em objetos `publisher`

---

problema: `Observer` quando alguns objetos na aplicação precisarem de observar outros, mas apenas por um período limitado ou em casos específicos

solução: a lista de assinantes é dinâmica, permitindo que os assinantes entrem ou saiam da lista.

## Como implementar

Vou explicar cada passo com um exemplo simples em Java, para que fique claro **quem é o Publisher, quem é o Subscriber e como a comunicação acontece**.

### Primeiro: a ideia geral

Imagina uma **loja online**:

- Um produto fica disponível → isso é o **evento**.
- Vários clientes querem ser avisados → são os **subscribers**.
- A loja que sabe quando o produto fica disponível → é o **publisher**.

Em vez de o produto ter de conhecer diretamente cada cliente, temos:

```
                ┌─────────────────┐
                │    Publisher    │
                │                 │
                │ Produto          │
                └────────┬────────┘
                         │
                    notifica()
                         │
             ┌───────────┼───────────┐
             ▼           ▼           ▼
       ┌──────────┐ ┌──────────┐ ┌──────────┐
       │Subscriber│ │Subscriber│ │Subscriber│
       │ Cliente A│ │ Cliente B│ │ Cliente C│
       └──────────┘ └──────────┘ └──────────┘
```

O ponto fundamental é:

> **O Publisher não conhece as classes concretas dos Subscribers. Conhece apenas a interface `Subscriber`.**

#### 1. Separar a lógica em Publisher e Subscribers

O primeiro passo é olhar para a lógica existente e perguntar:

> **Qual é a parte principal que produz acontecimentos?**

Essa parte será o **Publisher**.

E depois:

> **Quem precisa reagir quando esses acontecimentos acontecem?**

Essas partes serão os **Subscribers**.

### Exemplo

Temos:

```
class Produto {
    void alterarPreco() {
        // altera preço
        // envia email
        // envia SMS
        // atualiza aplicação
        // regista log
    }
}
```

Aqui temos um problema.

A classe `Produto` está a fazer demasiadas coisas.

Podemos separar:

```
Produto
   │
   │ acontece algo
   ▼
notifica os interessados
   │
   ├── EmailNotifier
   ├── SmsNotifier
   ├── AppNotifier
   └── Logger
```

Assim:

- `Produto` → **Publisher**
- `EmailNotifier` → **Subscriber**
- `SmsNotifier` → **Subscriber**
- `AppNotifier` → **Subscriber**
- `Logger` → **Subscriber**

#### 2. Criar a interface do Subscriber

Agora precisamos de definir uma forma comum para todos os subscribers receberem notificações.

Criamos:

```
interface Subscriber {
    void update();
}
```

Isto significa:

> Qualquer objeto que queira ser notificado deve saber executar `update()`.

Por exemplo:

```
class EmailNotifier implements Subscriber {

    @Override
    public void update() {
        System.out.println("Enviar email");
    }
}
```

E:

```
class SmsNotifier implements Subscriber {

    @Override
    public void update() {
        System.out.println("Enviar SMS");
    }
}
```

Temos agora:

```
              Subscriber
                  │
          ┌───────┴────────┐
          ▼                ▼
 EmailNotifier        SmsNotifier
```

O Publisher não precisa de saber que existem `EmailNotifier` ou `SmsNotifier`.

Só precisa de saber:

```
Subscriber
```

#### 3. Criar a interface do Publisher

Agora precisamos de permitir que os subscribers se **inscrevam** e **cancelem a inscrição**.

Criamos:

```
interface Publisher {

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);
}
```

Temos então duas operações:

### `subscribe()`

Adiciona um subscriber.

```
publisher.subscribe(emailNotifier);
```

Significa:

> "Quero que este objeto seja avisado."

### `unsubscribe()`

Remove um subscriber.

```
publisher.unsubscribe(emailNotifier);
```

Significa:

> "Já não quero receber notificações."


#### 4. Onde guardar a lista de subscribers?

Agora surge uma questão importante:

> Onde vamos guardar todos os subscribers?

Precisamos de algo como:

```
List<Subscriber> subscribers;
```

Uma implementação possível seria:

```
class Produto implements Publisher {

    private List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
```

Mas existe um detalhe.

Imagine que temos vários publishers:

```
Produto
Video
Noticia
Pedido
```

Todos podem precisar de:

```
List<Subscriber>
subscribe()
unsubscribe()
```

Estaríamos a repetir código.

Por isso, podemos criar uma classe abstrata:

```
abstract class PublisherBase implements Publisher {

    protected List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
```

Depois:

```
class Produto extends PublisherBase {
    // comportamento específico
}
```

Assim:

```
              Publisher
                  ▲
                  │
            PublisherBase
                  ▲
          ┌───────┼────────┐
          │       │        │
       Produto   Video   Noticia
```

`PublisherBase` trata da infraestrutura do Observer.

#### 5. Criar o Publisher concreto

Agora criamos aquilo que realmente produz acontecimentos.

Por exemplo:

```
class Produto extends PublisherBase {

    private boolean disponivel;

    public void tornarDisponivel() {
        disponivel = true;

        notifySubscribers();
    }

    private void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update();
        }
    }
}
```

Aqui está o coração do Observer.

Quando acontece algo importante:

```
tornarDisponivel()
```

o Publisher executa:

```
notifySubscribers();
```

E:

```
for (Subscriber subscriber : subscribers) {
    subscriber.update();
}
```

Significa:

> "Percorre todos os inscritos e avisa cada um."


#### 6. Os Subscribers reagem à notificação

Agora criamos diferentes comportamentos.

### Email

```
class EmailNotifier implements Subscriber {

    @Override
    public void update() {
        System.out.println("Produto disponível! Enviar email.");
    }
}
```

### SMS

```
class SmsNotifier implements Subscriber {

    @Override
    public void update() {
        System.out.println("Produto disponível! Enviar SMS.");
    }
}
```

Agora temos:

```
Produto
   │
   │ update()
   ├──────────────► EmailNotifier
   │
   ├──────────────► SmsNotifier
   │
   └──────────────► AppNotifier
```

O `Produto` **não sabe o que cada subscriber vai fazer**.

Ele apenas diz:

```
subscriber.update();
```

É responsabilidade de cada subscriber decidir o que fazer.


#### 7. Passar dados sobre o evento

Na prática, `update()` muitas vezes precisa de informações.

Por exemplo:

> Qual produto ficou disponível?

Podemos fazer:

```
interface Subscriber {
    void update(Produto produto);
}
```

Agora:

```
class EmailNotifier implements Subscriber {

    @Override
    public void update(Produto produto) {
        System.out.println(
            "Enviar email: " + produto.getNome()
        );
    }
}
```

E o Publisher:

```
private void notifySubscribers() {
    for (Subscriber subscriber : subscribers) {
        subscriber.update(this);
    }
}
```

O `this` significa:

> "Estou a passar o próprio Publisher que originou o evento."

Assim o subscriber pode obter os dados:

```
produto.getNome();
produto.getPreco();
produto.isDisponivel();
```


#### 8. As três formas de passar informação

O texto que colocaste apresenta três possibilidades.

### Opção 1 — Passar os dados diretamente

Por exemplo:

```
void update(String nome, double preco);
```

O Publisher fornece exatamente aquilo que o subscriber precisa.

É simples, mas pode tornar a interface muito grande.

---

### Opção 2 — Passar o Publisher

```
void update(Produto produto);
```

O subscriber recebe o objeto que originou o evento.

Pode então fazer:

```
produto.getNome();
produto.getPreco();
```

É uma abordagem bastante comum.

---

### Opção 3 — Guardar o Publisher no Subscriber

Por exemplo:

```
class EmailNotifier implements Subscriber {

    private Produto produto;

    public EmailNotifier(Produto produto) {
        this.produto = produto;
    }

    @Override
    public void update() {
        System.out.println(produto.getNome());
    }
}
```

Aqui o subscriber fica fortemente ligado ao `Produto`.

É a opção **menos flexível**, porque o subscriber passa a depender diretamente daquela implementação.

#### 9. O cliente cria tudo e faz as inscrições

Finalmente temos o código cliente.

Por exemplo:

```
public class Main {

    public static void main(String[] args) {

        Produto produto = new Produto();

        Subscriber email = new EmailNotifier();
        Subscriber sms = new SmsNotifier();

        produto.subscribe(email);
        produto.subscribe(sms);

        produto.tornarDisponivel();
    }
}
```

O fluxo é:

```
1. Cliente cria Produto
          │
          ▼
2. Cliente cria EmailNotifier
          │
          ▼
3. Cliente cria SmsNotifier
          │
          ▼
4. Cliente inscreve ambos
          │
          ▼
5. Produto torna-se disponível
          │
          ▼
6. Produto chama notifySubscribers()
          │
          ├────────► email.update()
          │
          └────────► sms.update()
```

---

# O Observer completo

Podemos juntar tudo:

```
interface Subscriber {
    void update(Produto produto);
}
```

```
interface Publisher {
    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);
}
```

```
class Produto implements Publisher {

    private List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void tornarDisponivel() {
        System.out.println("Produto disponível!");

        notifySubscribers();
    }

    private void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(this);
        }
    }
}
```

```
class EmailNotifier implements Subscriber {

    @Override
    public void update(Produto produto) {
        System.out.println(
            "Email: " + produto.getNome()
        );
    }
}
```

```
class SmsNotifier implements Subscriber {

    @Override
    public void update(Produto produto) {
        System.out.println(
            "SMS: " + produto.getNome()
        );
    }
}
```

E finalmente:

```
Produto produto = new Produto();

produto.subscribe(new EmailNotifier());
produto.subscribe(new SmsNotifier());

produto.tornarDisponivel();
```

---

## O que deves memorizar

O padrão Observer resume-se essencialmente a isto:

```
             PUBLISHER
                 │
          mantém uma lista
          de subscribers
                 │
                 ▼
        ┌─────────────────┐
        │   Subscribers   │
        └─────────────────┘
          │      │      │
          ▼      ▼      ▼
       update  update  update
```

### Responsabilidades

|Elemento|Responsabilidade|
|---|---|
|**Publisher**|Produz acontecimentos|
|**Subscriber**|Reage aos acontecimentos|
|`subscribe()`|Regista um subscriber|
|`unsubscribe()`|Remove um subscriber|
|`notifySubscribers()`|Avisa todos|
|`update()`|Reação do subscriber|

### A grande vantagem

Sem Observer:

```
Produto ─────► EmailNotifier
       ├─────► SmsNotifier
       ├─────► AppNotifier
       └─────► Logger
```

O `Produto` fica dependente de muitas classes.

Com Observer:

```
Produto ─────► Subscriber
                  ▲
          ┌───────┼────────┐
          │       │        │
        Email    SMS      App
```

O `Produto` depende **apenas da abstração `Subscriber`**.

É precisamente isso que torna possível adicionar:

```
class PushNotifier implements Subscriber
```

sem alterar a classe `Produto`.

**Em uma frase:** o **Observer** permite que um objeto (_Publisher_) notifique automaticamente vários outros objetos (_Subscribers_) quando o seu estado ou comportamento sofre uma alteração, mantendo-os fracamente acoplados.