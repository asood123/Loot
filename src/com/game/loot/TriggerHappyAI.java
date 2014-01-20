package com.game.loot;

import java.util.Random;

import com.game.loot.Move.ACTION;

/*
 * AI that always attacks if it can
 * Otherwise, flips a coin to draw or play merchant ship
 */

public class TriggerHappyAI extends VirtualPlayer {

	private Random rand;
	
	public TriggerHappyAI(String name) {
		super(name);
		rand = new Random();
	}
	
	@Override
	public Move getNextMove(GameState gm) {
		super.runPreMoveHelpers(gm);
		
		
		// if there is a battle
		//	and there is a valid way to attack it
		// 		attack it
		// else flip a coin to draw or play merchant ships
		
		if (gm.getBattleList().size() > 0) {
			for (Battle b: gm.getBattleList()) {
				// if valid and winning way to attack this battle, do it
				Card c = canAttack(b);
				
				if (c != null) {
					return new Move(ACTION.PLAY_ATTACK, c, b);
				}
			}
		}
		
		// flip a coin to draw or play merchant ships (assuming either is possible)
				
		if (getHand().hasMShip() && getDeckSize() > 0) {
			int n = rand.nextInt(2);

			if (n==0) {
				for (Card c: getHand().getCards()){
					if (c instanceof MerchantShip) {
						return new Move(ACTION.PLAY_MERCHANT_SHIP, c, null);
					}
				}
			}
		}
		else if (getHand().hasMShip() && getDeckSize() == 0) {
			// decksize is 0, so can only play mShip
			
			for (Card c: getHand().getCards()){
				if (c instanceof MerchantShip) {
					return new Move(ACTION.PLAY_MERCHANT_SHIP, c, null);
				}
			}
		}
		else if (!getHand().hasMShip() && getDeckSize() == 0){
			// no merchant ships in hand and deck depleted. discard random card
			return new Move(ACTION.DISCARD, getHand().getRandCard(), null);
		}

		// no mships and decksize > 0, so draw a card
		return new Move(ACTION.DRAW, null, null);			

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

		// check 
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
