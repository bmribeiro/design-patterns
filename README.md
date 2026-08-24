# Design Patterns

A collection of **GoF (Gang of Four) Design Patterns** implemented in Java, with practical examples and concise explanations focused on understanding when and why each pattern should be used.

The project is organized according to the three main categories of the GoF Design Patterns:

- **Creational** — object creation mechanisms
- **Structural** — object and class composition
- **Behavioral** — communication and responsibility between objects

## Project Structure

```text
design-patterns/
│
├── README.md
│
└── patterns/
    └── src/
        ├── creational/
        ├── structural/
        └── behavioral/
```

## Creational Patterns

Creational patterns provide mechanisms for creating objects while reducing coupling between the client code and the concrete classes being instantiated.

| Pattern | Description | Status |
|---|---|---|
| [Singleton](patterns/src/creational/singleton/) | Ensures a class has only one instance and provides a global access point to it. | ✅ |
| [Factory Method](patterns/src/creational/factory-method/) | Defines an interface for creating an object while allowing subclasses to decide which concrete object to instantiate. | ✅ |
| [Abstract Factory](patterns/src/creational/abstract-factory/) | Creates families of related objects without specifying their concrete classes. | ✅ |
| [Builder](patterns/src/creational/builder/) | Separates the construction of a complex object from its representation. | ✅ |
| [Prototype](patterns/src/creational/prototype/) | Creates new objects by copying existing objects instead of constructing them from scratch. | ✅ |

## Structural Patterns

Structural patterns explain how classes and objects can be composed to form larger and more flexible structures.

| Pattern | Description | Status |
|---|---|---|
| Adapter | Allows incompatible interfaces to work together. | 🚧 |
| Bridge | Separates an abstraction from its implementation. | 🚧 |
| Composite | Composes objects into tree structures to represent part-whole hierarchies. | 🚧 |
| Decorator | Adds responsibilities to objects dynamically without modifying their classes. | 🚧 |
| Facade | Provides a simplified interface to a complex subsystem. | 🚧 |
| Flyweight | Reduces memory usage by sharing common object state. | 🚧 |
| Proxy | Provides a substitute or placeholder for another object to control access to it. | 🚧 |

## Behavioral Patterns

Behavioral patterns focus on communication between objects and the distribution of responsibilities.

| Pattern | Description | Status |
|---|---|---|
| Chain of Responsibility | Passes requests along a chain of handlers until one handles the request. | 🚧 |
| Command | Encapsulates a request as an object. | 🚧 |
| Iterator | Provides a way to access elements of a collection sequentially without exposing its underlying representation. | 🚧 |
| Mediator | Centralizes communication between related objects. | 🚧 |
| Memento | Captures and restores an object's previous state without violating encapsulation. | 🚧 |
| Observer | Defines a one-to-many dependency so that changes in one object notify its dependents. | 🚧 |
| State | Allows an object to change its behavior when its internal state changes. | 🚧 |
| Strategy | Defines a family of algorithms and makes them interchangeable. | 🚧 |
| Template Method | Defines the skeleton of an algorithm while allowing subclasses to redefine specific steps. | 🚧 |
| Visitor | Separates an algorithm from the objects on which it operates. | 🚧 |

## Goals

This repository is intended to:

- Understand the intent and structure of each GoF Design Pattern.
- Identify when a pattern is appropriate and when it is not.
- Implement the patterns using modern Java.
- Understand the trade-offs introduced by each pattern.
- Provide practical examples suitable for technical interview preparation.
- Build a reusable reference for future projects.

## Implementation Approach

Each pattern is implemented independently and focuses on the core idea of the pattern rather than on unnecessary framework or library dependencies.

Where appropriate, examples include:

- The problem the pattern solves.
- The pattern's structure and participants.
- A Java implementation.
- A practical usage example.
- The advantages and disadvantages.
- When to use and when to avoid the pattern.

## GoF Pattern Categories

The original **Gang of Four** catalog contains **23 design patterns**, divided into three categories:

### Creational — 5

1. Singleton
2. Factory Method
3. Abstract Factory
4. Builder
5. Prototype

### Structural — 7

1. Adapter
2. Bridge
3. Composite
4. Decorator
5. Facade
6. Flyweight
7. Proxy

### Behavioral — 11

1. Chain of Responsibility
2. Command
3. Iterator
4. Mediator
5. Memento
6. Observer
7. State
8. Strategy
9. Template Method
10. Visitor
11. Interpreter

## Technologies

- **Java**
- **Git**
- **Maven** *(if applicable)*

## References

- [Refactoring.Guru — Design Patterns](https://refactoring.guru/design-patterns)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://www.oreilly.com/library/view/design-patterns-elements/0201633612/)

## License

This project is intended for educational and reference purposes.