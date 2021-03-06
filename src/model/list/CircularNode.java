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
public class CircularNode<E> {

        private E data;
        private CircularNode<E> nextElement;

        public CircularNode(E value, CircularNode<E> next) {
            data = value;
            nextElement = next;
        }

        public CircularNode<E> getNext() {
            return nextElement;
        }

        public void setNext(CircularNode<E> next) {
            nextElement = next;
        }

        public E getValue() {
            return data;
        }

        public void setValue(E value) {
            data = value;
        }
}
