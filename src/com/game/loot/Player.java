package com.game.loot;

import java.util.ArrayList;

public class Player {
	private String name;
	private CardSet mShips;
	private int currentHandCount;
	private int id;
	private static int pCount = 1;
	private ArrayList<Battle> activeBattles;

	public Player(String n, CardSet merchantShips, int hand){
		name = n;
		mShips = merchantShips;
		currentHandCount = hand;
		id = pCount;
		pCount++;
		activeBattles = new ArrayList<Battle>();
	}
	
	public Player(){
		this("Jack Sparrow" + pCount, new CardSet(), 6);
	}
	
	public Player(String n){
		this(n, new CardSet(), 6);
	}
	
	// Getters
	public String getName(){
		return name;
	}
	
	public CardSet getMShips(){
		return mShips;
	}
	
	public int getHandCount(){
		return currentHandCount;
	}
	
	public int getId(){
		return id;
	}
	
	public int getPCount(){
		return pCount;
	}
	
	public ArrayList<Battle> getActiveBattles(){
		return activeBattles;
	}
	
	// Setters
	
	public void setName(String n){
		name = n;
	}
	
	public void insertCardInHand(){
		currentHandCount++;
	}
	
	public void removeCardFromHand(){
		currentHandCount--;
	}
	
	public void addBattle(Battle b){
		activeBattles.add(b);
	}
	
	public void addMShip(MerchantShip m){
		mShips.addCard(m);
	}
	
	public void removeBattle(Battle b){
		activeBattles.remove(b);
	}

	// Other
	
	public boolean isPartOfBattle(int battleId){
		for (Battle b: activeBattles){
			if (b.getId() == battleId){
				return true;
			}
		}
		return false;
	}
	
	// test/debug functions
	
	public void printPlayerStats(){
		System.out.println("N: " + name + " | Id: " + id);
		System.out.println("handCount: " + currentHandCount);
		System.out.println("mShips: " + mShips.getCount());
		System.out.println("activeBattles: "+ activeBattles.size());
		System.out.println("");
		
	}
}
