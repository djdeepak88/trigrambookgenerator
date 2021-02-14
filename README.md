# README #

###Project Introduction###

The project aims at providing java based solution to the problem mentioned in
[Kata14: Tom Swift under the Milkwood](http://codekata.com/kata/kata14-tom-swift-under-the-milkwood/)

###Solution Approach###

**Reading File**

The input file is read and each line is then broken into words and then trigrams are created using HashMap
with key as the two words and value being the next word.

**Trigram object storage into a HashMap since our data is in the form of key-value pair**

I have used HashMap<String, Trigram> to store trigrams. The key for hashing is the Trigram key. The value is a Trigram object containing
all the values of the words coming after key values.

**Generate Output File**

The output file is generated using the generateFileWithTrigrams() recursive function whose control parameters can
be defined in the "config.properties". The process is started by randomly selecting the trigram with a capital letter
and using that as the starting point.


**config.properties parameters**

Maximum words per lines to be generated.
maxWordsPerLine=20

Maximum sentences per paragraphs to be generated.
maxSentencesPerParagraphs=10

Maximum paragraphs to be generated.
maxParagraphs =12

Input File name to be read and processed
inputFileName=TomSwiftAirship.txt

Output file name
outputFileName=OutputText.txt


**Maven Build**

Maven build is created for the project along with unit test cases written in Junit.
