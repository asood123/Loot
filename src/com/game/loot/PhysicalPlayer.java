package com.game.loot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhysicalPlayer extends Player {

	private int handCount;
	private CardSet hand;
	private boolean virtualGamePlay;
	
	public PhysicalPlayer(String name, boolean virtualGamePlay) {
		super(name);
		this.handCount = 0;
		this.virtualGamePlay = virtualGamePlay;
		this.hand = new CardSet();
	}

	@Override
	public int getHandCount() {
		if (virtualGamePlay) {
			return hand.getCount();
		} else {
			return handCount;
		}
	}

	@Override
	public Move getNextMove(GameState gameState) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		
		
		if (virtualGamePlay) {
			System.out.println(getName() + "'s Hand:");
			System.out.println(hand);
		}
		
		System.out.println(ACTION.DRAW.ordinal() + ": Draw card");
		System.out.println(ACTION.PLAY_MERCHANT_SHIP.ordinal() + ": Play merchant ship");
		System.out.println(ACTION.PLAY_ATTACK.ordinal() + ": Play attack card");
		System.out.println(ACTION.DISCARD.ordinal() + ": Discard card");
		
        try {
            ACTION a = ACTION.values()[Integer.parseInt(br.readLine())];
            
            switch(a) {
            case DRAW:
            	return new Move(ACTION.DRAW, null, null);
            case PLAY_MERCHANT_SHIP:
            	Card playCard = null;
            	while (playCard == null) {
            		System.out.println("Which card?");
                	String cardString = br.readLine();
                	
                	playCard = hand.findCardFromString(cardString);
            	
	            	if (playCard != null) {
	            		if (playCard instanceof MerchantShip) {
	            			return new Move(ACTION.PLAY_MERCHANT_SHIP, playCard, null);
	            		} else {
	            			System.out.println("Didn't find that ship, please try again");
	            			playCard = null;
	            		}
	            	} else {
	            		System.out.println("Didn't find that ship, please try again");
	            	}
            	}
            	
            	break;
            case PLAY_ATTACK:
            	playCard = null;
            	while (playCard == null) {
            		System.out.println("Which card?");
                	String cardString = br.readLine();
                	
                	playCard = hand.findCardFromString(cardString);
            	
	            	if (playCard != null) {
	            		if (playCard instanceof AttackCard) {
	            			Battle battle = null;
	            			while (battle == null) {
	            				System.out.println("Which battle do you want to attack?");
	            				int battleId = Integer.parseInt(br.readLine());
	            				battle = gameState.findBattleById(battleId);
	            				if (battle == null) {
	            					System.out.println("That battle wasn't found, please enter another.");
	            				}
	            			}                  	
	                    	
	            			return new Move(ACTION.PLAY_ATTACK, playCard, battle);
	            		} else {
		            		System.out.println("Didn't find that ship, please try again");
		            		playCard = null;
	            		}
	            	} else {
	            		System.out.println("Didn't find that ship, please try again");
	            	}
            	}
            case DISCARD:
            	Card discardCard = null;
            	while (discardCard == null) {
            		System.out.println("Which card?");
                	String cardString = br.readLine();
                	
                	discardCard = hand.findCardFromString(cardString);
	            	if (discardCard != null) {
	            		return new Move(ACTION.DISCARD, discardCard, null);
	            	} else {
	            		System.out.println("Didn't find that card, please try again");
	            	}
            	}
            	break;
            }
        } catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        } catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addCard(Card card) {
		if (virtualGamePlay) {
			System.out.println("Just got card: " + card);
			hand.addCard(card);
		} else {
			handCount++;
		}
	}

	@Override
	public void removeCard(Card card) {
		if (virtualGamePlay) {
			hand.removeCard(card);
		} else {
			handCount--;
		}
	}
}
