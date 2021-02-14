package trigramsbookgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Trigrams {
	
	private String key;
	private ArrayList<String> values;
	
	public Trigrams(String key, String value){ 
		this.key = key;
		this.values = new ArrayList<String>();
		values.add(value);
	};
	
	public String getKey() {
		return this.key;
	}
	
	//Pick a random value
	public String getValue() {
		Random randomizer = new Random();
		String randomValue = values.get(randomizer.nextInt(values.size()));
		return randomValue;
	}
	
	public List<String> getValues() {
		return values;
	}
	
	public void add( String value ) {
		if(!values.contains(value))
			values.add(value);
	}
	
	public String getSeed() {
		String delims = "[\\s+]";
		String[] tokens = this.key.split(delims);
		return tokens[1];
	}
	
	@Override
	public String toString() {
		return key + ": " + values.toString();
	}

}
