package com.game.loot;

import java.util.ArrayList;

public class Battle {
	private MerchantShip mShip;
	private int mShipPlayerId;
	private ArrayList<Attacker> pShips;
	private int currentWinner;
	private int id;
	private static int bCount = 1;
	private int order; // to track trump order
	
	public Battle(MerchantShip m, int playerId, ArrayList<Attacker> pirates){
		mShip = m;
		mShipPlayerId = playerId;
		pShips = pirates;
		id = bCount;
		bCount++;
		currentWinner = calcCurrentWinner();
		order = 1;
	}
	
	public Battle(MerchantShip m, int playerId){
		this(m, playerId, new ArrayList<Attacker>());
	}
	
	// getters
	public int getId(){
		return id;
	}
	
	public MerchantShip getMShip(){
		return mShip;
	}
	
	public int getMShipPlayerId(){
		return mShipPlayerId;
	}
	
	public ArrayList<Attacker> getPShips(){
		return pShips;
	}
	
	/*
	 * Broken. TODO: doesn't account for trumps yet
	 */
	
	public int calcCurrentWinner(){
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
		}
	}
	
	/*
	 * General function to add card to a battle
	 */
	
	public void addCard(Card c, int pId){
		// if this player is already in battle, add to their attacker element
		// else create a new one

		Attacker a = findAttacker(pId);
		if (a != null){
			a.addCard(c, order++);
		}
		else {
			addAttacker(c, pId);
		}
	}
	
	public void addAttacker(Card c, int pId) {
		pShips.add(new Attacker(c, pId, order++));
		currentWinner = calcCurrentWinner();
	}
	
	public CardSet allCards(){
		CardSet cards = new CardSet();
		
		cards.addCard(mShip);
		for (Attacker a: pShips){
			cards.addCardSet(a.getCardSet());
		}
		return cards;
	}
	
	public String toString(){
		String s = new String();
		
		s += "P" + mShipPlayerId + ":" + mShip.toString();
		
		for (Attacker a: pShips){
			s+= " | ";
			s+= a.getCardSet().toString();
		}
		
		return s;
	}
	
	// find functions
	
	private Attacker findAttacker(int pId){
		for (Attacker a: pShips){
			if (a.getPlayerId() == pId) {
				return a;
			}
		}
		return null;
	}
}
