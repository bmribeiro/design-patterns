Permite salvar e restaurar o estado anterior de um objeto sem revelar os detalhes da sua implementação.

**Exemplos de uso:** O princípio do Memento pode ser alcançado usando serialização, que é bastante comum em Java. Embora não seja a única nem a mais eficiente maneira de criar snapshots do estado de um objeto, ainda permite armazenar backups do estado, protegendo a estrutura original de outros objetos.

### Exemplos nas Java Core Libraries

Aqui estão alguns exemplos desse padrão em bibliotecas principais do Java:

- Todas [`java.io.Serializable`](http://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html)as implementações podem simular o Memento.
- Todas [`javax.faces.component.StateHolder`](http://docs.oracle.com/javaee/7/api/javax/faces/component/StateHolder.html)as implementações.

## Editor de formas e desfazer/refazer complexos.

Este editor gráfico permite alterar a cor e a posição das formas na tela. Qualquer modificação pode ser desfeita e repetida.

A função "desfazer" baseia-se na colaboração entre os padrões Memento e Command. O editor rastreia um histórico dos comandos executados. Antes de executar qualquer comando, ele cria um backup e o conecta ao objeto do comando. Após a execução, ele adiciona o comando executado ao histórico.

Quando um usuário solicita o comando de desfazer, o editor busca um comando recente no histórico e restaura o estado a partir do backup armazenado nesse comando. Se o usuário solicitar outro comando de desfazer, o editor busca o comando seguinte no histórico e assim por diante.

Os comandos revertidos são mantidos no histórico até que o usuário faça alguma modificação nas formas na tela. Isso é crucial para refazer comandos desfeitos.


## Problema

Aplicação de edição de texto. Formatar, inserir imagens etc

Desfazer quaisquer operações realizados no texto.
Antes de realizar qualquer operação a aplicação regista todos os objetos e salva. `Undo` restaura o último e restaura.

![[Memento 1.png]]

Para permitir que outros objetos leiam e escrevam dados num snapshot, precisará tornar os campos públicos. Isso iria expor todos os estados do editor, privados e públicos. Outras classes se tornariam dependentes de cada pequena alteração na classe snapshot.

Ou expõe todos os detalhes internos das classes tornando-os muito frágeis, ou restringe o acess ao seu estado, impossibilitando a criação de snapshots.

## Solução

Alguns objetos tentam fazer mais do que deveriam. Para guardar dados necessários para alguma ação, invadem o espaço privado de outros objetos em vez de permitir que esses objetos executem a ação propriamente dita.

`Memento` delega a criação dos snapshots de estado ao verdadeiro proprietário desse estado, o objeto originador. Em vez de outros objetos tentarem copiar o estado do editor de `de fora`, a própria classe do editor pode criar o snapshot, já que tem acesso total ao seu próprio estado.

O padrão sugere armazenar a cópia do estado do objeto num objeto especial chamado `memento`. O conteúdo não é acessível a nenhum outro objeto, excepto aquele que o produziu. Outros objetos devem se comunicar com os `mementos` usando uma interface limitada que pode permitir a obtenção dos metadados do snapshot, mas não o estado original do objeto contido no snapshot.


![[Memento 2.png]]

Essa política restritiva permite armazenar snapshots detro de outros objetos `guardiões`. Como o `guardião` interage com a lembrança apenas por meio da interface limitada, ele não consegue alterar o estado armazenado dentro do snapshot. Ao mesmo tempo, o `criador` tem acesso a todos os campos dentro do `snapshot`, permitindo que ele restaure seu estado anterior quando quiser.

Em nosso exemplo de editor de texto, podemos criar uma classe de histórico separada para atuar como um gerenciador. Uma pilha de registros armazenada dentro desse gerenciador crescerá cada vez que o editor estiver prestes a executar uma operação. Você pode até mesmo renderizar essa pilha na interface do usuário do aplicativo, exibindo o histórico de operações realizadas anteriormente para o usuário.

Quando um usuário aciona o desfazer, o histórico recupera o lembrete mais recente da pilha e o envia de volta ao editor, solicitando um rollback. Como o editor tem acesso total ao lembrete, ele altera seu próprio estado com os valores obtidos do lembrete.


## Estrutura

#### Implementação baseada em classes aninhadas


![[Memento 3.png]]

1. `Originator` pode gerar `Snapshot` a partir  do seu próprio estado, bem como restaurar seu estado a partir desse `Snapshot`.

2. `Memento` é um objeto de valor que funciona como um `Snapshot`do estado do `Originator`. É prática tornar o `Memento` imutável e passar os dados para ele apenas uma vez, através de construtor.

3. `Caretaker` sabe não apenas "quando" e "por que" capturar o estado original, mas também quando o estado deve ser restaurado.

4. A classe `Memento` está aninhada dentro da classe `Originator`. Isso permite que a classe `Originator` acesse os campos e métodos do `Memento`, mesmo que sejam privados. A classe `Caretaker` tem acess muito limitado aos campos e métodos do `Memento`, o que lhe permite armazenar `Mementos` numa pilha, mas não alterar o seu estado.


#### Implementação baseada em uma interface intermediária (PHP)

#### Implementação com encapsulamento ainda mais rigoroso.

Existe outra implementação que é útil quando você não quer deixar a menor possibilidade de outras classes acessarem o estado do originador através do memento.

![[Memento 4.png]]

1. Esta implementação permite múltiplos tipos de `Originator` e `Memento`. Cada `Originator` funciona com uma classe de memento correspondente. Nem os originadores nem os mementos expôem o seu estado a ninguém.

2. Os responsáveis pela manutenção estão explicitamente impedidos de alterar o estado armazenado nos `Mementos`. A classe responsável pela manutenção torna-se independente da classe originadora, pois o método de restauração agora está definido na classe `Memento`.

3. Cada snapshot fica vinculada ao `Originador` que a produziu. O `originador` passa a si mesmo para o construtor da snapshot, juntamente com os valores de seu estado. Graças á estreita relação entre essas classes, uma lembrança pode restaurar o estado de seu `Originator` desde que este tenha definido os métodos setters apropriados.

## Pseudocódigo

Este exemplo utiliza o padrão Memento juntamente com o padrão [Command](https://refactoring.guru/design-patterns/command) para armazenar instantâneos do estado do editor de texto complexo e restaurar um estado anterior a partir desses instantâneos quando necessário.

![[Memento 5.png]]

Os objetos de comando atuam como guardiões. Eles recuperam o memento do editor antes de executar operações relacionadas a comandos. Quando um usuário tenta desfazer o comando mais recente, o editor pode usar o memento armazenado nesse comando para reverter ao estado anterior.

A classe `memento` não declara nenhum campo público, getter ou setter. Portanto, nenhum objeto pode alterar seu conteúdo. Os mementos são vinculados ao objeto editor que os criou. Isso permite que um memento restaure o estado do editor vinculado, passando os dados por meio de setters no objeto editor. Como os mementos são vinculados a objetos editor específicos, você pode fazer com que seu aplicativo suporte várias janelas de editor independentes com uma pilha de desfazer centralizada.


## Aplicabilidade

problema: `Memento` quando for para criar snapshots do estado do objeto para poder restaurar um estado anterior do objeto.

solução: permite criar cópias completas do estado de um objeto, incluindo campos privados e armazená-las separadamente do objeto. Caso de uso `undo`, também é indispensável ao lidar com transações ( ex. reverter uma operação em caso de erro ).

---

problema: `Memento`, quando o acesso direto aos campos getters / setters do objeto violar o seu encapsulamento.

solução: `Memento`torna o próprio objeto responsável por criar um snapshot do seu estado. Nenhum outro objeto pode ler esse snapshot, garantindo a segurança e a proteção dos dados de estado do objeto original.


## Como implementar
#### Memento em Java — passo a passo

## 1. Primeiro: qual problema o Memento resolve?

Imagine um editor de texto:

```
Editor editor = new Editor();

editor.setTexto("Olá");
editor.setCursor(5);

editor.setTexto("Olá mundo");
editor.setCursor(10);
```

Agora queremos implementar:

```
Ctrl + Z
```

Ou seja, voltar o `Editor` para um estado anterior.

O problema é:

> **Como guardar o estado interno do `Editor` sem permitir que outras classes mexam diretamente nele?**

O Memento resolve exatamente isso.

A ideia é:

```
Editor
  │
  │ cria
  ▼
Memento
  │
  │ guarda
  ▼
estado anterior
```

E depois:

```
Editor
  ▲
  │ restaura
  │
Memento
```

Existe ainda um terceiro participante:

```
Originator → Memento ← Caretaker
```

- **Originator** → objeto cujo estado queremos guardar.
- **Memento** → fotografia do estado.
- **Caretaker** → guarda os Mementos e decide quando restaurá-los.


# 2. Etapa 1 — Determine o Originator

O primeiro passo do texto diz:

> "Determine qual classe desempenhará o papel de originadora."

No nosso exemplo, é:

```
public class Editor {

    private String texto;
    private int cursor;
}
```

O `Editor` é o **Originator** porque é ele que possui o estado que queremos salvar.

Por exemplo:

```
Editor
 ├── texto = "Olá mundo"
 └── cursor = 10
```

Esse é o estado que queremos conseguir recuperar posteriormente.

---

# 3. Etapa 2 — Crie o Memento

Agora precisamos de um objeto capaz de guardar esse estado.

```
public class EditorMemento {

    private final String texto;
    private final int cursor;

    public EditorMemento(String texto, int cursor) {
        this.texto = texto;
        this.cursor = cursor;
    }
}
```

Perceba algo importante.

O `Memento` possui uma cópia dos dados:

```
Editor
 ├── texto
 └── cursor

        ↓ salvar

Memento
 ├── texto
 └── cursor
```

Por exemplo:

```
Editor
texto  = "Olá mundo"
cursor = 10

        ↓

Memento
texto  = "Olá mundo"
cursor = 10
```

O Memento é como uma **fotografia do estado naquele momento**.

# 4. Por que o Memento precisa guardar o estado?

Imagine que fazemos:

```
editor.setTexto("Olá");
editor.setCursor(3);
```

Depois:

```
EditorMemento memento = editor.salvar();
```

O Memento fica:

```
Memento
texto  = "Olá"
cursor = 3
```

Depois o editor muda:

```
editor.setTexto("Olá mundo");
editor.setCursor(10);
```

Agora temos:

```
Editor
texto  = "Olá mundo"
cursor = 10

Memento
texto  = "Olá"
cursor = 3
```

O Memento continua representando o **estado antigo**.

É justamente isso que queremos.


# 5. Etapa 3 — Torne o Memento imutável

O texto diz:

> "Torne a classe memento imutável."

Isso significa que depois de criado:

```
EditorMemento memento =
    new EditorMemento("Olá", 3);
```

não podemos fazer:

```
memento.setTexto("outra coisa");
```

Nem:

```
memento.setCursor(50);
```

Por isso usamos:

```
private final String texto;
private final int cursor;
```

e inicializamos tudo no construtor:

```
public EditorMemento(String texto, int cursor) {
    this.texto = texto;
    this.cursor = cursor;
}
```

Não existem setters.

A ideia é:

```
criar Memento
      ↓
┌─────────────────┐
│ texto = "Olá"   │
│ cursor = 3      │
└─────────────────┘
      ↓
   NÃO MUDA
```

Isso é importante porque o Memento representa uma **fotografia**.

Uma fotografia antiga não deveria mudar quando o objeto original muda.

# 6. Etapa 4 — Quem pode acessar o Memento?

Aqui o texto entra numa questão mais avançada.

O problema é:

> Se qualquer classe conseguir acessar os campos do Memento, ela poderia descobrir ou modificar o estado do Originator.

Queremos algo parecido com:

```
Caretaker
   │
   │ pode guardar
   ▼
Memento

mas...

Caretaker
   ✖ não pode acessar
      texto
      cursor
```

Quem deve ter acesso completo ao Memento é o próprio Originator.

Em Java, podemos resolver isso usando uma **classe aninhada**.

Por exemplo:

```
public class Editor {

    private String texto;
    private int cursor;

    public static class Memento {

        private final String texto;
        private final int cursor;

        private Memento(String texto, int cursor) {
            this.texto = texto;
            this.cursor = cursor;
        }
    }
}
```

Agora:

```
Editor
 ├── texto
 ├── cursor
 │
 └── Memento
      ├── texto
      └── cursor
```

O Memento pertence conceitualmente ao `Editor`.


# 7. Etapa 5 — Crie o método para salvar

Agora o `Originator` precisa conseguir criar um Memento.

Criamos:

```
public Memento salvar() {
    return new Memento(texto, cursor);
}
```

A classe completa começa a ficar assim:

```
public class Editor {

    private String texto;
    private int cursor;

    public Memento salvar() {
        return new Memento(texto, cursor);
    }

    public static class Memento {

        private final String texto;
        private final int cursor;

        private Memento(String texto, int cursor) {
            this.texto = texto;
            this.cursor = cursor;
        }
    }
}
```

Quando fazemos:

```
Editor editor = new Editor();

editor.setTexto("Olá");
editor.setCursor(3);

Editor.Memento memento = editor.salvar();
```

acontece:

```
Editor
texto  = "Olá"
cursor = 3
       │
       │ salvar()
       ▼
Memento
texto  = "Olá"
cursor = 3
```


# 8. Etapa 6 — Crie o método para restaurar

Agora precisamos do caminho inverso.

O `Editor` precisa conseguir receber um Memento e recuperar o estado.

Por exemplo:

```
public void restaurar(Memento memento) {
    this.texto = memento.texto;
    this.cursor = memento.cursor;
}
```

Agora temos os dois métodos principais:

```
public Memento salvar() {
    return new Memento(texto, cursor);
}

public void restaurar(Memento memento) {
    this.texto = memento.texto;
    this.cursor = memento.cursor;
}
```

A relação fica:

```
          salvar()
Editor ──────────────► Memento
  ▲                     │
  │                     │
  └──── restaurar() ────┘
```

# 9. Vamos colocar getters e setters no Editor

Para enxergar o funcionamento:

```
public class Editor {

    private String texto;
    private int cursor;

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void mostrarEstado() {
        System.out.println(
            "Texto: " + texto +
            ", Cursor: " + cursor
        );
    }

    public Memento salvar() {
        return new Memento(texto, cursor);
    }

    public void restaurar(Memento memento) {
        this.texto = memento.texto;
        this.cursor = memento.cursor;
    }

    public static class Memento {

        private final String texto;
        private final int cursor;

        private Memento(String texto, int cursor) {
            this.texto = texto;
            this.cursor = cursor;
        }
    }
}
```

Agora podemos fazer:

```
Editor editor = new Editor();

editor.setTexto("Olá");
editor.setCursor(3);

Editor.Memento estadoAnterior = editor.salvar();

editor.setTexto("Olá mundo");
editor.setCursor(10);

editor.mostrarEstado();
```

Resultado:

```
Texto: Olá mundo, Cursor: 10
```

Agora:

```
editor.restaurar(estadoAnterior);
```

E:

```
editor.mostrarEstado();
```

Resultado:

```
Texto: Olá, Cursor: 3
```

**É isso que o Memento faz.**

---

# 10. Etapa 7 — Entra o Caretaker

Até agora temos:

```
Editor
  │
  └── Memento
```

Mas quem guarda os Mementos?

Essa é a função do **Caretaker**.

Imagine que queremos vários níveis de `Ctrl + Z`.

```
Estado 1
   ↓
Estado 2
   ↓
Estado 3
   ↓
Estado 4
```

Precisamos guardar:

```
Memento 1
Memento 2
Memento 3
```

Podemos ter:

```
import java.util.Stack;

public class Historico {

    private final Stack<Editor.Memento> estados =
        new Stack<>();

    public void salvar(Editor editor) {
        estados.push(editor.salvar());
    }

    public void desfazer(Editor editor) {
        if (!estados.isEmpty()) {
            Editor.Memento estado = estados.pop();
            editor.restaurar(estado);
        }
    }
}
```

Agora:

```
             cria
Editor ─────────────► Memento
                         │
                         │
                         ▼
                    Historico
```

O `Historico` não precisa saber como o estado do Editor funciona.

Ele simplesmente diz:

```
editor.salvar();
```

e guarda o resultado.

---

# 11. Exemplo completo

Podemos usar assim:

```
Editor editor = new Editor();
Historico historico = new Historico();

editor.setTexto("Olá");
editor.setCursor(3);

historico.salvar(editor);

editor.setTexto("Olá mundo");
editor.setCursor(10);

historico.salvar(editor);

editor.setTexto("Olá mundo!");
editor.setCursor(11);

historico.desfazer(editor);

editor.mostrarEstado();
```

Depois do `desfazer()`:

```
Texto: Olá mundo
Cursor: 10
```

Porque o histórico tinha guardado:

```
Memento 1
    texto = "Olá"
    cursor = 3

Memento 2
    texto = "Olá mundo"
    cursor = 10
```

Quando chamamos:

```
historico.desfazer(editor);
```

ele pega:

```
Memento 2
```

e manda para:

```
editor.restaurar(memento);
```

---

# 12. Visualizando o fluxo inteiro

Esse é o ponto mais importante para entender o padrão:

```
                    ┌───────────────┐
                    │    Editor     │
                    │  Originator   │
                    └───────┬───────┘
                            │
                       salvar()
                            │
                            ▼
                    ┌───────────────┐
                    │    Memento    │
                    │               │
                    │ texto         │
                    │ cursor        │
                    └───────┬───────┘
                            │
                         guarda
                            │
                            ▼
                    ┌───────────────┐
                    │   Histórico   │
                    │  Caretaker    │
                    └───────┬───────┘
                            │
                         desfazer
                            │
                            ▼
                    ┌───────────────┐
                    │    Memento    │
                    └───────┬───────┘
                            │
                       restaurar()
                            │
                            ▼
                    ┌───────────────┐
                    │    Editor     │
                    │ estado antigo │
                    └───────────────┘
```

---

# 13. Agora vamos entender cada participante

## Originator

É o objeto cujo estado queremos preservar.

```
Editor
```

Ele sabe:

- qual é seu estado;
- como criar um Memento;
- como restaurar seu estado a partir de um Memento.

---

## Memento

É a **fotografia do estado**.

```
Memento
```

Ele guarda:

```
texto
cursor
```

Mas não deve permitir alterações externas.

---

## Caretaker

É quem administra os Mementos.

```
Historico
```

Ele pode:

```
guardar Memento
remover Memento
escolher Memento
```

Mas não precisa conhecer os detalhes internos do estado.

---

# 14. E por que não simplesmente copiar os campos?

Você poderia pensar:

> "Por que o `Historico` não simplesmente pega `editor.getTexto()` e `editor.getCursor()`?"

Porque isso faria o `Historico` conhecer a estrutura interna do `Editor`.

Hoje:

```
Editor {
    texto;
    cursor;
}
```

Amanhã:

```
Editor {
    texto;
    cursor;
    selecao;
    fonte;
    zoom;
    scroll;
}
```

O `Historico` teria que conhecer todas essas propriedades.

Com Memento:

```
Historico
    │
    │ não conhece detalhes
    ▼
Memento
```

Quem sabe exatamente como salvar/restaurar o estado é o próprio `Editor`.

Isso mantém o **encapsulamento**.

---

# 15. Explicando a etapa 8

A última etapa do texto é mais avançada:

> "A ligação entre os responsáveis e os criadores pode ser movida para a classe memento."

Essa parte você **não precisa dominar agora** para entender o Memento.

A ideia é que o próprio Memento poderia saber qual Originator o criou.

Em vez de:

```
editor.restaurar(memento);
```

poderíamos ter algo conceitualmente parecido com:

```
memento.restaurar();
```

O Memento saberia:

```
"Eu pertenço a este Editor."
```

e poderia restaurá-lo.

Mas isso aumenta o acoplamento entre Memento e Originator.

Por isso, a implementação mais simples e comum para aprender é:

```
editor.salvar()
editor.restaurar(memento)
```

---

# 16. Resumindo as 8 etapas do seu texto

|Etapa|O que significa|
|---|---|
|**1**|Escolha quem terá o estado que precisa ser salvo → `Editor`|
|**2**|Crie o objeto que guardará esse estado → `Memento`|
|**3**|Faça o Memento imutável → `final` + sem setters|
|**4**|Proteja o Memento → classe aninhada/interface|
|**5**|Crie `salvar()` no Originator|
|**6**|Crie `restaurar()` no Originator|
|**7**|Crie o Caretaker para guardar os Mementos → `Historico`|
|**8**|Opcionalmente, faça o Memento conhecer o Originator|

A essência é simplesmente:

```
        SALVAR

Originator ───────► Memento
    │                  │
    │                  │
    │               fotografia
    │                  │
    │                  ▼
    │              Caretaker
    │
    │
    ◄────── RESTAURAR ──────┘
```

### A frase para memorizar

> **Memento permite capturar e restaurar o estado anterior de um objeto sem expor sua implementação interna.**

E a divisão de responsabilidades é:

```
Originator → sabe COMO salvar/restaurar
Memento    → GUARDA o estado
Caretaker  → GUARDA os Mementos
```

Isso é o núcleo do padrão.


---

## Prós

- É possível gerar instantâneos do estado do objeto sem violar seu encapsulamento.
- Você pode simplificar o código do originador permitindo que o zelador mantenha o histórico do estado do originador.

## Contras

- O aplicativo pode consumir muita memória RAM se os clientes criarem lembranças com muita frequência.
- Os responsáveis ​​devem acompanhar o ciclo de vida do item original para poderem destruir lembranças obsoletas.
- A maioria das linguagens de programação dinâmicas, como PHP, Python e JavaScript, não pode garantir que o estado dentro do memento permaneça inalterado.