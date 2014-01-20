package com.game.loot.learning;

public class AttackBattleFeatures {
	int numCardsInHand;
	int valueOfMerchantCard;
	
	boolean attacked;
	
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	public AttackBattleFeatures(int numCardsInHand, int valueOfMerchantCard) {
		this.numCardsInHand = numCardsInHand;
		this.valueOfMerchantCard = valueOfMerchantCard;
	}

	public static String getFeatureHeaders() {
		String output = "";
		output += "numCardsInHand";
		output += "valueOfMerchantCard";
		return output;
	}
	
	public String getFeaturesString() {
		String output = "";
		output += numCardsInHand + " ";
		output += valueOfMerchantCard + " ";
		return output;
	}
	
	public static String getOutputHeader() {
		return "Attacked";
	}

	public String getOutputString() {
		if (attacked) {
			return "1";
		} else {
			return "0";
		}
	}
}
