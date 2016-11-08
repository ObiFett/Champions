package col.champions;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	
	public void goToDuel(View view){
		Intent intent;
		if(((ChampionsApplication) this.getApplication()).savedGame == null)
			intent = new Intent(this, HeroSelect.class);
		else
			intent = new Intent(this, Duel.class);
        
		startActivity(intent);
	}

}
