package com.game.loot;

public class PirateShip extends AttackCard {
	private int value;
	
	public PirateShip(int v, Color color) {
		super(color);
		if (v > 0 && v < 5) {
			value = v;
		}
		else {
			value = -1;
		}
	}
	
	public int getValue() {
		return value;
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
