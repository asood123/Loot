package com.game.loot;

public abstract class Card {
	
	public abstract int getValue();
	public abstract String toString();
	public abstract boolean equals(Card c);
	
	// testing/debugging
	public void print(){
		System.out.println(toString());
	}
	
}

enum Color {
	Blue, Green, Purple, Yellow, Admiral;
	
	@Override
	public String toString(){
		String value = "";
		switch (ordinal()) {
		case 0: 
			value = "B";
			break;
		case 1: 
			value = "G";
			break;
		case 2: 
			value = "P";
			break;
		case 3: 
			value = "Y";
			break;
		case 4: 
			value = "A";
			break;
		default: 
			value = "Error";
			break;
		}
		return value;
	}
}