O padrão `Command` transforma uma requisição num objeto independente que contém todas as informações da requisição. Essa transformação permite passar requisições como argumentos de um método, atrasar ou enfileirar a execução de uma requisição e suportar operações reversíveis.


**Exemplos de uso:** O padrão Command é bastante comum em código Java. Geralmente, ele é usado como uma alternativa para callbacks na parametrização de elementos da interface do usuário com ações. Também é usado para enfileirar tarefas, rastrear o histórico de operações, etc.

### Exemplos nas Java Core Libraries

Aqui estão alguns exemplos de comandos nas bibliotecas principais do Java:

- Todas as implementações de[`java.lang.Runnable`](http://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)

- Todas as implementações de[`javax.swing.Action`](http://docs.oracle.com/javase/8/docs/api/javax/swing/Action.html)

**Identificação:** Se você observar um conjunto de classes relacionadas que representam ações específicas (como "Copiar", "Recortar", "Enviar", "Imprimir", etc.), isso pode ser um padrão Command. Essas classes devem implementar a mesma interface/classe abstrata. Os comandos podem implementar as ações relevantes por conta própria ou delegar o trabalho a objetos separados — que serão os receptores. A última peça do quebra-cabeça é identificar um invocador — procure por uma classe que aceite os objetos de comando nos parâmetros de seus métodos ou construtor.


![[Pasted image 20260827102301.png]]


## Problema

Aplicação de edição de texto. Barra de ferramentas com vários botões para diferentes operações do editor. Foi criado uma classe `Button` que pode ser utilizada para botões da barra de ferramentas quanto para botões genêricos.

![[Pasted image 20260827102736.png]]

Botões diferentes, funções diferentes. Onde seria colocado o código para os vários manipuladores de click desses botões. Solução simples, criar subclasses para cada local onde o botão é usado.

![[Pasted image 20260827102854.png]]

A Abordagem falha. Porque existe um número grande de subclasses, o que não seria um problema se não existisse risco de quebrar o código nessas subclasses sempre que fosse mudificado `Button`

![[Pasted image 20260827103016.png]]

Algumas operações podem ser invocadas de vários lugares.
`Copiar`, barra de ferramentas, menu de contexto, Ctrl+C

Quando a aplicação tinha apenas a barra de ferramentas, era aceitável colocar a implementação de várias operações nas subclasses. Ter o código para copiar texto na subclasse `CopyButton` funciona bem. Mas ao implementar menu de contexto, atalhos, o código foi duplicado em várias classes ou tornar os menus dependentes de botões.

## Solução

Um bom design baseia-se no princípio da separação de responsabilidades. Divisão aplicação em camadas. O mais comum: interface gráfica do utilizador (GUI) e outra para lógica de negócio.

Uma GUI é responsável por renderizar uma imagem atraente na tela, capturar qualquer entrada e exibir resultados das ações do utilizador e da aplicação.

Quando se trata de algo imiportante, calcular a trajetória da Lua ou elaborar um relatório anual, a GUI delega essa tarefa á camada de lógica de negócio.

No código: um objeto da interface gráfica chama um método de um objeto da lógica de negócios, passando argumentos. É um processo descrito como um objeto enviado uma solicitação para outro.

![[Pasted image 20260827103858.png]]

O padrão `Command` sugere que objetos GUI não devem enviar solicitações diretamente. Deve extrair todos os detalhes da solicitação, como o objeto que está sendo chamado, o nome do método e a lista de argumentos, para uma classe `Command` com um único método que dispara essa solicitação.

Os objetos `Command` servem como elos entre vários objetos da GUI e da lógica de negócio. O objeto da GUI não precisa saber qual o objeto da lógica de negócios receberá a solicitação e como será processada. GUI apenas aciona `Command` que lida com todos os detalhes.

![[Pasted image 20260827104302.png]]

Próximo passo é fazer com que os `Command` implementem a mesma interface. Possui apenas um único método de execução que não recebe parâmetros. Essa interface permite usar vários comandos com o mesmo remetente da requisição, sem acoplá-lo a classes concretas.

Bônus: pode alternar entre objetos `Command` vinculados ao remetente, alterando o comportamento do remetente em tempo de execução.
Um objeto da GUI pode ter fornecido alguns parâmetros ao objeto da camada de negócio.
O método de execução do `Command` não tem parâmetros. O `Command` deve ser pré-configurado com esses dados ou ser capaz de obtê-los por conta própria.

![[Pasted image 20260827104828.png]]


Editor de texto, depois de aplicar o `Command` não é necessário todas as subclasses para implementar diversos comportamentos. Basta adicionar um campo à classe `Button` que armazena uma referência a um objeto `Command` e fazer com que o botão execute esse comando ao ser clicado.

Implementar várias classes de `Command` para cada operação e vincular a botões específicos, dependendo do comportamento pretendido.

Outros elementos GUI, menus, atalhos, diálogos, pode ser implementados da mesma forma. Serão vinculados a um `Command` que será executado quando o utilizador interagir com o elemento da interface.

Os `Command` tornam-se numa camada intermediária que reduz o acoplamento entre a GUI e a lógica de negócios.

## Analogia com o mundo real

![[Pasted image 20260827105721.png]]

O pedido em papel serve como uma ordem. O pedido contém todas as informações relevantes para o preparo da refeiçõa.

## Estrutura

![[Pasted image 20260827105833.png]]


1. A classe remetente `Invoker` é responsável por iniciar as requisições. Essa classe deve ter um espaço para armazenar uma referência a um objeto `Command`. O `Invoker` aciona o `Command` em vez de enviar requisição ao destinatário. O `Invoker` não é responsável por criar o objeto de comando. Normalmente tem um comando pré-criado.

2. A interface `Command` declara apenas um único método para executar.

3. Os `Comandos Concretos`implementam vários tipos de requisições. Um `Comando Concreto` não deve executar o trabalho por si só, mas sim passar a camada para um dos objetos da lógica de negócio.

   Os parâmetros necessários para executar um método num `Receiver`podem ser declarados como campos no `Command`. É possível tornar os objetos de `Command`imutáveis permitindo a inicialização desses apenas por meio do construtor.

4. A classe `Receiver` tem alguma lógica de negócio. Quase qualquer objeto pode atuar como `Receiver`. O `Command` lida com os detalhes de como uma solicitação é passada para o `Receiver`, enquanto o próprio receptor realiza o trabalho em si.

5. O `Client` cria e configura objetos de comando concreto. O cliente deve passar todos os parâmetros da requisição, incluindo uma instância do receptor, para o construtor do comando. Depois disse, o `Command` resultante pode ser associado a um ou mais `Invokers`.

## Pseudocódigo

![[Pasted image 20260827110804.png]]


Comandos que resultam na alteração do estado do editor (ex. recortar, colar) criam uma cópia de segurança do estado do editor antes de executar a operação `Command`. Após a execução do `Command` é adicionado ao histórico de `Command` (pilha) com a cópia de segurança do estado do editor naquele momento. Se for necessário reverter, lê a cópia de segurança.

O código do cliente (elementos da interface gráfica, histórico de comandos, etc.) não está acoplado a classes de comando concretas, pois interage com os comandos por meio da interface de comando. Essa abordagem permite introduzir novos comandos no aplicativo sem quebrar nenhum código existente.

## Aplicabilidade

problema: utilizar `Command` quando desejar parametrizar objetos com operações.

solução: `Command` permite transformar uma chamada de método específica num objeto independente. Pode passar comandos com argumentos, armazená-los dentro de outros objetos, alternar entre comandos vinculados em tempo de execução.

Exemplo: Componente GUI com um menu de contexto, permitir utilizador configurar items de menu que acionem operações quando o utilizador clicar.

----

problema: `Command` quando deseja enfileirar operações, agendar execução ou executá-las remotamente

solução: o `Command` pode ser serializado, convertido numa string que pode ser guardado num ficheiro ou BD. A String pode ser restaurada como objeto comando original. Dessa forma é possível atrasar e agendar a execução de comandos.

---

problema: usar `Command` quando desejar implementar operações reversíveis

solução: implementação do `desfazer/refazer`

problema: para reverter é preciso histórico de operações. A pilha `Command` tem todos os objetos executados.

O Método tem 2 desvantagens:
- não é fácil saltar o estado de uma aplicação, parte pode ser privada. Pode ser atenuado com o `Memento`

- os backups de estado podem consumir bastante RAM. Em vez de restaurar o estado anterior, o comando executa a operação inversa (pode ser difícil ou até mesmo impossível implementar)



## Como implementar

#### Command — Como implementar

A ideia principal do **Command** é transformar uma **ação/pedido** num **objeto**.

Em vez de termos:

```
Botão → chama diretamente → Luz.acender()
```

passamos a ter:

```
Botão → Command → Luz.acender()
```

O benefício é que o botão **não precisa de saber como a ação é realizada**.


#### 1. Declarar a interface de comando

> Declare a interface de comando com um único método de execução.

Primeiro criamos uma interface que representa uma ação.

Por exemplo:

```
public interface Command {
    void execute();
}
```

O `Command` diz apenas:

> "Qualquer comando sabe executar uma ação."

Não interessa ao remetente saber **qual** ação será executada.

Por exemplo:

```
Command
   │
   ├── TurnOnCommand
   ├── TurnOffCommand
   └── ChangeVolumeCommand
```

Todos possuem:

```
execute()
```


#### 2. Criar os comandos concretos

Agora precisamos transformar cada **pedido específico** numa classe.

Imagina que temos uma televisão:

```
public class Television {

    public void turnOn() {
        System.out.println("Televisão ligada");
    }

    public void turnOff() {
        System.out.println("Televisão desligada");
    }
}
```

A televisão é quem realmente sabe **como ligar e desligar**.

Ela será o **receptor (Receiver)**.

Criamos então um comando para ligá-la:

```
public class TurnOnCommand implements Command {

    private Television television;

    public TurnOnCommand(Television television) {
        this.television = television;
    }

    @Override
    public void execute() {
        television.turnOn();
    }
}
```

Aqui temos três coisas importantes.

### O comando guarda o receptor

```
private Television television;
```

O comando precisa saber **quem vai executar a operação**.

Neste caso:

```
TurnOnCommand
       │
       ▼
Television
```

### O receptor é passado pelo construtor

```
public TurnOnCommand(Television television) {
    this.television = television;
}
```

Quando criamos o comando:

```
Television television = new Television();

Command command = new TurnOnCommand(television);
```

estamos a dizer:

> "Este comando de ligar deve atuar sobre esta televisão."

### `execute()` faz a ligação

```
@Override
public void execute() {
    television.turnOn();
}
```

Portanto:

```
command.execute()
       │
       ▼
television.turnOn()
```


#### 3. Identificar os remetentes

Agora precisamos de identificar **quem dispara o comando**.

Esse objeto chama-se **Invoker (remetente)**.

Por exemplo, um botão:

```
public class Button {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }
}
```

O botão não sabe absolutamente nada sobre a televisão.

Ele apenas conhece:

```
Command
```

Quando alguém carrega no botão:

```
button.press();
```

o botão faz:

```
command.execute();
```

---

## Porque isto é importante?

Sem Command poderíamos ter:

```
public class Button {

    private Television television;

    public void press() {
        television.turnOn();
    }
}
```

Agora o `Button` está diretamente dependente de `Television`.

Se amanhã quisermos que o botão:

- ligue uma lâmpada;
- abra uma porta;
- inicie um motor;
- execute um pagamento;

teríamos de alterar o `Button`.

Com Command:

```
public class Button {

    private Command command;

    public void press() {
        command.execute();
    }
}
```

O botão não precisa de mudar.

#### 4. O remetente executa o comando

O texto diz:

> Alterar os remetentes para que executem o comando em vez de enviar uma solicitação diretamente ao destinatário.

Esta é provavelmente a parte **mais importante** do padrão.

Antes:

```
Button
   │
   │ turnOn()
   ▼
Television
```

O botão conhece diretamente a televisão.

Depois:

```
Button
   │
   │ execute()
   ▼
Command
   │
   │ turnOn()
   ▼
Television
```

Agora o botão só conhece:

```
Command
```

Não conhece:

```
Television
```

#### 5. O cliente inicializa tudo

Finalmente temos o **Client**.

O cliente é quem conhece todas as peças e as liga.

A ordem indicada pelo texto é:

```
1. Criar receptores
2. Criar comandos
3. Criar remetentes
4. Associar tudo
```

Vamos fazer isso.

---

## Passo 1 — Criar o receptor

```
Television television = new Television();
```

Temos:

```
Television
   ↑
Receiver
```

---

## Passo 2 — Criar o comando

```
Command turnOnCommand = new TurnOnCommand(television);
```

Agora:

```
TurnOnCommand
      │
      ▼
Television
```

O comando sabe para quem deve enviar a operação.

---

## Passo 3 — Criar o remetente

```
Button button = new Button();
```

Temos:

```
Button
  ↑
Invoker
```

---

## Passo 4 — Associar o comando ao remetente

```
button.setCommand(turnOnCommand);
```

Agora temos a estrutura completa:

```
Button
  │
  │ Command
  ▼
TurnOnCommand
  │
  │ Receiver
  ▼
Television
```


#### 6. Executar

Agora basta:

```
button.press();
```

O que acontece internamente?

##### 1. O botão recebe o pedido

```
button.press();
```

##### 2. O botão executa o comando

```
command.execute();
```

##### 3. O comando chama o receptor

```
television.turnOn();
```

Portanto:

```
button.press()
      ↓
command.execute()
      ↓
television.turnOn()
```

---

#### Exemplo completo

Temos então:

##### Command

```
public interface Command {
    void execute();
}
```

##### Receiver

```
public class Television {

    public void turnOn() {
        System.out.println("Televisão ligada");
    }

    public void turnOff() {
        System.out.println("Televisão desligada");
    }
}
```

##### Concrete Command

```
public class TurnOnCommand implements Command {

    private Television television;

    public TurnOnCommand(Television television) {
        this.television = television;
    }

    @Override
    public void execute() {
        television.turnOn();
    }
}
```

##### Invoker

```
public class Button {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }
}
```

##### Client

```
public class Main {

    public static void main(String[] args) {

        // Receiver
        Television television = new Television();

        // Command
        Command command = new TurnOnCommand(television);

        // Invoker
        Button button = new Button();

        // Associar Command ao Invoker
        button.setCommand(command);

        // Executar
        button.press();
    }
}
```

Resultado:

```
Televisão ligada
```

---

# O papel de cada classe

Esta tabela é muito importante para perceber o padrão:

|Elemento|Exemplo|Responsabilidade|
|---|---|---|
|**Command**|`Command`|Define `execute()`|
|**Concrete Command**|`TurnOnCommand`|Representa uma ação específica|
|**Receiver**|`Television`|Sabe realizar a ação|
|**Invoker**|`Button`|Dispara o comando|
|**Client**|`Main`|Cria e configura os objetos|

A relação é:

```
                 CLIENT
                   │
          cria/configura tudo
                   │
                   ▼
               INVOKER
                Button
                   │
                   │ execute()
                   ▼
          CONCRETE COMMAND
          TurnOnCommand
                   │
                   │ turnOn()
                   ▼
               RECEIVER
              Television
```

---

# Mas por que precisamos do Command?

Imagina que tens:

```
Button
```

e queres que ele possa executar várias ações.

Sem Command:

```
Button → Television
Button → Light
Button → Door
Button → MusicPlayer
```

O `Button` teria de conhecer todas essas classes.

Com Command:

```
                 ┌── TurnOnCommand ──→ Television
                 │
Button ──Command─┼── TurnOnLightCommand ──→ Light
                 │
                 └── OpenDoorCommand ──→ Door
```

O `Button` continua exatamente igual.

Só mudamos o comando:

```
button.setCommand(turnOnCommand);
```

ou:

```
button.setCommand(turnOnLightCommand);
```

ou:

```
button.setCommand(openDoorCommand);
```

---

# A ideia fundamental para memorizar

Pensa no Command como uma **encomenda/pedido encapsulado num objeto**.

Sem Command:

```
"Faz isto!"
   ↓
executa diretamente
```

Com Command:

```
"Faz isto!"
   ↓
transformo o pedido num objeto
   ↓
Command
   ↓
posso guardar / passar / executar posteriormente
```

É por isso que o padrão Command é particularmente útil quando queremos coisas como:

- **undo/redo**
- filas de tarefas
- histórico de operações
- agendamento de operações
- logging de comandos
- operações que podem ser executadas posteriormente

---

## Uma forma simples de decorar

Quando vires **Command**, pensa nestas quatro perguntas:

**1. Quem pede?** → `Invoker`

**2. O que foi pedido?** → `Command`

**3. Quem realmente faz?** → `Receiver`

**4. Quem liga tudo?** → `Client`

Ou:

```
Invoker
   │
   │ execute()
   ▼
Command
   │
   │ chama
   ▼
Receiver
```

E o **Client** fica responsável por montar essa ligação.

---

## Prós

- _Princípio da Responsabilidade Única_ . É possível desacoplar as classes que invocam operações das classes que executam essas operações.
- _Princípio Aberto/Fechado_ . Você pode introduzir novos comandos no aplicativo sem quebrar o código do cliente existente.
- Você pode implementar as funções de desfazer/refazer.
- Você pode implementar a execução adiada de operações.
- Você pode combinar um conjunto de comandos simples em um comando complexo.

## Contras

- O código pode ficar mais complicado, já que você está introduzindo uma camada totalmente nova entre remetentes e destinatários.