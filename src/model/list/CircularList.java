/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

import model.Card;

/**
 *
 * @author Alan Tian
 */
public class CircularList<E> implements AbstractList<E> {

    protected CircularNode tail;
    protected int nodeCount;

    public CircularList() {
        tail = null;
        nodeCount = 0;
    }

    public CircularList(CircularNode tail, int nodeCount) {
        this.tail = tail;
        this.nodeCount = nodeCount;
    }

    public CircularNode<E> getTailNode() {
        return tail;
    }

    public void setSize(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public CircularNode<E> getNode(int i) {
        if (i < 0 || i >= nodeCount) {
            return null;
        }
        CircularNode<E> currentNode = tail.next();
        while (i > 0) {
            currentNode = currentNode.next();
            i--;
        }
        return currentNode.next();
    }

    @Override
    public int indexOf(E e) {
        if (tail == null) {
            return -1;
        } else {
            CircularNode<E> finger = getHead();
            int index = 0;
            while (index < nodeCount) {
                if (finger.value() == e) {
                    return index;
                } else {
                    finger = finger.next();
                    index++;
                }
            }
            return -1;
        }
    }

    @Override
    public int size() {
        return nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return nodeCount == 0;
    }

    @Override
    public E get(int i) {
        if (i < 0 || i >= nodeCount) {
            return null;
        } else {
            return (E) getNode(i).value();
        }
    }

    @Override
    public boolean set(int i, E e) {
        if (i < 0 || i >= nodeCount) {
            return false;
        } else {
            getNode(i).setValue(e);
            return true;
        }
    }

    @Override
    public boolean add(int i, E e) {
        if (i < 0 || i > nodeCount) {
            return false;
        } else {
            if (tail == null) {
                if (i > 0) {
                    return false;
                } else {
                    CircularNode<E> newNode = new CircularNode<E>(e, null, null);
                    newNode.setNext(newNode);
                    newNode.setPrev(newNode);
                    tail = new CircularNode<E>(null, null, newNode);
                }
            } else {
                if (i < nodeCount) {
                    CircularNode<E> finger = getNode(i);
                    CircularNode<E> newNode = new CircularNode<E>(e, finger.prev(), finger);
                    finger.prev().setNext(newNode);
                    finger.setPrev(newNode);
                } else {
                    CircularNode<E> newNode = new CircularNode<E>(e, tail.next(), getHead());
                    getHead().setPrev(newNode);
                    tail.next().setNext(newNode);
                    tail.setNext(newNode);
                }
            }
            nodeCount++;
            return true;
        }
    }

    @Override
    public boolean contains(E e) {
        CircularNode<E> finger = tail;
        while (finger != null) {
            if (finger.value() == e) {
                return true;
            } else {
                finger = finger.prev();
            }
        }
        return false;
    }

    protected CircularNode<E> getHead() {
        if (nodeCount > 0) {
            return tail.next().next();
        } else {
            return null;
        }
    }
    
    protected void remove(CircularNode<E> node){
        if(nodeCount == 1)
            tail.setNext(null);
        else{
            node.prev().setNext(node.next());
            node.next().setPrev(node.prev());
            if (node == tail.next())
                tail.setNext(node.prev());
        }
    }

    @Override
    public E remove(E e) {
        CircularNode<E> finger = getHead();
        int index = 0;
        while (index < nodeCount) {
            if (finger.value() == e) {
                break;
            } else {
                finger = finger.next();
                index++;
            }
        }
        if (index == nodeCount) {
            return null;
        }

        nodeCount--;
        if (nodeCount == 0) {
            tail = null;
        } else {
            finger.prev().setNext(finger.next());
            finger.next().setPrev(finger.prev());
        }
        return e;
    }

    @Override
    public E remove(int i) {
        if (i < 0 || i >= nodeCount) {
            return null;
        }
        CircularNode<E> finger = getNode(i);
        E e = finger.value();
        if (nodeCount > 1) {
            tail.setNext(finger.prev());
            finger.prev().setNext(finger.next());
            finger.next().setPrev(finger.prev());
        } else {
            tail = null;
        }
        nodeCount--;
        return e;
    }

    public void add(E e) {
        add(nodeCount, e);
    }

    public void setTailNode(CircularNode tail) {
        this.tail = tail;
    }
}
