package com.game.loot;

import java.util.ArrayList;
import java.util.Random;


/*
 * Picks a valid move at random
 */

public class RandomAI extends VirtualPlayer {

	
	public RandomAI(String name) {
		super(name);
	}
	
	@Override
	public Move getNextMove(GameState gm) {
		super.runPreMoveHelpers(gm);
		
		/*
		 * General Algorithm: Make a list of all valid (and not wasted) moves 
		 * and pick one at random
		 */
		ArrayList<Move> validMoves = new ArrayList<Move>();
		Random rand = new Random();
		
		// check for draw move
		if (getDeckSize() > 0) {
			validMoves.add(new Move(ACTION.DRAW, null, null));
		}
		else {
			// check for discarding move (note: only allowing if decksize is empty)
			for (Card c: getHand().getCards()){
				if (!(c instanceof MerchantShip)) {
					validMoves.add(new Move(ACTION.DISCARD, c, null));
				}
			}
		}
		
		// check for playing merchant ships
		if (getHand().hasMShip()) {
			for (Card c: getHand().getCards()) {
				if (c instanceof MerchantShip) {
					validMoves.add(new Move(ACTION.PLAY_MERCHANT_SHIP, c, null));
				}
			}
		}
		
		// check for attacking battles
		if (gm.getBattleList().size() > 0) {
			for (Battle b: gm.getBattleList()) {
				if (!b.isBattleUsingTrumps()) {
					for (Card c: getHand().getCards()){
						if (c instanceof PirateShip) {
							if (canPirateShipCardAttack(b, (PirateShip)c)){
								validMoves.add(new Move(ACTION.PLAY_ATTACK, c, b));
							}
						}
					}
				}
				for (Card c: getHand().getCards()) {
					if (c instanceof Trump) {
						if (canTrumpCardAttack(b, (Trump)c)) {
							validMoves.add(new Move(ACTION.PLAY_ATTACK, c, b));
						}
					}
				}
			}
		}
		
		System.out.println("Valid moves found: " + validMoves.size());

		return validMoves.get(rand.nextInt(validMoves.size()));
	
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

}
