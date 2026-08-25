
É um padrão de projeto estrutural que fornece uma interface simplificada para uma biblioteca, um framework ou qualquer outro conjunto complexo de classes.

A ideia central é:

> **Facade fornece uma interface simples para esconder a complexidade de um subsistema.**
## Frase para memorizar

> **Facade fornece uma interface simplificada para um subsistema complexo, reduzindo o acoplamento do código cliente com as classes internas desse subsistema.**

E a palavra-chave aqui é **simplificação**.

### Exemplos nas Java Core Libraries

**Exemplos de uso:** O padrão Facade é comumente usado em aplicativos escritos em Java. É especialmente útil ao trabalhar com bibliotecas e APIs complexas.

- [`javax.faces.context.FacesContext`](http://docs.oracle.com/javaee/7/api/javax/faces/context/FacesContext.html)usa classes internamente, mas a maioria dos clientes não tem conhecimento [`LifeCycle`](http://docs.oracle.com/javaee/7/api/javax/faces/lifecycle/Lifecycle.html)disso [`ViewHandler`](http://docs.oracle.com/javaee/7/api/javax/faces/application/ViewHandler.html).[`NavigationHandler`](http://docs.oracle.com/javaee/7/api/javax/faces/application/NavigationHandler.html)

- [`javax.faces.context.ExternalContext`](http://docs.oracle.com/javaee/7/api/javax/faces/context/ExternalContext.html)usa [`ServletContext`](http://docs.oracle.com/javaee/7/api/javax/servlet/ServletContext.html), [`HttpSession`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpSession.html), [`HttpServletRequest`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletRequest.html), [`HttpServletResponse`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletResponse.html)e outros dentro.

**Identificação:** Uma fachada pode ser reconhecida em uma classe que possui uma interface simples, mas delega a maior parte do trabalho para outras classes. Normalmente, as fachadas gerenciam todo o ciclo de vida dos objetos que utilizam.
## Problema

É necessário fazer o código funcionar com um conjunto amplo de objetos pertencentes a uma biblioteca ou framework. Normalmente, seria necessário inicializar todos esses objetos, controlar as dependências, executar os métodos na ordem corrente e assim por adiante.

A lógica de negócio ficaria fortemente acoplada aos detalhes de implementação de classes de terceiros, dificultando compreensão e manutenção.

## Solução

O padrão `Facade` é uma classe que fornece uma interface simples para um subsistema complexo.
Pode oferecer funcionalidade limitada em comparação com o trabalho direto com o subsistema.
No entanto, inclui apenas os recursos realmente importantes.

Exemplo, aplicação que publica vídeos curtos de gatos nas redes sociais podia usar uma biblioteca profissional de vídeo. O que precisa é de uma classe com um único método `enconde(filename, format)`

## Analogia com o mundo real

![[Facade 1.png]]

Ao ligar para um call center, um operador é uma interface com todos os serviços. O operador fornece uma interface de voz simples para sistema de pedidos, gateways, pagamentos etc

## Estrutura

![[Facade 2.png]]

1. `Facade` proporciona acesso conveniente a uma parte especifica da funcionalidade do substistema. Sabe para onde direcionar a solicitação do cliente e como operar todas as partes.

2. Uma classe `facade adicional` pode ser criada para evitar que uma `facade principal` seja poluída com elementos não relacionados, o que poderia transformá-la noutra estrutura complexa. `Facade Adicional` pode ser usadas tanto por clientes quanto por outras fachadas.

3. O `subsistema complexto` consiste em dezenas de objetos diferentes. Para que todos executem tarefas significativas, é necessário analisar detalhadamente a implementação, como inicializar os objetos na ordem correta e fornecer dados no formato adequado.

   As classes de subsistema não têm conhecimento da existência da fachada.
   Elas operam dentro do sistema e interagem diretamente entre si.

4. O cliente utiliza a fachada em vez de chamar os objetos do subsistema diretamente.

## Pseudocódigo

![[Facade 3.png]]

Em vez do código funcionar diretamente com dezenas de classes do framework, cria uma classe `facade` que encapsula essa funcionalidade. Também ajuda a minimizar o esforço de atualização para versões futuras do framework ou da subsituição por outro.

## Aplicabilidade

Problema: quando precisa de uma interface limitada, simples, para um sistema complexo.

Solução: subsistemas tornam-se mais complexos com o tempo.
A aplicação de padrões de projeto leva a criação de mais classes. Um subsistema pode tornar mas flexivel e fácil de reutilizar em diversos contextos, mas a quantidade de configuração aumenta.
`Facade` fornece um atalho para os recursos mais utilizados do susbsitema, que atendem à maioria dos requisitos.


----

Problema: `Facade`quando for necessário estruturar um subsistema em camadas

Solução: Crie `Facades` para definir pontos de entrada em cada nível de um subsistema.
Pode reduzir o acoplamento entre múltiplos subsistemas exigindo que eles se comuniquem apenas por meio de `Facade`.

Exemplo: Estrutura conversão de vídeo. Pode ser dividida em 2 camadas: uma de vídeo outra a audio. Para cada camada pode ser criada uma `Facade` e fazer com que as classes se comuniquem entre si. Semelhante ao `Mediator`.

## Como implementar

1. Verificar se existe um subsistema demasiado complexo

Imagina que tens um sistema para **processar um pedido de compra**.

Para fazer uma compra, precisas de interagir com várias classes:

````
Order
PaymentService
InventoryService
ShippingService
EmailService
````

Exemplo:

````
order.validate();
inventory.checkStock();
payment.authorize();
payment.capture();
inventory.removeStock();
shipping.createShipment();
email.sendConfirmation();
````

O cliente precisa de conhecer **todas estas classes e a ordem correta das operações**.

````
                  Cliente
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
     Payment      Inventory     Shipping
        │            │            │
        └────────────┼────────────┘
                     │
                  Email
````

O problema é:

> **O cliente precisa de conhecer demasiado sobre o funcionamento interno do subsistema.**

2. Criar uma interface mais simples

Em vez de obrigar o cliente a conhecer tudo isto, criamos uma classe:

````
class OrderFacade {

    public void placeOrder(Order order) {
        // ...
    }
}
````

Agora o cliente só precisa de saber:

````
facade.placeOrder(order);
````

Em vez de:

````
order.validate();
inventory.checkStock();
payment.authorize();
payment.capture();
inventory.removeStock();
shipping.createShipment();
email.sendConfirmation();
````

Tem:

````
Cliente
   │
   │ placeOrder()
   ▼
OrderFacade
   │
   ├── Order
   ├── InventoryService
   ├── PaymentService
   ├── ShippingService
   └── EmailService
````

A **Facade é a interface simplificada** para o subsistema.

3. Implementar a Facade

````
class OrderFacade {

    private final InventoryService inventory;
    private final PaymentService payment;
    private final ShippingService shipping;
    private final EmailService email;

    public OrderFacade(
            InventoryService inventory,
            PaymentService payment,
            ShippingService shipping,
            EmailService email) {

        this.inventory = inventory;
        this.payment = payment;
        this.shipping = shipping;
        this.email = email;
    }

    public void placeOrder(Order order) {

        order.validate();

        inventory.checkStock(order);

        payment.authorize(order);
        payment.capture(order);

        inventory.removeStock(order);

        shipping.createShipment(order);

        email.sendConfirmation(order);
    }
}
````

A Facade está a fazer algo muito importante:

> **Orquestrar o subsistema.**

Ela sabe:

- quais classes devem ser chamadas;
- em que ordem;
- quais operações são necessárias;
- como coordenar os vários componentes.

O cliente não precisa de saber nada disso.

4. O cliente passa a usar apenas a Facade

Antes:
````
Order order = new Order();

order.validate();

inventory.checkStock(order);

payment.authorize(order);
payment.capture(order);

inventory.removeStock(order);

shipping.createShipment(order);

email.sendConfirmation(order);
````

Depois:
````
OrderFacade facade = new OrderFacade(
    inventory,
    payment,
    shipping,
    email
);

facade.placeOrder(order);
````


5. O cliente fica protegido das alterações

Este é um dos pontos mais importantes do passo 3.

Imagina que amanhã o sistema de pagamentos muda.

Antes:
````
PaymentService
````

Depois:
````
StripePaymentService
````

Se o cliente conhece diretamente `PaymentService`, podes ter de alterar vários pontos da aplicação.

Com Facade:

````
Cliente
   │
   ▼
OrderFacade
   │
   ▼
StripePaymentService
````

````
class OrderFacade {

    private final StripePaymentService payment;

    // ...
}
````

Portanto:

> **A Facade reduz o acoplamento entre o cliente e o subsistema.**

6. A Facade não impede acesso ao subsistema

O padrão **não significa necessariamente**:

> "As classes internas deixam de existir ou ficam inacessíveis."

````
Cliente
   │
   ├───────────────┐
   │               │
   ▼               ▼
Facade       Subsistema
````

Mas a recomendação do padrão é:

> **Sempre que possível, o código cliente deve comunicar através da Facade.**

Assim o cliente não fica dependente da implementação interna.

7. O que significa "inicializar e gerir o ciclo de vida"?

````
OrderFacade facade = new OrderFacade();
````

````
class OrderFacade {

    public OrderFacade() {
        database.connect();
        cache.initialize();
        payment.initialize();
        shipping.initialize();
    }
}
````

Cliente
````
OrderFacade facade = new OrderFacade();

facade.placeOrder(order);
````

````
Cliente
   │
   ▼
Facade
   │
   ├── inicializa subsistema
   ├── utiliza subsistema
   └── gere operações
````

8. E se a Facade ficar demasiado grande?

````
OrderFacade
````

````
OrderFacade
 ├── pagamentos
 ├── encomendas
 ├── autenticação
 ├── notificações
 ├── relatórios
 ├── faturação
 ├── logística
 └── ...
````


Isso é um sinal de que a Facade está a assumir demasiadas responsabilidades.

Então podes dividir:

````
Facade
 │
 ├── OrderFacade
 ├── PaymentFacade
 ├── ShippingFacade
 └── NotificationFacade
````

````

class PaymentFacade {

    public void processPayment(...) {
        // ...
    }
}

class ShippingFacade {

    public void createShipment(...) {
        // ...
    }
}
````

9. Os 4 passos resumidos

Encontrar Complexidade

````
Cliente
 ├── A
 ├── B
 ├── C
 ├── D
 └── E
````

Criar Interface Simples

````
Cliente
   │
   ▼
Facade
   │
   ├── A
   ├── B
   ├── C
   ├── D
   └── E
````

Fazer o cliente usar o `Facade`

````
a.doSomething();
b.doSomething();
c.doSomething();
d.doSomething();
````

Dividir se ficar demasiado grande

````
BigFacade
    ↓
┌───────────────┐
│               │
▼               ▼
OrderFacade  PaymentFacade
````

10. Facade vs Adapter vs Decorator

````
|Padrão|Problema|Solução|
|---|---|---|
|**Adapter**|Interface incompatível|Converte uma interface noutra|
|**Decorator**|Quero adicionar comportamento|Envolve o objeto e adiciona comportamento|
|**Facade**|Subsistema demasiado complexo|Cria uma interface simples|
````

Adapter
````
Cliente
   ↓
Adapter
   ↓
Serviço incompatível
````
"Quero que isto seja compatível."

Decorator
````
Cliente
   ↓
Decorator
   ↓
Decorator
   ↓
Objeto
````
"Quero acrescentar funcionalidades."

Facade
````
Cliente
   ↓
Facade
   ↓
┌───┼───┬───┐
A   B   C   D
````
"Não quero que o cliente tenha de conhecer toda esta complexidade."

**Adapter → compatibilidade**  
**Decorator → extensão**  
**Facade → simplificação**
