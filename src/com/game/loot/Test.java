package com.game.loot;

public class Test {

	public static void main(String[] args){
		
		GameState gs = new GameState(3, new String[]{"Henry", "Jon"});
		
		
		gs.drawCard(1);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.drawCard(2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.playCard(new MerchantShip(3), 3, 0);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		
		gs.playCard(new MerchantShip(3), 1, 0);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.playCard(new MerchantShip(3), 2, 0);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.playCard(new PirateShip(2, Color.Blue), 3, 2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		
		gs.playCard(new PirateShip(2, Color.Green), 1, 2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.playCard(new MerchantShip(3), 2, 0);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.playCard(new Trump(Color.Blue), 3, 2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		
		gs.playCard(new Trump(Color.Green), 1, 2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.drawCard(2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.drawCard(3);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		
		gs.drawCard(1);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.drawCard(2);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		gs.drawCard(3);
		gs.getCurrentPlayer().printPlayerStats();
		gs.nextTurn();
		
		
		
		//gs.getAllCards().printCardSetStats();
		//gs.getAllCards().printAllCards();
		
		System.out.println("\n\n");
		
		for (Player p: gs.getPlayers()) {
			p.printPlayerStats();
			System.out.println("");
		}
		
		gs.verifyGameState();
	}

}
