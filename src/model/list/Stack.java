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
public interface Stack<E> {
public E peek();
// pre: stack is not empty
// post: top value is returned
public void push(E o);
// post: add o to the top of stack
public E pop();
// pre: stack is not empty
// post: remove and return top element
}