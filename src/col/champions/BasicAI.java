package col.champions;

import java.util.List;
import java.util.Random;

public class BasicAI extends Player {

	private static final long serialVersionUID = 1L;

	public BasicAI(List<Card> d) {
		super(d);
		// TODO Auto-generated constructor stub
		this.hero = new Hero("Enemy", "", "enemy_avatar");
	}
	
	public void getRandomTop(){
		Random rand = new Random();
		int card = rand.nextInt(deck.size());
		topM.clear();
		topM.add(deck.remove(card));
	}

}
