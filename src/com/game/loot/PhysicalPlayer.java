package com.game.loot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.game.loot.Move.ACTION;

public class PhysicalPlayer extends Player {

	private int handCount;
	private CardSet hand;
	private boolean virtualGamePlay;
	private int deckSize;
	
	public PhysicalPlayer(String name, boolean virtualGamePlay) {
		super(name);
		this.handCount = 0;
		this.virtualGamePlay = virtualGamePlay;
		this.hand = new CardSet();
		
		if (virtualGamePlay) {
			deckSize = 100;
		}
	}
	
	public int getDeckSize(){
		if (virtualGamePlay) {
			return deckSize;
		}
		return -1;
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
		boolean done = false;
		
		if (virtualGamePlay) {
			System.out.println(getName() + "'s Hand: " + hand);
			updateDeckSize(gameState.getPlayers());
			System.out.println("Undrawn cards: " + getDeckSize());
		}
		
		while (!done){
			try {
				System.out.println("What would you like to do? Ex: M5, P2, TA, [Enter] (to draw), etc.");
				String input = br.readLine();

				if (input.equalsIgnoreCase("d") || input.equalsIgnoreCase("draw")
						|| input.equalsIgnoreCase("")) {
					if (deckSize > 0 || !virtualGamePlay) {
						return new Move(ACTION.DRAW, null, null);
					}
					else {
						System.out.println("No cards left to draw. Pick another move.");
					}
				}
				else if (hand.findCardFromString(input) != null || !virtualGamePlay){
					Card playCard;
					if (virtualGamePlay) {
						playCard = hand.findCardFromString(input);
					}
					else {
						playCard = Card.stringToCard(input);
					}
	            	// If we're playing a Merchant Ship, return that Move
	            	if (playCard != null && playCard instanceof MerchantShip) {
	            		return new Move(ACTION.PLAY_MERCHANT_SHIP, playCard, null);
	            	}
	            	
	            	if (deckSize == 0) {
	            		System.out.println("Do you want to discard " + playCard + "? Y/N");
	            		input = br.readLine();
	            		if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
	            			return new Move(ACTION.DISCARD, playCard, null);
	            		}
	            	}

            		Battle battle = null;
        			while (battle == null) {
        				List<Battle> battleList = gameState.getBattleList();

        				if (battleList.size() == 1) {
        					battle = battleList.get(0);
        				} else {
        					System.out.println("Which battle do you want to attack?");
            				int battleId = Integer.parseInt(br.readLine());
            				battle = gameState.findBattleById(battleId);
            				if (battle == null) {
            					System.out.println("That battle wasn't found, please enter another.");
            				}
        				}
        			}                  	
                	
        			return new Move(ACTION.PLAY_ATTACK, playCard, battle);
				}
				else {
					throw new Exception();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Input unclear. Try again!");
			}
		}
		return null;
		
	}

	@Override
	public void addCard(Card card) {
		if (virtualGamePlay) {
			hand.addCard(card);
			System.out.println("I'm " + getName() + " and I just got card: " + card + "  and my hand is now:  " + hand);
		} else {
			handCount++;
		}
	}

	@Override
	public void removeCard(Card card) {
		if (virtualGamePlay) {
			hand.removeCard(card);
			System.out.println("I'm " + getName() + " and I just removed card: " + card + "  and my hand is now:  " + hand);
		} else {
			handCount--;
		}
	}
	
	@Override
	public int getFinalPoints(){
		
		if (virtualGamePlay) {
			return getPoints() - hand.sumMShips();
		}
		else {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
			boolean done = false;
			try {
				while (!done) {
					System.out.println("Enter the sum of merchant ships left in " + getName() + "'s hand: ");			
					int i = Integer.parseInt(br.readLine());
				
					if (i >= 0 && i < 100){
						done = true;
						System.out.println("Reaching here.");
						return getPoints() - i;
					}
					else {
						System.out.println("Invalid entry. Please try again.");
					}
				}
				
			} catch(NumberFormatException nfe){
	          System.err.println("Invalid number!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		
	}
	
	public void updateDeckSize(List<Player> players){
		if (deckSize == 100) {
			// initialize it
			deckSize = 78-(players.size())*6;
		}
		for (Player p: players) {
			if (p.getLastMove() != null){
				if (p.getLastMove().getAction() == ACTION.DRAW) {
					deckSize--;
				}
			}
		}
	}

	@Override
	public void init(GameState gm) {
		// TODO Auto-generated method stub
		
	}
}
