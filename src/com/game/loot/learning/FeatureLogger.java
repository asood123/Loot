package com.game.loot;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FeatureLogger {

	private GameState gm;
	private VirtualPlayer p;
	private ArrayList<ArrayList<Float>> turnFeatureArrays;
	private ArrayList<ArrayList<Float>> attackFeatureArrays;
	private ArrayList<ArrayList<Float>> trumpFeatureArrays;
	private ArrayList<ArrayList<Float>> drawFeatureArrays;
	private ArrayList<ArrayList<Float>> merchantFeatureArrays;
	
	public FeatureLogger(GameState state, VirtualPlayer p){
		this.p = p;
		this.gm = state;
		this.turnFeatureArrays = new ArrayList<ArrayList<Float>>();
		this.attackFeatureArrays = new ArrayList<ArrayList<Float>>();
		this.trumpFeatureArrays = new ArrayList<ArrayList<Float>>();
		this.drawFeatureArrays = new ArrayList<ArrayList<Float>>();
		this.merchantFeatureArrays = new ArrayList<ArrayList<Float>>();
	}
	
	public Player getPlayer() {
		return p;
	}

	public void logMoveForPlayer(Move move) {
		ArrayList<Float> featuresForLastTurn = getFeaturesForStateAndPlayer(); 
		this.turnFeatureArrays.add(featuresForLastTurn);
		
		if (move.getAction() == ACTION.PLAY_ATTACK) {
			if (move.getCard() instanceof PirateShip) {
				this.attackFeatureArrays.add(getAttackFeatures(move));
			} else {
				this.trumpFeatureArrays.add(getTrumpFeatures());
			}
		} else if (move.getAction() == ACTION.DRAW) {
			this.drawFeatureArrays.add(getDrawFeatures());
		} else if (move.getAction() == ACTION.PLAY_MERCHANT_SHIP){
			this.merchantFeatureArrays.add(getMerchantFeatures());
		}
	}
	

private ArrayList<Float> getAttackFeatures(Move move) {
	ArrayList<Float> attackFeatures = new ArrayList<Float>();
	
	
	
	return null;
}


private ArrayList<Float> getTrumpFeatures() {
	// TODO Auto-generated method stub
	return null;
}
	
private ArrayList<Float> getMerchantFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

private ArrayList<Float> getDrawFeatures() {
		// TODO Auto-generated method stub
		return null;
	}



/*
Pirate ships in hand
Val pirate ships
Val all one color pirate ships
Num battles
Val battles fighting
Num battles winning
Num battles draw
Num unattacked ships other
Num unattacked ships self
Points captured
Other players points captured
*/
	


	private ArrayList<Float> getFeaturesForStateAndPlayer(){
		ArrayList<Float> featureArray = new ArrayList<Float>();
		
		//Pirates in hand
		featureArray.add(new Float(p.getHand().numPShips()));
		featureArray.add(new Float(p.getHand().sumPShips()));
		
		//Battles
		int numBattlesFighting = 0;
		int valBattlesFighting = 0;
		int numBattlesNotFighting = 0;
		int valBattlesNotFighting = 0;
		int numBattlesWinning = 0;
		int valBattlesWinning = 0;
		int numBattlesLosing = 0;
		int valBattlesLosing = 0;
		int numOwnedUncontestedMerchants = 0;
		int valOwnedUncontestedMerchants = 0;
		int numUnownedUncontestedMerchants = 0;
		int valUnownedUncontestedMerchants = 0;
		
		for (Battle battle : gm.getBattleList()) {
			if (battle.getAttackerByPlayerId(p.getId()) != null) {
				//this is a battle we're currently fighting or merchant we played
				if (battle.isBattleUncontested()) {
					numOwnedUncontestedMerchants++;
					valOwnedUncontestedMerchants += battle.getMerchantShip().getValue();
				} else {
					numBattlesFighting++;
					valBattlesFighting += battle.getMerchantShip().getValue();
					if (battle.getWinningPlayer() == p) {
						numBattlesWinning++;
						valBattlesWinning += battle.getMerchantShip().getValue();
					} else {
						numBattlesLosing++;
						valBattlesLosing += battle.getMerchantShip().getValue();
					}
				}
			} else {
				if (battle.isBattleUncontested()) {
					numUnownedUncontestedMerchants++;
					valUnownedUncontestedMerchants += battle.getMerchantShip().getValue();
				} else {
					numBattlesNotFighting++;
					valBattlesNotFighting += battle.getMerchantShip().getValue();
				}
			}	
		}
		
		featureArray.add(new Float(numBattlesFighting));
		featureArray.add(new Float(valBattlesFighting));
		featureArray.add(new Float(numBattlesNotFighting));
		featureArray.add(new Float(valBattlesNotFighting));
		featureArray.add(new Float(numBattlesWinning));
		featureArray.add(new Float(valBattlesWinning));
		featureArray.add(new Float(numBattlesLosing));
		featureArray.add(new Float(valBattlesLosing));
		featureArray.add(new Float(numOwnedUncontestedMerchants));
		featureArray.add(new Float(valOwnedUncontestedMerchants));
		featureArray.add(new Float(numUnownedUncontestedMerchants));
		featureArray.add(new Float(valUnownedUncontestedMerchants));

		//Points
		featureArray.add(new Float(p.getFinalPoints()));
		featureArray.add(new Float(p.getPoints()));
		for (Player player : gm.getPlayers()) {
			if (player != p) featureArray.add(new Float(player.getPoints()));
		}
		
		return featureArray;
	}
	
	
	private String featureArrayToString(ArrayList<Float> featureArray) {
		String resultString = "";
		for (Float feature : featureArray) {
			resultString += feature.toString() + " ";
		}
		return resultString;
	}
	
	private int finalValue()
	{
		int highestScore = -Integer.MAX_VALUE;
		int secondHighestScore = -Integer.MAX_VALUE;
		for (Player player : gm.getPlayers())
		{
			int points = player.getFinalPoints();
			if (points > highestScore) {
				secondHighestScore = highestScore;
				highestScore = points;
			}
			else if (points > secondHighestScore) {
				secondHighestScore = points;
			}
		}
		if (p.getFinalPoints() == highestScore) {
			return highestScore - secondHighestScore;
		} else {
			return p.getFinalPoints() - highestScore;
		}
	}
	
	
	void endGame() {
		
		int finalValue = finalValue();
		
		PrintWriter Xwriter = null, Ywriter = null;
		
		try {
		    Xwriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/hbridge/Documents/LootX.txt", true)));			
		    Ywriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/hbridge/Documents/LootY.txt", true)));
		    
		    int turn = 1;
			for (ArrayList<Float> featureArray : turnFeatureArrays) {
				Xwriter.print(Integer.toString(turn++) + " ");
				Xwriter.println(featureArrayToString(featureArray));
				Ywriter.println(finalValue);
			}
			Xwriter.close();
			Ywriter.close();
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (Xwriter != null) Xwriter.close();
			if (Ywriter != null) Ywriter.close();
		}
		
	}
	
	
}
