package com.game.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.game.loot.Move.ACTION;

/*
 * Splits scoring into several functions
 */

public class ArtemisAI extends VirtualPlayer {
	
	// summed up top 5 mships that could possibly be won with trumps
	// in reality, this is probably a bit lower
	private static final float trumpValue = 6.4f; 
	
	public ArtemisAI(String name) {
		super(name);
	}
	
	public ArtemisAI(){
		super("Artemis");
	}
	
	@Override
	public Move getNextMove(GameState gm) {
		super.runPreMoveHelpers(gm);
		
		// generate all valid moves
		List<ScoredMove> moves = allValidMoves(gm);
		
		// evaluate and assign scores
		evaluate(gm, moves);		

		// find the highest score
		float hScore = -100;
		ScoredMove bestMove = null;
		for (ScoredMove sm: moves) {
			if (sm.getScore() > hScore) {
				bestMove = sm;
				hScore = sm.getScore();
			}
		}
		// make the move with the highest score
		return bestMove.getMove();
	}

	
	/*
	 * Evaluate the game if I make Move m
	 */
	
	public void evaluate(GameState gm, List<ScoredMove> moves) {
		float d = 0;
		CardSet cs;
		float newBattleListScore = 0;
		
		float currentHandScore = calcCardSetScore(gm, getHand());
		float currentBattleListScore = calcBattleListScore(gm, gm.getBattleList());
		float unknownCardsScore = calcCardSetScore(gm, getUnknownCards());
		
		if (super.getVerbosity()) {
			System.out.println("Current Hand Score: " + currentHandScore);
			System.out.println("Current BattleList Score: " + currentBattleListScore);
			System.out.println("UnknownCards Score: " + unknownCardsScore
				+ " | Count: " + getUnknownCards().getCount() + " | Avg/card: " + 
				unknownCardsScore/(float)getUnknownCards().getCount());
		}
		
		for (ScoredMove m: moves) {
			if (m.getMove().getAction() == ACTION.DRAW) {
				d = calcDrawScore(gm) + currentHandScore + currentBattleListScore;
			}
			else if (m.getMove().getAction() == ACTION.PLAY_ATTACK) {
				cs = new CardSet(getHand());
				cs.removeCard(m.getMove().getCard());
				newBattleListScore = calcBattleListScore(gm, createNewBattleList(gm, m.getMove()));
				d= calcCardSetScore(gm, cs) + newBattleListScore; 
			}
			else if (m.getMove().getAction() == ACTION.PLAY_MERCHANT_SHIP) {
				cs = new CardSet(getHand());
				cs.removeCard(m.getMove().getCard());
				newBattleListScore = calcBattleListScore(gm, createNewBattleList(gm, m.getMove()));
				d= calcCardSetScore(gm, cs) + newBattleListScore;
			}
			else if (m.getMove().getAction() == ACTION.DISCARD) {
				cs = new CardSet(getHand());
				cs.removeCard(m.getMove().getCard());
				d = calcCardSetScore(gm, cs) + currentBattleListScore;
			}
			
			// TODO: could be done better somewhere else?
     		if (m.getMove().getAction() != ACTION.PLAY_MERCHANT_SHIP) {
				d += calcMShipPenaltyScore(gm);
			}
			
			m.setScore(d);
			if (super.getVerbosity()) {
				System.out.println(m);
			}
		}
	}
	
	public ArrayList<Battle> createNewBattleList(GameState gm, Move m) {
		ArrayList<Battle> newBList = new ArrayList<Battle>();
		
		if (m.getAction() == ACTION.PLAY_ATTACK) {
		
			Battle modBattle = null;
			for (Battle b: gm.getBattleList()) {
				if (m.getBattle() == b) {
					modBattle = new Battle(b);
					newBList.add(modBattle);
				}
				else {
					newBList.add(new Battle(b));
				}
			}
			modBattle.addAttackCard((Player)this, (AttackCard)m.getCard(), gm.getMoveCount());
		}
		else if (m.getAction() == ACTION.PLAY_MERCHANT_SHIP) {
			for (Battle b: gm.getBattleList()) {
				newBList.add(new Battle(b));
			}
			newBList.add(new Battle((MerchantShip)m.getCard(), (Player)this, gm.getMoveCount()));
		}
		return newBList;
	}
	
	/*
	 * Meant for calculating a player's current hand value
	 * TODO: move away from fixed values for trump; calc real-time like pship and mship
	 * TODO: Take the rarity of the color + value of pirateship into account (like last B4 left)
	 * TODO: Award extra points for having multiple cards of the same color
	 * TODO: Is this the best function to account for negative points at the end?
	 */
	
	public float calcCardSetScore(GameState gm, CardSet cardSet) {
		float f = 0;
		float temp = 0;
		float totalMShipPoints = totalMShipPointsRem(gm);
		float totalPShipPoints = totalPShipPointsRem();
		
		// System.out.println("Unknown Cards: " + getUnknownCards().getCount());
		// System.out.println("MShipPoints: " + totalMShipPoints + " | PShipPoints: " + totalPShipPoints);
		
		for (Card c: cardSet.getCards()) {
			if (c instanceof MerchantShip) {
				/*
				 * negative points for having to use up a move
				 * positive points for small chance of collecting without attack
				 * evaluate as if an avgRemPShip will attack it by the time it comes to you
				 */
				
				f += -1*totalMShipPoints/numRemainingPlays(gm.getPlayers()); 					
				// f += -1*pointsPerTurn; //from spreadsheet
				
			}
			else if (c instanceof PirateShip) {
				if (totalPShipPoints > 0) {
					temp = (float)c.getValue()*totalMShipPoints/totalPShipPoints;
					f+= temp;
				}
				else {
					// Should anything go there?
				}
			}
			else if (c instanceof Trump){
				if ((((Trump) c).getColor() == Color.Admiral)
						&& cardSet.hasMShip()){
					// TODO: find highest merchant ship that I have or is on the board that I'm not winning
					f += trumpValue*totalMShipPoints/totalPShipPoints;
				}
				else if (cardSet.hasPShip(((Trump) c).getColor())) {
					f += trumpValue*totalMShipPoints/totalPShipPoints;
				}
			}
		}
		return f;
	}
	

	
	/*
	 * Returns number of plays that can be made (excluding draws)
	 * calculated by summing cards in each player's hand and adding decksize to it
	 */
	
	public float numRemainingPlays(List<Player> players) {
		float f = 0;
		for (Player p: players) {
			f += (float)p.getHandCount();
		}
		f += (float)getDeckSize();
		
		return f;
	}

	
	/*
	 * Returns score for a set of battles
	 */
	public float calcBattleListScore(GameState gm, List<Battle> bList) {
		List<Player> pList = gm.getPlayers();
		int index = pList.indexOf(this);
		index = (index + 1) % gm.getPlayers().size();
		HashMap<Battle, Float[]> bMatrix = new HashMap<Battle, Float[]>();
		HashMap<Battle, Boolean> bDone = new HashMap<Battle, Boolean>();
		Float[] fTemp = new Float[gm.getPlayers().size()];
		
		int outs = 0;
		int diff = 0;
		int activeMShipSum = 0;
		float fScore = 0;
		float fTempScore = 1.0f;


		//initiate bMatrix
		for (Battle b: bList) {
			bMatrix.put(b, new Float[gm.getPlayers().size()]);
			bDone.put(b, false);
			for (int i = 0; i< gm.getPlayers().size(); i++) {
				bMatrix.get(b)[i] = 1.0f;
			}
		}
		
		int turn = 1;
		while (turn <= pList.size()){
			activeMShipSum = 0;
			for (Battle b: bList) {
				if (!bDone.get(b)) {
					if (pList.get(index) == b.getWinningPlayer()) {
						if (b.getWinningPlayer() != this) {
							fTemp = bMatrix.get(b);
							fTemp[turn-1] = -1.0f*(float)b.getMerchantShip().getValue();
							bMatrix.put(b, fTemp);
						}
						else {
							fTemp = bMatrix.get(b);
							fTemp[turn-1] = (float)b.getMerchantShip().getValue();
							bMatrix.put(b, fTemp);
						}
						/*if (sumOfPlayersPoints(pList) > 0) {
							fTemp[turn-1] *= (float)pList.get(index).getPoints()/(float)sumOfPlayersPoints(gm.getPlayers());
						}*/
						bDone.put(b, true);
					}
					else {
						// sum up the count of merchantship value
						activeMShipSum += b.getMerchantShip().getValue();
					}
				}
			}
			
			for (Battle b: bList) {
				outs = 0;
				if (!bDone.get(b)) {
					Attacker attacker = b.getAttackerByPlayerId(pList.get(index).getId());
					if (attacker != null && attacker.getScore() > 0) {
						// this player is already attacking, what are the chances that she'll attack again
						if (!b.isBattleUsingTrumps()) {
							diff = b.getHighScore() - attacker.getScore();
							outs += findGreaterNumCards(diff, attacker.getAttackerColor(), false).getCount();
						}
						if (getUnknownCards().hasCard(new Trump(Color.Admiral)) &&
								b.getOwnerPlayerId() == pList.get(index).getId()) {
								outs++; // add admiral
						}
						if (getUnknownCards().hasCard(new Trump(attacker.getAttackerColor()))){
								outs++; // add regular trump
						}
						
					}
					else {
						// not attacking currently, what are the chances that she'll attack at all
						if (b.isBattleUsingTrumps()) {
							if (b.getOwnerPlayerId() == pList.get(index).getId()) {
								// could play admiral
								if (getUnknownCards().hasCard(new Trump(Color.Admiral))) {
									// calc prob of playing it
									// rough approx for now. TODO: revisit
									//f *= (float)pList.get(index).getHandCount()/(getUnknownCards().getCount());
									outs++;
								}
								else {
									// this player can't compete in this battle
								}
							}
						}
						else {
							// battle isn't using trumps
							if (b.getHighScore() <= 4) {
								// what's the probability that this player has a card worth this battle's highest score
								// in a different color?
								//f*= (float)pList
								outs += findValidAttackCards(b, pList.get(index));
							}
							else {
								// this player can't compete							
							}
						}
					}
					//System.out.println("Outs found: " + outs);
					//System.out.println("activeMShipSum: " + activeMShipSum);
					
					fTemp = bMatrix.get(b);
					// trying to account for # of cards in a player's hands. Approximation at best.
					/*
					float outsProb = (float)outs*(float)pList.get(index).getHandCount()/(float)getUnknownCards().getCount();
					if (outsProb > 1){
						outsProb = 1.0f;
					}
					
					
					fTemp[turn-1] = 1.0f - (outsProb*(float)b.getMerchantShip().getValue()/(float)activeMShipSum);
					*/
					fTemp[turn-1] = 1.0f - ((float)outs/(float)getUnknownCards().getCount()*(float)b.getMerchantShip().getValue()/(float)activeMShipSum);
					bMatrix.put(b, fTemp);
				}
			}
			index = (index + 1) % gm.getPlayers().size();
			turn++;
		}

		// calculate final score per battle
		
		for (Battle b: bList) {
			fTemp = bMatrix.get(b);
			//System.out.println(b);
			fTempScore = 1.0f;
			for (int i = 0; i< gm.getPlayers().size(); i++) {
				//fTempScore *= fTemp[i];
				fTempScore *= fTemp[i];
				/*if (sumOfPlayersPoints(gm.getPlayers()) > 0) {
					fTempScore *= gm.getPlayers().get(i).getPoints()/sumOfPlayersPoints(gm.getPlayers());
				}*/
					
				if (super.getVerbosity()) {
					System.out.print(fTemp[i] + " ");
				}
			}
			// to account for ties that I'm not part of 
			if (b.getWinningAttacker() == null) {
				if (b.getAttackerByPlayerId(getId()) == null) {
					fTempScore *=-1.0f;
				}
				else if (b.getAttackerByPlayerId(getId()).getScore() != b.getHighScore()) {
					fTempScore *=-1.0f;
				}
			}
			if (super.getVerbosity()) {
				System.out.println("");
				System.out.println("Battle: " + b + " | Score: " + fTempScore);
			}
			fScore += fTempScore;
		}
		
		return fScore;
	}
	
	public int sumOfPlayersPoints(List<Player> players) {
		int sum = 0;
		for (Player p: players) {
			sum += p.getPoints();
		}
		return sum;
	}

	public int findValidAttackCards(Battle b, Player p) {
		int count = 0;
		Attacker a = b.getAttackerByPlayerId(p.getId());
		if (a!=null && a.getScore() > 0) {
			return findGreaterNumCards(b.getHighScore() - a.getScore(),a.getAttackerColor(), false).getCount();
		}
		
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.Blue);
		colors.add(Color.Green);
		colors.add(Color.Purple);
		colors.add(Color.Yellow);
		
		for (Attacker attacker: b.getAttackers()) {
			if (attacker.getScore() > 0) {
				colors.remove(attacker.getAttackerColor());
			}
		}
		
		for (Color color: colors) {
			count += findGreaterNumCards(0, color, false).getCount();
		}
		return count;
	}
	

	
	/*
	 * return the num of moves until this player's turn
	 */
	public int numTurnsUntilMove(GameState gm, Player p) {
		int count = 0;
		
		count = gm.getPlayers().indexOf(p)-gm.getPlayers().indexOf(this);
		if (count < 0) {
			return count + gm.getPlayers().size();
		}
		else {
			return count;
		}
	}
	
	// calc functions
	
	public float calcDrawScore(GameState gm){
		
		/*
		 * probability of drawing a merchant ship * fixed negative value
		 * probability of drawing a pirate ship * it's value
		 */
		float f = 0;
		float mShipScore, pShipScore, trumpScore;
		float probMShip, probPShip, probTrump;
		
		CardSet u = getUnknownCards();
		float totalMShipPoints = totalMShipPointsRem(gm);
		float totalPShipPoints = totalPShipPointsRem();

		
		probMShip = (float)u.numMShips()/(float)u.getCount();
		probPShip = (float)u.numPShips()/(float)u.getCount();
		probTrump = (float)u.numTrumps()/(float)u.getCount();
		
		mShipScore = probMShip*-1*totalMShipPoints/numRemainingPlays(gm.getPlayers());
		if (totalPShipPoints > 0) {
			pShipScore = probPShip*avgAttackShipRem()*totalMShipPoints/totalPShipPoints;
			trumpScore = probTrump*trumpValue*totalMShipPoints/totalPShipPoints;
		}
		else {
			pShipScore = 0;
			trumpScore = 0;
		}
				
		f = mShipScore;
		if (pShipScore >0 ) {
			// check that pShipScore wasn't divided by 0, if all pShips were gone
			f += pShipScore;
		}
		if (trumpScore > 0) {
			f += trumpScore;
		}
		if (super.getVerbosity()) {
			System.out.println("mShipScore: " + mShipScore + " | pShipScore: " + pShipScore + " | trumpScore: " + trumpScore);
			System.out.println("Draw score: " + f);
		}
		return f;
	}
	
	public float calcMShipPenaltyScore(GameState gm) {
		float f = 0;
		float sum = 0;
		float count = 0;
		float minMoves = calcMinRemainingMoves(gm);
		
		if (super.getVerbosity()) {
			System.out.println("Min moves remaining: " + minMoves + " | MShips: " + getHand().numMShips());
		}
		
		if (!getHand().hasMShip()) {
			return 0;
		}
		
		if (getHand().numMShips() >= Math.ceil(minMoves)) {
			for (Card c: getHand().getCards()) {
				if (c instanceof MerchantShip){ 
					sum += c.getValue();
					count++;
				}
			}
			f = -1.0f*sum/(float)count;
		}
		return f;
	}
	
	/*
	 * Calc min number of moves this player will get
	 * TODO: Needs to be tested. Might have a bug?
	 */
	
	public float calcMinRemainingMoves(GameState gm){
		float moves = 0;
		int smallestHand = 78;

		// find the player with smallest hand
		for (Player p: gm.getPlayers()) {
			//System.out.println(p.getName() + "'s hand: " + p.getHandCount() + " cards");
			if (smallestHand > p.getHandCount()) {
				smallestHand = p.getHandCount();
			}
		}
		
		// formula: smallestHand + (decksize - smallesthand*(#players-1))/(#players-1)
		
		moves = (float)smallestHand 
				+ (((float)getDeckSize() - ((float)smallestHand*((float)gm.getPlayers().size()-1)))/((float)gm.getPlayers().size()-1));
		
		return moves;
	}
	
	/*
	 * Find the highest merchant ship still in the game
	 */
	public int hMShipRemaining(GameState gm){
		int imShip = 0;
		
		// check unknowncards
		// check my hand
		// check existing battles
		
		MerchantShip mShip = getUnknownCards().hMShip();
	
		if (mShip != null && mShip.getValue() > imShip) {
			imShip = mShip.getValue();
		}
		mShip = getHand().hMShip();
		if (mShip != null && mShip.getValue() > imShip) {
			imShip = mShip.getValue();
		}
		
		for (Battle b: gm.getBattleList()) {
			if (b.getMerchantShip().getValue() > imShip) {
				imShip = b.getMerchantShip().getValue();
			}
		}
		
		return imShip;
	}
	
	/*
	 * Calculate average value of merchant ships remaining
	 */
	public float avgMShipRem() {
		int sum = 0;
		int count = 0;
		
		for (Card c: getUnknownCards().getCards()) {
			if (c instanceof MerchantShip) {
				sum += c.getValue();
				count++;
			}
		}
		for (Card c: getHand().getCards()) {
			if (c instanceof MerchantShip) {
				sum += c.getValue();
				count++;
			}
		}

		return (float)sum/(float)count;
	}
	
	public float totalMShipPointsRem(GameState gm){
		float sum = 0;
		for (Card c: getUnknownCards().getCards()) {
			if (c instanceof MerchantShip) {
				sum += c.getValue();
			}
		}
		for (Card c: getHand().getCards()) {
			if (c instanceof MerchantShip) {
				sum += c.getValue();
			}
		}
		for (Battle b: gm.getBattleList()) {
			sum += b.getMerchantShip().getValue();
		}
		
		return sum;
	}
	
	public float totalPShipPointsRem(){
		float sum = 0;
		for (Card c: getUnknownCards().getCards()) {
			if (c instanceof PirateShip) {
				sum += c.getValue();
			}
			else if (c instanceof Trump) {
				sum += trumpValue;
			}
		}
		
		for (Card c: getHand().getCards()) {
			if (c instanceof PirateShip) {
				sum += c.getValue();
			}
			else if (c instanceof Trump) {
				sum += trumpValue;
			}
		}
		return sum; 
	}
	
	public float avgAttackShipRem(){
		int sum = 0;
		int count = 0;
		
		for (Card c: getUnknownCards().getCards()) {
			if (c instanceof PirateShip) {
				sum += c.getValue();
				count++;
			}
			else if (c instanceof Trump) {
				sum += trumpValue;
				count++;
			}
		}
		return (float)sum/(float)count;
	}
	
	public CardSet findGreaterNumCards(int value, Color color, boolean trumpable) {
		
		CardSet cards = new CardSet();
		
		for (Card c: getUnknownCards().getCards()) {
			if ((c instanceof PirateShip) 
					&& (((PirateShip) c).getColor() == color)
					&& c.getValue() > value){
				cards.addCard(c);
			}
		}
		
		return cards;
	}
	
	public int numGreaterCards(Card c) {
		int count = 0;
		
		if (c instanceof Trump) {
			return getUnknownCards().numTrumps();
		}
		
		for (Card s: getUnknownCards().getCards()) {
			if (s instanceof PirateShip) {
				if ((s.getValue() > c.getValue())
						&& ((PirateShip)s).getColor() != ((PirateShip)c).getColor()){
					count++;
				}
			}
		}
		
		count += getUnknownCards().numTrumps();
		return count;
	}
	
	
	
	// Move generation functions
	
	/*
	 * Generates a list of all valid (and not wasted) moves
	 */
	public List<ScoredMove> allValidMoves(GameState gm){
		List<ScoredMove> validMoves = new ArrayList<ScoredMove>();
		
		// check for draw move
		if (getDeckSize() > 0) {
			validMoves.add(new ScoredMove(ACTION.DRAW, null, null));
		}
		/*
		else {
			// check for discarding move (note: only allowing if decksize is empty)
			for (Card c: getHand().getCards()){
				if (!(c instanceof MerchantShip)) {
					validMoves.add(new ScoredMove(ACTION.DISCARD, c, null));
				}
			}
		}*/
		
		validMoves.addAll(allDiscardMoves(gm));
		
		// check for playing merchant ships
		validMoves.addAll(allmShipMoves(gm));
		
		// add all attacks
		validMoves.addAll(allAttackMoves(gm));
		
		if (super.getVerbosity()) {
			System.out.println("Valid moves found: " + validMoves.size());
		}
		return validMoves;
	}
	
	/*
	 * Returns a list of all valid (and winnable) attacks
	 */
	
	public List<ScoredMove> allAttackMoves(GameState gm) {
		List<ScoredMove> attackMoves = new ArrayList<ScoredMove>();
		
		// check for attacking battles
		if (gm.getBattleList().size() > 0) {
			for (Battle b: gm.getBattleList()) {
				if (!b.isBattleUsingTrumps()) {
					for (Card c: getHand().getCards()){
						if (c instanceof PirateShip) {
							if (canPirateShipCardAttack(b, (PirateShip)c)){
								attackMoves.add(new ScoredMove(ACTION.PLAY_ATTACK, c, b));
							}
						}
					}
				}
				for (Card c: getHand().getCards()) {
					if (c instanceof Trump) {
						if (canTrumpCardAttack(b, (Trump)c)) {
							attackMoves.add(new ScoredMove(ACTION.PLAY_ATTACK, c, b));
						}
					}
				}
			}
		}
		return attackMoves;
	}
	
	/*
	 * All merchant ship moves
	 */
	
	public List<ScoredMove> allmShipMoves(GameState gm){
		
		List<ScoredMove> mShipMoves = new ArrayList<ScoredMove>();
		
		if (getHand().hasMShip()) {
			for (Card c: getHand().getCards()) {
				if (c instanceof MerchantShip) {
					mShipMoves.add(new ScoredMove(ACTION.PLAY_MERCHANT_SHIP, c, null));
				}
			}
		}
		return mShipMoves;
	}
	
	/*
	 * All discard moves
	 */
	
	public List<ScoredMove> allDiscardMoves(GameState gm) {
		List<ScoredMove> discardMoves = new ArrayList<ScoredMove>();
		
		for (Card c: getHand().getCards()) {
			if (!(c instanceof MerchantShip)) {
				discardMoves.add(new ScoredMove(ACTION.DISCARD, c, null));
			}
		}
		return discardMoves;
	}
	
	public Card canAttack(Battle b) {
		// if there is a card in my hand that can attack this battle and win it, return one
		
		if (!b.isBattleUsingTrumps()) {
			// if no trump played, see if a pirateship can be played.
			for (Card c: getHand().getCards()) {
				if (c instanceof PirateShip) {
					if (canPirateShipCardAttack(b, (PirateShip)c)) {
						return c;
					}
				}
			}
		}
		
		// Either no trumps played or no pirateship can be played
		// So, evaluate trumps now
		for (Card c: getHand().getCards()) {
			if (c instanceof Trump) {
				if (canTrumpCardAttack(b, (Trump)c)) {
					return c;
				}
			}
		}
		return null;
	}
	
	public boolean canPirateShipCardAttack(Battle b, PirateShip c) {
		// can this card attack this battle
		
		// check if player is already participating in this battle
		// if so, is this card of same color
		// else check if anyone else is using this color
		
		Attacker a = b.getAttackerByPlayerId(getId());
 
		if (a != null) {
			if (a.getAttackCards().getCards().size() > 0) {
				// this is to account for the empty ownerAttacker
				if (a.getScore() + c.getValue() >= b.getHighScore()) {
					return a.getAttackerColor() == c.getColor();
				}
			}
		}
		
		for (Attacker at: b.getAttackers()) {
			if (at.getAttackerColor() == c.getColor()){
				return false;
			}
		}
		
		if (c.getValue() >= b.getHighScore()){
			return true;
		}
		return false;
	}
	
	public boolean canTrumpCardAttack(Battle b, Trump c){
		
		if (c.getColor() == Color.Admiral){
			if (getId() == b.getOwnerPlayerId()){
				return true;
			}
		}
		else {
			Attacker a = b.getAttackerByPlayerId(getId());
			if ((a != null) && (a.getAttackerColor() == c.getColor())) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public class ScoredMove {
		private float score;
		private float cardValue;
		private Move move;
		
		ScoredMove(ACTION action, Card card, Battle battle) {
			move = new Move(action, card, battle);
			score = 0;
			cardValue = 0;
		}
		
		public float getScore(){
			return score;
		}
		
		public float getCardValue(){
			return cardValue;
		}
		
		public Move getMove(){
			return move;
		}
		
		public void setScore(float d){
			score = d;
		}
		
		public void setCardValue(float d) {
			cardValue = d;
		}
		
		public String toString(){
			return move.toString() + " | Score: " + score;					
		}
	}
}
