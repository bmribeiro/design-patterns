Permite definir uma família de algoritmos, colocar cada um deles numa classe separada e tornar os seus objetos intercambiáveis.


### Exemplos nas Java Core Libraries

Aqui estão alguns exemplos de estratégia em bibliotecas principais do Java:

- [`java.util.Comparator#compare()`](http://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html#compare-T-T-)chamado de `Collections#sort()`.

- [`javax.servlet.http.HttpServlet`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html): `service()`método, além de todos os `doXXX()`métodos que aceitam `HttpServletRequest`objetos `HttpServletResponse`como argumentos.

- [`javax.servlet.Filter#doFilter()`](http://docs.oracle.com/javaee/7/api/javax/servlet/Filter.html#doFilter-javax.servlet.ServletRequest-javax.servlet.ServletResponse-javax.servlet.FilterChain-)

**Identificação:** O padrão Strategy pode ser reconhecido por um método que permite que um objeto aninhado execute o trabalho propriamente dito, bem como por um método setter que permite substituir esse objeto por outro diferente.
## Problema

Aplicação de navegação para viajantes pontuais. Mapa ajudava utilizadores a se orientarem numa cidade. Funcionalidade mais solicitada, planeamento automático de rotas. Ex. Inserir um endereço e ver a rota mais rápida até aquele destino.

A primeira versão só permitia rotas por estrada. Na segunda versão, rotas a pé. Depois transportes públicos. Mais tarde, rotas para ciclistas, e ainda mais tarde rotas para atrações turísticas.

![[Pasted image 20260826124424.png]]

Cada vez que se adicionava novo algoritmo, a classe principal duplicava.

## Solução

A `Strategy` sugere que se pegue numa classe e realize algo específico de várias formas diferentes e extraia todos esses algoritmos em classes diferentes chamadas estratégias.

A classe original `Context`, deve ter um campo para armazenar uma referência a uma das estratégias. O contexto delega o trabalho a um objeto de estratégia vinculado.

O `Contexto` não é responsável por selecionar um algoritmo apropriado. Passa a estratégia desejada para o contexto. O `contexto` não sabe muito de estratégias, trabalha com todas através da mesma interface genérica, que expõe apenas um único método para acionar o algoritmo encapsulado na estratégia selecionada.

Desta forma, o `Contexto` torna-se independente de estratégias concretas, permitindo adicionar novos algoritmos ou modificar os existentes sem alterar o código do contexto ou de outras estratégias.

![[Pasted image 20260826125325.png]]


Cada algoritmo de roteamento pode ser extraído para a sua própria classe com o método `buildRoute`. O método aceita uma origem e um destino e devolve uma coleção de pontos.

Com os mesmos argumentos, cada classe de roteamento pode construir uma rota diferente, a classe principal `Navigator` não se importa com o algoritmo selecionado, sendo a sua função, renderizar pontos no mapa. A classe tem botões de interface para alterar o comportamento de roteamento.

## Analogia com o mundo real

![[Pasted image 20260826125742.png]]

Opções de transporte.

## Estrutura

![[Pasted image 20260826125812.png]]

1. O `Contexto` tem uma referência a uma das estratégias e comunica com esse objeto por meio de uma interface da estratégia
2. A interface `Strategy` é comum a todas as estratégias. Declara um método para executar a estratégia.
3. As `Estratégias concretas` implementam variações de um algoritmo
4. O contexto chama o método de execução no objeto de estratégia vinculado. Não sabe com que tipo de estratégia está a trabalhar nem como o algoritmo é executado.
5. `Cliente` cria um objeto de estratégia específico e passa o contexto. O contexto expõe um método setter que permite aos clientes substituir a estratégia associada ao contexto.

## Aplicabilidade

problema: utilizar `strategy` quando for necessário utilizar diferentes variantes de um algoritmo dentro de um objeto e poder alternar entre um ou outro em tempo de execução

solução: `strategy` permite alterar o comportamento de um objeto em tempo de execução, associando-o a diferentes objetos que podem executar tarefas específicas de forma distinta.

--- 

problema: `strategy` quando existirem muitas classes semelhantes que diferem na forma como executam determinado comportamento

solução: `strategy` permite extrair os comportamentos variáveis para uma hierarquia de classes separada e combinar classes originais em uma só

--- 

problema: `strategy` para isolar a lógica de negócios de uma classe dos detalhes de implementação de algoritmos que podem não ser importantes no contexto dessa lógica

solução: `strategy` permite isolar o código, os dados internos e as dependências de vários algoritmos do restante código. Diversos clientes obtêm uma interface simples para executar os algoritmos e alterná-los em tempo de execução.

---

problema: Utilize esse padrão quando sua classe tiver uma instrução condicional extensa que alterna entre diferentes variantes do mesmo algoritmo.

solução: o padrão Strategy permite eliminar essa condicional extraindo todos os algoritmos para classes separadas, que implementam a mesma interface. O objeto original delega a execução a um desses objetos, em vez de implementar todas as variantes do algoritmo.

## Como implementar


A ideia central do Strategy é: **retirar diferentes versões de um algoritmo de uma classe e colocá-las em objetos separados, que podem ser trocados através de uma interface comum**. O `Context` deixa de saber _como_ o algoritmo funciona e apenas delega a execução para a estratégia escolhida.

### Exemplo inicial — sem Strategy

Imagina que temos:

```
public class PaymentService {

    public void pay(String type, double amount) {

        if (type.equals("CREDIT_CARD")) {
            System.out.println("Pagamento com cartão: " + amount);

        } else if (type.equals("PAYPAL")) {
            System.out.println("Pagamento com PayPal: " + amount);

        } else if (type.equals("MBWAY")) {
            System.out.println("Pagamento com MB WAY: " + amount);
        }
    }
}
```

Funciona.

Mas imagina que começam a aparecer:

```
CREDIT_CARD
PAYPAL
MBWAY
BANK_TRANSFER
APPLE_PAY
GOOGLE_PAY
...
```

A classe começa a crescer.

E pior: cada método de pagamento pode ter uma implementação bastante complexa.

É precisamente este tipo de situação — várias variantes do mesmo comportamento ou um `if/switch` grande — que o Strategy procura resolver.

### 1. Identificar o algoritmo que muda

O primeiro passo diz:

> "Na classe de contexto, identifique um algoritmo que esteja sujeito a mudanças frequentes."

No nosso caso, temos:

```
public class PaymentService {

    public void pay(String type, double amount) {

        if (type.equals("CREDIT_CARD")) {
            // algoritmo de pagamento com cartão

        } else if (type.equals("PAYPAL")) {
            // algoritmo de pagamento com PayPal

        } else if (type.equals("MBWAY")) {
            // algoritmo de pagamento com MB WAY
        }
    }
}
```

Temos **um objetivo**:

```
pagar
```

Mas temos **várias formas de o fazer**:

```
pagar
 ├── cartão
 ├── PayPal
 └── MB WAY
```

Essa parte variável é o nosso **algoritmo/estratégia**.

### Portanto:

```
PaymentService
      │
      └── pay()
            │
            ├── cartão
            ├── PayPal
            └── MB WAY
```

O `PaymentService` está a conhecer demasiados detalhes.

O objetivo será transformar isto em:

```
PaymentService
      │
      └── PaymentStrategy
             ├── CreditCardStrategy
             ├── PayPalStrategy
             └── MBWayStrategy
```

### 2. Criar a interface Strategy

Agora perguntamos:

> "O que é que todas estas estratégias têm em comum?"

Todas sabem **efetuar um pagamento**.

Então criamos:

```
public interface PaymentStrategy {

    void pay(double amount);
}
```

Esta é a nossa **Strategy interface**.

Ela não sabe _como_ o pagamento será feito.

Apenas define:

```
Qualquer estratégia de pagamento
        ↓
        deve saber
        ↓
       pay()
```

Portanto:

```
PaymentStrategy
       │
       └── pay(double amount)
```

É exatamente esta interface comum que permite ao `Context` trabalhar com todas as estratégias sem conhecer as classes concretas.

### 3. Extrair cada algoritmo para uma classe

Agora pegamos em cada variante e colocamo-la numa classe própria.

## Cartão

```
public class CreditCardStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println(
            "Pagamento com cartão: " + amount
        );
    }
}
```

## PayPal

```
public class PayPalStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println(
            "Pagamento com PayPal: " + amount
        );
    }
}
```

## MB WAY

```
public class MBWayStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println(
            "Pagamento com MB WAY: " + amount
        );
    }
}
```

Agora temos:

```
PaymentStrategy
       │
       ├── CreditCardStrategy
       │
       ├── PayPalStrategy
       │
       └── MBWayStrategy
```

Cada classe contém **uma única forma de executar o algoritmo**.

### 4. Colocar a Strategy dentro do Context

Agora voltamos ao `PaymentService`.

Anteriormente ele fazia isto:

```
if (...)
    ...
else if (...)
    ...
```

Agora ele não deve conhecer essas implementações.

Ele apenas precisa de uma:

```
PaymentStrategy
```

Então:

```
public class PaymentService {

    private PaymentStrategy strategy;

}
```

Temos agora:

```
PaymentService
      │
      │ possui
      ▼
PaymentStrategy
```

Repara numa coisa **muito importante**:

O campo é:

```
private PaymentStrategy strategy;
```

e não:

```
private CreditCardStrategy strategy;
```

Nem:

```
private PayPalStrategy strategy;
```

O `Context` conhece **a interface**, não as implementações concretas.

Isso é fundamental no Strategy. O contexto comunica com a estratégia através da interface comum.

### 5. Criar o setter

O passo seguinte diz:

> "Forneça um método setter para substituir os valores desse campo."

Então:

```
public void setStrategy(PaymentStrategy strategy) {
    this.strategy = strategy;
}
```

Agora podemos fazer:

```
PaymentService paymentService = new PaymentService();

paymentService.setStrategy(
    new CreditCardStrategy()
);
```

Ou:

```
paymentService.setStrategy(
    new PayPalStrategy()
);
```

Ou:

```
paymentService.setStrategy(
    new MBWayStrategy()
);
```

O mesmo `PaymentService` pode trabalhar com qualquer estratégia.

# 6. O Context delega o trabalho

Agora falta uma coisa importante.

O `PaymentService` precisa de um método:

```
public void pay(double amount) {
    strategy.pay(amount);
}
```

A classe completa fica:

```
public class PaymentService {

    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void pay(double amount) {
        strategy.pay(amount);
    }
}
```

E aqui está **o coração do Strategy**:

```
strategy.pay(amount);
```

O `PaymentService` diz:

> "Strategy, faz o pagamento."

Mas ele **não sabe como**.

Pode ser:

```
CreditCardStrategy
        ↓
     pay()

PayPalStrategy
        ↓
     pay()

MBWayStrategy
        ↓
     pay()
```

O `Context` simplesmente delega.

O Refactoring.Guru descreve precisamente esta responsabilidade: o contexto mantém uma referência para a estratégia e delega o trabalho para ela, sem conhecer a implementação concreta.

### 7. O cliente escolhe a estratégia

Agora chegamos ao quinto passo.

> "Os clientes do contexto devem associá-lo a uma estratégia adequada."

Por exemplo:

```
public class Main {

    public static void main(String[] args) {

        PaymentService paymentService =
            new PaymentService();

        paymentService.setStrategy(
            new CreditCardStrategy()
        );

        paymentService.pay(100);
    }
}
```

Resultado:

```
Pagamento com cartão: 100.0
```

Podemos mudar a estratégia:

```
paymentService.setStrategy(
    new PayPalStrategy()
);

paymentService.pay(100);
```

Agora:

```
Pagamento com PayPal: 100.0
```

E novamente:

```
paymentService.setStrategy(
    new MBWayStrategy()
);

paymentService.pay(100);
```

Resultado:

```
Pagamento com MB WAY: 100.0
```

**O `PaymentService` não mudou.**

Apenas trocámos o objeto que representa o comportamento.

---

# A implementação completa

No final temos:

```
                 PaymentStrategy
                 ┌──────────────┐
                 │    pay()     │
                 └──────┬───────┘
                        │
          ┌─────────────┼─────────────┐
          │             │             │
          ▼             ▼             ▼
   CreditCard       PayPal         MBWay
   Strategy         Strategy       Strategy
```

E o `Context`:

```
              PaymentService
                    │
                    │ possui
                    ▼
             PaymentStrategy
                    │
                    │ pay()
                    ▼
          estratégia selecionada
```

---

# Agora vamos relacionar diretamente com os 5 passos

#### Passo 1 — Encontrar o comportamento variável

Antes:

```
if (type.equals("CREDIT_CARD")) {
    ...
}
else if (type.equals("PAYPAL")) {
    ...
}
else if (type.equals("MBWAY")) {
    ...
}
```

Identificamos:

```
formas diferentes de efetuar um pagamento
```

---

#### Passo 2 — Criar a interface

Criamos:

```
public interface PaymentStrategy {

    void pay(double amount);
}
```

Definimos **o contrato comum**.

---

#### Passo 3 — Criar as estratégias concretas

Separamos cada algoritmo:

```
CreditCardStrategy
PayPalStrategy
MBWayStrategy
```

Todos implementam:

```
PaymentStrategy
```

---

#### Passo 4 — Colocar a estratégia no Context

O `PaymentService` passa a ter:

```
private PaymentStrategy strategy;
```

E:

```
public void setStrategy(PaymentStrategy strategy) {
    this.strategy = strategy;
}
```

Depois delega:

```
public void pay(double amount) {
    strategy.pay(amount);
}
```

---

#### Passo 5 — O cliente escolhe

O cliente decide:

```
paymentService.setStrategy(
    new PayPalStrategy()
);
```

Depois:

```
paymentService.pay(100);
```

O cliente escolheu **como** o `PaymentService` deve efetuar o pagamento.

---

# A ideia mais importante

Não memorizes o Strategy como:

> "Criar uma interface, depois criar várias classes."

Isso é apenas a implementação.

Memoriza-o assim:

> **Tenho uma coisa que precisa de fazer X, mas existem várias formas diferentes de fazer X. Então retiro essas formas para objetos separados e permito que o objeto principal utilize uma delas através de uma interface comum.**

Visualmente:

```
ANTES

PaymentService
      │
      ├── algoritmo A
      ├── algoritmo B
      └── algoritmo C


DEPOIS

                 PaymentService
                       │
                       ▼
                PaymentStrategy
                       │
             ┌─────────┼─────────┐
             ▼         ▼         ▼
         Strategy A Strategy B Strategy C
```

A grande mudança é:

**antes, o `Context` executava os algoritmos; depois, o `Context` delega os algoritmos.**

E é precisamente essa delegação através de composição que distingue o Strategy de uma solução baseada em herança.

### Uma distinção muito importante

Não confundas:

```
Strategy = diferentes formas de fazer a mesma coisa
```

com:

```
Command = transformar uma ação num objeto
```

Por exemplo:

```
Strategy:
"Como calculo o preço?"
 ├── PreçoNormal
 ├── PreçoDesconto
 └── PreçoPremium


Command:
"Que operação quero executar?"
 ├── CriarPedidoCommand
 ├── CancelarPedidoCommand
 └── EnviarPedidoCommand
```

O próprio Refactoring.Guru destaca esta diferença: Strategy normalmente representa **diferentes maneiras de realizar a mesma tarefa**, enquanto Command transforma uma operação em objeto para permitir, entre outras coisas, adiá-la, colocá-la numa fila ou guardar histórico.