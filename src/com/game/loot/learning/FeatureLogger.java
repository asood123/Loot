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
public abstract class FeatureLogger {
	Map<String, PrintWriter> featureWriters;
	Map<String, PrintWriter> outputWriters;
	Map<String, List<Features>> playerFeatures;
	
	public FeatureLogger(List<Player> players, String featureFilename, String outputFilename, Features featureInst) {
		featureWriters = new HashMap<String, PrintWriter>();
		outputWriters = new HashMap<String, PrintWriter>();
		
		playerFeatures = new HashMap<String, List<Features>>();
		
		for (Player player : players) {
			playerFeatures.put(player.getName(), new ArrayList<Features>());
			try {
				featureWriters.put(player.getName(), new PrintWriter(new BufferedWriter(new FileWriter(featureFilename + "-" + player.getName(), false))));
				outputWriters.put(player.getName(), new PrintWriter(new BufferedWriter(new FileWriter(outputFilename + "-" + player.getName(), false))));
				
				featureWriters.get(player.getName()).print("#");
				featureWriters.get(player.getName()).println(featureInst.getFeatureHeaders());
				outputWriters.get(player.getName()).print("#");
				outputWriters.get(player.getName()).println(featureInst.getOutputHeader());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		for (Player player : players) {
			
		}
	}
	
	public abstract void logMove(GameState gm, Move move, Player player);
	
	/**
	 * output all pending data to 
	 * @param player
	 */
	public void outputToFile() {
		for (String name : featureWriters.keySet()) {
			for (Features features : playerFeatures.get(name)) {
				featureWriters.get(name).println(features.getFeaturesString());
				outputWriters.get(name).println(features.getOutputString());
			}
			featureWriters.get(name).close();
			outputWriters.get(name).close();
		}
	}
	
}
