package col.champions;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ChampionsApplication extends Application {
	
	List<Card> cards;
	List<Hero> heroes;
	GameData savedGame;
	boolean tutorial;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		loadRemoteData();
		loadGame();
		loadLocalSettings();
		
	}
	
	public void loadRemoteData(){
		// Add your initialization code here
		Parse.initialize(this, "Qw18V1HXzlTLoSKORjVegSC8g1JQ8t2IgGwEsAyq", "UuC4uVqmT3vMWnd03BVHoT85t3zC9xfbfZ2VGtOf");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		
		//load cards
		cards = new ArrayList<Card>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Cards");
		try {
			List<ParseObject> results = query.find();
			CardParser reader = new CardParser();
			
			for (int i = 0; i < results.size(); i++){
				Card temp = reader.parse(results.get(i));
				cards.add(temp);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//load heroes
		heroes = new ArrayList<Hero>();
		query = ParseQuery.getQuery("Heroes");
		try {
			List<ParseObject> results = query.find();
			
			for (int i = 0; i < results.size(); i++){
				ParseObject object = results.get(i);
				Hero temp = new Hero(object.getString("name"), object.getString("Description"), object.getString("picName"));
				heroes.add(temp);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadGame(){
		//load saved game
		String filename = "SavedGame.txt";
		File file = new File(getFilesDir(), filename);
		Log.w("App:", file.toString());
		if(file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream is = new ObjectInputStream(fis);
				GameData save = (GameData) is.readObject();
				is.close();
				savedGame =  save;
			} catch (Exception ex){
				ex.printStackTrace();
				Log.w("App:", "Exception Error with File");
			}
		} else
			savedGame = null;
	}
	
	public void loadLocalSettings(){
		//load user options
		SharedPreferences prefs = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
		
		tutorial = prefs.getBoolean("tutorial", true);
		
		tutorial = true; //force Tutorial for DEBUG
	}
}
