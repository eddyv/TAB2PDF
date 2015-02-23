package eecs2311.week4;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WordAnalyzer_Test {
	WordAnalyzer firstWordAnalyzer;
	WordAnalyzer secondWordAnalyzer;
	@Before
	public void setUp() throws Exception {
		firstWordAnalyzer = new WordAnalyzer("hollow");
		secondWordAnalyzer = new WordAnalyzer("mississippi!!!");
	}

	@Test
	public void testFirstRepeatedCharacter() {
		assertEquals('l',firstWordAnalyzer.firstRepeatedCharacter());
		assertEquals('s',secondWordAnalyzer.firstRepeatedCharacter());
	}

	@Test
	public void testFirstMultipleCharacter() {
		assertEquals('o',firstWordAnalyzer.firstMultipleCharacter());
		assertEquals('i',secondWordAnalyzer.firstMultipleCharacter());
		
	}

	@Test
	public void testCountRepeatedCharacters() {
		assertEquals(1,firstWordAnalyzer.countRepeatedCharacters());
		assertEquals(4,secondWordAnalyzer.countRepeatedCharacters());
	}

}
