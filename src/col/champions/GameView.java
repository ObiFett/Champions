package col.champions;

public interface GameView {
	
	void refreshRow(int row, boolean load);
	void refreshHand();
	void gameOver(boolean win);
	void updateHealth();
	void showManuevers(String playerResult);
	void showDamage();
}
