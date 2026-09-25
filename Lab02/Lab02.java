import java.util.Objects;

class Lab02 {
    public static void main(String[] args) {
        // Test your code here...
        DoublyLinkedList<String> list = new DoublyLinkedList<>();

        list.addLast("zeynep");
        System.out.println(list.toString());
        System.out.println(list.size());
        list.removeFirst();
        System.out.println(list.size());
        System.out.println(list.toString());

    }
}

interface MyList<E> {

    /**
     * Returns the number of elements in the list.
     */
    int size();

    /**
     * Returns true if the list is empty.
     */
    boolean isEmpty();

    /**
     * Removes all elements from the list.
     */
    void clear();

    /**
     * Inserts an element at the beginning of the list.
     */
    void addFirst(E e);

    /**
     * Inserts an element at the end of the list.
     */
    void addLast(E e);

    /**
     * Inserts an element at a given index (0..size).
     */
    void insertAt(int index, E e);

    /**
     * Removes and returns the first element, or null if empty.
     */
    E removeFirst();

    /**
     * Removes and returns the last element, or null if empty.
     */
    E removeLast();

    /**
     * Removes and returns the element at a given index.
     * Returns null if index is invalid.
     */
    E removeAt(int index);

    /**
     * Returns the element at a given index, or null if invalid.
     */
    E get(int index);

    /**
     * Replaces the element at a given index with a new value.
     * Returns the old value, or null if index is invalid.
     */
    E set(int index, E e);

    /**
     * Returns the index of the first occurrence of the given object,
     * or -1 if not found.
     */
    int indexOf(E o);

    /**
     * Removes the first occurrence of the given object.
     * Returns true if removed, false otherwise.
     */
    boolean remove(E o);
}

class Node<E> {
    public E item;
    public Node<E> next;

    public Node(E item, Node<E> next) {
        this.item = item;
        this.next = next;
    }
}

class SinglyLinkedList<E> implements MyList<E> {
    Node<E> head;

    public SinglyLinkedList() {
        head = null;
    }

    public Node<E> getHead() {
        // Do not modify this method
        return head;
    }

    @Override
    public int size() {
        // Kontrolleri yapıldı
        int a = 0;
        Node<E> current = head;
        while (current != null) {
            a++;
            current = current.next;
        }
        return a;
    }

    @Override
    public boolean isEmpty() {
        // Kontrolleri yapıldı
        if (head == null) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

    @Override
    public void clear() {
        // Kontrolleri yapıldı
        // while (head != null) {
        // head = head.next;
        // if (head.next == null)
        // head = null;
        // }
        head = null;
    }

    @Override
    public void addFirst(E e) {
        // Kontrolleri yapıldı
        if (head == null) {
            Node<E> newNode = new Node<>(e, null);
            head = newNode;
        } else {
            Node<E> newNode = new Node<>(e, head);
            head = newNode;
        }
    }

    @Override
    public void addLast(E e) {
        // Kontrolleri yapıldı
        Node<E> newNode = new Node<>(e, null);
        // System.out.println("Yeni node oluşturuldu");
        if (head == null) {
            // System.out.println("Head: Null kontrolü yapıldı");
            head = newNode;
            return;
        }
        Node<E> current = head;
        // int a = 0;
        while (current.next != null) {
            // a++;
            // System.out.println(a + ". current.next kontrolü");
            current = current.next;
        }
        // System.out.println("Döngüden çıkıldı. current.next= newNode");
        current.next = newNode;

    }

    @Override
    public void insertAt(int index, E e) {
        // Kontrolleri yapıldı
        if (index == 0 && head != null) {
            Node<E> newNode = new Node<>(e, head);
            head = newNode;
        }
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            i++;
            if (i == index) {
                Node<E> newNode = new Node<>(e, current.next);
                current.next = newNode;
                break;
            }
            current = current.next;
        }
    }

    @Override
    public E removeFirst() {
        // Kontrolleri yapıldı
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            Node<E> deleted = head;
            head = null;
            return deleted.item;
        }
        Node<E> current = head;
        head = head.next;
        return current.item;
    }

    @Override
    public E removeLast() {
        // Kontrolleri yapıldı
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            Node<E> deleted = head;
            head = null;
            return deleted.item;
        }
        Node<E> current = head;
        while (current.next != null) {
            if (current.next.next == null) {
                Node<E> deleted = current.next;
                current.next = null;
                return deleted.item;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public E removeAt(int index) {
        // Kontrolleri yapıldı
        if (head == null) {
            return null;
        }
        if (index == 0) {
            Node<E> deleted = head;
            head = head.next;
            return deleted.item;
        }
        Node<E> current = head;
        int i = 1;
        while (current.next != null) {
            if (i == index) {
                Node<E> deleted = current.next;
                current.next = current.next.next;
                return deleted.item;
            }
            current = current.next;
            i++;
        }
        return null;
    }

    @Override
    public E get(int index) {
        // Kontrolleri yapıldı
        Node<E> current = head;
        int a = 0;
        while (current != null) {
            if (a == index) {
                return current.item;
            }
            current = current.next;
            a++;
        }
        return null;
    }

    @Override
    public E set(int index, E e) {
        // Kontrolleri yapıldı
        if (head == null) {
            return null;
        }
        if (index == 0) {
            Node<E> newNode = new Node<>(e, head.next);
            head = newNode;
            return e;
        }
        Node<E> current = head;
        int a = 0;
        while (current.next != null) {
            if (a == index - 1) {
                if (current.next.next == null) {
                    Node<E> newNode = new Node<>(e, null);
                    current.next = newNode;
                    return e;
                }
                Node<E> newNode = new Node<>(e, current.next.next);
                current.next = newNode;
                return e;
            }
            a++;
            current = current.next;
        }
        return null;
    }

    @Override
    public int indexOf(E o) {
        // Kontrolleri yapıldı
        if (o == null) {
            return -1;
        }
        Node<E> current = head;
        int a = 0;
        while (current != null) {
            if (current.item.equals(o)) {
                return a;
            }
            current = current.next;
            a++;
        }
        return -1;
    }

    @Override
    public boolean remove(E o) {
        // Kontrolleri yapıldı
        if (head != null && head.item.equals(o)) {
            head = head.next;
            return true;
        }
        Node<E> current = head;
        while (current != null) {
            if (current.next != null && current.next.item == o) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "Başlangıç: ";
        Node<E> current = head;
        while (current != null) {
            s += String.valueOf(current.item) + " --> ";
            current = current.next;
        }
        return s;
    }

}

class CircularLinkedList<E> implements MyList<E> {
    Node<E> tail = null;
    int size;

    public Node<E> getTail() {
        // Do not modify this method
        return tail;
    }

    public Node<E> node(int index) {
        if (index < 0 || index >= size() || size() == 0) {
            return null;
        }
        Node<E> cur = tail.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        tail = null;
        size = 0;
        return;
    }

    @Override
    public void addFirst(E e) {
        insertAt(0, e);
    }

    @Override
    public void addLast(E e) {
        insertAt(size(), e);
    }

    @Override
    public void insertAt(int index, E e) {
        if (index < 0 || index > size())
            return;
        if (size == 0) {
            tail = new Node<>(e, null);
            tail.next = tail;
            size++;
            return;
        }
        if (index == 0) {
            Node<E> newNode = new Node<>(e, tail.next);
            tail.next = newNode;
            size++;
            return;
        }
        if (index == size()) {
            Node<E> newNode = new Node<>(e, tail.next);
            tail.next = newNode;
            tail = newNode;
            size++;
            return;
        }
        Node<E> prev = node(index - 1);
        Node<E> newNode = new Node<>(e, prev.next);
        prev.next = newNode;
        size++;
        return;
    }

    @Override
    public E removeFirst() {
        if (size == 0)
            return null;
        E e = node(0).item;
        removeAt(0);
        return e;
    }

    @Override
    public E removeLast() {
        if (size == 0)
            return null;
        E e = node(size - 1).item;
        removeAt(size - 1);
        return e;
    }

    @Override
    public E removeAt(int index) {
        if (index < 0 || index >= size() || size() == 0)
            return null;
        if (size == 1) {
            E e = tail.item;
            tail = null;
            size = 0;
            return e;
        }
        if (index == 0) {
            E e = tail.next.item;
            tail.next = tail.next.next;
            size--;
            return e;
        }
        Node<E> prevNode = node(index - 1);
        E e = prevNode.next.item;
        if (index == size - 1) {
            prevNode.next = prevNode.next.next;
            tail = prevNode;
            size--;
            return e;
        }
        prevNode.next = prevNode.next.next;
        size--;
        return e;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size() || size == 0) {
            return null;
        }
        return node(index).item;
    }

    @Override
    public E set(int index, E e) {
        if (index < 0 || index >= size() || size == 0) {
            return null;
        }
        E x = node(index).item;
        node(index).item = e;
        return x;
    }

    @Override
    public int indexOf(E o) {
        if (size == 0) {
            return -1;
        }
        Node<E> cur = tail.next;
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(cur.item, o))
                return i;
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public boolean remove(E o) {
        int index = indexOf(o);
        if (index < 0 || index >= size() || size() == 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    @Override
    public String toString() {
        String s = "Başlangıç: ";
        if (tail == null) {
            return s;
        }
        if (tail.next == tail) {
            System.out.println("Tek item: ");
            s += tail.item + " --> ";
            return s;
        }
        Node<E> current = tail.next;
        int i = 1;
        for (int j = 0; j < size(); j++) {
            s += i + ". " + String.valueOf(current.item) + " --> ";
            current = current.next;
            i++;
        }
        return s;
    }

}

class DNode<E> {
    public E item;
    public DNode<E> prev;
    public DNode<E> next;

    public DNode(E item, DNode<E> prev, DNode<E> next) {
        this.item = item;
        this.prev = prev;
        this.next = next;
    }
}

class DoublyLinkedList<E> implements MyList<E> {
    DNode<E> head;
    DNode<E> tail;
    int logicalSize;

    public DNode<E> getHead() {
        // Do not modify this method
        return head;
    }

    public DNode<E> getTail() {
        // Do not modify this method
        return tail;
    }

    public DNode<E> node(int index) {
        DNode<E> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    @Override
    public int size() {
        return logicalSize;
    }

    @Override
    public boolean isEmpty() {
        return logicalSize == 0;
    }

    @Override
    public void clear() {
        tail = null;
        head = null;
        logicalSize = 0;
        return;
    }

    @Override
    public void addFirst(E e) {
        DNode<E> newNode = new DNode<>(e, null, head);
        if (head == null) {
            tail = newNode;
        } else {
            head.prev = newNode;
        }
        head = newNode;
        logicalSize++;
        return;
    }

    @Override
    public void addLast(E e) {
        DNode<E> n = new DNode<>(e, tail, null);
        if (tail == null) {
            head = n;
        } else {
            tail.next = n;
        }
        tail = n;
        logicalSize++;
        return;
    }

    @Override
    public void insertAt(int index, E e) {
        if (index < 0 || index > size())
            return;
        if (index == 0) {
            addFirst(e);
            return;
        }
        if (index == size()) {
            addLast(e);
            return;
        }
        DNode<E> indexNode = node(index);
        DNode<E> prevNode = indexNode.prev;
        DNode<E> n = new DNode<>(e, prevNode, indexNode);
        prevNode.next = n;
        indexNode.prev = n;
    }

    @Override
    public E removeFirst() {
        return removeAt(0);
    }

    @Override
    public E removeLast() {
        return removeAt(logicalSize - 1);
    }

    @Override
    public E removeAt(int index) {
        if (index < 0 || index >= size() || size() == 0)
            return null;
        E e = node(index).item;
        if (index == 0) {
            head = head.next;
            head.prev = null;
            logicalSize--;
            return e;
        }
        if (index == size() - 1) {
            tail = tail.prev;
            tail.next = null;
            logicalSize--;
            return e;
        }
        DNode<E> indexNode = node(index);
        DNode<E> prevNode = indexNode.prev;
        DNode<E> nextNode = indexNode.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        logicalSize--;
        return e;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size() || size() == 0) {
            return null;
        }
        return node(index).item;
    }

    @Override
    public E set(int index, E e) {
        if (index < 0 || index >= size() || size() == 0) {
            return null;
        }
        E x = node(index).item;
        node(index).item = e;
        return x;
    }

    @Override
    public int indexOf(E o) {
        DNode<E> cur = head;
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(o, cur.item))
                return i;
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public boolean remove(E o) {
        int index = indexOf(o);
        if (index == -1 || isEmpty()) {
            return false;
        }
        removeAt(index);
        return true;
    }

    public String toString() {
        String s = "Başlangıç: ";
        for (int i = 0; i < size(); i++) {
            s += "" + i + ". --> " + node(i).item + " ";
        }
        return s;
    }

}
