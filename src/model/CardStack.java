/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.list.SinglyStack;

/**
 *
 * @author Alan Tian
 */
public class CardStack extends SinglyStack<Card>{

//    private SinglyStack<Card> stack;
    
//    public CardStack(){
//        stack = new SinglyStack<Card>();
//    }

    public boolean add(Card card) {
        push(card);
        return true;
    }
    
    
}