package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

// JavaFX equivalent of a node
public class CustomNode {
    CustomNode leftChild, middleChild, rightChild;
    Ellipse ellipse = new Ellipse(20, 20);
    Text leftKey, rightKey;

    Line left, middle, right;
    boolean hasLeftChild, hasMiddleChild, hasRightChild;

    // Constructor to visualise a node from the code
    CustomNode(Node root) {
        leftKey = new Text(root.leftElement.toString());
        if (root.rightElement != null) {
            expandNodeAndSetRightKey(root.rightElement.toString());
            if (root.hasLeftChild()) {
                this.leftChild = new CustomNode(root.leftChild);
                this.hasLeftChild = true;
            }
            if (root.hasMiddleChild()) {
                this.middleChild = new CustomNode(root.middleChild);
                this.hasMiddleChild = true;
            }
            if (root.hasRightChild()) {
                this.rightChild = new CustomNode(root.rightChild);
                this.hasRightChild = true;
            }
        } else {
            if (root.hasLeftChild()) {
                this.leftChild = new CustomNode(root.leftChild);
                this.hasLeftChild = true;
            }
            if (root.hasMiddleChild()) {
                this.rightChild = new CustomNode(root.middleChild);
                this.hasRightChild = true;
            }
        }
        ellipse.setFill(Color.LIGHTGREEN.brighter());
        ellipse.setStroke(Color.DEEPSKYBLUE);
        ellipse.setStyle("-fx-stroke-width: 5.0");
    }

    void setTextCoordinates() {
        if (leftKey != null) {
            leftKey.setX(ellipse.getCenterX() - 3);
            leftKey.setY(ellipse.getCenterY() + 3);
            leftKey.setFill(Color.DEEPSKYBLUE);
            leftKey.setStyle("-fx-font-size: 15.0; -fx-font-weight: bold");
        }
        if (rightKey != null) {
            assert leftKey != null;
            leftKey.setX(leftKey.getX() - 20);
            rightKey.setX(ellipse.getCenterX() + 10);
            rightKey.setY(ellipse.getCenterY() + 3);
            rightKey.setFill(Color.DEEPSKYBLUE);
            rightKey.setStyle("-fx-font-size: 15.0; -fx-font-weight: bold");
        }
    }

    void setLineCoordinates() {
        if (ellipse.getCenterX() == GLOperations.ROOT_ELLIPSE_X && ellipse.getCenterY() == GLOperations.ROOT_ELLIPSE_Y) {
            left = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX() - 240, ellipse.getCenterY() + 30);
            middle = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX(), ellipse.getCenterY() + 30);
            right = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX() + 240, ellipse.getCenterY() + 30);
        } else {
            left = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX() - 80, ellipse.getCenterY() + 80);
            middle = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX(), ellipse.getCenterY() + 80);
            right = new Line(ellipse.getCenterX(), ellipse.getCenterY() + ellipse.getRadiusY(), ellipse.getCenterX() + 80, ellipse.getCenterY() + 80);
        }
        left.setStyle("-fx-stroke: deepskyblue; -fx-stroke-width: 5.0");
        middle.setStyle("-fx-stroke: deepskyblue; -fx-stroke-width: 5.0");
        right.setStyle("-fx-stroke: deepskyblue; -fx-stroke-width: 5.0");
    }

    void expandNodeAndSetRightKey(String rightKey) {
        ellipse.setRadiusX(40);
        this.rightKey = new Text(rightKey);
        this.rightKey.setX(leftKey.getX() + 20);
        this.rightKey.setY(leftKey.getY());
    }

    void display(Pane pane) {
        setLineCoordinates();
        setTextCoordinates();
        if (rightKey != null)
            pane.getChildren().addAll(ellipse, leftKey, rightKey);
        else if (leftKey != null)
            pane.getChildren().addAll(ellipse, leftKey);
        if (hasLeftChild && hasMiddleChild && hasRightChild)
            pane.getChildren().addAll(left, middle, right);
        else if (!hasMiddleChild && hasLeftChild && hasRightChild)
            pane.getChildren().addAll(left, right);

    }

}
