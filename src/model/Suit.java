package model;

/**
 *
 * @author Alan Tian 
 * @version 1.0.0 2014.8
 */

public enum Suit
{
    SPADE(1, "S"), HEART(2, "H"), CLUB(3, "C"), DIAMOND(4, "D");
    private int index;
    private String name;
    private Suit(int index, String name){
        this.name = name;
        this.index = index;
    }
    
    public int toInt(){
        return index;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
