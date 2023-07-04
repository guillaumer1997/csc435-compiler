package Env;

public class ListNode<K, V> {
    public K key;
    public V value;

    public ListNode<K, V> next;

    public ListNode(K k, V v, ListNode<K, V> next) {
        this.key = k;
        this.value = v;
        this.next = next;
    }

}