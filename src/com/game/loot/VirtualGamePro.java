package com.game.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/*
 * Allows you to run multiple games (through command line)
 * And reports count of wins and total points won
 */

public class VirtualGamePro {
	
	//final int TOTALGAMES = 1000;
	
	HashMap<String, Float> hTally;
	HashMap<String, Integer> hScoreTally;
	HashMap<String, Integer> hPenaltyTally;
	List<Player> players = new ArrayList<Player>();
	int totalGames;
	int gamesPlayed;
	Random rand;
	
	VirtualGamePro(){		
		// setup tally
		rand = new Random();
		hTally = new HashMap<String, Float>();
		hScoreTally = new HashMap<String, Integer>();
		hPenaltyTally = new HashMap<String, Integer>();
		totalGames = 1;
		gamesPlayed = 0;
	}
	
	public void initializeNewGame(){
		// Add players
		ArrayList<Player> tPlayers = new ArrayList<Player>();
		Player temp;
		players = new ArrayList<Player>();
		tPlayers.add((Player) new RandomAI("Ender"));
		tPlayers.add((Player) new RandomAI("Efficiency"));
		tPlayers.add((Player) new RandomAI("Artemis"));
		tPlayers.add((Player) new RandomAI("Lowballer"));
		
		while (!tPlayers.isEmpty()){
			temp = tPlayers.get(rand.nextInt(tPlayers.size()));
			players.add(temp);
			tPlayers.remove(temp);
		}
	}

	public static void main(String[] args){
		
		int games = 0;
		if (args.length > 0) {
			games = Integer.parseInt(args[0]);
		}
		
		VirtualGamePro v = new VirtualGamePro();
		if (games > 0) {
			v.totalGames = games;
		}
		for (int i = 0; i< v.totalGames; i++) {
			v.initializeNewGame();
			GameState gameState = new GameState(v.players);
			
			CardSet deck = CardSet.addFullDeck();
			
			GamePlay gamePlay = new VirtualGamePlay(gameState, deck);
			
			LootEngine engine = new LootEngine(v.players, gamePlay, gameState);
			engine.play();
			v.gamesPlayed++;
			v.hTallyPoints();
		}
		v.printHTally();
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
		System.out.println("");
		System.out.println("Games Played: " + totalGames);
		float temp = 0;
		
		for (Player p: players) {
			System.out.println(p.getName() + "'s Score: " + hTally.get(p.getName()) 
					+ " | Points: " + hScoreTally.get(p.getName())
					+ " | Penalty: " + hPenaltyTally.get(p.getName()));
			temp += hTally.get(p.getName());
		}
		if (temp < (float)gamesPlayed){
			System.err.println("Score adds up to only: " + temp);
		}
	}
}

