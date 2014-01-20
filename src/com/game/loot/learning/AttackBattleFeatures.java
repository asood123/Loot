package com.game.loot.learning;

public class AttackBattleFeatures {
	int numCardsInHand;
	int valueOfMerchantCard;
	int playersInBattle;
	int highPointsInBattle;
	int numAttackCardsPlayed;
	int numCardsDrawn;
	int numMerchantPlayed;
	int deckSize;
	int discardSize;
	boolean isTie;
	
	boolean attacked;
	
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	public AttackBattleFeatures(int numCardsInHand,
			int valueOfMerchantCard,
			int playersInBattle,
			int highPointsInBattle,
			int numAttackCardsPlayed,
			int numCardsDrawn,
			int numMerchantPlayed,
			int deckSize,
			int discardSize,
			boolean isTie) {
		this.numCardsInHand = numCardsInHand;
		this.valueOfMerchantCard = valueOfMerchantCard;
		this.playersInBattle = playersInBattle;
		this.highPointsInBattle = highPointsInBattle;
		this.numAttackCardsPlayed = numAttackCardsPlayed;
		this.numCardsDrawn = numCardsDrawn;
		this.numMerchantPlayed = numMerchantPlayed;
		this.deckSize = deckSize;
		this.discardSize = discardSize;
		this.isTie = isTie;
	}

	public static String getFeatureHeaders() {
		String output = "";
		output += "numCardsInHand" + " ";
		output += "valueOfMerchantCard" + " ";
		output += "playersInBattle" + " ";
		output += "highPointsInBattle" + " ";
		output += "numAttackCardsPlayed" + " ";
		output += "numCardsDrawn" + " ";
		output += "numMerchantPlayed" + " ";
		output += "deckSize" + " ";
		output += "discardSize" + " ";
		output += "isTie" + " ";
		return output;
	}
	
	public String getFeaturesString() {
		String output = "";
		output += numCardsInHand + " ";
		output += valueOfMerchantCard + " ";
		output += playersInBattle + " ";
		output += highPointsInBattle + " ";
		output += numAttackCardsPlayed + " ";
		output += numCardsDrawn + " ";
		output += numMerchantPlayed + " ";
		output += deckSize + " ";
		output += discardSize + " ";
		output += (isTie ? "1" : "0") + " ";
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
