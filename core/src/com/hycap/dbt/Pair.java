package com.hycap.dbt;

public class Pair<T> {
    private T left;
    private T right;

    public void setLeft(final T left) {
        this.left = left;
    }

    public void setRight(final T right) {
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    public Pair(final T left, final T right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Pair) {
            final Pair pair = (Pair)o;
            return pair.getLeft().equals(left) && pair.getRight().equals(right);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ", " + right.toString() + ")";
    }
}
