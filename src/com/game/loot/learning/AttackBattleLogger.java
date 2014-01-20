package com.game.loot.learning;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.loot.Battle;
import com.game.loot.GameState;
import com.game.loot.Move;
import com.game.loot.Move.ACTION;
import com.game.loot.Player;

/**
 * Class that will log information around if a player attacks a battle or not along with the features about the gamestate at that point.
 * This should be created at the start of the set of games (like 1000) and outputToFile called at the end.  This way we're not
 * slowing down execution for disk writes.
 * 
 * @author derek
 *
 */
public class AttackBattleLogger {
	Map<String, List<AttackBattleFeatures>> playerAttackBattleFeatures;
	Map<String, PrintWriter> featureWriters;
	Map<String, PrintWriter> outputWriters;
	
	public AttackBattleLogger(List<Player> players, String featureFilename, String outputFilename) {
		playerAttackBattleFeatures = new HashMap<String, List<AttackBattleFeatures>>();
		featureWriters = new HashMap<String, PrintWriter>();
		outputWriters = new HashMap<String, PrintWriter>();
		for (Player player : players) {
			playerAttackBattleFeatures.put(player.getName(), new ArrayList<AttackBattleFeatures>());
			try {
				featureWriters.put(player.getName(), new PrintWriter(new BufferedWriter(new FileWriter(featureFilename + "-" + player.getName(), false))));
				outputWriters.put(player.getName(), new PrintWriter(new BufferedWriter(new FileWriter(outputFilename + "-" + player.getName(), false))));
				
				featureWriters.get(player.getName()).print("#");
				featureWriters.get(player.getName()).println(AttackBattleFeatures.getFeatureHeaders());
				outputWriters.get(player.getName()).print("#");
				outputWriters.get(player.getName()).println(AttackBattleFeatures.getOutputHeader());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * Log a move to memory, we'll output to a file later on.
	 * @param gm
	 * @param move
	 * @param player
	 */
	public void logMove(GameState gm, Move move, Player player) {
		int numCardsInHand = player.getHandCount();
		
		
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
					battle.getWinningPlayer() == null ? true : false);
			
			boolean attacked = false;
			// If the player attacks this battle, that's the output we're tracking
			if (move.getAction() == ACTION.PLAY_ATTACK && move.getBattle() == battle) {
				attacked = true;
			}
			features.setAttacked(attacked);

			playerAttackBattleFeatures.get(player.getName()).add(features);
		}	
	}
	
	/**
	 * output all pending data to 
	 * @param player
	 */
	public void outputToFile() {
		for (String name : featureWriters.keySet()) {
			for (AttackBattleFeatures features : playerAttackBattleFeatures.get(name)) {
				featureWriters.get(name).println(features.getFeaturesString());
				outputWriters.get(name).println(features.getOutputString());
			}
			featureWriters.get(name).close();
			outputWriters.get(name).close();
		}
	}
}
