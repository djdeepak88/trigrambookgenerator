package trigramsbookgenerator;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



class TrigramsTest {
    
	@Test
	public void testConstructor() {
		String key = "I wish";
		String value = "I";
		Trigrams trigram = new Trigrams(key, value);
		assertEquals("failure - expected result content match", key, trigram.getKey());
		assertEquals("failure - expected result content match", value, trigram.getValue());
	}

	@Test
	public void testMultipleValues() {
		String key = "I wish";
		String value = "I";
		Trigrams trigram = new Trigrams(key, value);
		assertEquals(1, trigram.getValues().size(),"failure - expected result content match");
		trigram.add("he");
		assertEquals(2, trigram.getValues().size(),"failure - expected result content match");
	}
	
	
	@Test
	public void testDuplicateValue() {
		String key = "I wish";
		String value = "I";
		Trigrams trigram = new Trigrams(key, value);
		assertEquals(1, trigram.getValues().size(),"failure - expected result content match");
		trigram.add(value);
		assertEquals(1, trigram.getValues().size(),"failure - expected result content match");
	}
	
	@Test
	public void testNull() {
		String key = null;
		String value = "I";
		Trigrams trigram = new Trigrams(key, value);
		assertEquals(1, trigram.getValues().size(),"failure - expected result content match");
		
	}
	
	@Test
	public void testGetSeed() {
		String key = "I wish";
		String value = "I";
		Trigrams trigram = new Trigrams(key, value);
		assertEquals("wish", trigram.getSeed(),"failure - expected result content match");
		
	}
	
	@Test
	public void testGetRandomValue() {
		String key = "I wish";
		String value = "I";
		String value2 = "he";
		String value3 = "she";
		Trigrams trigram = new Trigrams(key, value);
		trigram.add(value2);
		trigram.add(value3);
		assertTrue(trigram.getValue().matches("I|he|she"),"failure - expected result content match");
		
	}

}
