package com.game.loot.learning;

public class AttackBattleFeatures extends Features {
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

	public AttackBattleFeatures() {
		// hacky, allow for static
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

	@Override
	public String getFeatureHeaders() {
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
	
	@Override
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
	
	@Override
	public String getOutputHeader() {
		return "Attacked";
	}
}
