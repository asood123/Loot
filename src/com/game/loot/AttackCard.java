package com.game.loot;

public abstract class AttackCard extends Card {
	protected Color color;
	
	public AttackCard(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
