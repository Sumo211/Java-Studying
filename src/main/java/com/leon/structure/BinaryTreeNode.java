package com.leon.structure;

public class BinaryTreeNode {

    private int value;

    private BinaryTreeNode leftNode;

    private BinaryTreeNode rightNode;

    public BinaryTreeNode(int value) {
        this.value = value;
    }

    public BinaryTreeNode insertLeftNode(int value) {
        this.leftNode = new BinaryTreeNode(value);
        return this.leftNode;
    }

    public BinaryTreeNode insertRightNode(int value) {
        this.rightNode = new BinaryTreeNode(value);
        return this.rightNode;
    }

    public int getValue() {
        return value;
    }

    public BinaryTreeNode getLeftNode() {
        return leftNode;
    }

    public BinaryTreeNode getRightNode() {
        return rightNode;
    }

}
