import java.util.*;
import java.util.concurrent.atomic.*;

class BackoffStack<T> {
  AtomicReference<Node<T>> top;
  static final long MIN_WAIT = 1;
  static final long MAX_WAIT = 100;


  public void push(T x) {
    long W = MIN_WAIT;
    Node<T> n = new Node<>(x);
    while (true) {
      if (tryPush(n)) return;
      else W = backoff(W);
    }
  }

  public T pop() throws EmptyStackException {
    long W = MIN_WAIT;
    while (true) {
      Node<T> n = tryPop();
      if (n != null) return n.value;
      else W = backoff(W);
    }
  }

  protected boolean tryPush(Node<T> n) {
    Node<T> m = top.get();
    n.next = m;
    return top.compareAndSet(m, n);
  }

  protected Node<T> tryPop() throws EmptyStackException {
    Node<T> m = top.get();
    if (m == null) throw new EmptyStackException();
    Node<T> n = m.next;
    return top.compareAndSet(m, n)? m : null;
  }

  private long backoff(long W) {
    long w = (long) (Math.random() * // 1
      (W-MIN_WAIT) + MIN_WAIT);      // 1
    try { Thread.sleep(w); }         // 2
    catch(InterruptedException e) {} // 2
    return Math.min(2*W, MAX_WAIT);  // 3
  }
}
