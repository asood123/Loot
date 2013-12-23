package com.game.loot;

public abstract class Card {
	
	public abstract int getValue();
	public abstract String toString();
	public abstract boolean equals(Card c);
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	// testing/debugging
	public void print(){
		System.out.println(toString());
	}
	
}

enum Color {
	Blue, Green, Purple, Yellow, Admiral;
	
	public String getANSIColorCode(){
		String value = "";
		switch (ordinal()) {
		case 0: 
			value = Card.ANSI_BLUE;
			break;
		case 1: 
			value = Card.ANSI_GREEN;
			break;
		case 2: 
			value = Card.ANSI_PURPLE;
			break;
		case 3: 
			value = Card.ANSI_YELLOW;
			break;
		default: 
			value = Card.ANSI_RESET;
			break;
		}
		return value;
	}
	
	@Override
	public String toString(){
		
		String value = "";
		switch (ordinal()) {
		case 0: 
			value = Card.ANSI_BLUE + "B";
			break;
		case 1: 
			value = Card.ANSI_GREEN + "G";
			break;
		case 2: 
			value = Card.ANSI_PURPLE + "P";
			break;
		case 3: 
			value = Card.ANSI_YELLOW + "Y";
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