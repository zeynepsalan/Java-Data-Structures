package Postlabs.Postlab02;

/**
 * Student deliverable for CSE201 Post-Lab 2.
 *
 * Complete this file without using arrays or Java collection classes as the
 * internal storage of the list. Do not change the class or method names.
 *
 * Keep DNode and its one-argument constructor exactly as they are. Lab 2 uses
 * a three-argument node constructor; this file does not, and the player
 * depends on the version below.
 */
class DNode<E> {
    E item;
    DNode<E> prev;
    DNode<E> next;

    DNode(E item) {
        this.item = item;
    }
}

public class DoublyCircularLinkedList<E> {
    private DNode<E> head;
    private DNode<E> tail;
    private DNode<E> current;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        current = null;
        size = 0;
        return;
    }

    public DNode<E> node(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        DNode<E> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr;
    }

    public DNode<E> TNode(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        DNode<E> current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
        return current;

    }

    public void addFirst(E item) {
        DNode<E> newNode = new DNode<>(item);
        if (size == 0) {
            current = newNode;
            tail = newNode;
            head = newNode;
            head.next = tail;
            head.prev = tail;
            tail.next = head;
            tail.prev = head;
            size++;
            return;
        }
        newNode.next = head;
        newNode.prev = tail;
        head.prev = newNode;
        tail.next = newNode;
        head = newNode;
        size++;
    }

    public void addLast(E item) {
        DNode<E> newNode = new DNode<>(item);
        if (size == 0) {
            current = newNode;
            tail = newNode;
            head = newNode;
            head.next = tail;
            head.prev = tail;
            tail.next = head;
            tail.prev = head;
            size++;
            return;
        }
        newNode.next = head;
        newNode.prev = tail;
        head.prev = newNode;
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    public E removeFirst() {
        if (size == 0) {
            return null;
        }
        boolean currentMi = false;
        if (current == head) {
            currentMi = true;
        }
        if (tail == head) {
            E e = head.item;
            head = null;
            tail = null;
            current = null;
            size--;
            return e;
        }
        E e = head.item;
        tail.next = head.next;
        head = head.next;
        head.prev = tail;
        size--;
        if (currentMi)
            current = head;
        return e;

    }

    public E removeLast() {
        if (size == 0) {
            var a = "0";
            return null;
        }
        boolean currentMi = false;
        if (current == tail) {
            currentMi = true;
        }
        if (tail == head) {
            E e = head.item;
            head = null;
            tail = null;
            current = null;
            size--;
            return e;
        }
        E e = tail.item;
        head.prev = tail.prev;
        tail = tail.prev;
        tail.next = head;
        size--;
        if (currentMi)
            current = head;
        return e;
    }

    public E get(int index) {
        if (index >= size || index < 0)
            return null;
        if (index > size / 2) {
            return TNode(index).item;
        }
        return node(index).item;
    }

    public E getCurrent() {
        if (current == null)
            return null;
        return current.item;
    }

    // node(i) çağırarak yapmak mı daha mantıklı yoksa DNode<E> current = head;
    // yapmak mı?
    public int currentIndex() {
        DNode<E> next = head;
        for (int i = 0; i < size(); i++) {
            if (next.equals(current))
                return i;
            next = next.next;
        }
        return -1;
    }

    public E moveNext() {
        if (current == null)
            return null;
        current = current.next;
        return current.item;
    }

    public E movePrevious() {
        if (current == null)
            return null;
        current = current.prev;
        return current.item;
    }

    /**
     * Removes the current track and returns it. Afterwards current is the
     * track that came after the removed one, wrapping to head when the removed
     * track was the tail. Returns null when the list is empty.
     */
    public E removeCurrent() {
        if (current == null) {
            return null;
        }
        if (tail == head) {
            E e = head.item;
            head = null;
            tail = null;
            current = null;
            size--;
            return e;
        }
        if (current == head)
            return removeFirst();
        if (current == tail)
            return removeLast();
        E e = current.item;
        DNode<E> prevNode = current.prev;
        current = current.next;
        prevNode.next = current;
        current.prev = prevNode;
        size--;
        return e;
    }

    /**
     * Makes the track at index current and returns true. An invalid index
     * returns false and leaves current where it was.
     */
    public boolean jumpTo(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        current = node(index);
        return true;
    }

    private UnsupportedOperationException notImplemented() {
        return new UnsupportedOperationException(
                "Complete DoublyCircularLinkedList.java to enable the music player.");
    }
}
