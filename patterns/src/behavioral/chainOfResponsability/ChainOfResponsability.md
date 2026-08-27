Permite encaminhar requisições ao longo de uma cadeia de manipuladores.
Ao receber uma requisição, cada manipulador decide se a processa ou se a passa para o próximo manipulador.


Um dos casos de uso mais populares para esse padrão é a propagação de eventos para os componentes pai em classes de interface gráfica. Outro caso de uso notável são os filtros de acesso sequencial.

### Exemplos nas Java Core Libraries.

Aqui estão alguns exemplos desse padrão em bibliotecas principais do Java:

- [`javax.servlet.Filter#doFilter()`](http://docs.oracle.com/javaee/7/api/javax/servlet/Filter.html#doFilter-javax.servlet.ServletRequest-javax.servlet.ServletResponse-javax.servlet.FilterChain-)
- [`java.util.logging.Logger#log()`](http://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html#log-java.util.logging.Level-java.lang.String-)

**Identificação:** O padrão é reconhecível pelos métodos comportamentais de um grupo de objetos que, indiretamente, chamam os mesmos métodos em outros objetos, enquanto todos os objetos seguem a interface comum.

## Filtrar acesso

Este exemplo mostra como uma solicitação contendo dados do usuário passa por uma cadeia sequencial de manipuladores que executam várias tarefas, como autenticação, autorização e validação.

Este exemplo difere um pouco da versão canônica do padrão apresentada por diversos autores. A maioria dos exemplos do padrão baseia-se na ideia de encontrar o manipulador correto, iniciá-lo e sair da cadeia em seguida. Mas aqui, executamos todos os manipuladores até que haja um que **não possa lidar** com a requisição. Observe que este ainda é o padrão Chain of Responsibility, mesmo que o fluxo seja um pouco diferente.

![[ChainOfResponsability 1.png]]

## Problema

Sistema de pedidos online. Restringir sistema a utilizadores autenticados para criar pedidos. Utilizadores com permissão ADMIN devem ter acesso a todos os pedidos.

Essas verificações devem ser realizadas sequencialmente.
Se a aplicação tentar autenticar um utilizador, ao falhar, não há motivo para outras verificações.

![[ChainOfResponsability 2.png]]
Outras verificações:

- não é seguro passar dados brutos para sistema de pedidos. Foi adicionada uma etapa extra de validação para higienizar os dados na requisição
- vulnerável a ataques de força bruta, foi adicionada verificação que filtra solicitações repetidas do mesmo IP
- devolver dados em cache em solicitações repetidas contendo os mesmo dados.

![[ChainOfResponsability 3.png]]

O código de verificações era confuso e ficou mais quando era adicionada nova funcionalidade.
Pior, quando tentava reutilizar as verificações para proteger outros componentes do sistema.

## Solução

`Chain of responsability` baseia-se na transformação de comportamentos específicos em objetos independentes chamados `handlers`. Cada verificação deve ser extraída para a sua própria classe com um único método que realiza a verificação.
A requisição, juntamente com seus dados, é passada para esse método como argumento.

O padrão sugere o encadeamento de `handlers`.  Cada `handler` possui um campo para armazenar uma referência ao próximo `handler`. Além de processar uma solicitação, os `handlers`a repassam adiante na cadeia. A solicitação percorre a cadeia até que todos os `handlers` tenham tido a oportunidade de processá-la.

Um `handler` pode não repassar a solicitação adiante e interromper processamento adicional.

![[ChainOfResponsability 4.png]]

Existe uma abordagem diferente, se ao receber uma solicitação, um `handler` decide se pode processá-la. Se puder, não repassa. Portanto, apenas um processa ou nenhum. Abordagem comum ao lidar com eventos em pilhas.

Utilizador carrega botão, o evento propaga, passa pelos containers, termina na janela da aplicação.

![[ChainOfResponsability 5.png]]



Todas as classes de `handlers` implementam a mesma interface. Cada `handler` concreto deve se preocupar com o `handler` seguinte. Pode compor cadeias em tempo de execução, usando vários `handlers` sem acoplar o código ás suas classes concretas.

## Analogia com o mundo real


![[ChainOfResponsability 6.png]]

Você acabou de comprar e instalar um novo componente no seu computador. Como você é um entusiasta de tecnologia, o computador tem vários sistemas operacionais instalados. Você tenta inicializar todos eles para ver se o hardware é compatível. O Windows detecta e habilita o hardware automaticamente. No entanto, seu amado Linux se recusa a funcionar com o novo hardware. Decide ligar para suporte técnico.

A primeira coisa que você ouve é a voz robótica do sistema de resposta automática.
O operador também não consegue sugerir nada específico.
Exige ser transferido para um técnico qualificado.
O operador transfere sua ligação para um dos engenheiros. O engenheiro lhe informa onde baixar os drivers corretos para seu novo hardware e como instalá-los no Linux

## Estrutura

![[ChainOfResponsability 7.png]]

1. `Handler`declara a interface comum a todos os `handlers`. Tem apenas um único método para lidar com requisições. Pode também ter outro método para definir o próximo `handler`.

2. `Base Handler`, classe opcional, código padrão comum a classes `handler`.

   Classe define um campo para armazenar uma referência ao próximo `handler`. `Clients`podem construir uma cadeia passando um `handler`. A classe também pode implementar o comportamento de tratamento parão: passar a execução para o próximo `handler`.

3. `Concrete Handlers` tem o código real para processar solicitações. Ao receber uma solicitação, cada `handler` deve decidir se a processa ou se a encaminha.

   Os `handlers` são autocontidos e imutáveis, aceitam todos os dados necessários apenas uma vez por meio do construtor.

4. `Client` pode compor cadeias de requisições uma única vez ou compô-las dinamicamete. Uma requisição pode ser enviada para qualquer manipulador na cadeia.


## Pseudocódigo

![[ChainOfResponsability 8.png]]


As classes da interface gráfica são construídas com o padrão Composite. Cada elemento está vinculado ao seu elemento contêiner. A qualquer momento, você pode construir uma cadeia de elementos que começa com o próprio elemento e percorre todos os seus elementos contêineres.

A GUI da aplicação geralmente é estruturada como uma árvore de objetos.
Um componente simples pode exibir dicas contextuais breves, desde que tenha algum texto de ajuda atribuído.

![[ChainOfResponsability 9.png]]


## Aplicabilidade

problema: `chain of responsability` precisa processar diferentes tipos de requisições de diversas maneiras, mas os tipos exatos de requisições e suas sequências forem desconhecidas antemão.

Solução: permite conectar vários `handlers` numa única cadeia e, ao receber uma solicitação, perguntar a cada `handler` se ele pode processá-la. Todos os handlers têm a oportunidade de processar a solicitação.

---

problema: `chain of responsability` quando for essencial executar vários `handlers` numa ordem específica

solução: os `handlers` podem ser encadeados em qualquer ordem, todas as solicitações passarão pela cadeia como planeado.

---

problema: `chain of responsability` quando o conjunto de `handlers` e sua ordem precisarem ser alterados em tempo de execução.

solução: fornecer métodos setter para um campo de referência dentro das classes `handlers`, poderá inserir, remover ou reordenar `handlers`dinamicamente


## Como implementar

O **Chain of Responsibility (Cadeia de Responsabilidade)** parece complicado quando vemos os 6 passos juntos, mas a ideia central é bastante simples:

> **Uma requisição passa por uma sequência de objetos até que algum deles a processe — ou até chegar ao fim sem ser processada.**

Pensa numa sequência assim:

```
Cliente
   │
   ▼
Handler 1 ──► Handler 2 ──► Handler 3 ──► Handler 4
   │              │              │              │
   └─ não ────────┘              │              │
                  └─ não ────────┘              │
                                 └─ não ────────┘
```

Vou explicar **cada passo**, usando Java e um exemplo de **sistema de suporte técnico**.



#### 1. Declare a interface do manipulador

Primeiro precisamos definir o que todos os elementos da cadeia sabem fazer.

Por exemplo:

```
interface Handler {
    void setNext(Handler next);
    void handle(Request request);
}
```

Temos duas responsabilidades:

### `setNext()`

Define quem será o próximo elemento:

```
handler1.setNext(handler2);
```

A cadeia fica:

```
handler1 → handler2
```

### `handle()`

É o método responsável por receber a requisição:

```
handler1.handle(request);
```

A requisição pode ser representada por um objeto:

```
class Request {

    private String type;

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
```

Por exemplo:

```
Request request = new Request("password");
```

Em vez de passar vários parâmetros:

```
handle("password", "Bruno", 123, true);
```

passamos um único objeto:

```
handle(request);
```

Isso torna o sistema mais flexível.


#### 2. Criar uma classe base abstrata

Agora começa a aparecer a verdadeira vantagem do padrão.

Imagine que teremos:

```
PasswordHandler
TechnicalHandler
ManagerHandler
DirectorHandler
```

Todos eles precisam saber quem é o próximo.

Se colocarmos essa lógica em cada classe, teremos repetição.

Então criamos uma classe abstrata:

```
abstract class BaseHandler implements Handler {

    protected Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(Request request) {

        if (next != null) {
            next.handle(request);
        }
    }
}
```

Agora temos uma implementação padrão.

O comportamento é:

```
Recebe requisição
       │
       ▼
Existe próximo?
   │        │
  sim      não
   │        │
   ▼        ▼
próximo   termina
```

Ou seja:

```
if (next != null) {
    next.handle(request);
}
```

Isso é extremamente importante no Chain of Responsibility.

#### 3. Criar os manipuladores concretos

Agora criamos as classes que realmente sabem tratar determinados tipos de requisição.

Por exemplo:

```
class PasswordHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("password")) {
            System.out.println("PasswordHandler resolveu a requisição.");
            return;
        }

        super.handle(request);
    }
}
```

Aqui acontece algo importante.

O `PasswordHandler` toma duas decisões:

### Decisão 1 — consigo tratar?

```
if (request.getType().equals("password"))
```

Se sim:

```
System.out.println("PasswordHandler resolveu a requisição.");
return;
```

A cadeia para.

### Decisão 2 — não consigo tratar, então encaminho?

```
super.handle(request);
```

O `super.handle()` chama:

```
next.handle(request);
```

Portanto:

```
PasswordHandler
       │
       ├── password → resolve
       │
       └── outro → próximo
```

---

## Outro Handler

Podemos criar:

```
class TechnicalHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("technical")) {
            System.out.println("TechnicalHandler resolveu a requisição.");
            return;
        }

        super.handle(request);
    }
}
```

E outro:

```
class ManagerHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("manager")) {
            System.out.println("ManagerHandler resolveu a requisição.");
            return;
        }

        super.handle(request);
    }
}
```

Agora temos:

```
PasswordHandler
       │
       ▼
TechnicalHandler
       │
       ▼
ManagerHandler
```


#### 4. Montar a cadeia

Agora precisamos conectar os objetos.

Por exemplo:

```
Handler password = new PasswordHandler();
Handler technical = new TechnicalHandler();
Handler manager = new ManagerHandler();

password.setNext(technical);
technical.setNext(manager);
```

A cadeia fica:

```
PasswordHandler
      │
      ▼
TechnicalHandler
      │
      ▼
ManagerHandler
```

Podemos imaginar como uma corrente:

```
[Password] → [Technical] → [Manager]
```

O cliente pode montar essa cadeia manualmente.


# 5. O cliente envia a requisição

Agora podemos fazer:

```
Request request = new Request("technical");

password.handle(request);
```

Observe que o cliente chama **apenas o primeiro Handler**.

O que acontece internamente?

### Primeiro:

```
password.handle(request)
```

O `PasswordHandler` pergunta:

```
É uma requisição de password?
```

Não.

Então:

```
super.handle(request);
```

que chama:

```
technical.handle(request)
```

---

### Segundo:

O `TechnicalHandler` pergunta:

```
É uma requisição technical?
```

Sim.

Então:

```
System.out.println("TechnicalHandler resolveu a requisição.");
return;
```

A cadeia para.

Visualmente:

```
Cliente
   │
   ▼
PasswordHandler
   │
   │ não trata
   ▼
TechnicalHandler
   │
   │ trata
   ▼
 FIM
```

O `ManagerHandler` **nem é chamado**.



#### 6. E se ninguém conseguir tratar?

Imagine:

```
Request request = new Request("payment");
```

O cliente faz:

```
password.handle(request);
```

A sequência será:

```
PasswordHandler
      │
      │ não consegue
      ▼
TechnicalHandler
      │
      │ não consegue
      ▼
ManagerHandler
      │
      │ não consegue
      ▼
     FIM
```

No `ManagerHandler`:

```
super.handle(request);
```

E o `BaseHandler` verifica:

```
if (next != null) {
    next.handle(request);
}
```

Mas não existe próximo.

Então simplesmente termina.

Isso corresponde ao ponto 6 do texto:

> Outros podem chegar ao fim da cadeia sem serem tratados.


#### 7. A parte mais importante: quem decide parar?

Essa é provavelmente a parte que mais confunde no início.

**Cada Handler decide individualmente se a requisição continua.**

Por exemplo:

```
class PasswordHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("password")) {

            System.out.println("Resolvido pelo PasswordHandler");

            return;
        }

        super.handle(request);
    }
}
```

Temos:

```
             ┌── consegue → processa → PARA
             │
Request ─────┤
             │
             └── não consegue → próximo
```

Portanto, o Chain of Responsibility **não significa necessariamente que todos os objetos serão executados**.

Significa que existe uma **possibilidade de encaminhamento**.

#### 8. O cliente pode começar em qualquer ponto

O texto também diz:

> O cliente pode acionar qualquer manipulador na cadeia, não apenas o primeiro.

Isso significa que podemos fazer:

```
technical.handle(request);
```

em vez de:

```
password.handle(request);
```

Nesse caso:

```
Cliente
   │
   ▼
TechnicalHandler
   │
   ▼
ManagerHandler
```

O `PasswordHandler` foi ignorado.

Isso pode ser útil dependendo da arquitetura.


#### 9. Cadeia com apenas um elemento

Também é perfeitamente válido:

```
Handler handler = new TechnicalHandler();

handler.handle(request);
```

Temos:

```
Cliente
   │
   ▼
TechnicalHandler
   │
   ▼
  FIM
```

Não precisamos obrigatoriamente de 3, 4 ou 10 handlers.

Uma cadeia pode ter apenas um.



#### 10. Por que usar uma classe abstrata?

Poderíamos implementar tudo diretamente na interface:

```
class PasswordHandler implements Handler {

    private Handler next;

    // ...
}
```

Depois:

```
class TechnicalHandler implements Handler {

    private Handler next;

    // ...
}
```

Depois:

```
class ManagerHandler implements Handler {

    private Handler next;

    // ...
}
```

Teríamos repetição:

```
private Handler next;
```

```
setNext()
```

```
if (next != null)
```

```
next.handle(request)
```

A classe:

```
BaseHandler
```

centraliza essa lógica.

Assim os handlers concretos ficam focados apenas naquilo que realmente importa:

> **"Eu consigo tratar esta requisição?"**



#### 11. O `super.handle()` é fundamental

Quando você vê:

```
super.handle(request);
```

nesse padrão, pense:

> **"Eu não tratei. Passe para o próximo."**

Por exemplo:

```
class TechnicalHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("technical")) {
            System.out.println("Resolvido!");
            return;
        }

        super.handle(request);
    }
}
```

O fluxo é:

```
TechnicalHandler
       │
       ├── consegue?
       │     │
       │    SIM
       │     ↓
       │   resolve
       │
       └── NÃO
             ↓
       super.handle()
             ↓
       próximo Handler
```


# 12. E onde entra a Factory?

O passo 4 fala sobre criar **fábricas** quando não queremos que o cliente tenha que montar a cadeia manualmente.

Sem Factory:

```
Handler password = new PasswordHandler();
Handler technical = new TechnicalHandler();
Handler manager = new ManagerHandler();

password.setNext(technical);
technical.setNext(manager);
```

O cliente precisa conhecer toda a estrutura.

Com uma Factory:

```
class HandlerFactory {

    public static Handler createChain() {

        Handler password = new PasswordHandler();
        Handler technical = new TechnicalHandler();
        Handler manager = new ManagerHandler();

        password.setNext(technical);
        technical.setNext(manager);

        return password;
    }
}
```

Agora o cliente faz apenas:

```
Handler handler = HandlerFactory.createChain();

handler.handle(request);
```

O cliente não precisa saber como a cadeia foi construída.

---

# 13. Exemplo completo

Juntando tudo:

```
class Request {

    private String type;

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
```

Interface:

```
interface Handler {

    void setNext(Handler next);

    void handle(Request request);
}
```

Classe base:

```
abstract class BaseHandler implements Handler {

    protected Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(Request request) {

        if (next != null) {
            next.handle(request);
        }
    }
}
```

Handler 1:

```
class PasswordHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("password")) {
            System.out.println("Password resolvido.");
            return;
        }

        super.handle(request);
    }
}
```

Handler 2:

```
class TechnicalHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("technical")) {
            System.out.println("Problema técnico resolvido.");
            return;
        }

        super.handle(request);
    }
}
```

Handler 3:

```
class ManagerHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getType().equals("manager")) {
            System.out.println("Problema encaminhado ao gerente.");
            return;
        }

        super.handle(request);
    }
}
```

Cliente:

```
public class Main {

    public static void main(String[] args) {

        Handler password = new PasswordHandler();
        Handler technical = new TechnicalHandler();
        Handler manager = new ManagerHandler();

        password.setNext(technical);
        technical.setNext(manager);

        Request request = new Request("technical");

        password.handle(request);
    }
}
```

Resultado:

```
Problema técnico resolvido.
```

O fluxo foi:

```
Main
 │
 │ request("technical")
 ▼
PasswordHandler
 │
 │ não é password
 ▼
TechnicalHandler
 │
 │ É technical!
 ▼
RESOLVE
```

O `ManagerHandler` nunca foi executado.

---

# 14. Resumindo os 6 passos

Podemos transformar o texto do Refactoring Guru em algo muito mais simples:

|Passo|O que significa|
|---|---|
|**1**|Criar a interface `Handler`|
|**2**|Criar `BaseHandler` para guardar `next` e encaminhar|
|**3**|Criar os handlers que sabem tratar determinados pedidos|
|**4**|Montar a cadeia, manualmente ou através de Factory|
|**5**|Enviar a requisição para um Handler; ela vai seguindo a cadeia|
|**6**|Aceitar que ela pode ser tratada, parar no meio ou chegar ao fim sem tratamento|

A ideia inteira pode ser reduzida a isto:

```
                    consegue tratar?
                         │
                         ▼
Request → Handler A ── NÃO ──→ Handler B ── NÃO ──→ Handler C
             │                     │                    │
             SIM                   SIM                  SIM
              ↓                     ↓                    ↓
           resolve               resolve              resolve
              │                     │                    │
              └────── PARA ─────────┴──────── PARA ──────┘
```

**A essência do padrão é separar quem envia a requisição de quem decide tratá-la**, permitindo adicionar, remover ou reorganizar os handlers sem alterar o código que cria a requisição.

## Prós

- Você pode controlar a ordem de processamento das solicitações.
-  _Princípio da Responsabilidade Única_ . É possível separar as classes que invocam operações das classes que executam as operações.
-  _Princípio Aberto/Fechado_ . Você pode introduzir novos manipuladores no aplicativo sem quebrar o código do cliente existente.


## Contras

- Algumas solicitações podem acabar não sendo atendidas.