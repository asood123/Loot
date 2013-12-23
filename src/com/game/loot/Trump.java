package com.game.loot;

public class Trump extends AttackCard {
	
	private int value;
	
	public Trump(Color color) {
		super(color);
		value = Integer.MAX_VALUE;
	}
	
	public int getValue(){
		return value;
	}
	
	public String toString(){
		return this.getColor().getANSIColorCode() + "T" + color.toString() + Card.ANSI_RESET;
	}
	
	public boolean equals(Card c){
		return ((c instanceof Trump) && 
				(value == ((Trump)c).getValue()) &&
				(color == ((Trump)c).getColor()));
	}
	
}