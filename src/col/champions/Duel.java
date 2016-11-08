package col.champions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public class Duel extends Activity implements GameView, OnGestureListener{

	DuelFlow duelFlow;
	
	RelativeLayout top;
	ArrayList<RelativeLayout> middle;
	ArrayList<RelativeLayout> bottom;
	ArrayList<RelativeLayout> hand;
	
	List<ArrayList<RelativeLayout>> board;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duel);
		// Show the Up button in the action bar.
		setupActionBar();
		
		setupImageArrays();
		
		duelFlow = new DuelFlow(this);
		if(((ChampionsApplication) this.getApplication()).savedGame == null){
			Player p1 = new Player(randomDeck());
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
			    String heroName = extras.getString("heroName");
			    
			    for(int i = 0; i < ((ChampionsApplication) this.getApplication()).heroes.size(); i++){
			    	if(heroName.equalsIgnoreCase(((ChampionsApplication) this.getApplication()).heroes.get(i).name))
			    		p1.hero = ((ChampionsApplication) this.getApplication()).heroes.get(i);
			    }
			} else {
				p1.hero = new Hero("Basic", "", "player_avatar");
			}
			Player ai = new BasicAI(randomDeck());
			duelFlow.runGame(p1, ai);
		} else {
			duelFlow.gameData = ((ChampionsApplication) this.getApplication()).savedGame;
			loadGameState();
			
		}
		setupPlayerInfo();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.duel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setupPlayerInfo(){
		TextView name = (TextView) findViewById(R.id.playerLabel);
		name.setText(duelFlow.gameData.p1.hero.name);
		
		ImageView avatar = (ImageView) findViewById(R.id.player);
		int id = getResources().getIdentifier(duelFlow.gameData.p1.hero.imageTag, "drawable", getPackageName());
		avatar.setImageResource(id);
	}
	/* ON CLICK FUNCTIONS */
	public void moveCardUp(View view){
		int cardRow = Character.getNumericValue(((RelativeLayout)view).getTag().toString().charAt(0));
		int cardNum = Integer.parseInt(((RelativeLayout)view).getTag().toString().substring(1));
		if(cardRow == duelFlow.gameData.selectableRow && duelFlow.gameData.p1.board.get(duelFlow.gameData.selectableRow).size()-1 >= cardNum){
			if(duelFlow.gameData.p1.board.get(duelFlow.gameData.activeRow).size() < duelFlow.gameData.activeRow+1){
				duelFlow.gameData.p1.board.get(duelFlow.gameData.activeRow).add(
						duelFlow.gameData.p1.board.get(duelFlow.gameData.selectableRow).remove(cardNum));
				refreshRow(duelFlow.gameData.activeRow, false);
				refreshRow(duelFlow.gameData.selectableRow, false);
			}		
		}
		
		gameFlowNext(view);
	}
	
	public void gameFlowNext(View view){
		if(duelFlow.gameData.p1.board.get(duelFlow.gameData.activeRow).size() == duelFlow.gameData.activeRow+1 
				|| duelFlow.gameData.p1.board.get(duelFlow.gameData.selectableRow).size() == 0)
			next();
	}

	
	/*VIEW REFRESH FUNCTIONS*/
	@Override
	public void refreshHand() {
		refreshRow(3, false);
	}
	
	@Override
	public void refreshRow(int row, boolean load){
		List<Card> cardData = duelFlow.gameData.p1.board.get(row);
		
		for(int i = 0; i < board.get(row).size(); i++){
			if(i < cardData.size()) {
				updateCard(board.get(row).get(i), "cardfront", cardData.get(i));
			} else if(row == duelFlow.gameData.activeRow) {
				updateCard(board.get(row).get(i), "needs_card", null);
			} else {
				updateCard(board.get(row).get(i), "no_card", null);
			}
		}
		
		if(!load && row == duelFlow.gameData.selectableRow && row < 3 
				&& duelFlow.gameData.p1.board.get(row+1).isEmpty())
			next();
	}
	
	private void updateCard(RelativeLayout cardView, String type, Card cardData){
		int id = getResources().getIdentifier(type, "drawable", getPackageName());
		cardView.setBackgroundResource(id);
		setCardText(cardView, cardData);
	}
	
	private void setCardText(RelativeLayout cardView, Card cardData){
		cardView.removeAllViewsInLayout();
		if(cardData != null){
			List<String> cardVals = new ArrayList<String>();
			cardVals.add(Integer.toString(cardData.actionAttack));
			cardVals.add(Integer.toString(cardData.actionDefense));
			cardVals.add(Integer.toString(cardData.speed));
			cardVals.add(Integer.toString(cardData.reactionAttack));
			cardVals.add(Integer.toString(cardData.reactionDefense));
			cardVals.add(cardData.name);
			
			buildCard(cardVals, cardView);
		}
	}
	
	private void buildCard(List<String> cardVals, RelativeLayout cardView){
		for(int i = 0; i < cardVals.size(); i++){
			TextView label = new TextView(this);
			label.setText(cardVals.get(i));

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			label.setTextSize(14);
			switch(i){
				case 0: //action attack
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					label.setGravity(Gravity.LEFT);
					break;
				case 1: //action defense
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					label.setGravity(Gravity.RIGHT);
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					break;
				case 2: //speed
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					label.setGravity(Gravity.LEFT);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
					label.setTextSize(20);
					break;
				case 3: //reaction attack
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					label.setGravity(Gravity.LEFT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					break;
				case 4: //reaction defense
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					label.setGravity(Gravity.RIGHT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					break;
				case 5: //name
				default:
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
					label.setTextSize(6);
					label.setGravity(Gravity.RIGHT);
					break;
						
				
			}
			label.setTextColor(0xFFFFFFFF);
			label.setLayoutParams(params);
			cardView.addView(label);
		}
	}
	
	public void next(){
		String filename = "SavedGame.txt";
		File file = new File(getFilesDir(), filename);
		Log.w("FileName:", file.toString());
		try
		{
		    if (!file.exists()) {
		        if (!file.createNewFile()) {
		           throw new IOException("Unable to create file");
		        }
	        }

		    FileOutputStream fileout = new FileOutputStream(file);
		    ObjectOutputStream out = new ObjectOutputStream(fileout);
		    out.writeObject(this.duelFlow.gameData);
		    out.close();
		} catch (Exception ex) {
		    //show the error message
			Log.w("Duel:", "Exception Error with File");
		}
		((ChampionsApplication) this.getApplication()).savedGame = this.duelFlow.gameData;
		duelFlow.next();
	}

	@Override
	public void gameOver(boolean win) {
		TextView gameOver = (TextView) findViewById(R.id.endText);
		if(win){
			gameOver.setText("YOU WIN!");
			gameOver.setTextColor(Color.GREEN);
		} else {
			gameOver.setText("YOU LOSE!");
			gameOver.setTextColor(Color.RED);
		}
		((ChampionsApplication) this.getApplication()).savedGame = null;
		String filename = "SavedGame.txt";
		File file = new File(getFilesDir(), filename);
		file.delete();
	}
	
	@Override
	public void updateHealth() {
		TextView pHealth = (TextView) findViewById(R.id.playerHealth);
		TextView eHealth = (TextView) findViewById(R.id.enemyHealth);
		
		pHealth.setText(Integer.toString(duelFlow.gameData.p1.health));
		eHealth.setText(Integer.toString(duelFlow.gameData.p2.health));
	}
	
	@Override
	public void showManuevers(String response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDamage() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadGameState(){
		for(int i = 0; i < 4; i++)
			this.refreshRow(i, true);
		this.updateHealth();
		setupPlayerInfo();
	}
	
	private List<Card> randomDeck(){
		List<Card> cards = ((ChampionsApplication) this.getApplication()).cards;
		
		List<Card> deck = new ArrayList<Card>();
		for(int i = 0; i < cards.size(); i++){
			deck.add(cards.get(i));
			deck.add(cards.get(i));
		}
		return deck;
	}
	
	private void setupImageArrays(){
		
		top = (RelativeLayout)findViewById(R.id.topM);
		ArrayList<RelativeLayout> topArray = new ArrayList<RelativeLayout>();
		topArray.add(top);
		
		
		middle = new ArrayList<RelativeLayout>();
		middle.add((RelativeLayout)findViewById(R.id.midM1));
		middle.add((RelativeLayout)findViewById(R.id.midM2));
		
		bottom = new ArrayList<RelativeLayout>();
		bottom.add((RelativeLayout)findViewById(R.id.botM1));
		bottom.add((RelativeLayout)findViewById(R.id.botM2));
		bottom.add((RelativeLayout)findViewById(R.id.botM3));
		
		hand = new ArrayList<RelativeLayout>();
		hand.add((RelativeLayout)findViewById(R.id.hand1));
		hand.add((RelativeLayout)findViewById(R.id.hand2));
		hand.add((RelativeLayout)findViewById(R.id.hand3));
		hand.add((RelativeLayout)findViewById(R.id.hand4));
		hand.add((RelativeLayout)findViewById(R.id.hand5));
		
		board = new ArrayList<ArrayList<RelativeLayout>>();
		board.add(topArray);
		board.add(middle);
		board.add(bottom);
		board.add(hand);
		
	}

	@Override
	public void onBackPressed() {
	   Intent intent = new Intent(this, MainScreen.class);
	   startActivity(intent);
	   finish();
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
