package com.game.loot;

public class Lootmar {
	
	private int myPlayerId;
	private CardSet myHand; // myhand
	
	
	public Lootmar(int myId, CardSet cs){
		myPlayerId = myId;
		myHand = cs;
	}
	
	public void calcNextMove(GameState gs){
		
		// enumerate all possible moves for me
		// calc score for each move
		// for every other player
		// 		enumerate all possible moves with probability for next player
		// 		calc score for each move	
		
	}
	
	public void enumerateMyMoves(){
		/*
		 * 1. Draw a card
		 * 2. if (decksize==0), allow discarding of any non-merchant ship
		 * 3. Play a merchant ship
		 * 4. For each battle, 
		 * 		if i'm in it, 
		 * 			consider adding more
		 * 		else 
		 * 			consider entering with any pirate ship that's allowed
		 */
	}
	
	public void enumerateGeneralMoves(){
		/*
		 * Other player's moves
		 * 1. Draw a card
		 * 2. if (decksize ==0), discard a non-merchantship
		 * 3. Play any of 7 merchant ships (depending on what's available)
		 * 4. For each battle,
		 * 		if player in it, 
		 * 			calculate odds of adding cards based on remaining cards
		 * 		else 
		 * 			calculate chances of entering the play
		 */
	}
	
	public double myScore(GameState gs){
		/*
		 * Scoring function
		 * - merchant ships collected (face value)
		 * - score current battles
		 * - score cards in hand
		 * - Other
		 * 		- too many merchant ships in hand
		 * 		- trumps without colors
		 * 		- remaining moves
		 */
		
		double score = 0;
		int tempCount = 0;
		int tMShipCount = gs.findPlayer(myPlayerId).getMShips().numMShips();
		int tpShipCount = gs.findPlayer(myPlayerId).getMShips().numPShips();;
		int tTrumpCount = gs.findPlayer(myPlayerId).getMShips().numTrumps();;
		
		MerchantShip m = null;
		PirateShip p = null;
		Trump t = null;
		CardSet cs = null;
		
		// merchant ships collected
		score += gs.findPlayer(myPlayerId).getMShips().sumMShips();
		
		// Score current battles
		
		// Score cards in hand		
		for (Card c: myHand.getCards()) {
			if (c instanceof MerchantShip){
				// assuming a slight negative value
			}
			else if (c instanceof PirateShip){
				
			}
			else {
				/*
				 * Admiral
				 * 	highest mShip in hand (or one i have already played) * # of trumps used or known/5?
				 */
				if (((Trump)c).getColor() == Color.Admiral) {
					m = myHand.hMShip(); // TODO: account for ones that might already be in play
					
					
					if (m != null) {
						tempCount = gs.getUnknownCards().numTrumps() + myHand.numTrumps(); 
						score += m.getValue() * (5 - tempCount/5);
					}
				}
				/*
				 * Other trumps
				 * 	if (pirate ship of that color in hand) {
				 * 		add score
				 */
				else {
					cs = myHand.pShipsSet(((Trump)c).getColor());
					if (cs.getCount() > 0){
						// score this trump
						// TODO
					}
				}
			}
		}
		
		return score;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Lootmar l = new LootMar();
		
	}
	
	
	

}
