package col.champions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable{

	private static final long serialVersionUID = 1L;

	int health;
	Hero hero;
	
	List<Card> deck;
	ArrayList<Card> hand;
	
	ArrayList<Card> topM;
	ArrayList<Card> middleM;
	ArrayList<Card> bottomM;
	
	List<ArrayList<Card>> board;
	
	int attack;
	int defense;
	
	public Player(List<Card> d){
		attack = 0;
		defense = 0;
		health = 20;
		
		deck = d;
		
		topM = new ArrayList<Card>();
		middleM = new ArrayList<Card>();
		bottomM = new ArrayList<Card>();
		hand = new ArrayList<Card>();
		
		board = new ArrayList<ArrayList<Card>>();
		
		board.add(topM);
		board.add(middleM);
		board.add(bottomM);
		board.add(hand);
	}
	
	public void addAttack(int atk){
		attack = attack + atk;
	}
	
	public void addDefense(int def){
		defense = defense + def;
	}
	
	public boolean alive(){
		boolean alive = false;
		if (health > 0)
			alive = true;
		return alive;
	}
	
	public void clearStats(){
		attack = 0;
		defense = 0;
	}
}
