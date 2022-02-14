package com.hycap.dbt;

public class Pair<T> {
    private T left;
    private T right;

    public void setLeft(T left) {
        this.left = left;
    }

    public void setRight(T right) {
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    public Pair(T left, T right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ", " + right.toString() + ")";
    }
}
