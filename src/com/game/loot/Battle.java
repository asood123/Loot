package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public class Battle {
	private MerchantShip merchantShip;
	private Player ownerPlayer;
	
	private int id;
	private static int battleCount = 1;
	
	private List<Attacker> attackers;
	
	public Battle(MerchantShip merchantShip, Player ownerPlayer) {
		this.merchantShip = merchantShip;
		this.ownerPlayer = ownerPlayer;
		attackers = new ArrayList<Attacker>();
		id = battleCount++;
	}
	
	// getters
	public int getId(){
		return id;
	}
	
	public MerchantShip getMerchantShip() {
		return merchantShip;
	}
	
	public int getOwnerPlayerId() {
		return ownerPlayer.getId();
	}
	
	public CardSet getAllCards() {
		CardSet cards = new CardSet();
		
		cards.addCard(merchantShip);
		for (Attacker attacker : attackers){
			cards.addCardSet(attacker.getAttackCards());
		}
		return cards;
	}
	
	public Attacker getAttackerByPlayerId(int playerId) {
		for (Attacker attacker : attackers) {
			if (attacker.getPlayerId() == playerId) {
				return attacker;
			}
		}
		return null;
	}
	
	/*
	 * General function to add card to a battle
	 */
	public void addAttackCard(int playerId, Card card) {
		Attacker attacker = getAttackerByPlayerId(playerId);
		if (attacker == null) {
			Attacker newAttacker = new Attacker(playerId, card);
			
			// Place at top of list
			attackers.add(0, newAttacker);
		} else {
			attacker.addCard(card);
			
			// Move to top of list to maintain order
			attackers.remove(attacker);
			attackers.add(0, attacker);
		}
	}
	

	
	// TODO: Implement
	public boolean isBattleOver(int currentPlayerId) {
		// If we've gone an entire round and the same player is winning
		return false;
	}
	
	/*
	 * Broken. TODO: doesn't account for trumps yet
	 */
	public int calcCurrentWinner() {
		/*
		if (pShips.size() == 0) {
			return mShipPlayerId;
		}
		else if (pShips.size() == 1){
			return pShips.get(0).getPlayerId();
		}
		else {
			// calculate score for each player
			// if single highest score, declare winner
			// else if tie, return -1;
			// TODO: Needs to be rewritten for trumps
			
			ArrayList<Integer[]> score = new ArrayList<Integer[]>();
			int hScore = 0;
			// sum all the scores and record the highest score
			for (Attacker a: pShips) {
				if (a.getCardSet().hasTrump()) {
					score.add(new Integer[]{a.getPlayerId(), Integer.MAX_VALUE, a.getOrder()});
					hScore = Integer.MAX_VALUE;
				} 
				else {
					score.add(new Integer[]{a.getPlayerId(), a.getCardSet().sumPShips(), a.getOrder()});
					if (hScore < a.getCardSet().sumPShips()) {
						hScore = a.getCardSet().sumPShips();
					}
				}
			}
			
			int winningPlayerIndex = 0;
			
			if (hScore == Integer.MAX_VALUE) {
				// trump found, find the one with the highest order
				int hOrder = 0;
				
				for (Integer[] i: score) {
					if ((i[1] == Integer.MAX_VALUE) && (i[2] > hOrder)) {
						hOrder = i[2];
						winningPlayerIndex = score.indexOf(i);
					}
				}
				return score.get(winningPlayerIndex)[0];
			}
			else {
				// no trump but need to check for stalemates
				// Count number of highest scores
				// if 1, return winner
				// if more than 1, return -1
				
				int numHScores = 0;

				
				// count number of highest scores
				for (Integer[] i: score) {
					if (i[1] == hScore) {
						numHScores++;
						winningPlayerIndex = score.indexOf(i);
					}
				}
				
				if (numHScores == 1) {
					return score.get(winningPlayerIndex)[0];
				}
				else {
					return -1;
				}
			}
		}*/
		return 0;
	}

	
	public String toString() {
		String toPrint = new String();
		
		toPrint += "P" + ownerPlayer.getId() + ":" + merchantShip.toString();
		
		for (Attacker attacker : attackers){
			toPrint += " | ";
			toPrint += attacker.getAttackCards().toString();
		}
		
		return toPrint;
	}
}
