package sample;

import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GLOperations<T extends Comparable<T>> {

    static final int ROOT_ELLIPSE_X = 400; // Constant x-coordinate for the root node
    static final int ROOT_ELLIPSE_Y = 80; // Constant y-coordinate for the root node

    private static final int ROOT_IS_BIGGER = 1;
    private static final int ROOT_IS_SMALLER = -1;

    Pane pane;

    // List to keep a track of JavaFX nodes
    ArrayList<CustomNode> nodes = new ArrayList<>();

    private Node root;      // Tree root
    private int size;       // The number of tree elements
    private boolean flag;   // Tracks if the last element was added correctly or not.

    GLOperations(Pane pane) {
        this.root = new Node();
        this.size = 0;
        this.pane = pane;
    }

    // Method to clear the current JavaFX nodes list
    void clearNodes() {
        nodes.clear();
    }

    // Test method
    void showList() {
        for (CustomNode node : nodes) {
            System.out.print(node.leftKey.getText());
            if (node.rightKey != null)
                System.out.println(" - " + node.rightKey.getText());
            else
                System.out.print("->");
            if (node.hasLeftChild)
                System.out.print(node.leftChild.leftKey.getText() + "L, ");
            if (node.hasMiddleChild)
                System.out.print(node.middleChild.leftKey.getText() + "M, ");
            if (node.hasRightChild)
                System.out.println(node.rightChild.leftKey.getText() + "R, ");
            else System.out.println();
        }
    }

    // Method to recursively create a JavaFX tree from nodes in the code
    void createDisplay(Node root) {
        if (root != null) {
            CustomNode toAdd = new CustomNode(root);
            nodes.add(toAdd);
            if (root.rightElement != null) {
                if (toAdd.hasLeftChild) {
                    createDisplay(root.leftChild);
                }
                if (toAdd.hasMiddleChild) {
                    createDisplay(root.middleChild);
                }
                if (toAdd.hasRightChild) {
                    createDisplay(root.rightChild);
                }
            } else {
                if (toAdd.hasLeftChild) {
                    createDisplay(root.leftChild);
                }
                if (toAdd.hasRightChild) {
                    createDisplay(root.middleChild);
                }
            }
        }
        display();
    }

    // Method to call the createDisplay() method
    void callCreateDisplay() {
        createDisplay(root);
    }

    // Helper method for displaying the tree
    void display() {
        setEllipses(root, ROOT_ELLIPSE_X + 80, ROOT_ELLIPSE_Y - 80);
        pane.getChildren().clear();
        for (CustomNode node : nodes)
            node.display(pane);
    }

    // Method to recursively set ellipse positions of the whole tree
    void setEllipses(Node root, double x, double y) {
        for (CustomNode node : nodes) {
            if (node.leftKey.getText().equals(root.leftElement.toString())) {
                // The root's children must be set wider than interior nodes' children
                if (x == ROOT_ELLIPSE_X + 80 && y == ROOT_ELLIPSE_Y - 80) {
                    node.ellipse.setCenterX(x - 80);
                    node.ellipse.setCenterY(y + 80);
                    // If there are two keys
                    if (node.rightKey != null)
                        node.expandNodeAndSetRightKey(node.rightKey.getText());
                    if (root.rightElement != null) {
                        if (root.hasLeftChild())
                            setEllipses(root.leftChild, node.ellipse.getCenterX() - 160, node.ellipse.getCenterY() - 30);
                        if (root.hasMiddleChild())
                            setEllipses(root.middleChild, node.ellipse.getCenterX() + 80, node.ellipse.getCenterY() - 30);
                        if (root.hasRightChild())
                            setEllipses(root.rightChild, node.ellipse.getCenterX() + 320, node.ellipse.getCenterY() - 30);
                    } else {
                        if (root.hasLeftChild())
                            setEllipses(root.leftChild, node.ellipse.getCenterX() - 160, node.ellipse.getCenterY() - 30);
                        if (root.hasMiddleChild())
                            setEllipses(root.middleChild, node.ellipse.getCenterX() + 320, node.ellipse.getCenterY() - 30);
                    }
                }
                // For interior nodes
                else {
                    node.ellipse.setCenterX(x - 80);
                    node.ellipse.setCenterY(y + 80);
                    if (node.rightKey != null)
                        node.expandNodeAndSetRightKey(node.rightKey.getText());
                    if (root.rightElement != null) {
                        if (root.hasLeftChild())
                            setEllipses(root.leftChild, node.ellipse.getCenterX(), node.ellipse.getCenterY());
                        if (root.hasMiddleChild())
                            setEllipses(root.middleChild, node.ellipse.getCenterX() + 80, node.ellipse.getCenterY());
                        if (root.hasRightChild())
                            setEllipses(root.rightChild, node.ellipse.getCenterX() + 160, node.ellipse.getCenterY());
                    } else {
                        if (root.hasLeftChild())
                            setEllipses(root.leftChild, node.ellipse.getCenterX(), node.ellipse.getCenterY());
                        if (root.hasMiddleChild())
                            setEllipses(root.middleChild, node.ellipse.getCenterX() + 160, node.ellipse.getCenterY());
                    }
                }
            }
        }
    }

    int getListSize() {
        return nodes.size();
    }

    int getHeight() {
        return height(root);
    }

    /**
     * Adds a new element to the tree, keeping it balanced
     *
     * @param element - element to add
     */
    public void add(T element) {
        flag = false;

        if (root == null || root.getLeftElement() == null) {
            flag = true;

            if (root == null) {
                root = new Node();
            }

            root.setLeftElement(element);
        } else {
            Node newRoot = add(root, element);
            if (newRoot != null) {
                root = newRoot;
            }
        }
        if (flag) size++;

    }

    // Method to calculate height of the tree
    int height(Node node) {
        if (node == null)
            return 0;
        else
            return height(node.leftChild) + 1;
    }

    /**
     * @param current node to add to
     * @param element - element to add
     */
    private Node add(Node current, T element) {

        Node newParent = null; // sample.Node to be added

        // We are not yet at the deepest level
        if (!current.isLeaf()) {

            Node newNode;

            // Element already exists
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
            }

            // newNode < left element
            else if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                newNode = add(current.leftChild, element);

                // newNode comes from the left branch
                if (newNode != null) {

                    // newNode < than current.left
                    if (current.is2Node()) {
                        current.rightElement = current.leftElement; // Move the current left element to the right
                        current.leftElement = newNode.leftElement;
                        current.rightChild = current.middleChild;
                        current.middleChild = newNode.middleChild;
                        current.leftChild = newNode.leftChild;
                    }

                    // We have a new division, so the current element on the left will rise
                    else {

                        // Copy the right side of the subtree
                        Node rightCopy = new Node(current.rightElement, null, current.middleChild, current.rightChild);

                        // Create a new "structure" by inserting the right side
                        newParent = new Node(current.leftElement, null, newNode, rightCopy);
                    }
                }
            }

            // newNode is > left and < right
            else if (current.is2Node() || (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_BIGGER)) {
                newNode = add(current.middleChild, element);

                // New division
                if (newNode != null) {

                    // The right element is empty, so we can set newNode on the left, and the existing left element on the right
                    if (current.is2Node()) {
                        current.rightElement = newNode.leftElement;
                        current.rightChild = newNode.middleChild;
                        current.middleChild = newNode.leftChild;
                    }

                    // Another case where we have to split again
                    else {
                        Node left = new Node(current.leftElement, null, current.leftChild, newNode.leftChild);
                        Node mid = new Node(current.rightElement, null, newNode.middleChild, current.rightChild);
                        newParent = new Node(newNode.leftElement, null, left, mid);
                    }
                }
            }

            // newNode is larger than the right element
            else if (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {

                newNode = add(current.rightChild, element);

                // Divide -> the right element rises
                if (newNode != null) {
                    Node leftCopy = new Node(current.leftElement, null, current.leftChild, current.middleChild);
                    newParent = new Node(current.rightElement, null, leftCopy, newNode);
                }
            }
        }

        // We are at the deepest level
        else {
            flag = true;

            // Element already exists
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {
                flag = false;
            }

            // The case when there is no right element
            else if (current.is2Node()) {

                // If the current left element is larger than newNode, we move the left element to the right
                if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
                    current.rightElement = current.leftElement;
                    current.leftElement = element;
                }

                // If newNode is larger, we add it to the right
                else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) {
                    current.rightElement = element;
//                    nodes.get(findNodeIndex(current.leftElement.toString())).expandNodeAndSetRightKey(element.toString());
                }
            }

            // The case when the node has 2 elements, and we want to add another one. To do this, we share the node
            else newParent = split(current, element);
        }

        return newParent;
    }

    /**
     * The method creates a new node structure that will be attached at the bottom of the add() method
     *
     * @param current - the node where the separation occurs
     * @param element - element to insert
     * @return two-node structure with a nonzero left and middle node
     */
    private Node split(Node current, T element) {
        Node newParent = null;
        CustomNode newParentFigure;

        // The left element is larger, so it will rise, allowing newParent to stand on the left
        if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {
            Node<T> left = new Node<>(element, null);
            Node right = new Node(current.rightElement, null);
            newParent = new Node(current.leftElement, null, left, right);

        } else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) {
            Node left = new Node(current.leftElement, null);

            // newParent is greater than the current on the right and smaller than the right. newParent rises.
            if (current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {
                Node right = new Node(current.rightElement, null);
                newParent = new Node(element, null, left, right);
            }

            // newParent is the largest, so the current right element is raised
            else {
                Node<T> right = new Node<>(element, null);

                newParent = new Node(current.rightElement, null, left, right);
            }
        }

        return newParent;
    }

    // Method to find an element in the tree
    void find(String toFind) {
        int nodeIndex = -1;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).leftKey.getText().equals(toFind)) {
                if (nodes.get(i).rightKey != null)
                    new Alert(Alert.AlertType.INFORMATION, "Found as the left key of the red node!").show();
                nodeIndex = i;
            } else if (nodes.get(i).rightKey != null && nodes.get(i).rightKey.getText().equals(toFind)) {
                new Alert(Alert.AlertType.INFORMATION, "Found as the right key of the red node!").show();
                nodeIndex = i;
            }
        }
        if (nodeIndex != -1) {
            for (int i = 0; i < nodes.size(); i++) {
                if (i == nodeIndex)
                    nodes.get(i).ellipse.setStroke(Color.RED);
                else
                    nodes.get(i).ellipse.setStroke(Color.DEEPSKYBLUE);
            }
        } else
            new Alert(Alert.AlertType.WARNING, "Number not found!").show();
    }

    /**
     * Method for removing an element from the tree
     *
     * @param element - element to remove
     * @return true, if the element was removed, otherwise false
     */
    public boolean remove(T element) {

        // Reduce the number of levels at the beginning
        this.size--;

        boolean ifRemoved = remove(root, element);

        root.reBalance();

        // If you deleted the last element of the tree
        if (root.getLeftElement() == null) root = null;

        // If the element could not be deleted, then increase the number of levels
        if (!ifRemoved) {
            new Alert(Alert.AlertType.ERROR, "Element could not be found!").show();
            this.size++;
        }

        return ifRemoved;
    }

    /**
     * @param current - node to be deleted
     * @param element - element to be deleted
     * @return true, if the element was deleted, otherwise false
     */
    private boolean remove(Node current, T element) {
        boolean ifRemoved = true;

        // The case when we are at the deepest level of the tree, but we did not find the element (it does not exist)
        if (current == null) {
            ifRemoved = false;
            return false;
        }

        // Recursive case, we still find the element to delete
        else {
            if (!current.getLeftElement().equals(element)) {

                // If there is no element on the right or the element to be deleted is smaller than the right element
                if (current.getRightElement() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                    // The left element is larger than the element to be deleted, so we go through the left child element
                    if (current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
                        ifRemoved = remove(current.leftChild, element);
                    }

                    // Otherwise -> try to remove the middle child
                    else {
                        ifRemoved = remove(current.middleChild, element);
                    }

                } else {

                    // If the element to be deleted is not equal to the desired element, we pass the right child
                    if (!current.getRightElement().equals(element)) {
                        ifRemoved = remove(current.rightChild, element);
                    }

                    // Otherwise, we found an element
                    else {

                        // *** Situation 1 ***
                        // The element is equal to the right element of the sheet, so we just delete it
                        if (current.isLeaf()) {
                            current.setRightElement(null);
                        }

                        // *** Situation 2 ***
                        // We found the element, but it is not in the sheet
                        else {

                            // We get the min element of the right branch,
                            // delete it from the current position and place it where we found the element to delete.
                            T replacement = (T) current.getRightNode().replaceMin();
                            current.setRightElement(replacement);
                        }
                    }
                }
            }

            // The left element is equal to the element to be deleted.
            else {

                // *** Situation 1 ***
                if (current.isLeaf()) {

                    // The left element, the element to delete, is replaced by the right element
                    if (current.getRightElement() != null) {
                        current.setLeftElement(current.getRightElement());
                        current.setRightElement(null);

                    }

                    // If there is no element on the right, then balancing is required
                    else {
                        current.setLeftElement(null); // Release the node
                        return true;
                    }
                }

                // *** Situation 2 ***
                else {

                    // Move the "max" element of the left branch, where we found the element
                    T replacement = (T) current.getLeftNode().replaceMax();
                    current.setLeftElement(replacement);
                }
            }
        }

        // The lower level must be balanced
        if (!current.isBalanced()) {
            current.reBalance();

        } else if (!current.isLeaf()) {
            boolean isBalanced = false;

            while (!isBalanced) {
                if (current.getRightNode() == null) {

                    // A critical case of situation 2 for the left child
                    if (current.getLeftNode().isLeaf() && !current.getMidNode().isLeaf()) {
                        T replacement = (T) current.getMidNode().replaceMin();
                        T tempLeft = (T) current.getLeftElement();
                        current.setLeftElement(replacement);

                        add(tempLeft);
                    }

                    // A critical case of situation 2 for the right child
                    else if (!current.getLeftNode().isLeaf() && current.getMidNode().isLeaf()) {
                        if (current.getRightElement() == null) {
                            T replacement = (T) current.getLeftNode().replaceMax();
                            T tempLeft = (T) current.getLeftElement();
                            current.setLeftElement(replacement);

                            add(tempLeft);
                        }
                    }
                }

                if (current.getRightNode() != null) {
                    if (current.getMidNode().isLeaf() && !current.getRightNode().isLeaf()) {
                        current.getRightNode().reBalance();
                    }

                    if (current.getMidNode().isLeaf() && !current.getRightNode().isLeaf()) {
                        T replacement = (T) current.getRightNode().replaceMin();
                        T tempRight = (T) current.getRightElement();
                        current.setRightElement(replacement);

                        add(tempRight);
                    } else {
                        isBalanced = true;
                    }
                }

                if (current.isBalanced()) isBalanced = true;
            }
        }

        return ifRemoved;
    }

}
