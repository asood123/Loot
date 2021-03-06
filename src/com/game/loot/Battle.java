package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public class Battle{

	private MerchantShip merchantShip;
	private List<Attacker> attackers;
	private Attacker winningAttacker;
	private Attacker ownerAttacker;
	private int id;
	private static int battleCount = 1;
	private boolean verbose;

	
	public Battle(MerchantShip merchantShip, Player ownerPlayer, int moveNum) {
		this.merchantShip = merchantShip;
		ownerAttacker = new Attacker(ownerPlayer, moveNum);
		
		attackers = new ArrayList<Attacker>();
		attackers.add(ownerAttacker);

		winningAttacker = ownerAttacker;
		
		id = battleCount++;
		verbose = false;
	}
	
	public Battle(Battle b) {
		this.merchantShip = b.getMerchantShip();
		ownerAttacker = new Attacker(b.getAttackerByPlayerId(b.getOwnerPlayerId()));
		attackers = new ArrayList<Attacker>();
		attackers.add(ownerAttacker);
		for (Attacker a: b.getAttackers()) {
			if (a != b.getAttackerByPlayerId(b.getOwnerPlayerId())){
				attackers.add(new Attacker(a));
			}
		}
		winningAttacker = ownerAttacker;
		winningAttacker = calcCurrentWinner();
		id = battleCount++;
		this.verbose = b.getVerbosity(); 
	}
	
	// getters
	public int getId(){
		return id;
	}
	
	public boolean getVerbosity(){
		return verbose;
	}
	
	public MerchantShip getMerchantShip() {
		return merchantShip;
	}
	
	public int getOwnerPlayerId() {
		return ownerAttacker.getPlayer().getId();
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
			if (attacker.getPlayer().getId() == playerId) {
				return attacker;
			}
		}
		return null;
	}
	
	public List<Attacker> getAttackers() {
		return attackers;
	}
	
	public Attacker getWinningAttacker(){
		return winningAttacker;
	}
	
	public int getHighScore(){
		// returns the sum of the highest attack cards
		
		int hScore = 0;
		int temp = 0;
		
		for (Attacker a: attackers) {
			temp = a.getScore();
			if (temp == Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
			if (temp > hScore) {
				hScore = temp;
			}
		}
		return hScore;
	}
	
	// setter
	
	public void setVerbosity(boolean v) {
		verbose = v;
	}
	
	protected boolean isColorInUse(Color c) {
		// Make sure that no other attacker is using this color
		for (Attacker a : attackers) {
			ArrayList<Card> attackerCards = a.getAttackCards().getCards();
						
			if (attackerCards.size() > 0) {
				AttackCard attackCard = (AttackCard) attackerCards.get(0);
				if (attackCard.getColor() == c) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * General function to add card to a battle
	 */
	public boolean addAttackCard(Player player, AttackCard card, int moveNum) {
		Attacker attacker = getAttackerByPlayerId(player.getId());
		
		if (card.getColor() == Color.Admiral) {
			if (player != ownerAttacker.getPlayer()) {
				System.out.println("Can only play Admiral on ships that you started.");
				return false;
			}
			ownerAttacker.addCard(card, moveNum);
		} else if (attacker == null) {
			// If this is a new attacker, first make sure this color isn't in use yet
			if (isColorInUse(card.getColor())) {
				System.out.println("An attack card of that color has already been played in this battle.");
				return false;
			}
			
			attacker = new Attacker(player, card, moveNum);
			
			attackers.add(attacker);
		} else {
			// Existing attacker
			// Make sure this is the correct color for this player
			ArrayList<Card> attackerCards = attacker.getAttackCards().getCards();
			
			if (attackerCards.size() > 0) {
				AttackCard attackCard = (AttackCard) attackerCards.get(0);
				if (attackCard.getColor() != card.getColor()) {
					System.out.println("Need to play attack card of the same color");
					return false;
				}
			} else {
				// If this is the first time the merchant owner plays, make sure they pick a unique color
				if (isColorInUse(card.getColor())) {
					System.out.println("An attack card of that color has already been played in this battle.");
					return false;
				}
			}			
			
			attacker.addCard(card, moveNum);
		}
		
		winningAttacker = calcCurrentWinner();
		if (verbose) {
			System.out.println("Added " + card + " to battle " + getId() + ".");
			
			if (winningAttacker == null) {
				System.out.println("Its currently a tie");
			} else {
				System.out.println("Current winner is: " + winningAttacker.getPlayer().getName());
			}
		}
		
		return true;
	}
	
	public Player getWinningPlayer() {
		if (getWinningAttacker() != null) {
			return winningAttacker.getPlayer();
		}
		return null;
	}
	
	
	// NOTE:  Check the maxLastMoveNum
	public boolean isBattleOver(int currentPlayerId, int moveNum, int roundSize) {
		// If we've gone an entire round and the same player is winning
		int maxLastMoveNum = moveNum - roundSize;
		
		// If tie, not over
		if (winningAttacker == null) {
			return false;
		}
		
		if (winningAttacker.getLastMoveNum() <= maxLastMoveNum) {
			return true;
		}
		
		return false;
	}
	
	public boolean isBattleUsingTrumps() {
		for (Attacker a : attackers) {
			if (a.getAttackCards().hasTrump()) {
				return true;
			}
		}
		return false;
	}

	public Attacker calcCurrentWinner() {
		int numAttackers = attackers.size();
		
		if (numAttackers == 1) {
			return attackers.get(0);
		} 
		else if (isBattleUsingTrumps()) {
			Attacker currentWinningAttacker = null;
			// Loop through all players and see who has both a trump and the lastMoveNum
			for (Attacker a : attackers) {
				if (a.getAttackCards().hasTrump()) {
					if (currentWinningAttacker == null) {
						currentWinningAttacker = a;
					} else {
						if (a.getLastMoveNum() > winningAttacker.getLastMoveNum()) {
							currentWinningAttacker = a;
						}
					}
				}
			}
			
			return currentWinningAttacker;
		} 
		else {
			Attacker currentWinningAttacker = null;
			int winningScore = 0;
			boolean haveTie = false;
			
			// Loop through all players and see who has the highest ship count
			for (Attacker a : attackers) {
				int total = a.getAttackCards().sumPShips();
				
				if (total > winningScore) {
					currentWinningAttacker = a;
					winningScore = total;
					haveTie = false;
				} else if (total == winningScore) {
					haveTie = true;
				}
			}
			
			if (haveTie) {
				return null;
			} 
			else {
				return currentWinningAttacker;
			}
		}
	}
	
	public String toString() {
		String toPrint = new String();
		
		toPrint += getId() + ": " + merchantShip.toString();
		
		for (Attacker attacker : attackers){
			toPrint += " | ";
			toPrint += attacker.getPlayer().getName() + ": ";
			toPrint += attacker.getAttackCards().toString() + " ";
		}
		
		return toPrint;
	}

}
