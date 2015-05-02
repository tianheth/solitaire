/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

/**
 *
 * @author Alan Tian 1302662
 * 
 * @param <E> generic element in the stack
 */
public class SinglyStack<E> extends SinglyList<E> implements Stack<E>{

    @Override
    public E peek() {
        if (nodeCount == 0) {
            return null;
        } else {
            return get(nodeCount - 1);
        }
    }

    @Override
    public E pop() {
        if (nodeCount == 0) {
            return null;
        } else {
            if (nodeCount == 1) {
                E e = (E) head.getNext().getValue();
                head.setNext(null);
                return e;
            } else {
                SinglyNode<E> preNode = locate(nodeCount - 2);
                E e = preNode.getNext().getValue();
                preNode.setNext(null);
                return e;
            }
        }
    }

    @Override
    public void push(E e) {
        add(nodeCount, e);
    }
}
