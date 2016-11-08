package col.champions;

import java.io.Serializable;

public class Hero implements Serializable{

	private static final long serialVersionUID = 1L;
	public String name;
	public String description;
	public String imageTag;
	
	public Hero(String n, String d, String i){
		name = n;
		description = d;
		imageTag = i;
	}
}
