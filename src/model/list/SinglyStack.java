/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

/**
 *
 * @author Alan Tian
 */
public class SinglyStack<E> extends SinglyList<E> {

    public void push(E e) {
        add(nodeCount, e);
    }

    public E peek() {
        if (nodeCount == 0) {
            return null;
        } else {
            return locate(nodeCount - 1).value();
        }
    }

    public E pop() {
        if (nodeCount == 0) {
            return null;
        } else {
            if (nodeCount == 1) {
                E e = (E) head.next().value();
                head.setNext(null);
                return e;
            } else {
                SinglyNode<E> preNode = locate(nodeCount - 2);
                E e = preNode.next().value();
                preNode.setNext(null);
                return e;
            }
        }
    }
}
