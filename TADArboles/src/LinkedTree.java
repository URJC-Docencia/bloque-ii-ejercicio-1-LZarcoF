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
public class LinkedTree<E> extends DrawableTree<E> {

    /**
     * The root of the tree.
     */
    private TreeNode<E> root;

    /**
     * The number of nodes in the tree.
     */
    private int size;

    /**
     * Check if a given position is valid for the children list of a TreeNode.
     *
     * @param n      The position to check
     * @param parent The parent TreeNode
     * @throws RuntimeException If the position is invalid
     */
    private static <E> void checkPositionOfChildrenList(int n, LinkedTree<E>.TreeNode<E> parent) {
        if (n < 0 || n > parent.getChildren().size()) {
            throw new RuntimeException("The position is invalid");
        }
    }

    @Override
    public Position<E> addRoot(E e) {
        if (!isEmpty()) {
            throw new RuntimeException("The tree already has a root");
        }
        root = new TreeNode<>(e);
        size++;
        return root;
    }


    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode<>(element, parent);
        parent.getChildren().add(newNode);
        size++;
        return newNode;
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
            throw new RuntimeException("The position is invalid");
        }
        return (TreeNode<E>) p;
    }


    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode<>(element, parent);
        checkPositionOfChildrenList(n, parent);
        parent.getChildren().add(n, newNode);
        size++;
        return newNode;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.element = node2.getElement();
        node2.element = aux;
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
        TreeNode<E> node = checkPosition(p);
        if (node == root) {
            root = null;
            size = 0;
        } else {
            TreeNode<E> parent = node.getParent();
            parent.getChildren().remove(node);
            // Now we have to compute the size of the subtree rooted at the current node
            size -= computeSize(node);
        }
    }

    /**
     * Compute the size of a TreeNode and all its children.
     *
     * @param node The TreeNode to compute size for
     * @return The size of the TreeNode and its children
     */
    private int computeSize(TreeNode<E> node) {
        int size = 1;
        for (TreeNode<E> child : node.getChildren()) {
            size += computeSize(child);
        }
        return size;
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        LinkedTree<E> tree = new LinkedTree<>();
        tree.root = node;
        tree.size = computeSize(node);
        return tree;
    }

    /**
     * Attach tree t as children of node p if t and "this" are of the same class.
     *
     * @param p Node in which t will be attached or null if t is attached in the root.
     * @param t Tree to be attached.
     */
    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        TreeNode<E> node = checkPosition(p);
        LinkedTree<E> tree = checkTree(t);
        node.getChildren().addAll(tree.root.getChildren());
        size += tree.size;

    }

    /**
     * Checks if the given tree is of type LinkedTree.
     *
     * @param t The tree to be checked.
     * @return The LinkedTree instance if the tree is of type LinkedTree.
     * @throws RuntimeException If the tree is not a valid LinkedTree instance.
     */
    private LinkedTree<E> checkTree(NAryTree<E> t) {
        if (!(t instanceof LinkedTree)) {
            throw new RuntimeException("The tree is invalid");
        }
        return (LinkedTree<E>) t;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getChildren();
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return !node.getChildren().isEmpty();
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getChildren().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node == root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        if (isEmpty()) {
            // empty iterator
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> positions = new ArrayList<>();
        breadthFirstTraversal(root, positions);
        return positions.iterator();
    }

    public Iterator<Position<E>> iteratorPreOrder() {
        if (isEmpty()) {
            // empty iterator
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> positions = new ArrayList<>();
        preOrderTraversal(root, positions);
        return positions.iterator();
    }

    public Iterator<Position<E>> iteratorPostOrder() {
        if (isEmpty()) {
            // empty iterator
            return new ArrayList<Position<E>>().iterator();
        }
        List<Position<E>> positions = new ArrayList<>();
        postOrderTraversal(root, positions);
        return positions.iterator();
    }

    private void breadthFirstTraversal(TreeNode<E> node, List<Position<E>> positions) {
        if (node != null) {
            List<TreeNode<E>> queue = new ArrayList<>();
            queue.add(node);
            while (!queue.isEmpty()) {
                TreeNode<E> nodeToVisit = queue.remove(0);
                positions.add(nodeToVisit);
                queue.addAll(nodeToVisit.getChildren());
            }
        }
    }


    private void postOrderTraversal(TreeNode<E> node, List<Position<E>> positions) {
        if (node != null) {
            for (TreeNode<E> child : node.getChildren()) {
                postOrderTraversal(child, positions);
            }
            positions.add(node);
        }
    }

    private void preOrderTraversal(TreeNode<E> node, List<Position<E>> positions) {
        if (node != null) {
            positions.add(node);
            for (TreeNode<E> child : node.getChildren()) {
                preOrderTraversal(child, positions);
            }
        }
    }

    /**
     * Return the number of elements stored in the tree.
     *
     * @return the number of elements in the tree
     */
    public int size() {
        return size;
    }

    /**
     * This class represents a node in a tree data structure.
     * It implements the Position interface.
     *
     * @param <T> the type of element stored in the node
     */
    private class TreeNode<T> implements Position<T> {


        /**
         * The list of children of a TreeNode in a tree structure.
         */
        private final List<TreeNode<T>> children = new ArrayList<>();
        /**
         * Holds a reference to an element of type T.
         */
        private T element;
        /**
         * The parent of a TreeNode in a tree structure.
         */
        private TreeNode<T> parent;


        /**
         * Constructs a TreeNode with the specified element.
         *
         * @param element the element to be stored in the node
         */
        public TreeNode(T element) {
            this.element = element;
        }

        /**
         * Constructs a TreeNode with the specified element and parent.
         *
         * @param element the element to be stored in the node
         * @param parent  the parent of the node
         */
        public TreeNode(T element, TreeNode<T> parent) {
            this.element = element;
            this.parent = parent;
        }


        @Override
        public T getElement() {
            return element;
        }

        /**
         * Returns the parent node of this TreeNode.
         *
         * @return the parent node of this TreeNode
         */
        public TreeNode<T> getParent() {
            return parent;
        }

        /**
         * Returns a list of child nodes of this tree node.
         *
         * @return a list of child nodes
         */
        public List<TreeNode<T>> getChildren() {
            return children;
        }

    }


}
