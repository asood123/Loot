package com.game.loot;

public class VirtualPlayer extends Player {

	private CardSet hand;
	
	public VirtualPlayer(String name) {
		super(name);
		hand = new CardSet();
	}

	@Override
	public int getHandCount() {
		return hand.getCount();
	}

	@Override
	public Move getNextMove(GameState gm) {
		// Always draw for now
		return new Move(ACTION.DRAW, null, null);
	}

	@Override
	public void addCard(Card card) {
		hand.addCard(card);
		System.out.println("I'm " + getName() + " and I just got card: " + card + "  and my hand is now:  " + hand);
	}

	@Override
	public void removeCard(Card card) {
		hand.removeCard(card);
		System.out.println("I'm " + getName() + " and I just removed card: " + card + "  and my hand is now:  " + hand);
	}

}
