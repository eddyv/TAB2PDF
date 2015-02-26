package tab2pdf;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;


public class DrawOutputDebugTest
{

	drawOutput draw1;
	drawOutput draw2;
	drawOutput draw3;

	@Before
	public void setUp() throws Exception
	{
		draw1 = new drawOutput("t1.txt","test1.pdf");
		draw1.createPdf("test1.pdf");
		draw2 = new drawOutput("t2.txt","test2.pdf");
		draw2.createPdf("test2.pdf");
		draw3 = new drawOutput("t3.txt","test3.pdf");
		draw3.createPdf("test3.pdf");
	}


	@Test
	/** 
	 * test the the segment size and the amount of vertical bar in each segment 
	 * for void method DispalyVerticalBars()
	 */
	public void testDisplayVerticalBars()
	{ // case 1
		System.out.println("Case1: \n");
		// expect value
		StringBuffer test1 = new StringBuffer();
		test1.append("3\n");
		for(int k = 0; k<3;k++){
			
			test1.append("12\n");

			for (int j = 0; j < 12; j++){
				test1.append("|\n");

			}
		}
		
		// test case1
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		draw1.displayVerticalBars();
		assertEquals(test1.toString(), outContent.toString());

		
		// case2
		//expect value
		StringBuffer test2 = new StringBuffer();
		test2.append("5\n");
		for (int k = 0; k < 4; k++)
		{
			test2.append("12\n");
			for (int j = 0; j < 12; j++)
			{
				test2.append("|\n");
			}	
			
		}
		
		test2.append("12\n||\n||\n||\n||\n||*\n*||\n||*\n*||\n||\n||\n||\n||\n");
		
		final ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent2));
		draw2.displayVerticalBars();
		assertEquals(test2.toString(), outContent2.toString());

	}

	 @Test
	 public void testDisplayInfo()
	 { //Test case 1
		 //expect value for Case1
		 StringBuffer test1 = new StringBuffer();
		 test1.append("3\n6\n");
		 test1.append("0 -------------------------\n");
		 test1.append("0 -------------------------\n");
		 test1.append("0 -------------------------\n");	 
		 test1.append("0 -----2-3-2-----0-3---0-3-\n");
		 test1.append("0 ---5-------5-1-----0-----\n");
		 test1.append("0 -4-----------------------\n\n");
		 
		 test1.append("6\n1 -------------------------\n");
		 test1.append("1 -------------------------\n");
		 test1.append("1 -----------------2-----2-\n");
		 test1.append("1 -----2-3-2---------------\n");
		 test1.append("1 ---5-------5-0h3---0h3---\n");
		 test1.append("1 -4-----------1-----------\n\n");
		 
		 test1.append("6\n2 -------------------------\n");
		 test1.append("2 -----1-----1-----1-----1-\n");
		 test1.append("2 ---2-----2-----2-----2---\n");
		 test1.append("2 -2-----2-----2-----2-----\n");
		 test1.append("2 -0-----------------------\n");
		 test1.append("2 -------------------------\n\n");
		// test case1
		 final ByteArrayOutputStream outContent3 = new ByteArrayOutputStream();
		 System.setOut(new PrintStream(outContent3));
		 draw1.displayInfo();
		 assertEquals(test1.toString(), outContent3.toString());
	 
			
		//Test case 2
		 StringBuffer test2 = new StringBuffer();
		 test2.append("5\n");
		 test2.append("6\n0 -------------------------\n0 -----1-----1-----1-----1-\n0 ---2-----2-----2-----2---\n0 -2-----2-----2-----2-----\n0 -0-----------------------\n0 -------------------------\n\n");
		 test2.append("6\n1 -------------------------\n1 -----1-----1-----1-----1-\n1 ---2-----2-----2-----2---\n1 -2-----2-----2-----2-----\n1 -------------------------\n1 -3-----------------------\n\n");
		 test2.append("6\n2 -------------------------\n2 -----1-----1-----3-----3-\n2 ---2-----2-----3-----3---\n2 -3-----3-----3-----3-----\n2 -------------5-----------\n2 -1-----------------------\n\n");
		 test2.append("6\n3 -------------------------\n3 -----3-----1-----0-----0-\n3 ---1-----2-----2-----1---\n3 -2-----2-----2-----0-----\n3 -------------------------\n3 -0-----------0-----------\n\n");
		 test2.append("6\n4 ------------------------------<12>----------\n4 -<12>------------------------------<12>-----\n4 *-----<5>-----------<7>--------------------*\n4 *---------------<7>------------------------*\n4 --------------------------------------------\n4 --------------------------------------------\n\n");
		 final ByteArrayOutputStream outContent4 = new ByteArrayOutputStream();
		 System.setOut(new PrintStream(outContent4));
		 draw2.displayInfo();
		 assertEquals(test2.toString(), outContent4.toString());
	 }
	 
	 @Test
	 /*
	  * Test the functionality of createPDF() 
	  */
	 public void testCreatePDF()
	 {
		 
	 }
	 // TODO: 
	 // 1. test two constructor generate same pdf
	 

}
