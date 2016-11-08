package col.champions;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class Tutorial extends Activity {

	List<String> titles;
	List<String> descriptions;
	int currentStep;
	String heroName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    heroName = extras.getString("heroName");
		}
		
		setupTitles();
		setupDescriptions();
		currentStep = 1;
		updatePage();
	}
	
	private void setupTitles(){
		//setup titles
		titles = new ArrayList<String>();
		titles.add("Gameboard");
		titles.add("Card Anatomy");
		titles.add("Winning");
		titles.add("Setup");
		titles.add("Game Turn");
	}
	
	private void setupDescriptions(){
		descriptions = new ArrayList<String>();
		descriptions.add("WINNING\r\nA player wins when either:\r\n" +
				"- Both Players have played all of their cards and that player has more health\r\n" +
				"- One Player is reduced to 0 health during the course of the game\r\n\r\n" +
				"How to reduce a player's health is explained later in the tutorial.");
		descriptions.add("SETUP\r\n" +
				"Initially your PLANNING TREE will be empty and you must fill it to start the game. " +
				"You will fill your tree from the top down, with your hand refilling after every level has been filled.");
		descriptions.add("GAME TURN\r\n" +
				"Once the initial setup has been completed, your TOP MANEUVER will be compared against the opponent's. " +
				"The maneuever with the highest SPEED will apply its ACTION's attack and defense to its player, while the maneuver with " +
				"the lower speed will apply its REACTION's attack and defense to its player. When one player has higher attack than the other" +
				"enemy's defense, it will reduce the enemy's health by the difference.");
	}
	
	private void updatePage(){
		TextView title = (TextView) findViewById(R.id.tutLabel);
		title.setText(titles.get(currentStep-1));
		
		ImageView image = (ImageView) findViewById(R.id.gameboardImage);
		
		TextView step = (TextView) findViewById(R.id.stepLabel);
		step.setText("Step " + currentStep + " of " + titles.size());
		
		TextView desc = (TextView) findViewById(R.id.tutorialText);
		switch(currentStep){
			case 1:
				image.setImageResource(R.drawable.tutorial);
				desc.setText("");
				break;
			case 2:
				image.setImageResource(R.drawable.cardanatomy);
				desc.setText("");
				break;
			case 3:
			case 4:
			case 5:
			default:
				desc.setText(descriptions.get(currentStep-3));
				image.setImageResource(0);
				break;
				
		}
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
		getMenuInflater().inflate(R.menu.tutorial, menu);
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
	
	public void nextStep(View view){
		if(currentStep == titles.size()) {
			SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("tutorial", false);
			// Commit the edits!
			editor.commit();

			Intent intent;
			intent = new Intent(this, Duel.class);
			intent.putExtra("heroName", heroName);
			startActivity(intent);
		} else {
			currentStep++;
			updatePage();
		}
        
	}
	
	public void prevStep(View view){
		if(currentStep == 1)
			finish();
		else {
			currentStep--;
			updatePage();
		}
	}

}
