class Main {
    public static void main(String[] args) {
        DoublyLinkedList<Integer> lista1 = new DoublyLinkedList<>();
        lista1.insertOrder(1);
        lista1.insertOrder(3);
        lista1.insertOrder(5);
        lista1.insert(1,2);

        lista1.add(8);
        lista1.insert(2,9);

        //DoublyLinkedList<Integer> lista2 = new DoublyLinkedList<>();
        //lista2.insertOrder(2);
        //lista2.insertOrder(4);
       // lista2.insertOrder(6);


       // System.out.println("Lista 1 antes da mesclagem: " + lista1);
        //System.out.println("Lista 2 antes da mesclagem: " + lista2);

        //lista1.mergeOrdered(lista2);
        System.out.println(lista1);
        System.out.println(lista1.reverseString());

       // System.out.println("Lista 1 após mesclagem com Lista 2: " + lista1);
    }
}

class EmptyListException extends RuntimeException {
    public EmptyListException(String errorMessage) {
        super(errorMessage);
    }
}

interface List<E> {
    int size();
    void add(E value);
    void insert(E value);
    void insert(int index, E value) throws IndexOutOfBoundsException;
    E removeLast() throws EmptyListException;
    E removeFirst() throws EmptyListException;
    E removeByIndex(int index) throws IndexOutOfBoundsException, EmptyListException;
    boolean isEmpty();
    E get(int index) throws IndexOutOfBoundsException;
    void set(int index, E value) throws IndexOutOfBoundsException;
}
// Classe interna Node para armazenar os elementos da lista e os ponteiros para os nós anterior e próximo.
class DoublyLinkedList<E extends Comparable<E>> implements List<E> {
    private class Node {
        E value;
        Node next;
        Node previous;

        Node(E value) {
            this.value = value;
        }
    }

    private Node head; // Ponteiro para o primeiro nó da lista.
    private Node tail; // Ponteiro para o último nó da lista.
    private int size; // Mantém a contagem do número de elementos na lista.


    public DoublyLinkedList() {}

    // Adiciona um novo elemento ao final da lista.
    @Override
    public void add(E value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
    }

    // Insere um novo elemento no início da lista.
    @Override
    public void insert(E value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
        }
        size++;
    }

    // Insere um elemento em uma posição específica da lista.
    @Override
    public void insert(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            insert(value);
            return;
        }
        if (index == size) {
            add(value);
            return;
        }
        Node newNode = new Node(value);
        Node current = getNode(index);
        newNode.previous = current.previous;
        newNode.next = current;
        current.previous.next = newNode;
        current.previous = newNode;
        size++;
    }

    // Remove o último elemento da lista e o retorna.
    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new EmptyListException("List is empty");
        }
        E value = tail.value;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }
        size--;
        return value;
    }

    // Remove o primeiro elemento da lista e o retorna.
    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new EmptyListException("List is empty");
        }
        E value = head.value;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return value;
    }

    // Remove um elemento de uma posição específica da lista e o retorna.
    @Override
    public E removeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();

        Node toRemove = getNode(index);
        E value = toRemove.value;
        toRemove.previous.next = toRemove.next;
        toRemove.next.previous = toRemove.previous;
        size--;
        return value;
    }

    // Retorna true se a lista estiver vazia, caso contrário, retorna false.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Retorna o elemento em uma posição específica.
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).value;
    }

    // Atualiza o elemento em uma posição específica.
    @Override
    public void set(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        getNode(index).value = value;
    }

    // Retorna o número de elementos na lista.
    @Override
    public int size() {
        return size;
    }

    // Retorna o nó em uma posição específica.
    private Node getNode(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    // Insere um elemento de maneira ordenada na lista.
    public void insertOrder(E value) {
        Node newNode = new Node(value);
        if (head == null || head.value.compareTo(value) >= 0) {
            insert(value);
        } else if (tail.value.compareTo(value) <= 0) {
            add(value);
        } else {
            Node current = head;
            while (current.next != null && current.next.value.compareTo(value) < 0) {
                current = current.next;
            }
            newNode.next = current.next;
            if (current.next != null) {
                current.next.previous = newNode;
            }
            current.next = newNode;
            newNode.previous = current;
            size++;
        }
    }

    // Mescla outra lista ordenada nesta lista, mantendo a ordem.
    public void mergeOrdered(DoublyLinkedList<E> other) {
        Node currentOther = other.head;
        while (currentOther != null) {
            insertOrder(currentOther.value);
            currentOther = currentOther.next;
        }
    }

    // Retorna uma representação em String da lista em ordem inversa.
    public String reverseString() {
        StringBuilder sb = new StringBuilder("[");
        Node auxNode = tail;
        while (auxNode != null) {
            sb.append(auxNode.value);
            if (auxNode.previous != null) {
                sb.append(", ");
            }
            auxNode = auxNode.previous;
        }
        return sb.append("]").toString();
    }


    // Retorna uma representação em String da lista.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.value.toString());
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
