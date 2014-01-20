package com.game.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.game.loot.Move.ACTION;

/*
 * Picks a valid move at random
 */

public class PlaysMerchantShipsFirstAI extends VirtualPlayer {

	Random rand;
	
	public PlaysMerchantShipsFirstAI(String name) {
		super(name);
		rand = new Random();
	}
	
	@Override
	public Move getNextMove(GameState gm) {
		super.runPreMoveHelpers(gm);
		
		/*
		 * General Algorithm: Make a list of all valid (and not wasted) moves 
		 * and pick one at random
		 */
		
		List<Move> moves;
		
		// play mships first
		if (getHand().hasMShip()) {
			moves = allmShipMoves(gm);
			return moves.get(rand.nextInt(moves.size()));
		}
		
		// attack next
		moves = allAttackMoves(gm);
		
		if (moves.size() > 0) {
			return moves.get(rand.nextInt(moves.size()));
		}

		// Draw or discard next
		
		if (getDeckSize() > 0) {
			return new Move(ACTION.DRAW, null, null);
		}
		else {
			moves = allValidMoves(gm);
			return moves.get(rand.nextInt(moves.size()));
		}

	}
	
	/*
	 * Generates a list of all valid (and not wasted) moves
	 */
	public List<Move> allValidMoves(GameState gm){
		List<Move> validMoves = new ArrayList<Move>();
		
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
		validMoves.addAll(allmShipMoves(gm));
		
		// add all attacks
		validMoves.addAll(allAttackMoves(gm));
		
		if (super.getVerbosity()){
			System.out.println("Valid moves found: " + validMoves.size());
		}
		return validMoves;
	}
	
	/*
	 * Returns a list of all valid (and winnable) attacks
	 */
	
	public List<Move> allAttackMoves(GameState gm) {
		List<Move> attackMoves = new ArrayList<Move>();
		
		// check for attacking battles
		if (gm.getBattleList().size() > 0) {
			for (Battle b: gm.getBattleList()) {
				if (!b.isBattleUsingTrumps()) {
					for (Card c: getHand().getCards()){
						if (c instanceof PirateShip) {
							if (canPirateShipCardAttack(b, (PirateShip)c)){
								attackMoves.add(new Move(ACTION.PLAY_ATTACK, c, b));
							}
						}
					}
				}
				for (Card c: getHand().getCards()) {
					if (c instanceof Trump) {
						if (canTrumpCardAttack(b, (Trump)c)) {
							attackMoves.add(new Move(ACTION.PLAY_ATTACK, c, b));
						}
					}
				}
			}
		}
		return attackMoves;
	}
	
	public List<Move> allmShipMoves(GameState gm){
		
		List<Move> mShipMoves = new ArrayList<Move>();
		
		if (getHand().hasMShip()) {
			for (Card c: getHand().getCards()) {
				if (c instanceof MerchantShip) {
					mShipMoves.add(new Move(ACTION.PLAY_MERCHANT_SHIP, c, null));
				}
			}
		}
		return mShipMoves;
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

