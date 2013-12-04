package com.game.loot;

public class Trump extends Card {
	private Color color;
	private int value;
	
	public Trump(Color c){
		color = c;
		value = Integer.MAX_VALUE;
	}
	
	public Color getColor(){
		return color;
	}
	public int getValue(){
		return value;
	}
	public String toString(){
		return "T" + color.toString();
	}
	
	public boolean equals(Card c){
		return ((c instanceof Trump) && 
				(value == ((Trump)c).getValue()) &&
				(color == ((Trump)c).getColor()));
	}
	
}