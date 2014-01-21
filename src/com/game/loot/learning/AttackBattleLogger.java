package com.game.loot.learning;

import java.util.List;

import com.game.loot.Battle;
import com.game.loot.GameState;
import com.game.loot.Move;
import com.game.loot.Move.ACTION;
import com.game.loot.Player;

public class AttackBattleLogger extends FeatureLogger {
	
	
	public AttackBattleLogger(List<Player> players, String featureFilename, String outputFilename, Features featureInst) {
		super(players, featureFilename, outputFilename, featureInst);
		
	}
	/**
	 * Log a move to memory, we'll output to a file later on.
	 * @param gm
	 * @param move
	 * @param player
	 */
	@Override
	public void logMove(GameState gm, Move move, Player player) {
		int numCardsInHand = player.getHandCount();
		
		int index = 0;
		for (Battle battle : gm.getBattleList()) {
			int highScore = battle.getHighScore();
			if (highScore == Integer.MAX_VALUE) {
				highScore = 20;
			}
			
			int drawMoves = 6;
			int attackMoves = 0; 
			int merchantMoves = 0;
			for (Move m : player.getMoves()) {
				if (m.getAction() == ACTION.DRAW) {
					drawMoves++;
				} else if (m.getAction() == ACTION.PLAY_ATTACK) {
					attackMoves++;
				} else if (m.getAction() == ACTION.PLAY_MERCHANT_SHIP) {
					merchantMoves++;
				}
			}
			
			AttackBattleFeatures features = new AttackBattleFeatures(numCardsInHand,
					battle.getMerchantShip().getValue(),
					battle.getAttackers().size(),
					highScore,
					attackMoves,
					drawMoves,
					merchantMoves,
					gm.getDeck().getCount(),
					gm.getDiscardCards().getCount(),
					battle.getWinningPlayer() == null ? true : false,
					index,
					gm.getBattleList().size());
			
			boolean attacked = false;
			// If the player attacks this battle, that's the output we're tracking
			if (move.getAction() == ACTION.PLAY_ATTACK && move.getBattle() == battle) {
				attacked = true;
			}
			features.setOutput(attacked ? 1 : 0);

			playerFeatures.get(player.getName()).add(features);
			index++;
		}	
	}

}
