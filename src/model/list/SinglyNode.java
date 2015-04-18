/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.list;

/**
 *
 * @author cvg2836
 */
public class SinglyNode<E> {

        private E data;
        private SinglyNode<E> nextElement;

        public SinglyNode(E value, SinglyNode<E> next) {
            data = value;
            nextElement = next;
        }

        public SinglyNode<E> next() {
            return nextElement;
        }

        public void setNext(SinglyNode<E> next) {
            nextElement = next;
        }

        public E value() {
            return data;
        }

        public void setValue(E value) {
            data = value;
        }
    }