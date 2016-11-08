package col.champions;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HeroSelect extends Activity implements OnClickListener{

	int selectedHero;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_select);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		TableLayout hTable = (TableLayout) findViewById(R.id.heroTable);
		List<Hero> heroes = ((ChampionsApplication) this.getApplication()).heroes;
        
        TableRow.LayoutParams rp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rp.gravity = Gravity.CENTER_VERTICAL;
        //rp.bottomMargin = 30;
        //rp.rightMargin = 10;

        int heroID = 0;
		for(Hero hero : heroes){
			TableRow row= new TableRow(this);
	        
			LinearLayout llay = new LinearLayout(this); 
	        llay.setOrientation(LinearLayout.VERTICAL);
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        lp.gravity = Gravity.CENTER_HORIZONTAL;
	        llay.setLayoutParams(rp);
	        //llay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        
	        ImageView portrait = new ImageView(this);
	        int id = getResources().getIdentifier(hero.imageTag, "drawable", getPackageName());
	        portrait.setImageResource(id);
	        portrait.setMinimumHeight(200);
	        portrait.setMinimumWidth(200);
	        
	        TextView label = new TextView(this);
	        label.setText(hero.name);
	        label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        label.setGravity(Gravity.CENTER_HORIZONTAL);
	        label.setTypeface(Typeface.DEFAULT_BOLD);
	        
	        llay.addView(label);
	        llay.addView(portrait);
	        llay.setBackgroundResource(R.drawable.border);
	        
	        
	        TextView desc = new TextView(this);
	        desc.setText(hero.description);
	        
	        row.setLayoutParams(rp);
	        row.addView(llay);
	        row.addView(desc);
	        row.setBackgroundResource(R.drawable.border);
	        row.setId(heroID);
	        row.setClickable(true);
	        row.setOnClickListener(this);
	        
	        hTable.addView(row);
	        heroID++;
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
		getMenuInflater().inflate(R.menu.hero_select, menu);
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
	
	public void goToDuel(View view){
		Intent intent;
		if(!((ChampionsApplication) this.getApplication()).tutorial)
			intent = new Intent(this, Duel.class);
		else
			intent = new Intent(this, Tutorial.class);
		intent.putExtra("heroName", ((ChampionsApplication) this.getApplication()).heroes.get(selectedHero).name);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		selectedHero = arg0.getId();
		goToDuel(arg0);
	}

}
