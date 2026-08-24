**O Adapter** é um padrão de projeto estrutural que permite que objetos com interfaces incompatíveis colaborem.

O Adapter permite que duas classes com interfaces incompatíveis trabalhem juntas, sem alterar a classe existente.

**Exemplos de uso:** O padrão Adapter é bastante comum em código Java. É muito utilizado em sistemas baseados em código legado. Nesses casos, os Adapters permitem que o código legado funcione com classes modernas.
## A frase para memorizar

> **Adapter converte a interface de uma classe numa interface que o cliente espera, permitindo que classes incompatíveis trabalhem juntas.**

E há uma distinção importante relativamente ao **Prototype** que estiveste a estudar:

Prototype
→ "Quero criar um objeto através da cópia de outro."

Adapter
→ "Quero usar esta classe, mas a sua interface não é compatível com a que o meu código espera."

Prototype → COPIAR
Adapter   → ADAPTAR



![[Pasted image 20260824143941.png]]


### Exemplos nas Java Core Libraries

- [`java.util.Arrays#asList()`](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#asList-T...-)
- [`java.util.Collections#list()`](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#list-java.util.Enumeration-)
- [`java.util.Collections#enumeration()`](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#enumeration-java.util.Collection-)
- [`java.io.InputStreamReader(InputStream)`](https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html#InputStreamReader-java.io.InputStream-)(retorna um `Reader`objeto)
- [`java.io.OutputStreamWriter(OutputStream)`](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStreamWriter.html#OutputStreamWriter-java.io.OutputStream-)(retorna um `Writer`objeto)
- [`javax.xml.bind.annotation.adapters.XmlAdapter#marshal()`](https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/adapters/XmlAdapter.html#marshal-BoundType-)e`#unmarshal()`

**Identificação:** O adaptador é reconhecível por um construtor que recebe uma instância de um tipo abstrato/interface diferente. Quando o adaptador recebe uma chamada para qualquer um de seus métodos, ele traduz os parâmetros para o formato apropriado e, em seguida, direciona a chamada para um ou mais métodos do objeto encapsulado.
## Problema

Aplicação ações com dados XML - gráficos e diagramas.
Necessidade de integrar biblioteca de análise de terceiros que só funciona com JSON.

![[Adapter 2.png]]

Adaptar código biblioteca para XML se acesso código fonte.
## Solução

Adaptador - Converte a interface de um objeto para outro.
Envolve um dos objetos para ocultar a complecidade da conversão.
Envolver objeto que opera em metros e km com um adaptador que converte dados em unidades imperiais, como pés e milhas.

Adaptadores não só convertem dados em vários formatos como ajudam objetos com interfaces diferentes a colaborarem. Utilização:
1. O adaptador recebe uma interface compatível com um dos objetos existentes.
2. Utilizando essa interface, o objeto existente pode chamar os métodos do adaptador em segurança
3. Ao receber uma chamada, o adaptador passa a solicitação para o segundo objeto, mas em um formato e ordem que o segundo objeto espera.

É possível criar um adaptador bidirecional, capaz de converter chamadas em ambas as direções.

![[Adapter 3.png]]

Aplicação Mercado ações - criar adaptadores de XML para JSON para cada classe da biblioteca de análise com a qual o código interage. Ajuste o código para se comunicar com a biblioteca por meio desses adaptadores. Quando um adaptador recebe uma chamada, traduz os dados XML numa estrutura JSON e passa a chamada para os métodos apropriados de um objeto de análise encapsulado.

## Analogia com o mundo real

![[Adapter 4.png]]

Plugins e tomadas são diferetes em cada país.
## Estrutura

Adaptador de objeto

Esta implementação utiliza o princípio da composição de objetos: o adaptador implementa a interface de um objeto e encapsula o outro. Pode ser implementada em todas as linguagens de programação populares.

![[Adapter 5.png]]

1. O `cliente` é uma classe que contém a lógica de negócio existente do programa.

2. `Interface do Cliente` descreve um protocolo que outras classes devem seguir para poderem colaborar com o código do cliente

3. O `serviço` é uma classe útil ( terceiros ou legada ). O cliente não pode usar diretamente, tem uma interface incompatível.

4. O `Adapter` classe capaz de trabalhar com o `cliente` e com o `serviço`: implementa a interface do `cliente` e encapsula o objeto do `serviço`. O Adapter recebe chamadas do cliente por meio da interface do cliente e traduz em chamadas para o objeto do serviço encapsulado num formato que pode entender.

5. O código do cliente não fica acoplado á classe adaptadora concreta, desde que interaja com o adaptador por meio da interface do cliente. Com isto, é possível introduzir novos tipos de adaptadores no programa sem quebrar o código do cliente existente. Pode ser útil quando a interface da classe de serviço é alterada ou substituída: basta criar uma nova classe adaptadora sem alterar o código do cliente.

#### Adaptador de classe

Esta implementação utiliza herança: o adaptador herda interfaces de ambos os objetos simultaneamente. Observe que essa abordagem só pode ser implementada em linguagens de programação que suportam herança múltipla, como C++.

![[Adapter 6.png]]

1. O **adaptador de classe** não precisa encapsular nenhum objeto, pois herda comportamentos tanto do cliente quanto do serviço. A adaptação ocorre dentro dos métodos sobrescritos. O adaptador resultante pode ser usado no lugar de uma classe cliente existente.

## Pseudocódigo

![[Adapter 7.png]]

O adaptador simula ser um pino redondo, com um raio igual à metade do diâmetro do quadrado (em outras palavras, o raio do menor círculo que pode acomodar o pino quadrado).

## Aplicabilidade

Problema: usar Adapter quando quiser utilizar alguma classe mas a interface dela não for compatível com o restante código.

Solução: Adapter permite criar uma classe intermediária que serve como tradutora entre o código e a classe legada, de terceiros ou outra com uma interface incomum.

--- 

Problema:
Necessidade de reutilizar várias subclasses existentes que não possuem alguma funcionalidade comum que não pode ser adicionada á superclasse

Solução:
Poderia estender cada subclasse e colocar a funcionalidade ausente em novas classes filhas. No entanto era necessário duplicar código em todas essas novas classes (má ideia).

Solução mais elegante, colocar a funcionalidade ausente em uma classe adapter. Encapsular os objetos com os recursos ausentes dentro do adaptador, obtendo os recursos necessários dinamicamente. As classes de destino devem ter uma interface comum, e o campo do adaptador deve seguir essa interface.

## Como implementar

1. Ter duas interfaces incompatíveis

Aplicação que espera trabalhar com pagamentos através da interface:

````
interface PaymentProcessor {

    void pay(double amount);
}
````

O Cliente está preparado para usar: PaymentProcessor

Biblioteca Externa:

````
class StripeService {

    public void makePayment(double amount) {
        System.out.println("Payment: " + amount);
    }
}
````

Problema:

````
Cliente espera:

PaymentProcessor
       │
       └── pay()


Serviço existente:

StripeService
       │
       └── makePayment()
````

E suponhamos que **não podes alterar `StripeService`**.

2. Criar a interface do cliente

Primeiro defines aquilo que o teu código quer utilizar:

````
interface PaymentProcessor {

    void pay(double amount);
}
````

Esta é a **Target Interface** do Adapter Pattern.
A aplicação vai trabalhar com esta interface

3. Criar o Adapter

Agora crias uma classe que implementa a interface que o cliente espera:

````
class StripeAdapter implements PaymentProcessor {

    @Override
    public void pay(double amount) {
    }
}
````

````
PaymentProcessor
       ▲
       │ implements
       │
StripeAdapter
````

O Adapter é a "ponte" entre o cliente e o serviço incompatível.

4. Guardar uma referência ao serviço

O Adapter precisa de ter acesso ao objeto que realmente faz o trabalho.

````
class StripeAdapter implements PaymentProcessor {

    private final StripeService stripeService;

    public StripeAdapter(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public void pay(double amount) {
    }
}
````


````text
                 PaymentProcessor
                        ▲
                        │
                        │ implements
                        │
                 StripeAdapter
                        │
                        │ possui
                        ▼
                 StripeService
````

O Adapter conhece o serviço.

O cliente **não precisa de conhecer o serviço**.

5. Fazer a conversão/delegação

````text
@Override
public void pay(double amount) {
    stripeService.makePayment(amount);
}
````

O cliente diz: paymentProcessor.pay(100);

Mas o Adapter transforma isso em: stripeService.makePayment(100);

Tem:

````text
Cliente
   │
   │ pay(100)
   ▼
StripeAdapter
   │
   │ makePayment(100)
   ▼
StripeService
````

O Adapter **traduz uma interface para outra**.

6. O cliente usa apenas a interface

````text
class Checkout {

    private final PaymentProcessor paymentProcessor;

    public Checkout(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void checkout(double amount) {
        paymentProcessor.pay(amount);
    }
}
````

Usamos

````text
StripeService stripe = new StripeService();

PaymentProcessor processor =
        new StripeAdapter(stripe);

Checkout checkout = new Checkout(processor);

checkout.checkout(100);
````

Fluxo:

````
Checkout
   │
   │ PaymentProcessor
   ▼
StripeAdapter
   │
   │ StripeService
   ▼
StripeService
````

O `Checkout` **não sabe** que está a trabalhar com Stripe.

Ele só conhece: PaymentProcessor

7. Por que não chamar diretamente `StripeService`?

```` 
class Checkout {

    private StripeService stripeService;

    public void checkout(double amount) {
        stripeService.makePayment(amount);
    }
}
````

Mas agora criaste uma dependência direta:

Checkout
│
└──────> StripeService

Isso torna o `Checkout` dependente de uma implementação concreta.

Com Adapter:

````
Checkout
   │
   ▼
PaymentProcessor
   ▲
   │
StripeAdapter
   │
   ▼
StripeService
````

O `Checkout` depende apenas da interface.

8. E se amanhã mudares de serviço?

Imagina que amanhã queres utilizar: PayPalService

Com:
````text
class PayPalService {

    public void sendPayment(double value) {
        System.out.println("PayPal: " + value);
    }
}
````

A interface contínua:

````
interface PaymentProcessor {
    void pay(double amount);
}
````

Criamos outro Adapter:

````
class PayPalAdapter implements PaymentProcessor {

    private final PayPalService payPalService;

    public PayPalAdapter(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @Override
    public void pay(double amount) {
        payPalService.sendPayment(amount);
    }
}
````

Agora:

````
                   PaymentProcessor
                    ▲            ▲
                    │            │
                    │            │
            StripeAdapter    PayPalAdapter
                    │            │
                    ▼            ▼
             StripeService   PayPalService
````

E o `Checkout` **não precisa de ser alterado**.

Trocar

````
PaymentProcessor processor =
    new StripeAdapter(stripe);
    
Por

PaymentProcessor processor =
    new PayPalAdapter(payPal);
    
````

9. O ponto mais importante do Adapter

O Adapter **não altera o serviço existente**.

````
Cliente
   │
   ▼
Interface esperada
   │
   ▼
Adapter
   │
   ▼
Serviço existente
````

10. Relacionando diretamente com os 6 passos

Tens interfaces incompatíveis

````
Cliente → PaymentProcessor
Serviço → StripeService
````

Defines o que o cliente espera

````
interface PaymentProcessor {
    void pay(double amount);
}
````

Criar o Adapter

````
class StripeAdapter implements PaymentProcessor {
}
````

Guardar o serviço

````
private final StripeService stripeService;
````

Traduzir/delegar

````
@Override
public void pay(double amount) {
    stripeService.makePayment(amount);
}
````

Cliente utiliza a interface

````
Checkout checkout = new Checkout(processor);
````

