package trigramsbookgenerator;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.FileHandler;


public class TrigramsBookGenerator {
    
	// Maximum words/Line to be generated
	private static int maxWordsPerLine; 
	// Maximum sentences to be generated per paragraph.
	private static int maxSentencesPerParagraph;
	// Maximum paragraphs to be generated.
	private static int maxParagraphs;
	// Hashmap for storing trigrams with key, value pair.
	private HashMap<String, Trigrams> trigramMaps = new HashMap<String, Trigrams>();
	// Create a Logger object.
	private static final Logger LOGGER = Logger.getLogger(TrigramsBookGenerator.class.getName());
	
	//Read the Original File 
	private void readInputFile(String inputFileName){
			
			try { 
				//Path pathToFile = Paths.get(inputFileName);
			    //System.out.println(pathToFile.toAbsolutePath());
				List<String> lines = Files.readAllLines(Paths.get(inputFileName));	
				ArrayList<String> sentence = new ArrayList<String>();
				for (String line : lines) { 
					String cline = line.trim();
					if (!cline.equals("")) {
					String[] words = cline.split("\\s+");
					for (String word : words ) {	
						String cleanword = word.trim();
						sentence.add(cleanword);
						//Sentences ending condition
						char endchar = cleanword.charAt(cleanword.length()-1);
						System.out.println(endchar);
						// Searching for the end of a sentence.
						if(endchar=='.' || endchar=='!' || endchar=='?') {
							createTrigrams( sentence );
							//clear the sentence content to be ready for next sentence
							sentence.clear();
						}
					}
				  }
				}		
			}catch(IOException e) { 
				System.err.println("Error opening the input filename. " + e); 
				System.exit(1); 
			} 
	}
	
	/*
	 * For each sentence in the input file generate the trigram.
	 */
	private void createTrigrams( ArrayList<String> sentence ) {
		for(int i=0; i<=sentence.size()-3; i++) {
			String key = sentence.get(i) + " " + sentence.get(i+1);
			String value = sentence.get(i+2);
			
			//create trigramMap with trigrams values in it.
			if(trigramMaps.containsKey(key)) { 
				//
				Trigrams existingTrigram = trigramMaps.get(key);
				existingTrigram.add(value); 
			}
			else {
				Trigrams newTrigram = new Trigrams(key, value);
				trigramMaps.put(key, newTrigram);
			}
		}
	    
	}
	
	/*
	 * print out the content of the Trigram Map
	 */
	public void showTrigrams() {
		Set<String> keys = trigramMaps.keySet();
		LOGGER.info("Trigrams set:");
		
		for(String key : keys) {
			LOGGER.info(" Key: " + key + "Value: "+ trigramMaps.get(key).toString());
			//System.out.println(trigramMaps.get(key).toString());
		}
	}
	
	/*
	 * Write the output file using trigrams.
	 */
	public void writeOutputFile(String outputFileName) {
		//generate a random seed to be used as the start point
		List<String> keys = new ArrayList<String>();
		keys.addAll(trigramMaps.keySet());
		String seed = generateSeed(keys);
		
		if(trigramMaps.containsKey(seed)){
			Writer writer = null;
			int wordCounter = maxWordsPerLine;
			int sentenceCounter = maxSentencesPerParagraph;
			int paragraphCounter = maxParagraphs;		
			try {
				writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(outputFileName), "utf-8"));
				writer.append(seed);
				wordCounter--;
				generateFileWithTrigrams(writer, seed, wordCounter, sentenceCounter, paragraphCounter);
				writer.flush();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch ( UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Generate file in a recursive fashion. Stop till either find no match or reached the paragraph limit
	 */
	private void generateFileWithTrigrams( Writer writer, String key, int wordCounter, int sentenceCounter, int paragraphCounter) {
		try {
			if( trigramMaps.containsKey(key) ) {
				Trigrams trigram = trigramMaps.get(key);
				//Select random value from trigrams.
				String randomValue = trigram.getValue();
				char ending = randomValue.charAt(randomValue.length()-1);
				//end of a sentence is reached.
				if(ending == '.' || ending=='!' || ending=='?') {  
					sentenceCounter--;
					writer.append(" ");
					writer.append(randomValue);
					wordCounter--;
					if(sentenceCounter<=0) {
						sentenceCounter = maxSentencesPerParagraph;
						//start a new paragraph
						writer.append("\n\n"); 
						paragraphCounter--;
					}
				} 
				else { 
					writer.append(" ");
					writer.append(randomValue);
					wordCounter--;
					if(wordCounter <= 0) {
						writer.append("\n"); 
						wordCounter = maxWordsPerLine;
					}
					
				}
				String newKey = trigram.getSeed()+" " +randomValue;
				generateFileWithTrigrams(writer, newKey, wordCounter, sentenceCounter, paragraphCounter);
					
			}
			else { //No match found, end the sentence and starts a new one from seed
				char ending = key.charAt(key.length()-1);
				if(ending != '.' && ending!='!' && ending!='?' && ending!=',') //check previous ending, make sure no .?!, existing already
					writer.append(".");
				sentenceCounter--;
				if(sentenceCounter<=0) {
					sentenceCounter = maxSentencesPerParagraph;
					writer.append("\n\n"); //start a new paragraph
					paragraphCounter--;
				}
				if(paragraphCounter>0) { //get new seed
					List<String> keys = new ArrayList<String>();
					keys.addAll(trigramMaps.keySet());
					String seed = generateSeed(keys);
					generateFileWithTrigrams(writer, seed, wordCounter, sentenceCounter, paragraphCounter);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Generate a random seed to start a paragraph. 
	 * The function to call the generateSeed to get the random key with Capital letter is found.
	 */
	private String generateSeed( List<String> keys) {
		Random randomizer = new Random();
		String randomKey = keys.get(randomizer.nextInt(keys.size()));
		if( Character.isUpperCase(randomKey.charAt(0))) {
			return randomKey;
		}
		else
			return generateSeed(keys);
		
	}
	
	
	public static void main(String[] args) throws SecurityException, IOException {
		

		// Instantiating the filehandler for logging.
		try {
			
			FileHandler handler = new FileHandler("TrigramsBookGenerator.log", true);
			
			LOGGER.info("Logger Name: "+LOGGER.getName());
			
			LOGGER.addHandler(handler);
			
			} catch(IOException ex) {
			System.out.println("Error while opening the Logger file"+ ex);
		}
		
		// Read the configuration file for input properties.
		try (InputStream input = new FileInputStream("config.properties")) {
            
            Properties prop = new Properties();

            // load the properties file
            prop.load(input);

            // Fetch all the properties values.
            maxWordsPerLine = Integer.parseInt(prop.getProperty("maxWordsPerLine").trim());
            maxSentencesPerParagraph = Integer.parseInt(prop.getProperty("maxSentencesPerParagraphs").trim());
            maxParagraphs = Integer.parseInt(prop.getProperty("maxParagraphs").trim());
    		String inputFileName = prop.getProperty("inputFileName");
    		String outputFileName = prop.getProperty("outputFileName");
    	    
    		LOGGER.info("List of Configurable Parameters:");
    		LOGGER.info("maxWordsPerLine: "+ maxWordsPerLine);
    		LOGGER.info("maxSentencesPerParagraph: " + maxSentencesPerParagraph);
    		LOGGER.info("maxParagraphs: " + maxParagraphs);
    		LOGGER.info("inputFileName: " + inputFileName);
    		LOGGER.info("outputFileName: " + outputFileName);
    		
    		TrigramsBookGenerator bookgenerator = new TrigramsBookGenerator();
    		bookgenerator.readInputFile(inputFileName);
    		bookgenerator.showTrigrams(); 
    		bookgenerator.writeOutputFile(outputFileName);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
}