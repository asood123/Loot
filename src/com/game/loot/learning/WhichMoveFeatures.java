package com.game.loot.learning;

public class WhichMoveFeatures extends Features {
	int cardsInHand;
	
	public WhichMoveFeatures(int cardsInHand) {
		this.cardsInHand = cardsInHand;
	}
	
	public WhichMoveFeatures() {
		
	}
	
	@Override
	public String getFeatureHeaders() {
		String output = "";
		output += "CardsInHand" + " ";
		return output;
	}

	@Override
	public String getOutputHeader() {
		return "MoveMade";
	}

	@Override
	public String getFeaturesString() {
		String output = "";
		output += cardsInHand + " ";
		return output;
	}

}
