package com.game.loot;

public abstract class Card {
	
	public abstract int getValue();
	public abstract String toString();
	public abstract String toStringWithoutANSI();
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
	
	public static Card stringToCard(String input) {
		int num = 0;
		
		if (input.length() == 2) {
			// pX, mX, tX
			if (input.toLowerCase().startsWith("t")) {
				if (input.substring(1).equalsIgnoreCase("a")) {
					return new Trump(Color.Admiral);
				}
				else if (input.substring(1).equalsIgnoreCase("b")) {
					return new Trump(Color.Blue);
				}
				else if (input.substring(1).equalsIgnoreCase("g")) {
					return new Trump(Color.Green);
				}
				else if (input.substring(1).equalsIgnoreCase("p")) {
					return new Trump(Color.Purple);
				}
				else if (input.substring(1).equalsIgnoreCase("y")) {
					return new Trump(Color.Yellow);
				}
			}
			else {
				num = Integer.parseInt(input.substring(1));
				
				if (input.toLowerCase().startsWith("m")) {
					if (num >= 2 && num <= 8) {
						return new MerchantShip(num);
					}
				}
				else if (num >=1 && num <= 4) {
					if (input.toLowerCase().startsWith("b")) {
						return new PirateShip(num, Color.Blue);
					}
					else if (input.toLowerCase().startsWith("g")) {
						return new PirateShip(num, Color.Green);
					}
					else if (input.toLowerCase().startsWith("p")) {
						return new PirateShip(num, Color.Purple);
					}
					else if (input.toLowerCase().startsWith("y")) {
						return new PirateShip(num, Color.Yellow);	
					}
				}

			}
		}
		return null;
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