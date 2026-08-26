Permite que um objeto altere o seu comportamento quando o seu estado interno muda.

**Exemplos de uso:** O padrão State é comumente usado em Java para converter `switch`máquinas de estado massivas baseadas em objetos.

### Exemplos nas Java Core Libraries

- [`javax.faces.lifecycle.LifeCycle#execute()`](http://docs.oracle.com/javaee/7/api/javax/faces/lifecycle/Lifecycle.html#execute-javax.faces.context.FacesContext-)(controlado por [`FacesServlet`](http://docs.oracle.com/javaee/7/api/javax/faces/webapp/FacesServlet.html): o comportamento depende da fase (estado) atual do ciclo de vida do JSF)

**Identificação:** O padrão de estado pode ser reconhecido por métodos que alteram seu comportamento dependendo do estado dos objetos. Você pode confirmar a identificação se observar que esse estado pode ser controlado ou substituído por outros objetos, incluindo os próprios objetos de estado.

## Interface de um reprodutor de mídia

Neste exemplo, o padrão State permite que os mesmos controles do reprodutor de mídia se comportem de maneira diferente, dependendo do estado de reprodução atual. A classe principal do reprodutor contém uma referência a um objeto de estado, que realiza a maior parte do trabalho para o reprodutor. Algumas ações podem acabar substituindo o objeto de estado por outro, o que altera a forma como o reprodutor reage às interações do usuário.
## Problema

`State` está relacionado ao conceito de uma _Máquina de Estados Finitos.

[_Máquina de Estados Finitos._ .](https://en.wikipedia.org/wiki/Finite-state_machine)

A qualquer momento, existe um número finito de estados em que o programa pode estar.
Dentro de cada estado, o programa se comporta de forma diferente e pode ser alterado.
Dependendo do estado atual, o programa pode ou não alterar para estados específicos.
As regras de alternância (transições) são finitas e predeterminadas.

Exemplo: `Document` pode estar `Draft`, `Moderation` e `Published.

![[Pasted image 20260826164648.png]]

As máquinas de estado são implementadas com instruções `if`, `switch` que selecionam o comportamento apropriado dependendo do estado atual.

Maior fragilidade de uma máquina de estados baseada em condicionais é quando se adiciona mais estados e comportamentos.

## Solução

`State` sugere que crie novas classes para todos os estados possíveis de um objeto e extraia todos os comportamentos específicos de cada estado para essas classes.

Em vez de implementar todos os comportamentos, o objeto original `contexto` armazena uma referência a um dos objetos de estado e delega o trabalho a esse objeto

![[Pasted image 20260826170931.png]]

Para fazer a transição de contexto, substitui o objeto de estado ativo por outro.
Só é possível se todas as classes de estado seguirem a mesma interface.

Semelhante à `strategy`. No `Stete`, os estados específicos podem ter conhecimento uns dos outros e inicar transições, na `strategy` quase nunca têm conhecimento umas das outras.

## Analogia com o mundo real

Botões e interruptores do smartphone comportam-se de maneira diferente dependendo do estado atual.

- Com o telefone desbloqueado, pressionar os botões executa diversas funções.
- Com o telefone bloqueado, pressionar qualquer botão leva à tela de desbloqueio.
- Quando a bateria do telefone estiver fraca, pressionar qualquer botão exibirá a tela de carregamento.

## Estrutura

![[Pasted image 20260826171327.png]]

1. `Context` armazena uma referência a um dos objetos de estado concreto e delega nele o trabalho específico. O `Context` comunica com o objeto de estado pela interface. O `Context` expõe um método setter para passar um novo objeto de estado.

2. Interface `State` declara os métodos específicos de cada estado. Deve fazer sentido para todos os estados concretos.

3. `Estados concretos` fornecem as suas implementações. Para evitar duplicação de código semelhante pode forneceder classes abstratas intermediárias que encapsulam comportamento.

   Os objetos de estado podem armazenar uma referência inversa ao objeto de contexto. Através desta referência, o estado pode buscar qualquer info necessária do objeto de contexto, bem como iniciar transições de estado.

3. Tanto o `Context` quanto os `Estados concretos` podem definir o próximo estado e realizar a transição de estado.

## Pseudocódigo

![[Pasted image 20260826172048.png]]


## Aplicabilidade

problema: `State` quando tiver um objeto que se comporta de maneira diferente dependendo do seu estado atual, o número de estados for enorme e o código específico de cada estado mudar com frequência.

solução: extrair todo o código específico de cada estado para um conjunto de classes distintas. Como resultado podem ser adicionados novos estados / alterar os existentes independentemente uns dos outros, reduzindo o custo de manutenção

---

problema: `State`quando existir uma classe cheia de condicionais que alteram o comportamento da classe

solução: `state` permite extrair ramificações dessas condicionais para métodos das classes de estado. Podem ser removidos campos temporários e métodos auxiliares envolvidos em código específico do estado da sua classe principal.

---

problema: `state` quando existir código duplicado em estados e transições de uma máquina de estados.

solução: `state` permite compor hierarquias de classes de estado e reduzir a duplicação, extraindo o código comum para classes base abstratas.
## Como implementar

O **State** é muito parecido estruturalmente com o **Strategy**, por isso é ótimo estudá-lo logo depois. A grande diferença está na **intenção**:

> **Strategy:** escolhemos um algoritmo para executar uma tarefa.  
> **State:** o comportamento do objeto muda automaticamente conforme o seu estado interno.

Vou usar um exemplo clássico: **uma máquina de venda automática**.


### 1. Primeiro, perceber o problema

Imagina uma máquina com estes estados:

```
Sem moeda
   ↓
Moeda inserida
   ↓
Produto selecionado
   ↓
Produto entregue
```

Dependendo do estado atual, a mesma operação pode ter comportamentos diferentes.

Por exemplo, temos:

```
insertCoin();
selectProduct();
dispense();
```

Mas `insertCoin()` não significa a mesma coisa em todos os estados.

### Estado: sem moeda

```
insertCoin()
→ aceitar moeda
```

### Estado: moeda inserida

```
insertCoin()
→ rejeitar segunda moeda
```

### Estado: produto selecionado

```
insertCoin()
→ operação inválida
```

Normalmente alguém começaria com:

```
if (state == NO_COIN) {
    ...
} else if (state == HAS_COIN) {
    ...
} else if (state == PRODUCT_SELECTED) {
    ...
}
```

E isto começa a aparecer em vários métodos:

```
public void insertCoin() {
    if (...) {
        ...
    } else if (...) {
        ...
    }
}

public void selectProduct() {
    if (...) {
        ...
    } else if (...) {
        ...
    }
}

public void dispense() {
    if (...) {
        ...
    } else if (...) {
        ...
    }
}
```

O problema é que o comportamento específico de cada estado fica espalhado pelo `Context`.

É exatamente este tipo de situação que o **State** pretende organizar. ([refactoring.guru](https://refactoring.guru/design-patterns/state))

### 2. Passo 1 — Escolher o Context

O primeiro passo diz:

> "Decida qual classe atuará como contexto."

Neste exemplo:

```
public class VendingMachine {
}
```

A `VendingMachine` será o nosso **Context**.

Ela contém o estado atual da máquina e operações como:

```
insertCoin()
selectProduct()
dispense()
```

Antes do State, poderíamos ter:

```
public class VendingMachine {

    private String state;

    public void insertCoin() {

        if (state.equals("NO_COIN")) {
            System.out.println("Moeda aceita.");

        } else if (state.equals("HAS_COIN")) {
            System.out.println("Já existe uma moeda.");

        } else if (state.equals("PRODUCT_SELECTED")) {
            System.out.println("Produto já selecionado.");
        }
    }
}
```

O `Context` está a acumular demasiadas regras específicas dos estados.


### 3. Passo 2 — Criar a interface State

Agora perguntamos:

> "Quais comportamentos dependem do estado?"

Neste caso:

```
insertCoin()
selectProduct()
dispense()
```

Criamos:

```
public interface VendingMachineState {

    void insertCoin();

    void selectProduct();

    void dispense();
}
```

Esta é a nossa **State interface**.

Ela define os comportamentos que podem variar de acordo com o estado.

Visualmente:

```
           VendingMachineState
                   │
       ┌───────────┼───────────┐
       ▼           ▼           ▼
   NoCoinState  HasCoinState  ProductSelectedState
```


### 4. Passo 3 — Criar uma classe para cada estado

Agora criamos uma classe para cada estado real.

Por exemplo:

```
NoCoinState
HasCoinState
ProductSelectedState
```

Cada classe vai conter **o comportamento específico daquele estado**.

# 5. Criar o `NoCoinState`

Começamos com:

```
public class NoCoinState implements VendingMachineState {

    @Override
    public void insertCoin() {
        System.out.println("Moeda aceita.");
    }

    @Override
    public void selectProduct() {
        System.out.println("Insira uma moeda primeiro.");
    }

    @Override
    public void dispense() {
        System.out.println("Nenhum produto pode ser entregue.");
    }
}
```

Agora o comportamento relacionado com o estado:

```
SEM MOEDA
```

está concentrado numa única classe.


### 6. Criar o `HasCoinState`

Agora:

```
public class HasCoinState implements VendingMachineState {

    @Override
    public void insertCoin() {
        System.out.println("Já existe uma moeda.");
    }

    @Override
    public void selectProduct() {
        System.out.println("Produto selecionado.");
    }

    @Override
    public void dispense() {
        System.out.println("Selecione um produto primeiro.");
    }
}
```

Temos:

```
NoCoinState
    │
    ├── insertCoin()
    ├── selectProduct()
    └── dispense()

HasCoinState
    │
    ├── insertCoin()
    ├── selectProduct()
    └── dispense()
```

Cada classe sabe **como a máquina deve reagir naquele estado**.

### 7. Criar `ProductSelectedState`

Por exemplo:

```
public class ProductSelectedState
        implements VendingMachineState {

    @Override
    public void insertCoin() {
        System.out.println("Produto já selecionado.");
    }

    @Override
    public void selectProduct() {
        System.out.println("Produto já selecionado.");
    }

    @Override
    public void dispense() {
        System.out.println("Produto entregue.");
    }
}
```

Agora temos:

```
                 VendingMachineState
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
    NoCoinState     HasCoinState    ProductSelectedState
```


### 8. O problema que aparece aqui

Agora surge exatamente a questão mencionada no passo 3.

As classes de estado podem precisar de informações da `VendingMachine`.

Por exemplo:

```
public class HasCoinState {

    public void selectProduct() {
        // precisamos modificar o estado da máquina
    }
}
```

Como é que fazemos isso?

A classe de estado precisa de uma referência ao `Context`.

Por exemplo:

```
public class HasCoinState
        implements VendingMachineState {

    private VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    // ...
}
```

Agora:

```
VendingMachine
      ▲
      │
      │ referência
      │
HasCoinState
```

Isso permite ao estado fazer:

```
machine.setState(...);
```


### 9. Passo 4 — Adicionar o State ao Context

Agora voltamos à `VendingMachine`.

Precisamos de:

```
private VendingMachineState state;
```

Portanto:

```
public class VendingMachine {

    private VendingMachineState state;

}
```

E criamos o setter:

```
public void setState(VendingMachineState state) {
    this.state = state;
}
```

Agora temos:

```
              VendingMachine
                    │
                    │ possui
                    ▼
           VendingMachineState
                    │
          ┌─────────┼─────────┐
          ▼         ▼         ▼
       NoCoin    HasCoin   ProductSelected
```

Este é um ponto muito importante:

A `VendingMachine` **não guarda mais algo como**:

```
private String state;
```

Em vez disso, guarda um **objeto que representa o estado**:

```
private VendingMachineState state;
```

### 10. Passo 5 — Delegar as operações ao State

Este é o coração do padrão.

Antes teríamos:

```
public void insertCoin() {

    if (state.equals("NO_COIN")) {
        ...
    }
    else if (state.equals("HAS_COIN")) {
        ...
    }
    else if (...) {
        ...
    }
}
```

Agora eliminamos essas condicionais.

Fazemos simplesmente:

```
public void insertCoin() {
    state.insertCoin();
}
```

E:

```
public void selectProduct() {
    state.selectProduct();
}
```

E:

```
public void dispense() {
    state.dispense();
}
```

A `VendingMachine` deixou de perguntar:

> "Em que estado estou?"

Agora diz:

> "Estado atual, trata esta operação."


### 11. Isto é o verdadeiro coração do State

Temos:

```
public void insertCoin() {
    state.insertCoin();
}
```

Se:

```
state = NoCoinState
```

então:

```
state.insertCoin()
       ↓
NoCoinState.insertCoin()
```

Se:

```
state = HasCoinState
```

então:

```
state.insertCoin()
       ↓
HasCoinState.insertCoin()
```

O método chamado é o mesmo:

```
state.insertCoin();
```

Mas o comportamento muda.

Isso acontece através de **polimorfismo**.

# 12. Passo 6 — Alterar o estado

Agora chegamos ao último passo:

> "Para alterar o estado do contexto, crie uma instância de uma das classes de estado e passe-a para o contexto."

Por exemplo:

```
machine.setState(
    new HasCoinState(machine)
);
```

Agora:

```
VendingMachine
       │
       ▼
HasCoinState
```

Depois podemos fazer:

```
machine.setState(
    new ProductSelectedState(machine)
);
```

Agora:

```
VendingMachine
       │
       ▼
ProductSelectedState
```

O estado mudou.

# 13. Mas quem deve mudar o estado?

Existem três possibilidades.

## Opção 1 — O cliente

O cliente faz:

```
machine.setState(
    new HasCoinState(machine)
);
```

É simples, mas geralmente não é a melhor solução.

O cliente começa a conhecer os detalhes internos dos estados.

---

## Opção 2 — O próprio Context

A `VendingMachine` pode fazer:

```
public void insertCoin() {

    state.insertCoin();
}
```

E determinadas operações podem alterar o estado dentro do contexto.

---

## Opção 3 — O próprio State

É muito comum o estado atual mudar para outro estado.

Por exemplo:

```
NoCoinState
    │
    │ insertCoin()
    ▼
HasCoinState
```

Então:

```
public class NoCoinState
        implements VendingMachineState {

    private VendingMachine machine;

    public NoCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {

        System.out.println("Moeda aceita.");

        machine.setState(
            new HasCoinState(machine)
        );
    }

    // ...
}
```

Agora temos algo muito interessante.

O próprio estado diz:

> "Depois desta operação, a máquina passa para outro estado."


### 14. O fluxo completo

Agora podemos ter:

```
              ┌───────────────┐
              │ NoCoinState   │
              └───────┬───────┘
                      │
                 insertCoin()
                      │
                      ▼
              ┌───────────────┐
              │ HasCoinState  │
              └───────┬───────┘
                      │
                selectProduct()
                      │
                      ▼
          ┌────────────────────────┐
          │ ProductSelectedState   │
          └────────────┬───────────┘
                       │
                   dispense()
                       │
                       ▼
              ┌───────────────┐
              │ NoCoinState   │
              └───────────────┘
```

Isso é essencialmente uma **máquina de estados**.

### 15. A implementação simplificada completa

## Interface

```
public interface VendingMachineState {

    void insertCoin();

    void selectProduct();

    void dispense();
}
```

## Context

```
public class VendingMachine {

    private VendingMachineState state;

    public VendingMachine(VendingMachineState state) {
        this.state = state;
    }

    public void setState(VendingMachineState state) {
        this.state = state;
    }

    public void insertCoin() {
        state.insertCoin();
    }

    public void selectProduct() {
        state.selectProduct();
    }

    public void dispense() {
        state.dispense();
    }
}
```

## Estado

```
public class NoCoinState implements VendingMachineState {

    private VendingMachine machine;

    public NoCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {

        System.out.println("Moeda aceita.");

        machine.setState(
            new HasCoinState(machine)
        );
    }

    @Override
    public void selectProduct() {
        System.out.println("Insira uma moeda primeiro.");
    }

    @Override
    public void dispense() {
        System.out.println("Nenhum produto disponível.");
    }
}
```

## Outro estado

```
public class HasCoinState implements VendingMachineState {

    private VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Já existe uma moeda.");
    }

    @Override
    public void selectProduct() {

        System.out.println("Produto selecionado.");

        machine.setState(
            new ProductSelectedState(machine)
        );
    }

    @Override
    public void dispense() {
        System.out.println("Selecione um produto primeiro.");
    }
}
```

E:

```
public class ProductSelectedState
        implements VendingMachineState {

    private VendingMachine machine;

    public ProductSelectedState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Produto já selecionado.");
    }

    @Override
    public void selectProduct() {
        System.out.println("Produto já selecionado.");
    }

    @Override
    public void dispense() {

        System.out.println("Produto entregue.");

        machine.setState(
            new NoCoinState(machine)
        );
    }
}
```

### 16. E como o cliente utiliza?

```
public class Main {

    public static void main(String[] args) {

        VendingMachine machine =
            new VendingMachine(null);

        machine.setState(
            new NoCoinState(machine)
        );

        machine.insertCoin();

        machine.selectProduct();

        machine.dispense();
    }
}
```

O fluxo será:

```
machine.insertCoin()
        ↓
NoCoinState
        ↓
muda para HasCoinState

machine.selectProduct()
        ↓
HasCoinState
        ↓
muda para ProductSelectedState

machine.dispense()
        ↓
ProductSelectedState
        ↓
muda para NoCoinState
```

---

### 17. Agora vamos mapear exatamente os 6 passos

##### ① Identificar o Context

```
public class VendingMachine
```

É o objeto cujo comportamento depende do estado.

---

##### ② Criar a interface State

```
public interface VendingMachineState {

    void insertCoin();

    void selectProduct();

    void dispense();
}
```

Define os comportamentos que podem mudar.

---

##### ③ Criar uma classe para cada estado

```
NoCoinState
HasCoinState
ProductSelectedState
```

Cada uma contém o comportamento específico daquele estado.

---

##### ④ Colocar o State no Context

```
private VendingMachineState state;
```

E:

```
public void setState(VendingMachineState state) {
    this.state = state;
}
```

---

##### ⑤ Substituir as condicionais

Antes:

```
if (state == NO_COIN) {
    ...
}
else if (state == HAS_COIN) {
    ...
}
```

Depois:

```
state.insertCoin();
```

O polimorfismo decide qual implementação executar.

---

##### ⑥ Alterar o estado

Por exemplo:

```
machine.setState(
    new HasCoinState(machine)
);
```

Ou, frequentemente, a própria classe de estado faz a transição:

```
machine.setState(
    new ProductSelectedState(machine)
);
```


---

# Strategy vs State — a comparação mais importante

Como acabaste de estudar **Strategy**, esta é a parte que eu considero mais importante.

Estruturalmente, os dois são muito parecidos:

### Strategy

```
Context
   │
   ▼
Strategy
   ├── StrategyA
   ├── StrategyB
   └── StrategyC
```

### State

```
Context
   │
   ▼
State
   ├── StateA
   ├── StateB
   └── StateC
```

Mas a **intenção** é diferente.

### Strategy

O cliente diz:

```
context.setStrategy(
    new PayPalStrategy()
);
```

Está essencialmente a dizer:

> **"Quero que utilizes este algoritmo."**

A estratégia normalmente permanece enquanto for necessária.

---

### State

O objeto muda de estado durante a sua própria execução:

```
Estado A
   ↓
operação
   ↓
Estado B
   ↓
operação
   ↓
Estado C
```

Ou seja:

> **"O meu comportamento depende de onde estou no meu ciclo de vida."**

---

# Uma forma simples de distinguir

Se encontrares:

```
if (tipo == A) {
    // algoritmo A
}
else if (tipo == B) {
    // algoritmo B
}
```

pensa primeiro em:

**Strategy**

> "Tenho diferentes maneiras de executar uma tarefa."

Mas se encontrares:

```
if (estado == A) {
    // comportamento
}
else if (estado == B) {
    // comportamento
}
```

e o objeto **transita entre esses estados**, pensa em:

**State**

> "O comportamento do objeto muda conforme o seu estado atual."

---

## E há uma diferença conceptual ainda mais profunda

No Strategy:

```
       Cliente
          │
          ▼
       Context
          │
          ▼
      Strategy
```

O **cliente normalmente escolhe** a estratégia.

No State:

```
       Context
          │
          ▼
        State
          │
          │ muda
          ▼
      Outro State
```

O **próprio objeto/contexto pode mudar de estado durante o seu ciclo de vida**.

É por isso que o State pode ser visto como uma forma de transformar uma série complicada de `if/else` dependentes de estado numa espécie de **máquina de estados baseada em polimorfismo**. ([refactoring.guru](https://refactoring.guru/design-patterns/state))

**Frase para guardar:**

> **State encapsula os comportamentos associados a cada estado e permite que o objeto altere o seu comportamento quando o seu estado interno muda.**