public class LinkedList1 {
    public static void main(String[] args) {
        Node node1 = new Node(1);
        LinkedList linkedlist = new LinkedList(node1);
        linkedlist.addToEnd(2);
        linkedlist.addToEnd(3);
        linkedlist.addToEnd(4);
        System.out.println(linkedlist.toString());
        System.out.println(linkedlist.deleteNodeAfter(5));
        System.out.println(linkedlist.toString());
        System.out.println(linkedlist.deleteNodeAfter(4));
        System.out.println(linkedlist.toString());
        // System.out.println(linkedlist.toString());
        // linkedlist.addToHead(10);
        // System.out.println(linkedlist.toString());
        // linkedlist.addNodeAfter(1, 11);
        // System.out.println(linkedlist.toString());
        // linkedlist.addNodeAfter(4, 15);
        // System.out.println(linkedlist.toString());
        // linkedlist.addNodeAfter(10, 0);
        // System.out.println(linkedlist.toString());
        // linkedlist.addNodeAfter(150, 0);
        // System.out.println(linkedlist.toString());
        // System.out.println(linkedlist.deleteLast().data);
        // System.out.println(linkedlist.toString());
        // System.out.println(linkedlist.deleteAtStart().data);
        // System.out.println(linkedlist.toString());
        // // System.out.println(linkedlist);
        // linkedlist.deleteNodeAfter(2);
        // System.out.println(linkedlist.toString());
        // linkedlist.deleteNodeAfter(4);
        // System.err.println(linkedlist.toString());
    }
}

class Node {
    int data;
    Node next;

    public Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    Node head;

    public LinkedList(Node head) {
        this.head = head;
    }

    public void addToEnd(int data) {
        Node n = new Node(data);
        if (head == null) {
            head = n;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = n;
        }
    }

    public void addToHead(int data) {
        Node node = new Node(data);
        node.next = head;
        head = node;

    }

    public void addNodeAfter(int insertAfter, int data) {
        Node node = new Node(data);
        Node current = head;
        while (current != null) {
            if (current.data == insertAfter) {
                node.next = current.next;
                current.next = node;
            }
            current = current.next;
        }

    }

    public Node deleteNodeAfter(int deleteData) {
        Node current = head;
        Node node = null;
        while (current != null && current.next != null) {
            if (current.data == deleteData) {
                node = current.next;
                current.next = current.next.next;
                break;
            }
            current = current.next;
        }
        return node;
    }

    public Node deleteLast() {
        Node current = head;
        if (current == null || current.next == null) {
            head = null;
            if (current == null) {
                return null;
            }
            return current;
        }
        Node node = null;
        while (current.next != null) {
            if (current.next.next == null) {
                node = current.next;
                break;
            }
            current = current.next;
        }

        current.next = null;
        return node;

    }

    public Node deleteAtStart() {
        if (head != null) {
            Node n = head;
            head = head.next;
            return n;
        }
        return null;
    }

    public String toString() {
        String s = "";
        Node current = head;
        while (current != null) {
            s += String.valueOf(current.data) + " --> ";
            current = current.next;
        }
        return s;
    }

}
