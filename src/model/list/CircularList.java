/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

import model.Card;

/**
 *
 * @author Alan Tian 1302662
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

    public CircularNode<E> getTail() {
        return tail;
    }

    public void setSize(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public CircularNode<E> getHead() {
        if (tail != null) {
            return tail.getNext();
        } else {
            return null;
        }
    }

    public CircularNode<E> getNode(int i) {
        if (i < 0 || i >= nodeCount || tail == null) {
            return null;
        }
        CircularNode<E> curNode = tail;
        while (i > 0) {
            curNode = curNode.getNext();
            i--;
        }
        return curNode.getNext();
    }

    @Override
    public int indexOf(E e) {
        if (tail == null) {
            return -1;
        } else {
            CircularNode<E> finger = tail.getNext();
            int index = 0;
            while (index < nodeCount) {
                if (finger.getValue() == e) {
                    return index;
                } else {
                    finger = finger.getNext();
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
            return (E) getNode(i).getValue();
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
                    CircularNode<E> newNode = new CircularNode<E>(e, null);
                    newNode.setNext(newNode);
                    tail = newNode;
                }
            } else {
                if (i < nodeCount) {
                    CircularNode<E> prev;
                    prev = tail;
                    while (i > 0) {
                        prev = prev.getNext();
                        i--;
                    }
                    CircularNode<E> newNode = new CircularNode<E>(e, prev.getNext());
                    prev.setNext(newNode);
                } else {
                    CircularNode<E> newNode = new CircularNode<E>(e, tail.getNext());
                    tail.setNext(newNode);
                    tail = newNode;
                }
            }
            nodeCount++;
            return true;
        }
    }

    @Override
    public boolean contains(E e) {
        CircularNode<E> finger = tail;
        int i = 0;
        while (i < nodeCount) {
            if (finger.getValue() == e) {
                return true;
            } else {
                finger = finger.getNext();
            }
            i++;
        }
        return false;
    }

    @Override
    public E remove(E e) {
        if (tail == null) {
            return null;
        }

        CircularNode<E> finger = tail;
        int index = 0;
        while (index < nodeCount) {
            if (finger.getNext().getValue() == e) {
                break;
            } else {
                finger = finger.getNext();
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
            finger.setNext(finger.getNext().getNext());
        }
        return e;
    }

    public E removeTail(){
        if(tail == null)
            return null;
        E e = (E) tail.getValue();
        nodeCount--;
        if(nodeCount>0){
            CircularNode<E> head = tail.getNext();
            tail = getNode(nodeCount-1);
            tail.setNext(head);
        }
        else
            tail = null;
        return e;
    }
    @Override
    public E remove(int i) {
        if (i < 0 || i >= nodeCount) {
            return null;
        }
        E e;
        CircularNode<E> finger = tail;
        if (i == nodeCount - 1) {
            e = finger.getValue();
        } else {
            while (i > 0) {
                finger = finger.getNext();
                i--;
            }
            e = finger.getNext().getValue();
        }
        nodeCount--;
        if (nodeCount == 0) {
            tail = null;
        } else {
            finger.setNext(finger.getNext().getNext());
            tail = getNode(nodeCount-1);
        }
        return e;
    }

    public void add(E e) {
        add(nodeCount, e);
    }

    public void setTail(CircularNode tail) {
        this.tail = tail;
    }
}
