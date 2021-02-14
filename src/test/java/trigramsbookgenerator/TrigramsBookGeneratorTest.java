package trigramsbookgenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class TrigramsBookGeneratorTest {

private Scanner scanner;
	
	@Test
	public void testReadFile()  {
		int numSentences=0;
		int numWords=0;
		try { 
			scanner = new Scanner (new File("test.txt")); 
			ArrayList<String> sentence = new ArrayList<String>();
			while( scanner.hasNext() ) {
				String word = scanner.next();
				String cleanedWord = word.replaceAll("[^a-zA-Z]","");//strip away unwanted chars & numbers
				sentence.add(cleanedWord);
				numWords++;
				//determine when a sentence ends
				if(word.charAt(word.length()-1)=='.' || word.charAt(word.length()-1)=='!' || word.charAt(word.length()-1)=='?') {
					numSentences++;
					sentence.clear();
				}	
			}//end of while
			System.out.println("Number of words read: " + numWords);
			System.out.println("Number of sentences read: " + numSentences);
			
			assertEquals( 18, numWords,"failure - expected result content match");
			assertEquals( 2, numSentences,"failure - expected result content match");
			
		} catch(IOException e) { 
			System.err.println("Error opening file"); 
			System.exit(1); 
		} 

	}

}
