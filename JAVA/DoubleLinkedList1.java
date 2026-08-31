public class DoubleLinkedList1 {
    public static void main(String[] args) {

        Node2 node = new Node2(1);
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList(node);
        System.out.println(doubleLinkedList.deleteNodeAtStart().data);
        doubleLinkedList.addNodeToEnd(2);
        doubleLinkedList.addNodeToEnd(3);
        doubleLinkedList.addNodeToEnd(4);
        doubleLinkedList.addNodeToEnd(5);
        doubleLinkedList.addNodeToEnd(6);
        System.out.println(doubleLinkedList.toString());
        System.out.println(doubleLinkedList.deleteNodeAtStart().data);
        System.out.println(doubleLinkedList.toString());
        System.out.println(doubleLinkedList.deleteNodeAfter(4).data);
        System.out.println(doubleLinkedList.toString());
        System.out.println(doubleLinkedList.deleteNodeAfter(1));
        System.out.println(doubleLinkedList.toString());
        System.out.println(doubleLinkedList.deleteNodeAfter(6));
        System.out.println(doubleLinkedList.toString());
        // doubleLinkedList.addNodeToEnd(2);
        // doubleLinkedList.addNodeToEnd(3);
        // doubleLinkedList.addNodeToEnd(4);
        // System.out.println(doubleLinkedList.toString());
        // doubleLinkedList.addNodeToEnd(5);
        // System.out.println(doubleLinkedList.toString());
        // doubleLinkedList.addNodeAfter(3, 10);
        // System.out.println(doubleLinkedList.toString());
        // doubleLinkedList.addNodeAfter(5, 11);
        // System.out.println(doubleLinkedList.toString());
        // doubleLinkedList.addNodeAfter(7, 11);
        // System.out.println(doubleLinkedList.toString());
        // System.out.println(doubleLinkedList.deleteLast().data);
        // System.out.println(doubleLinkedList.toString());
    }
}

class Node2 {
    int data;
    Node2 next;
    Node2 prev;

    public Node2(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

class DoubleLinkedList {
    Node2 head;

    public DoubleLinkedList(Node2 head) {
        this.head = head;
    }

    public void addNodeToEnd(int data) {
        Node2 newNode = new Node2(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node2 current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
        newNode.prev = current;
    }

    public void addNodeStart(int data) {
        Node2 newNode = new Node2(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node2 currentHead = head;
        head = newNode;
        head.next = currentHead;
        currentHead.prev = head;
    }

    public void addNodeAfter(int insertAfter, int data) {
        Node2 current = head;
        while (current != null) {
            if (current.data == insertAfter) {
                Node2 node = new Node2(data);
                if (current.next != null) {
                    Node2 currentNext = current.next;
                    // Sonraki Node ile bağlıyoruz.
                    node.next = currentNext;
                    currentNext.prev = node;
                }
                // Önceki Node ile bağlıyoruz.
                current.next = node;
                node.prev = current;
                break;
            }
            current = current.next;
        }
    }

    public Node2 deleteLast() {
        Node2 current = head;
        if (head == null) {
            return head;
        }
        if (head.next == null) {
            Node2 n = head;
            head = null;
            return n;
        }
        while (current != null) {
            if (current.next.next == null) {
                Node2 deleted = current.next;
                current.next = null;
                deleted.prev = null;
                return deleted;
            }
            current = current.next;
        }
        return null;
    }

    public Node2 deleteNodeAtStart() {
        Node2 node = head;
        if (head == null || head.next == null) {
            head = null;
            return node;
        }
        head = head.next;
        head.prev = null;
        return node;

    }

    public Node2 deleteNodeAfter(int deleteAfter) {
        Node2 current = head;
        while (current != null && current.next != null) {
            if (current.data == deleteAfter) {
                Node2 n = current.next;
                if (current.next.next != null) {
                    current.next = current.next.next;
                    current.next.prev = current;
                } else {
                    current.next = null;
                }
                return n;
            }
            current = current.next;
        }
        return null;
    }

    public String toString() {
        String s = "";
        Node2 current = head;
        while (current != null) {
            s += current.data + " --> ";
            current = current.next;
        }
        return s;
    }
}
