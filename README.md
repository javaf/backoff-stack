Backoff stack is an unbounded lock-free LIFO linked
list, where pushes and pops synchronize at a single
location. It is compare-and-set (CAS) atomic operation
to provide concurrent access with obstruction freedom.

```java
push():
1. Create node for value.
2. Try pushing node to stack.
2a. If successful, return.
2b. Otherwise, backoff and try again.
```

```java
pop():
1. Try popping a node from stack.
1a. If successful, return its value.
1b. Otherwise, backoff and try again.
```

```java
tryPush():
1. Get stack top.
2. Set node's next to top.
3. Try push node at top (CAS).
```

```java
tryPop():
1. Get stack top, and ensure stack not empty.
2. Try pop node at top, and set top to next (CAS).
```

```java
backoff():
1. Get a random wait duration.
2. Sleep for the duration.
3. Double the max random wait duration.
```

See [BackoffStack.java] for code, [Main.java] for test, and [repl.it] for output.

[BackoffStack.java]: https://repl.it/@wolfram77/backoff-stack#BackoffStack.java
[Main.java]: https://repl.it/@wolfram77/backoff-stack#Main.java
[repl.it]: https://backoff-stack.wolfram77.repl.run


### references

- [The Art of Multiprocessor Programming :: Maurice Herlihy, Nir Shavit](https://dl.acm.org/doi/book/10.5555/2385452)
