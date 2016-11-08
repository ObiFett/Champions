package col.champions;

import java.io.Serializable;

public class Card implements Serializable{

	private static final long serialVersionUID = 1L;
	int speed;
	String name;
	String classType;
	
	String actionText;
	int actionAttack;
	int actionDefense;
	
	String reactionText;
	int reactionAttack;
	int reactionDefense;
	
	public Card(String n, String cT, String aT, String rT, int ... ints){
		name = n;
		actionText = aT;
		reactionText = rT;
		speed = ints[0];
		actionAttack = ints[1];
		actionDefense = ints[2];
		reactionAttack = ints[3];
		reactionDefense = ints[4];
	}
	
	public void action(Player player){
		player.addAttack(this.actionAttack);
		player.addDefense(this.actionDefense);
	}
	
	public void reaction(Player player){
		player.addAttack(this.reactionAttack);
		player.addDefense(this.reactionDefense);
	}
	
	public void onGoing(){
		
	}
}
