/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

/**
 *
 * Alan Tian 1302662
 */
public class DoublyNode<E> {
        private E data;
        private CircularNode<E> nextElement;
        private CircularNode<E> prevElement;

        public DoublyNode(E value, CircularNode<E> prev, CircularNode<E> next) {
            data = value;
            nextElement = next;
            prevElement = prev;
        }

        public CircularNode<E> getNext() {
            return nextElement;
        }

        public void setNext(CircularNode<E> next) {
            nextElement = next;
        }

        public CircularNode<E> getPrev() {
            return prevElement;
        }

        public void setPrev(CircularNode<E> prev) {
            prevElement = prev;
        }

        public E getValue() {
            return data;
        }

        public void setValue(E value) {
            data = value;
        }
}
