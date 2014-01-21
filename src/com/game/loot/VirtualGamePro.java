package com.game.loot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.game.loot.learning.AttackBattleFeatures;
import com.game.loot.learning.AttackBattleLogger;
import com.game.loot.learning.FeatureLogger;
import com.game.loot.learning.WhichMoveFeatures;
import com.game.loot.learning.WhichMoveLogger;


/*
 * Allows you to run multiple games (through command line)
 * And reports count of wins and total points won
 */

public class VirtualGamePro {
		
	HashMap<String, Float> hTally;
	HashMap<String, Integer> hScoreTally;
	HashMap<String, Integer> hPenaltyTally;
	List<Player> players;
	List<Player> origPlayers;
	int totalGames;
	int gamesPlayed;
	Random rand;
	long startTime;
	public static final String ANSI_CSI = "\u001b[";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	VirtualGamePro(){		
		// setup tally
		rand = new Random();
		hTally = new HashMap<String, Float>();
		hScoreTally = new HashMap<String, Integer>();
		hPenaltyTally = new HashMap<String, Integer>();
		totalGames = 1;
		gamesPlayed = 0;
		startTime = System.currentTimeMillis();
		players = new ArrayList<Player>();
		origPlayers = new ArrayList<Player>();
	}
	
	public void readArgs(String[] args, List<Player> pList) throws IllegalArgumentException{
		if (args.length > 1) {
			for (int x = 1; x < args.length; x+=2) {
				//System.out.println("Adding " + args[x]);
				Class cls;
				try {
					cls = Class.forName("com.game.loot." + args[x]);
					Constructor parentConstructor =   cls.getConstructor(String.class);
					Player player = (Player)parentConstructor.newInstance(args[x+1]);
					pList.add(player);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (InstantiationException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (SecurityException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					System.exit(0);
				}  
			}
		} else {
			pList.add((Player) new RandomAI("Ender"));
			pList.add((Player) new RandomAI("Efficiency"));
			pList.add((Player) new RandomAI("Artemis"));
			pList.add((Player) new TriggerHappyAI ("Lowballer"));
		}
	}
	
	public void initializeNewGame(String[] args){
		// Add players
		ArrayList<Player> tPlayers = new ArrayList<Player>();
		Player temp;
		players = new ArrayList<Player>();
		
		readArgs(args, tPlayers);
		while (!tPlayers.isEmpty()){
			temp = tPlayers.get(rand.nextInt(tPlayers.size()));
			players.add(temp);
			tPlayers.remove(temp);
		}
	}

	public static void main(String[] args){
		System.out.println("");
		int games = 0;
		if (args.length > 0) {
			games = Integer.parseInt(args[0]);
		}
		
		VirtualGamePro v = new VirtualGamePro();
		v.readArgs(args, v.origPlayers);
		
		
		FeatureLogger logger = new WhichMoveLogger(v.origPlayers, "/tmp/whichmove-features", "/tmp/whichmove-outputs", new WhichMoveFeatures());
		if (games > 0) {
			v.totalGames = games;
		}
		for (int i = 0; i< v.totalGames; i++) {
			v.initializeNewGame(args);
			CardSet deck = CardSet.addFullDeck();
			
			GameState gameState = new GameState(v.players, deck);
			
			GamePlay gamePlay = new VirtualGamePlay(gameState);
			
			LootEngine engine = new LootEngine(v.players, gamePlay, gameState);
			boolean verbosity = v.setVerbosity();
				
			// set verbosity
			gamePlay.setVerbosity(verbosity);
			engine.setVerbosity(verbosity);
			
			engine.setLogger(logger);
			for (Player p: v.players) {
				if (p instanceof VirtualPlayer){
					((VirtualPlayer) p).setVerbosity(verbosity);
				}
			}
			
			engine.play();
			v.gamesPlayed++;
			v.hTallyPoints();
			System.out.println("Games Completed: " + (i+1));
			v.printHTally();
			System.out.print(ANSI_CSI + "6A");

		}
		
		logger.outputToFile();
		
		System.out.print(ANSI_CSI + "6B");
	}
	
	
	
	public boolean setVerbosity() {
		if (totalGames > 10){
			return false;
		}
		return true;
	}
	
	public void hTallyPoints(){
		int hScore = -100;
		int count = 0;
		float dTemp = 0;
		
		for (Player p: players) {
			if (p.getFinalPoints() > hScore) {
				hScore = p.getFinalPoints();
			}
		}

		for (Player p: players) {
			if (p.getFinalPoints() == hScore) {
				count++;
			}
		}
		
		for (Player p: players) {
			if (!hTally.containsKey(p.getName())){
				hTally.put(p.getName(), 0.0f);
				hScoreTally.put(p.getName(), 0);
				hPenaltyTally.put(p.getName(), 0);
			}
			dTemp = 0;
			if (p.getFinalPoints() == hScore) {
				if (hTally.containsKey(p.getName())) {
					dTemp = hTally.get(p.getName());
				}
				hTally.put(p.getName(), dTemp + (float)(1/(float)count));	
			}
			hScoreTally.put(p.getName(), hScoreTally.get(p.getName()) + p.getFinalPoints());
			hPenaltyTally.put(p.getName(), hPenaltyTally.get(p.getName()) + p.getPoints() - p.getFinalPoints());
		}
	}
	
	public void printHTally() {
		float score = -1;
		Player winner = null;
		for (Player p: origPlayers) {
			if (hTally.get(p.getName()) > score){
				score = hTally.get(p.getName());
				winner = p;
			}
		}
		
		for (Player p: origPlayers) {
			if (p == winner) {
				System.out.print(ANSI_RED);
			}
			System.out.printf(p.getName() + "'s Score: " + "%.2f"
					+ " | Points: " + hScoreTally.get(p.getName())
					+ " | Penalty: " + hPenaltyTally.get(p.getName()) + "\n", hTally.get(p.getName()));
			
			if (p == winner) {
				System.out.print(ANSI_RESET);
			}
		}
		System.out.println("Total time elapsed: " + (System.currentTimeMillis() - startTime) + "ms");
	}
}

