package com.game.loot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public abstract class Player {
	private int id;
	private String name;
	private CardSet merchantShipsWon;
	private ArrayList<Move> moves;
	
	public static int playerCount = 1;

	public Player(String name) {
		this.id = playerCount++;
		this.name = name;
		merchantShipsWon = new CardSet();
		moves = new ArrayList<Move>();
	}
	
	public String toString(){
		return name;
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
	
	public abstract void init(GameState gm);

	public abstract Move getNextMove(GameState gm);
}
