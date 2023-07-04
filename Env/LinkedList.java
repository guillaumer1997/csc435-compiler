package Env;

public class LinkedList<K, V> {
    private ListNode<K, V> head;

    public LinkedList() {
        this.head = null;

    }

    public ListNode<K, V> getHead() {
        return this.head;
    }

    public V lookup(K key) {
        ListNode<K, V> temp = this.head;
        while (temp != null) {
            if (key.equals(temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    public void add(K key, V value) {
        ListNode<K, V> temp = new ListNode<K, V>(key, value, this.head);
        this.head = temp;
    }

    public boolean isEmpty() {
        if (this.head == null) {
            return true;
        }
        return false;

    }
}