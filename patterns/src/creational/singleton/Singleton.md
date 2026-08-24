
O **Singleton** resolve dois problemas:

1. **Garantir uma única instância**
    - Deve existir apenas uma instância da classe.
    - Útil quando existe um recurso partilhado, como uma ligação à base de dados ou um ficheiro.
2. **Fornecer um ponto de acesso global à instância**
    - A instância é acessível através de um método estático.
    - Impede que outras partes do código criem ou substituam a instância diretamente.

### Analogia

- Governo de um país
- Presidente de um país

Existe uma única entidade responsável e existe um ponto de acesso conhecido para a obter.

### Exemplos nas Java Core Libraries

- [`java.lang.Runtime#getRuntime()`](http://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html#getRuntime--)
- [`java.awt.Desktop#getDesktop()`](http://docs.oracle.com/javase/8/docs/api/java/awt/Desktop.html#getDesktop--)
- [`java.lang.System#getSecurityManager()`](http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#getSecurityManager--)

## Implementação

- **Construtor `private`**
    - Impede a criação direta através de `new`.
- **Campo `static`**
    - Guarda a única instância da classe.
- **Método `static`**
    - Funciona como ponto de acesso à instância.
    - Cria a instância se ainda não existir e guarda-a no campo `static`.

## Threads

A JVM permite que uma aplicação tenha **várias threads de execução concorrentes**.

Quando várias threads podem aceder simultaneamente ao Singleton, é necessário garantir que **apenas uma instância é criada**.

### Problema

Sem proteção, duas threads podem fazer simultaneamente:

| Thread A | Thread B |
|---|---|
| `instance == null` | `instance == null` |
| ↓ | ↓ |
| `new Singleton()` | `new Singleton()` |

Resultado: podem ser criadas **duas instâncias**.

## Thread-safe

Para tornar o Singleton seguro entre threads, é necessário controlar o acesso à instância.

`volatile` é uma palavra-chave usada em variáveis partilhadas entre threads.

Garante que as threads têm **visibilidade adequada do valor mais recente** de `instance`.

### Sem `volatile`

- Thread A pode alterar `instance`.
- Thread B pode não ver essa alteração.

---

Double-Checked Locking

````
public class Singleton {

    private static volatile Singleton instance;

    private Singleton(String value) {
        // ...
    }

    Singleton result = instance;
    
    if (result != null) {
	    return result;
	}
    
    synchronized(Singleton.class) {
        if (instance == null) {
           instance = new Singleton(value);
        }
        return instance;
    }
}
````

- `volatile` → garante visibilidade entre threads.
- `synchronized` → apenas uma thread de cada vez pode executar aquele bloco.
- `synchronized(Singleton.class)` → utiliza `Singleton.class` como **cadeado (lock)**.

---

````
static class ThreadFoo implements Runnable {

	@Override
	public void run() {
		Singleton singleton = Singleton.getInstance("FOO");
		System.out.println(singleton.value);
	}
}

	static class ThreadBar implements Runnable {
	
	@Override
	public void run() {
		Singleton singleton = Singleton.getInstance("BAR");
		System.out.println(singleton.value);
	}
}
````

**Com ou sem `@Override`, o comportamento em runtime é igual.**  
`@Override` serve para o compilador verificar que o método está realmente a sobrescrever/implementar um método da classe/interface pai.


---
## Referências

- [DigitalOcean — Java Singleton Pattern: Best Practices & Examples](https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples)
- [Refactoring.Guru — Singleton in Java](https://refactoring.guru/design-patterns/singleton/java/example#example-3)