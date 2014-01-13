package com.game.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardSet {

	private ArrayList<Card> cards;
	//private int count;
	
	
	public CardSet(){
		cards = new ArrayList<Card>();
		//count = 0;
	}
	
	public CardSet(ArrayList<Card> cardset) {
		cards = new ArrayList<Card>();
		cards.addAll(cardset);
		//count = cards.size();
	}
	
	public CardSet(CardSet c){
		cards = new ArrayList<Card>(c.cards);
		//count = cards.size();
	}
	
	
	
	public static CardSet merge(CardSet c1, CardSet c2) {
		CardSet c = new CardSet(c1);
		c.addCardSet(c2);
		return c;
	}
	
	public void addCardSet(CardSet cs) {
		cards.addAll(cs.getCards());
	}
	
	public void removeCardSet(CardSet cs){
		for (Card c: cs.getCards()){
			removeCard(c);
		}
	}
	
	// Getters
	public int getCount(){
		return cards.size();
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	// Card transactions: add, remove, contains
	public void addCard(Card c) {
		cards.add(c);
	}
	
	public boolean removeCard(Card c) {
		Card r = null;
		if (c == null) {
			return false;
		}
		for (Card l: cards) {
			if (c instanceof MerchantShip) {
				if ((l instanceof MerchantShip) && (l.getValue() == c.getValue())){
					r = l;
					break;
				}
			}
			else if (c instanceof PirateShip) {
				if ((l instanceof PirateShip) && (l.getValue() == c.getValue())){
					if (((PirateShip)c).getColor() == ((PirateShip)l).getColor()){
						r = l;
						break;
					}
				}
			}
			else {
				if (l instanceof Trump){
					if (((Trump)c).getColor() == ((Trump)l).getColor()){
						r = l;
						break;
					}
				}
			}
		}
		if (r != null) {
			cards.remove(r);
			return true;
		}
		return false;

	}
	
	public Card findCardFromString(String searchString) {
		for (Card card : cards) {
			if (card.toStringWithoutANSI().equalsIgnoreCase(searchString)) {
				return card;
			}
		}
		return null;
	}
	
	public boolean hasCard(Card c) {
		if (c == null) {
			return false;
		}
		for (Card l: cards) {
			if (c instanceof MerchantShip) {
				if ((l instanceof MerchantShip) && (l.getValue() == c.getValue())){
					return true;
				}
			}
			else if (c instanceof PirateShip) {
				if ((l instanceof PirateShip) && (l.getValue() == c.getValue())){
					if (((PirateShip)c).getColor() == ((PirateShip)l).getColor()){
						return true;
					}
				}
			}
			else {
				if (l instanceof Trump){
					if (((Trump)c).getColor() == ((Trump)l).getColor()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static CardSet addFullDeck(){
		CardSet cs = new CardSet();
		
		// Merchant Ships
		for (int i=0;i<5;i++){
			cs.addCard(new MerchantShip(2));
			cs.addCard(new MerchantShip(3));
			cs.addCard(new MerchantShip(4));
			cs.addCard(new MerchantShip(5));
		}
		cs.addCard(new MerchantShip(3)); // 6th merchantship of value 3		
		cs.addCard(new MerchantShip(6));
		cs.addCard(new MerchantShip(6));		
		cs.addCard(new MerchantShip(7));
		cs.addCard(new MerchantShip(8));
		
		// Pirate Ships
		for (Color c: Color.values()) {
			if (c != Color.Admiral) {
				cs.addCard(new PirateShip(1, c));
				cs.addCard(new PirateShip(1, c));
				cs.addCard(new PirateShip(2, c));
				cs.addCard(new PirateShip(2, c));
				cs.addCard(new PirateShip(2, c));
				cs.addCard(new PirateShip(2, c));
				cs.addCard(new PirateShip(3, c));
				cs.addCard(new PirateShip(3, c));
				cs.addCard(new PirateShip(3, c));
				cs.addCard(new PirateShip(3, c));
				cs.addCard(new PirateShip(4, c));
				cs.addCard(new PirateShip(4, c));
			}
		}
		
		// Trumps
		for (Color c: Color.values()) {
			cs.addCard(new Trump(c));
		}
		return cs;
	}
	
	// Card subset helper functions
	
	public int sumMShips(){
		int count = 0;
		for(Card c: cards) {
			if (c instanceof MerchantShip){
				count += c.getValue();
			}
		}
		return count;
	}
	
	public int sumPShips(){
		int count = 0;
		for(Card c: cards) {
			if (c instanceof PirateShip){
				count += c.getValue();
			}
		}
		return count;
	}

	public boolean hasTrump(){
		for(Card c: cards) {
			if (c instanceof Trump){
				return true;
			}
		}
		return false;
	}
	

	public boolean hasMShip(){
		for (Card c: cards){
			if (c instanceof MerchantShip) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPShip(Color color) {
		for (Card c: cards) {
			if ((c instanceof PirateShip) &&
					((PirateShip)c).getColor() == color) {
				return true;
			}
		}
		return false;
	}
	
	public int numMShips(){
		int count = 0;
		for(Card c: cards) {
			if (c instanceof MerchantShip){
				count++;
			}
		}
		return count;
	}
	public int numPShips(){
		int count = 0;
		for(Card c: cards) {
			if (c instanceof PirateShip){
				count++;
			}
		}
		return count;
	}
	public int numTrumps(){
		int count = 0;
		for(Card c: cards) {
			if (c instanceof Trump){
				count++;
			}
		}
		return count;
	}
	
	public String toString(){
		String mShip = new String();
		String pShipBlue = new String();
		String pShipGreen = new String();
		String pShipPurple = new String();
		String pShipYellow = new String();
		String trump = new String();
			
		for (Card c: cards) {
			if (c instanceof MerchantShip) {
				mShip += c + " ";
			}
			else if (c instanceof PirateShip) {
				Color color = ((PirateShip)c).getColor();
				if (color == Color.Blue) {
					pShipBlue += c + " ";
				}
				else if (color == Color.Green) {
					pShipGreen += c + " ";
				}
				else if (color == Color.Purple) {
					pShipPurple += c + " ";
				}
				else if (color == Color.Yellow) {
					pShipYellow += c + " ";
				}
			}
			else {
				trump += c + " ";
			}
		}
		
		if (mShip.length() + pShipBlue.length() + pShipGreen.length() 
				+ pShipPurple.length() + pShipYellow.length() 
				+ trump.length() > 0) {
			return mShip + pShipBlue + pShipGreen + pShipPurple + pShipYellow
					+ trump;
		}
		else {
			return "--";
		}
	}
	
	/*
	 * Returns highest merchant ship
	 */
	
	public MerchantShip hMShip(){
		MerchantShip m = null;
		for (Card c: cards){
			if (c instanceof MerchantShip){
				if (m == null){
					m = (MerchantShip)c;
				}
				else {
					if (m.getValue() < c.getValue()){
						m = (MerchantShip)c;
					}
				}
			}
		}
		return m;
	}
	
	/*
	 * Returns lowest merchant ship
	 */
	public MerchantShip lMShip(){
		MerchantShip m = null;
		for (Card c: cards){
			if (c instanceof MerchantShip){
				if (m == null){
					m = (MerchantShip)c;
				}
				else {
					if (m.getValue() > c.getValue()){
						m = (MerchantShip)c;
					}
				}
			}
		}
		return m;
	}
	
	public CardSet pShipsSet(Color l){
		// returns subset of pirateships of certain color
		CardSet cs = new CardSet();
		
		for (Card c: cards) {
			if (c instanceof PirateShip) {
				if (((PirateShip)c).getColor() == l){
					cs.addCard(c);
				}
			}
		}
		return cs;
	}
	
	// test/debug functions
	
	public void printCardSetStats(){
		System.out.println("Total cards: " + getCount());
		System.out.println("Total mShips: " + numMShips());
		System.out.println("Total pShips: " + numPShips());
		System.out.println("Total tShips: " + numTrumps());
		System.out.println("Sum mShips: " + sumMShips());
		System.out.println("Sum pShips: " + sumPShips());
	}
	
	public void printAllCards(){
		String mShips = new String("Merchant Ships: ");
		String pShips = new String("Pirate Ships: ");
		String trumps = new String("Trumps: ");
		
		for (Card c: cards) {
			if (c instanceof MerchantShip){
				mShips += c.toString() + " ";
			}
			else if (c instanceof PirateShip){
				pShips += c.toString() + " ";
			}
			else if (c instanceof Trump){
				trumps += c.toString() + " ";
			}
			else {
				System.out.println("Unknown card type found.");
				return;
			}
		}
		System.out.println(mShips + ".");
		System.out.println(pShips + ".");
		System.out.println(trumps + ".");
	}
	
	public Card getRandCard(){
		List<Card> cards = getCards();
		if (cards.size() == 0) {
			System.err.println("ERROR: No cards in set");
			return null;
		}
		Random r = new Random();
		int n = r.nextInt(cards.size());
		return cards.get(n);
	}
}
