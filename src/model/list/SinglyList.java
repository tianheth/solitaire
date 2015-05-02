/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

/**
 *
 * @author Alan Tian 1302662
 */
public class SinglyList<E> implements AbstractList<E> {

    protected SinglyNode head;
    protected int nodeCount;

    public SinglyList() {
        head = null;
        nodeCount = 0;
    }

    protected SinglyNode<E> locate(int i) {
        SinglyNode<E> currentNode = head;
        while (i > 0) {
            currentNode = currentNode.getNext();
            i--;
        }
        return currentNode.getNext();
    }

    @Override
    public int indexOf(E e) {
        SinglyNode<E> finger = head;
        int index = -1;
        while (finger != null) {
            if (finger.getValue() == e) {
                break;
            } else {
                finger = finger.getNext();
                index++;
            }
        }
        if (finger != null) {
            return index;
        } else {
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
        if (i < 0 || i > nodeCount) {
            return null;
        } else {
            return (E) locate(i).getValue();
        }
    }

    @Override
    public boolean set(int i, E e) {
        if (i < 0 || i > nodeCount) {
            return false;
        } else {
            locate(i).setValue(e);
            return true;
        }
    }

    @Override
    public boolean add(int i, E e) {
        if (i < 0 || i > nodeCount) {
            return false;
        } else {
            if (head == null) {
                if (i > 0) {
                    return false;
                } else {
                    SinglyNode<E> newNode = new SinglyNode<E>(e, null);
                    head = new SinglyNode<E>(null, newNode);
                }
            } else {
                if (i == 0) {
                    SinglyNode<E> newNode = new SinglyNode<E>(e, head);
                    head.setNext(newNode);
                } else {
                    SinglyNode<E> prevNode = locate(i - 1);
                    SinglyNode<E> newNode = new SinglyNode<E>(e, prevNode.getNext());
                    prevNode.setNext(newNode);
                }
            }
            nodeCount++;
            return true;
        }
    }

    @Override
    public boolean contains(E e) {
        SinglyNode<E> finger = head;
        while (finger != null) {
            if (finger.getValue() == e) {
                return true;
            } else {
                finger = finger.getNext();
            }
        }
        return false;
    }

    @Override
    public E remove(E e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E remove(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
