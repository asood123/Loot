package com.game.loot;


public class MerchantShip extends Card {
	private int value;
	
	public MerchantShip(int v) {
		if (v > 1 && v < 9) {
			value = v;
		}
		else {
			value = -1;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString(){
		return "M" + value;
	}
	
	public boolean equals(Card c){
		return ((c instanceof MerchantShip) && (value == ((MerchantShip)c).getValue()));
	}
	
}
