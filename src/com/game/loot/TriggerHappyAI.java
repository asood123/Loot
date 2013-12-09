package com.game.loot;

import java.util.Random;

// AI that always attacks

public class TriggerHappyAI extends VirtualPlayer {

	private Random rand;
	
	TriggerHappyAI(String name) {
		super(name);
		rand = new Random();
	}
	
	@Override
	public Move getNextMove(GameState gm) {
		runPreMoveHelpers(gm);
		
		
		// if there is a battle
		//	and there is a valid way to attack it
		// 		attack it
		// else flip a coin to draw or play merchant ships
		
		if (gm.getBattleList().size() > 0) {
			for (Battle b: gm.getBattleList()) {
				// if valid way to attack this battle, do it
				Card c = canAttack(b);
				
				if (c != null) {
					return new Move(ACTION.PLAY_ATTACK, c, b);
				}
			}
		}
		
		
		// flip a coin to draw or play merchant ships
		
		if (getHand().hasMShip()) {
			int n = rand.nextInt(2);
			
			if (n==0) {
				for (Card c: getHand().getCards()){
					if (c instanceof MerchantShip) {
						return new Move(ACTION.PLAY_MERCHANT_SHIP, c, null);
					}
				}
			}
		}
	
		return new Move(ACTION.DRAW, null, null);
	}
	
	public Card canAttack(Battle b) {
		// if there is a card in my hand that can attack this battle, return the largest one
		
		for (Card c: getHand().getCards()) {
			if (c instanceof PirateShip) {
				if (canPirateShipCardAttack(b, (PirateShip)c)) {
					return c;
				}
			}
			else if (c instanceof Trump) {
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
			return a.getAttackerColor() == c.getColor();
		}
		
		for (Attacker at: b.getAttackers()) {
			if (at.getAttackerColor() == c.getColor()){
				return false;
			}
		}
		return true;
	}
	
	public boolean canTrumpCardAttack(Battle b, Card c){
		return false;
	}
}
