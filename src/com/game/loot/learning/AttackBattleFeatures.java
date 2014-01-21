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
	int battleIndex;
	int numTotalBattles;
	
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
			boolean isTie,
			int battleIndex,
			int numTotalBattles) {
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
		this.battleIndex = battleIndex;
		this.numTotalBattles = numTotalBattles;
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
		output += "battleIndex" + " ";
		output += "numTotalBattles" + " ";
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
		output += battleIndex + " ";
		output += numTotalBattles + " ";
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
