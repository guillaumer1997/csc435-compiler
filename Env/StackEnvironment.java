package Env;

import AST.*;
import java.util.Stack;
import java.util.*;

public class StackEnvironment<K, V> implements Environment<K, V> {

    private Stack<LinkedList<K, V>> env;

    public StackEnvironment() {
        this.env = new Stack<LinkedList<K, V>>();
    }

    public void beginScope() {
        this.env.push(new LinkedList<K, V>());

    }

    public void endScope() {
        this.env.pop();
    }

    public boolean inCurrentScope(K key) {
        if (this.env.peek().lookup(key) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void add(K key, V value) {
        this.env.peek().add(key, value);

    }

    public V lookup(K key) {
        return this.env.peek().lookup(key);
    }

    public boolean isEmpty() {
        return this.env.peek().isEmpty();

    }

    public List<V> toList() {
        LinkedList<K, V> temp = this.env.peek();
        ListNode<K, V> head = temp.getHead();
        List<V> result = new ArrayList<V>();
        while (head != null) {
            result.add(head.value);
            head = head.next;

        }
        return result;
    }

}