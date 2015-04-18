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
public class CircularNode<E> {

        private E data;
        private CircularNode<E> nextElement;
        private CircularNode<E> prevElement;

        public CircularNode(E value, CircularNode<E> prev, CircularNode<E> next) {
            data = value;
            nextElement = next;
            prevElement = prev;
        }

        public CircularNode<E> next() {
            return nextElement;
        }

        public void setNext(CircularNode<E> next) {
            nextElement = next;
        }

        public CircularNode<E> prev() {
            return prevElement;
        }

        public void setPrev(CircularNode<E> prev) {
            prevElement = prev;
        }

        public E value() {
            return data;
        }

        public void setValue(E value) {
            data = value;
        }
}
