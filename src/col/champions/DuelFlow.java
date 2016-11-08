package col.champions;

import java.io.Serializable;
import java.util.Random;

public class DuelFlow implements Serializable{

	private static final long serialVersionUID = 1L;

	GameView delegate;
	
	GameData gameData;
	
	public DuelFlow(){
		
		gameData = new GameData();
	}
	
	public DuelFlow(GameView activity){
		setDelegate(activity);
		gameData = new GameData();
	}
	
	public void setDelegate(GameView activity){
		delegate = activity;
	}
	
	public void runGame(Player one, Player two){
		gameData.gameState = 0;
		gameData.p1 = one;
		gameData.p2 = two;
		gameData.priority = 0;
		
		gameData.setup = true;
		
		gameData.activeRow = -1;
		gameData.selectableRow = 3;
		
		next();
	}
	
	public void next(){
		
		switch(gameData.gameState){
			case 0:
				initialSetup();
				break;
			case 1:
				startTurn();
				break;
			case 2:
				turnPart();
				break;
			case 3:
				processCommit();
				break;
			case 4:
				checkWinConditions();
				break;
			case 5:
			default:		
				break;
		}
	}
	
	public void startTurn(){
		fillHand(gameData.p1);
		gameData.activeRow = 0;
		gameData.selectableRow = 1;
		gameData.gameState = 2;
		delegate.refreshRow(gameData.activeRow, false);
	}
	
	public void turnPart(){
		if(gameData.activeRow > 1){
			gameData.gameState = 3;
			next();
		} else {
			gameData.activeRow++;
			gameData.selectableRow++;
			delegate.refreshRow(gameData.activeRow, false);
		}
	}
	
	public void initialSetup(){
		fillHand(gameData.p1);
		if(gameData.setup && gameData.activeRow > 1){
			gameData.setup = false;
		}
		
		if(gameData.activeRow < 2){
			gameData.activeRow++;
			delegate.refreshRow(gameData.activeRow, true);
		} else {
			gameData.gameState = 3;
			next();
		}
	}
	
	public void fillHand(Player player){
		Random rand = new Random(); 
		while(player.hand.size() < 5 && !player.deck.isEmpty()){
			int card = rand.nextInt(player.deck.size());
			player.hand.add(player.deck.remove(card));
		}
		delegate.refreshHand();
	}
	
	public void processCommit(){
		((BasicAI)gameData.p2).getRandomTop();
		if(gameData.priority == 0)
			processTopManuevers(gameData.p1, gameData.p2);
		else
			processTopManuevers(gameData.p2, gameData.p1);
		delegate.showManuevers("action");
		//wait for animation
		processDamage();
		delegate.showDamage();
		
		
		gameData.gameState = 4;
		gameData.selectableRow = 1;
		gameData.activeRow = 0;
		next();
	}
	
	public void processTopManuevers(Player active, Player reactive){
		active.clearStats();
		reactive.clearStats();
		if(active.topM.get(0).speed >= reactive.topM.get(0).speed){
			active.topM.get(0).action(active);
			reactive.topM.get(0).reaction(reactive);
		} else {
			active.topM.get(0).reaction(active);
			reactive.topM.get(0).action(reactive);
			gameData.priority = 1 - gameData.priority;
		}
		active.topM.clear();
		reactive.topM.clear();
	}
	
	public void processDamage(){
		int p1Damage = 0;
		int p2Damage = 0;
		
		if(gameData.p1.defense < gameData.p2.attack)
			p1Damage = gameData.p2.attack - gameData.p1.defense;
		if(gameData.p2.defense < gameData.p1.attack)
			p2Damage = gameData.p1.attack - gameData.p2.defense;
		
		gameData.p1.health = gameData.p1.health - p1Damage;
		gameData.p2.health = gameData.p2.health - p2Damage;
		
		delegate.updateHealth();
	}
	
	public void checkWinConditions(){		
		if(!gameData.p2.alive())
			delegate.gameOver(true);
		else if (!gameData.p1.alive())
			delegate.gameOver(false);
		else if (gameData.p1.board.get(1).isEmpty()){
			if(gameData.p1.health >= gameData.p2.health)
				delegate.gameOver(true);
			else
				delegate.gameOver(false);
		} else {
			gameData.gameState = 1;
			next();
		}
	}
}
