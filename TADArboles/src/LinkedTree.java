import material.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents a tree data structure using a linked implementation.
 * It implements the NAryTree interface.
 *
 * @param <E> the type of element stored in the tree
 */
public class LinkedTree<E> implements NAryTree<E> {

    /**
     * This class represents a node in a tree data structure.
     * It implements the Position interface.
     *
     * @param <T> the type of element stored in the node
     */
    private int size;
    private TreeNode<E> root;

    private static <E> void checkPositionOfChildrenList(int n, LinkedTree<E>.TreeNode<E> parent) {
        if (n < 0 || n > parent.getChildren().size()) {
            throw new RuntimeException("The position is invalid");
        }
    }

    @Override
    public Position<E> addRoot(E e) {
        if (!isEmpty())
            throw new RuntimeException("El arbol ya tiene raiz");
        root = new TreeNode<>(e);
        size++;
        return root;
    }

    /**
     * Check if a given position is valid and return the corresponding TreeNode.
     *
     * @param p The position to check
     * @return The corresponding TreeNode
     * @throws RuntimeException If the position is invalid
     */
    private TreeNode<E> checkPosition(Position<E> p) {
        if (!(p instanceof TreeNode)) {
            throw new RuntimeException("Posicion invalida");
        }
        return (TreeNode<E>) p;
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> nodoAux = new TreeNode<>(element, parent);
        parent.getChildren().add(nodoAux);
        size++;
        return nodoAux;
    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        TreeNode<E> parent = checkPosition(p);
        checkPositionOfChildrenList(n, parent);
        TreeNode<E> nodoAux = new TreeNode<>(element, parent);
        parent.getChildren().add(n, nodoAux);
        size++;
        return nodoAux;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> nodo1 = checkPosition(p1);
        TreeNode<E> nodo2 = checkPosition(p2);
        E aux = nodo1.getElement();
        nodo1.element = nodo2.getElement();
        nodo2.element = aux;
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = checkPosition(p);
        E old = node.getElement();
        node.element = e;
        return old;
    }

    @Override
    public void remove(Position<E> p) {
        TreeNode<E> nodo = checkPosition(p);
        if (isRoot(p)) {
            root = null;
            size = 0;
        } else {
            TreeNode<E> parent = nodo.getParent();
            parent.getChildren().remove(nodo);
            size -= computeSize(nodo);
        }
    }

    private int computeSize(TreeNode<E> n) {
        int size = 1;
        for (TreeNode<E> child : n.getChildren()) {
            size += computeSize(child);
        }
        return size;
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        TreeNode<E> nodo = checkPosition(v);
        LinkedTree<E> nuevoArbol = new LinkedTree<E>();
        nuevoArbol.root = nodo;
        nuevoArbol.size = computeSize(nodo);
        return nuevoArbol;
    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInternal(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRoot(Position<E> v) {
        return v == root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        //comprobar si esta vacio
        List<Position<E>> positions = new ArrayList<>();
        breathOrder(root, positions);
        return positions;
    }

    private void breathOrder(TreeNode<E> nodo, List<Position<E>> positions) {
        if (root != null) {
            List<TreeNode> queue = new ArrayList<>();
            queue.add(nodo);
            while (!queue.isEmpty()) {
                TreeNode<E> toExplore = queue.remove(0);
                positions.add(toExplore);
                queue.addAll(nodo.getChildren());
            }
        }
    }

    public int size() {
        return size;
    }

    private class TreeNode<T> implements Position<T> {
        private List<TreeNode<T>> children = new ArrayList<>();
        private T element;
        private TreeNode<T> parent;

        public TreeNode(T element) {
            this.element = element;
        }

        public TreeNode(T element, TreeNode<T> parent) {
            this.element = element;
            this.parent = parent;
        }

        @Override
        public T getElement() {
            return element;
        }

        public List<TreeNode<T>> getChildren() {
            return children;
        }

        public TreeNode<T> getParent() {
            return parent;
        }
    }
}
