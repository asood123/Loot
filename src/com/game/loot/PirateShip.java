package com.game.loot;

public class PirateShip extends Card {
	private int value;
	private Color color;
	
	public PirateShip(int v, Color c) {
		if (v > 0 && v < 5) {
			value = v;
		}
		else {
			value = -1;
		}
		if (c != Color.Admiral){
			color = c;
		}
		else {
			color = null;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String toString() {
		return color.toString() + value;
	}
	
	public boolean equals(Card c) {
		return ((c instanceof PirateShip) && 
				(value == ((PirateShip)c).getValue()) &&
				(color == ((PirateShip)c).getColor()));
	}
}
