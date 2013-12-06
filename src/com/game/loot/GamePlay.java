package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public abstract class GamePlay {
	GameState gameState;
	CardSet deck;
	
	GamePlay(GameState gameState, CardSet deck) {
		this.gameState = gameState;
		this.deck = deck;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public abstract void playerDrawsCard(Player player, CardSet deck);
	
	boolean isEndOfGame() {
		if (deck.getCount() == 0){ // if decksize is 0
			// and any player is down to zero cards
			for (Player player : gameState.getPlayers()) {
				if (player.getHandCount() == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean executeMove(Player player, Move move) {
		switch(move.getAction()) {
		case DRAW:
			playerDrawsCard(player, deck);
			break;
		case PLAY_MERCHANT_SHIP:
			MerchantShip ship = (MerchantShip) move.getCard();
			player.removeCard(move.getCard());
			
			gameState.getBattleList().add(new Battle(ship, player));			
			break;
		case PLAY_ATTACK:
			Battle battle = move.getBattle();
			AttackCard attackCard = (AttackCard) move.getCard();

			if (!battle.addAttackCard(player, attackCard)) {
				// Not a valid attack
				return false;
			}
			
			player.removeCard(attackCard);
		case DISCARD:
			Card discardCard = move.getCard();
			player.removeCard(discardCard);
			gameState.getDiscardCards().addCard(discardCard);
		default:
			break;
		}
		return true;
	}
	
	public void collectMerchantShips(Player player) {
		List<Battle> doneBattles = new ArrayList<Battle>();
		List<Battle> ongoingBattles = gameState.getBattleList();
		
		// See if any battles are won by current player
		for (Battle battle : ongoingBattles) {
			if (battle.isBattleOver(player.getId())) {
				doneBattles.add(battle);
			}
		}
		
		// For each done battle, remove from list, discard cards, update player
		for (Battle battle : doneBattles) {
			// Remove battle from ongoing battles
			ongoingBattles.remove(battle);
			
			// Add all cards to discard
			CardSet cards = battle.getAllCards();
			gameState.getDiscardCards().addCardSet(cards);
			
			// Award merchant cards to current player
			player.addMerchantShipWon(battle.getMerchantShip());
		}		
	}
}
