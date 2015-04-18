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
public interface AbstractList<E> {

    public int indexOf(E e);

    public int size();

    public boolean isEmpty();

    public E get(int i);

    public boolean set(int i, E e);

    public boolean add(int i, E e);

    public boolean contains(E e);
    
    public E remove(E e);
    
    public E remove(int i);
}
