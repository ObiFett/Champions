package col.champions;

import com.parse.ParseObject;

public class CardParser {
	
	int[] stats;
	
	public CardParser(){
	}
	
	public Card parse(ParseObject object){
		
		stats = new int[5];
		
		String name = object.getString("Name");
		String classType = object.getString("Class");
		stats[0] = object.getInt("Speed");
		String action = object.getString("Action");
		String reaction = object.getString("Reaction");
		String rawData = object.getString("RawData");
		
		String[] split = rawData.split("\\|");
		
		processData(split[0], "action");
		processData(split[1], "reaction");
		
		Card tempCard = new Card(name, classType, action, reaction, stats);
		
		return tempCard;
	}
	
	public void processData(String action, String type){
		int attackI = 0;
		int defenseI = 0;
		
		if (type.equals("action")){
			attackI = 1;
			defenseI = 2;
		} else if (type.equals("reaction")){
			attackI = 3;
			defenseI = 4;
		}
		
		String[] split = action.split(":");
		for(int i = 0; i < split.length; i++){
			if(split[i].charAt(0) == 'A'){
				stats[attackI] = Integer.parseInt(split[i].substring(1));
			} else if (split[i].charAt(0) == 'D'){
				stats[defenseI] = Integer.parseInt(split[i].substring(1));
			}
		}
	}
}
