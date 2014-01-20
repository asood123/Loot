package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public abstract class GamePlay {
	GameState gameState;
	boolean verbose;
	
	GamePlay(GameState gameState) {
		this.gameState = gameState;
		this.verbose = true;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setVerbosity(boolean v) {
		verbose = v;
	}
	
	public abstract void playerDrawsCard(Player player, CardSet deck);
	
	boolean isEndOfGame() {		
		if (gameState.getDeck().getCount() == 0) {
			// if decksize is 0
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
			playerDrawsCard(player, gameState.getDeck());
			break;
		case PLAY_MERCHANT_SHIP:
			MerchantShip ship = (MerchantShip) move.getCard();
			player.removeCard(move.getCard());
			
			gameState.getBattleList().add(new Battle(ship, player, gameState.getMoveCount()));			
			break;
		case PLAY_ATTACK:
			Battle battle = move.getBattle();
			AttackCard attackCard = (AttackCard) move.getCard();

			if (!battle.addAttackCard(player, attackCard, gameState.getMoveCount())) {
				// Not a valid attack
				return false;
			}
			
			player.removeCard(attackCard);
			break;
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
			if (battle.isBattleOver(player.getId(), gameState.getMoveCount(), gameState.getPlayers().size())) {
				if (battle.getWinningPlayer() == player) {
					doneBattles.add(battle);
				} else {
					System.out.println("BUG? Battle " + battle.getId() + " is over, but " + player.getName() + " wasn't the winner");
				}
			}
		}
		
		// For each done battle, remove from list, discard cards, update player
		for (Battle battle : doneBattles) {
			if (verbose){
				System.out.println("Battle #" + battle.getId() + " is finished!");
			}
			// Remove battle from ongoing battles
			ongoingBattles.remove(battle);
			
			// Add all cards to discard
			CardSet cards = battle.getAllCards();
			gameState.getDiscardCards().addCardSet(cards);
			
			// Award merchant cards to current player
			player.addMerchantShipWon(battle.getMerchantShip());
			if (verbose) {
				System.out.println(player.getName() + " got a ship worth " + battle.getMerchantShip().getValue() + " points and now has " + player.getPoints());
			}
		}		
	}
}
