package com.game.loot;

import java.util.ArrayList;



public abstract class Player {
	private int id;
	private String name;
	private CardSet merchantShipsWon;
	private ArrayList<Move> moves;
	private int numWins;
	private int pointsAcrossRounds;
	
	public static int playerCount = 1;

	public Player(String name) {
		this.id = playerCount++;
		this.name = name;
		setupForNewGame();
	}
	
	public void setupForNewGame() {
		merchantShipsWon = new CardSet();
		moves = new ArrayList<Move>();
	}
		
	// Getters
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPoints() {
		int points = 0;
		
		for (Card ship : merchantShipsWon.getCards()) {
			points += ship.getValue();
		}
		
		return points;
	}
	
	public abstract int getFinalPoints();
	
	public ArrayList<Move> getMoves(){
		return moves;
	}
	
	// setters
	
	public void addMove(Move m){
		moves.add(m);
	}
	
	public Move getLastMove(){
		if (moves.size() > 0) {
			return moves.get(moves.size()-1);
		}
		else return null;
	}

	public void addMerchantShipWon(MerchantShip card) {
		merchantShipsWon.addCard(card);
	}
	
	// abstract
	public abstract int getHandCount();
	
	public abstract void addCard(Card card);
	
	public abstract void removeCard(Card card);

	
	// testing/debugging
	public void printPlayerStats() {
		System.out.println("N: " + name + " | Id: " + id);
		System.out.println("handCount: " + getHandCount());
		System.out.println("points: " + getPoints());
		System.out.println("");
	}

	public abstract Move getNextMove(GameState gm);

	public int getNumWins() {
		return numWins;
	}

	public void addWin() {
		this.numWins++;
	}

	public int getPointsAcrossRounds() {
		return pointsAcrossRounds;
	}

	public void addPointsAcrossRounds(int pointsAcrossRounds) {
		this.pointsAcrossRounds += pointsAcrossRounds;
	}
}
