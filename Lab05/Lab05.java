package Lab05;
/**
 * CS201 - Data Structures
 * Lab 06 - Stacks and Queues
 *
 * In this lab, you will implement both array-based and linked-list-based
 * versions.
 *
 * Implementations use fixed-size arrays (no resizing) for array-based versions.
 */

public class Lab05 {
    public static void main(String[] args) {

        LinkedQueue<String> arraystack = new LinkedQueue<>();

        System.out.println("BOŞ LİSTE KONTROLÜ");
        System.out.println("0 eleman: " + arraystack.isEmpty());
        System.out.println("0 eleman: " + arraystack.front());
        System.out.println("0 eleman: " + arraystack.dequeue());
        System.out.println("0 eleman: " + arraystack.size());
        System.out.println();
        System.out.println("ELEMAN EKLEME");
        arraystack.enqueue("zeynep");
        System.out.println("1 eleman: " + arraystack.isEmpty());
        System.out.println("1 eleman: " + arraystack.front());
        System.out.println("1 eleman: " + arraystack.size());
        System.out.println();
        arraystack.enqueue("durmuş");
        System.out.println("2 eleman: " + arraystack.isEmpty());
        System.out.println("2 eleman: " + arraystack.front());
        System.out.println("2 eleman: " + arraystack.size());
        System.out.println();
        arraystack.enqueue("zehra");
        System.out.println("3 eleman: " + arraystack.isEmpty());
        System.out.println("3 eleman: " + arraystack.front());
        System.out.println("3 eleman: " + arraystack.size());
        System.out.println();
        System.out.println("ELEMAN ÇIKARMA");
        arraystack.dequeue();
        System.out.println("2 eleman: " + arraystack.isEmpty());
        System.out.println("2 eleman: " + arraystack.front());
        System.out.println("2 eleman: " + arraystack.size());
        System.out.println();
        arraystack.dequeue();
        System.out.println("1 eleman: " + arraystack.isEmpty());
        System.out.println("1 eleman: " + arraystack.front());
        System.out.println("1 eleman: " + arraystack.size());
        System.out.println();
        arraystack.dequeue();
        System.out.println("0 eleman: " + arraystack.isEmpty());
        System.out.println("0 eleman: " + arraystack.front());
        System.out.println("0 eleman: " + arraystack.size());
        System.out.println();
    }
}

// Common list methods
interface List {
    boolean isEmpty();

    int size();
}

//
// Stack Interface
//

interface Stack<E> extends List {
    void push(E item);

    E pop();

    E top();
}

//
// Queue Interface
//

interface Queue<E> extends List {
    void enqueue(E item);

    E dequeue();

    E front();
}

//
// Array-Based Stack
//

class ArrayStack<E> implements Stack<E> {

    private static final int DEFAULT_CAPACITY = 4;
    private E[] data;
    private int top;

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        top = 0;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    @Override
    public void push(E item) {
        if (top > data.length) {
            return;
        }
        data[top] = item;
        top++;
    }

    @Override
    public E top() {
        if (isEmpty())
            return null;
        return data[top - 1];
    }

    @Override
    public E pop() {
        if (isEmpty())
            return null;
        E item = data[top - 1];
        data[top - 1] = null;
        top--;
        return item;
    }

}

//
// Array-Based Queue
//

class ArrayQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 4;
    private E[] data;
    private int front;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public void enqueue(E item) {
        if (size > data.length)
            return;
        int index = (front + size) % DEFAULT_CAPACITY;
        data[index] = item;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty())
            return null;
        E item = data[front];
        data[front] = null;
        front = (front + 1) % DEFAULT_CAPACITY;
        size--;
        return item;
    }

    @Override
    public E front() {
        if (isEmpty())
            return null;
        return data[front];
    }
}

//
// Node Class (for linked list)
//

class Node<E> {
    E data;
    Node<E> next;

    Node(E data) {
        this.data = data;
        this.next = null;
    }
}

//
// Linked List-Based Stack
//

class LinkedStack<E> implements Stack<E> {

    private Node<E> top;
    private int size;

    public LinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void push(E item) {
        Node<E> node = new Node<>(item);
        if (!isEmpty()) {
            node.next = top;
        }
        top = node;
        size++;
    }

    @Override
    public E pop() {
        if (isEmpty())
            return null;
        E item = top.data;
        top = top.next;
        size--;
        return item;
    }

    @Override
    public E top() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

}

//
// Linked List-Based Queue
//

class LinkedQueue<E> implements Queue<E> {

    private Node<E> front;
    private Node<E> rear;
    private int size;

    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void enqueue(E item) {
        Node<E> node = new Node<>(item);
        if (isEmpty()) {
            rear = node;
            front = node;
            size++;
            return;
        }
        rear.next = node;
        rear = node;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty())
            return null;
        E item = front.data;
        front = front.next;
        size--;
        return item;
    }

    @Override
    public E front() {
        if (isEmpty())
            return null;
        return front.data;
    }

}
