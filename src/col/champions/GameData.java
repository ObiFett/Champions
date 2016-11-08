package col.champions;

import java.io.Serializable;

public class GameData implements Serializable{

	private static final long serialVersionUID = 1L;

	int gameState;
	boolean setup;
	int activeRow;
	int selectableRow;
	
	Player p1;
	Player p2;
	int priority;
	boolean win;
	
	public GameData(){
		
	}
}
