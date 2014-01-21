package com.game.loot.learning;

import java.util.List;

import com.game.loot.Battle;
import com.game.loot.GameState;
import com.game.loot.Move;
import com.game.loot.Player;

public class WhichMoveLogger extends FeatureLogger {

	public WhichMoveLogger(List<Player> players, String featureFilename,
			String outputFilename, Features featureInst) {
		super(players, featureFilename, outputFilename, featureInst);
	}

	@Override
	public void logMove(GameState gm, Move move, Player player) {
		int numCardsInHand = player.getHandCount();
		
		WhichMoveFeatures features = new WhichMoveFeatures(numCardsInHand);
		
		switch(move.getAction()) {
		case DRAW:
			features.setOutput(0);
			break;
		case DISCARD:
			features.setOutput(1);
			break;
		case PLAY_MERCHANT_SHIP:
			features.setOutput(2);
			break;
		case PLAY_ATTACK:
			int index = 3;
			for (Battle battle : gm.getBattleList()) {
				if (battle == move.getBattle()) {
					features.setOutput(index);
				}
				index++;
			}
			break;
		}
		
		playerFeatures.get(player.getName()).add(features);
	}
}
