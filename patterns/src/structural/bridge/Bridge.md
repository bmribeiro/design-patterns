Permite dividir uma classe grande ou um conjunto de classes intimamente relacionadas em duas hierarquias separadas - abstração e implementação - que podem ser desenvolvidas independentemente uma da outra.

O **Bridge (Ponte)** serve para separar **duas dimensões que podem variar independentemente**.

A ideia central é:

> **Separar a abstração da implementação, para que ambas possam evoluir independentemente.**


**Exemplos de uso:** O padrão Bridge é especialmente útil ao lidar com aplicativos multiplataforma, que oferecem suporte a vários tipos de servidores de banco de dados ou que trabalham com diversos provedores de API de um determinado tipo (por exemplo, plataformas em nuvem, redes sociais etc.).

**Identificação:** A Bridge pode ser reconhecida por uma distinção clara entre uma entidade controladora e várias plataformas diferentes das quais depende.

## Problema

_Abstração?_ _Implementação?

Classe geométrica `Shape` com as subclasses `Circle` e `Square`. Deseja-se estender essa hierarquia para incorporar cores, subclasses `Red` and `Blue`. Com as 2 subclasses anteriores, são 4 combinações.

![[Bridge 1.png]]

Adicionar novas formas e cores fará crescer exponencialmente. Para adicionar um `Triangle` seria necessária 2 subclasses, uma para cada cor.

## Solução

O Problema acontece porque estamos a estender as classes de forma em duas dimensões independentes: `Shape` e `Colour`. Problema comum com herança.

`Bridge` tenta resolver o problema substituindo a `herança` por `composição`.

Significa que extrai uma das dimensões para uma hieraquia de classes separada, de forma que as classes originais referenciem um objeto da nova hierarquia, em vez de terem todo o seu estado e comportamento dentro de uma única classe.

![[Bridge 2.png]]

Podemos extrair o código relacionado à `Color` para a sua própria classe com duas subclasses `Red` e `Blue`. A classe `Shape` recebe um campo de referência para um dos objetos cor.
`Shape` pode delegar comportamento relacionado à cor para `Color`, atuando como uma `Bridge` entre `Shape` e `Color`. Adicionar novas cores não exige alterar a hieraquia da forma.

#### Abstração e Implementação

A `Abstração` (também chamada de `interface`) é uma camada de controle de alto nível para alguma entidade. Essa camada não deve realizar nenhum trabalho real por si. Ela deve delegar o trabalho para a camada de implementação (também chamada de `plataforma`).

Ao falar de aplicações reais, a abstração pode ser representada por uma interface gráfica do usuário (GUI), e a implementação pode ser o código subjacente do sistema operacional (API) que a camada GUI chama em resposta às interações do usuário.

De modo geral, você pode expandir esse aplicativo em duas direções independentes:

- Ter várias interfaces gráficas de usuário diferentes (por exemplo, personalizadas para clientes comuns ou administradores).
- Suporte a diversas APIs diferentes (por exemplo, para poder executar o aplicativo no Windows, Linux e macOS).

![[Bridge 3.png]]

Ele sugere que dividamos as classes em duas hierarquias:

- Abstração: a camada de interface gráfica do usuário (GUI) do aplicativo.
- Implementação: as APIs dos sistemas operacionais.

![[Bridge 4.png]]


O objeto de abstração controla a aparência do aplicativo, delegando o trabalho real ao objeto de implementação vinculado. Implementações diferentes são intercambiáveis, desde que sigam uma interface comum, permitindo que a mesma interface gráfica funcione tanto no Windows quanto no Linux.

## Estrutura

![[Bridge 5.png]]


1. `Abstração` fornece a lógica de controle de alto nível. Depende do objeto de implementação para realizar o trabalho de baixo nível.
2. `Implementação` declara a interface comum a todas as implementações concretas. Uma abstração só se pode comunicar com um objeto de implementação por meio de métodos aqui declarados.

   A `Abstração` pode listar os mesmos métodos que a `Implementação`, mas geralmente a abstração declara alguns comportamentos complexos que dependem de uma ampla variedade de operações primitivas declaradas pela implementação.

3. `Implementações concretas` contém o código específico da plataforma

4. `Abstrações refinadas` fornecem variantes da lógica de controle. Assim como as suas classes originais, elas funcionam com diferentes implementações por meio da interface de implementação geral.

5. `Cliente` está interessado em trabalhar com a abstração. No entanto, é responsabilidade do cliente vincular o objeto de abstração a um dos objetos de implementação.


## Pseudocódigo

**Bridge** pode ajudar a dividir o código monolítico de um aplicativo que gerencia dispositivos e seus controles remotos. As `Device`classes atuam como a implementação, enquanto os `Remote`controladores atuam como a abstração.

![[Bridge 6.png]]

A classe base do controle remoto declara um campo de referência que a vincula a um objeto de dispositivo. Todos os controles remotos funcionam com os dispositivos por meio da interface geral de dispositivo, o que permite que o mesmo controle remoto suporte vários tipos de dispositivos.

Podem ser desenvolvidas as classes de cntrolo remoto independentemente das classes de dispositivo. Tudo o que é necessário é criar uma nova subclasse de controlo remoto.

Ex. Controlo remoto básico pode ter dois botões, mas pode estendê-lo com recursos adicionais.
O Código do cliente associa o tipo de controle remoto desejado a um objeto de dispositivo específico por meio do construtor do controle remoto.


## Aplicabilidade

problema: `Bridge`quando é desejado dividir e organizar uma classe monolítica que possui diversas variantes de funcionalidade

solução: quando maior uma classe, mais dificil entender o seu funcionamento e mais tempo leva para implementar uma alteração. As mudanças feitas numa das variações pode exigir alterações em toda a classe.

O `Bridge` permite dividir uma classe monolítica em várias hierarquias de classes. Depois, pode alterar as classes em cada hieraquia independente das outras classes. Simplifica a manutenção do código e minimiza o risco de partir o código existente.

----

problema: quando é preciso estender uma classe em várias dimensões ortogonais (independentes)

soluções: a proposta `bridge` é extrair uma hierarquia de classes separada para cada uma das dimensões. A classe original delega o trabalho relacionado aos objetos pertencentes a essas hierarquias, em vez de realizar tudo sozinha

----

problema: `bridge` para alterar entre implementações em tempo de execução

solução: `bridge` permite substituir o objeto de implementação dentro da abstração. É tão simples quanto atribuir um novo valor a um campo.

NOTA: motivo pelo qual o `Bridge` é confundido com o `Strategy`.
Um padrão é mais do que apenas uma maneira específica de estruturar classes. Também pode comunicar uma intenção e um problema que está sendo abordado.

## Como implementar


O **Bridge (Ponte)** serve para separar **duas dimensões que podem variar independentemente**.

A ideia central é:

> **Separar a abstração da implementação, para que ambas possam evoluir independentemente.**

Um exemplo clássico é:

- **Abstração:** `RemoteControl`
- **Implementação:** `Device`
- **Variantes da abstração:** `AdvancedRemoteControl`
- **Variantes da implementação:** `TV`, `Radio`

```
RemoteControl ──────── Device
      │                    │
      │                    ├── TV
      │                    └── Radio
      │
      └── AdvancedRemoteControl
```

Vamos agora seguir **exatamente os 7 passos**.

## 1. Identifique as dimensões ortogonais

Primeiro tens de procurar **dois conceitos que variam independentemente**.

Por exemplo, imagina um sistema de controlo remoto.

Temos uma dimensão:

**Tipo de controlo remoto**

- controlo básico
- controlo avançado

E outra:

**Tipo de dispositivo**

- TV
- rádio
- projetor

Estas duas dimensões podem evoluir separadamente.

Sem Bridge, poderias acabar com:

```
BasicTVRemote
BasicRadioRemote
BasicProjectorRemote

AdvancedTVRemote
AdvancedRadioRemote
AdvancedProjectorRemote
```

O problema é a combinação das duas dimensões.

Se adicionares:

```
+ 3 tipos de controlo
+ 5 dispositivos
```

podes acabar com **15 classes**.

O Bridge tenta evitar essa explosão combinatória.

Em vez disso:

```
Controlo remoto
    ├── Básico
    └── Avançado

Dispositivo
    ├── TV
    ├── Rádio
    └── Projetor
```

As duas hierarquias ficam separadas.


# 2. Veja quais operações o cliente precisa

Agora defines aquilo que o **cliente quer fazer**.

Por exemplo, o cliente quer:

```
remote.power();
remote.volumeUp();
remote.volumeDown();
```

Então crias a abstração:

```
abstract class RemoteControl {

    protected Device device;

    public RemoteControl(Device device) {
        this.device = device;
    }

    public void power() {
        device.power();
    }

    public void volumeUp() {
        device.volumeUp();
    }

    public void volumeDown() {
        device.volumeDown();
    }
}
```

Aqui aparece uma coisa muito importante:

```
protected Device device;
```

A abstração **tem uma referência para a implementação**.

É essa referência que cria a ponte.

# 3. Determine as operações disponíveis em todas as plataformas

Agora tens de olhar para a outra dimensão.

O que é que **todos os dispositivos** conseguem fazer?

Por exemplo:

```
interface Device {

    void power();

    void volumeUp();

    void volumeDown();
}
```

Esta é a **interface de implementação**.

Temos agora:

```
RemoteControl
       │
       │ usa
       ▼
     Device
```

O `RemoteControl` não precisa de saber se está a controlar uma TV ou um rádio.

Só sabe:

```
device.power();
device.volumeUp();
device.volumeDown();
```

# 4. Crie as implementações concretas

Agora criamos as diferentes implementações.

Por exemplo:

```
class TV implements Device {

    @Override
    public void power() {
        System.out.println("TV ligada/desligada");
    }

    @Override
    public void volumeUp() {
        System.out.println("Aumentar volume da TV");
    }

    @Override
    public void volumeDown() {
        System.out.println("Diminuir volume da TV");
    }
}
```

E:

```
class Radio implements Device {

    @Override
    public void power() {
        System.out.println("Rádio ligado/desligado");
    }

    @Override
    public void volumeUp() {
        System.out.println("Aumentar volume do rádio");
    }

    @Override
    public void volumeDown() {
        System.out.println("Diminuir volume do rádio");
    }
}
```

Agora tens:

```
Device
  │
  ├── TV
  │
  └── Radio
```

E ambas respeitam:

```
Device
```

# 5. Adicione a referência para a implementação

Este é o ponto **mais importante do Bridge**.

Na abstração:

```
abstract class RemoteControl {

    protected Device device;

    public RemoteControl(Device device) {
        this.device = device;
    }

    public void power() {
        device.power();
    }

    public void volumeUp() {
        device.volumeUp();
    }

    public void volumeDown() {
        device.volumeDown();
    }
}
```

Repara na relação:

```
RemoteControl
      │
      │ referência
      ▼
    Device
```

Não temos:

```
class TVRemote extends TV
```

nem:

```
class RadioRemote extends Radio
```

Temos **composição**:

```
RemoteControl HAS-A Device
```

Por isso podemos trocar a implementação.

Por exemplo:

```
Device tv = new TV();

RemoteControl remote = new RemoteControl(tv);
```

Agora o comando:

```
remote.power();
```

é delegado para:

```
tv.power();
```

# 6. Crie abstrações refinadas

Agora imagina que queremos um comando remoto mais avançado.

Por exemplo:

```
class AdvancedRemoteControl extends RemoteControl {

    public AdvancedRemoteControl(Device device) {
        super(device);
    }

    public void mute() {
        System.out.println("Som desligado");
    }
}
```

Temos então duas dimensões:

```
ABSTRAÇÃO
RemoteControl
     │
     └── AdvancedRemoteControl


IMPLEMENTAÇÃO
Device
  ├── TV
  └── Radio
```

E podemos combiná-las livremente.

Por exemplo:

```
RemoteControl remote1 =
    new RemoteControl(new TV());

RemoteControl remote2 =
    new RemoteControl(new Radio());

AdvancedRemoteControl remote3 =
    new AdvancedRemoteControl(new TV());

AdvancedRemoteControl remote4 =
    new AdvancedRemoteControl(new Radio());
```

Sem criar:

```
BasicTVRemote
BasicRadioRemote
AdvancedTVRemote
AdvancedRadioRemote
```

Este é precisamente o benefício do Bridge.

# 7. O cliente associa as duas dimensões

Finalmente, o cliente escolhe:

1. qual é a **abstração**
2. qual é a **implementação**

Por exemplo:

```
Device device = new TV();

RemoteControl remote =
    new AdvancedRemoteControl(device);
```

Temos:

```
AdvancedRemoteControl
        │
        │
        ▼
       TV
```

Depois:

```
remote.power();
remote.volumeUp();
```

O cliente não precisa de saber como a TV implementa essas operações.

---

# O código completo

Uma versão simplificada ficaria assim:

```
interface Device {

    void power();

    void volumeUp();

    void volumeDown();
}
```

```
class TV implements Device {

    @Override
    public void power() {
        System.out.println("TV ligada/desligada");
    }

    @Override
    public void volumeUp() {
        System.out.println("Volume da TV +");
    }

    @Override
    public void volumeDown() {
        System.out.println("Volume da TV -");
    }
}
```

```
class Radio implements Device {

    @Override
    public void power() {
        System.out.println("Rádio ligado/desligado");
    }

    @Override
    public void volumeUp() {
        System.out.println("Volume do rádio +");
    }

    @Override
    public void volumeDown() {
        System.out.println("Volume do rádio -");
    }
}
```

```
abstract class RemoteControl {

    protected Device device;

    public RemoteControl(Device device) {
        this.device = device;
    }

    public void power() {
        device.power();
    }

    public void volumeUp() {
        device.volumeUp();
    }

    public void volumeDown() {
        device.volumeDown();
    }
}
```

```
class AdvancedRemoteControl extends RemoteControl {

    public AdvancedRemoteControl(Device device) {
        super(device);
    }

    public void mute() {
        System.out.println("Som desligado");
    }
}
```

E o cliente:

```
Device tv = new TV();

RemoteControl remote =
    new AdvancedRemoteControl(tv);

remote.power();
remote.volumeUp();
```

---

# O que deves memorizar

O Bridge pode ser entendido através desta estrutura:

```
        ABSTRAÇÃO
            │
            │ referência
            ▼
     IMPLEMENTAÇÃO
```

Ou, mais concretamente:

```
RemoteControl ────────── Device
      │                      │
      └── Advanced           ├── TV
                            └── Radio
```

### A ideia fundamental

**Sem Bridge:**

```
Abstração × Implementação
```

pode gerar muitas classes:

```
Remote + TV
Remote + Radio
AdvancedRemote + TV
AdvancedRemote + Radio
...
```

**Com Bridge:**

```
Hierarquia da abstração

        Remote
          │
       Advanced


Hierarquia da implementação

        Device
        /    \
       TV   Radio
```

As duas hierarquias podem crescer **independentemente**.

## Bridge vs Adapter

Uma distinção importante, especialmente porque estás a estudar os padrões em sequência:

**Adapter** normalmente responde a:

> "Tenho uma classe existente com uma interface incompatível. Como faço para o meu código trabalhar com ela?"

**Bridge** responde a:

> "Tenho duas dimensões que precisam de variar independentemente. Como evito criar uma classe para cada combinação?"

Portanto:

**Adapter = compatibilidade entre interfaces**

**Bridge = separação entre abstração e implementação**

O detalhe mais importante para reconhecer um **Bridge** num código Java é procurares uma **abstração que mantém uma referência para uma implementação através de uma interface**, permitindo trocar essa implementação sem alterar a abstração.