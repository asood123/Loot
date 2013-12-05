package com.game.loot;

import java.util.HashSet;
import java.util.Set;


public abstract class Player {
	private int id;
	private String name;
	private Set<MerchantShip> merchantShipsWon;
	
	public static int playerCount = 1;

	public Player(String name) {
		this.id = playerCount++;
		this.name = name;
		merchantShipsWon = new HashSet<MerchantShip>();
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
		
		for (MerchantShip ship : merchantShipsWon) {
			points += ship.getValue();
		}
		
		return points;
	}

	public void addMerchantShipWon(MerchantShip card) {
		merchantShipsWon.add(card);
	}
	
	public abstract int getHandCount();
	
	public abstract void addCard(Card card);
	
	public abstract void removeCard(Card card);


	public void printPlayerStats() {
		System.out.println("N: " + name + " | Id: " + id);
		System.out.println("handCount: " + getHandCount());
		System.out.println("points: " + getPoints());
		System.out.println("");
	}

	public abstract Move getNextMove(GameState gm);
}
