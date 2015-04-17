package tab2pdf;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
	Parser p1,p2,p3,p4,p5,p6,p7,p8;
	@Before
	public void setUp() throws Exception{
		p1 = new Parser("parser1.txt");
		p2 = new Parser("parser2.txt");
		p3 = new Parser("parser3.txt");
		p4 = new Parser("parser4.txt");
		p5 = new Parser("parser5.txt");
		p6 = new Parser("parser6.txt");
		p7 = new Parser("parser7.txt");
		p8 = new Parser("parser8.txt");
	}
	
	@Test
	public void testFile1() throws IOException
	{
		Parser p = new Parser("test1.txt");
		// test title
		assertEquals("Moonlight Sonata", p.title);
		// test subtitle
		assertEquals("Ludwig van Beethoven", p.subtitle);
		// test space
		assertEquals(5.0, p.spacing, 0.0001);
		// test segments
		assertEquals("-2-----2-----2-----2-----", p.input.get(0).get(3));	// input 
		assertEquals("|", p.vLinesInput.get(0).get(0));					// vertical input
	}
	
	@Test
	public void testFile2() throws IOException
	{
		Parser p = new Parser("test2.txt");
		// test title
		assertEquals("Remembering Rain", p.title);
		// test subtitle
		assertEquals("Jim Matheos", p.subtitle);
		// test space
		assertEquals(4.4, p.spacing, 0.0001);
		// test segments
		assertEquals("-------------------------------<12>-----------", p.input.get(0).get(0));	// input 				  
		assertEquals("*||", p.vLinesInput.get(2).get(7));	// vertical input
	}
	
	@Test
	public void testFile3() throws IOException
	{
		Parser p = new Parser("test3.txt");
		// test title
		assertEquals("This is 3rd test", p.title);
		// test subtitle
		assertEquals("and the subtitle", p.subtitle);
		// test space
		assertEquals(2.45, p.spacing, 0.0001);
		// test segments
		assertEquals("-3-----3-----3-----3------2-----2-----2-----0-----", p.input.get(0).get(3));	// input 
		assertEquals("|", p.vLinesInput.get(0).get(11));	// vertical input
	}
	
	@Test
	public void testFile4() throws IOException
	{
		Parser p = new Parser("sample3.txt");
		// test segments
		assertEquals("-3-----3-----3-----3------2-----2-----2-----0-----", p.input.get(0).get(3));	// input 
		assertEquals("--------------------------------------------------", p.input.get(1).get(0));
		assertEquals("|", p.vLinesInput.get(0).get(11));	// vertical input
	}
	
	// this tests the scenerio where the input has a number where a vertical bar should be. it will replace it with a vertical bar
	@Test
	public void testP1() throws IOException
	{
		String[] line = new String[6];
		String[] vline = new String [12];
		for(int i = 0 ; i < 12; i++)
		{
			vline[i]="||";
		}
		line[0]="1------------1-2------------";
		line[1]="-----------0-1-1---------1--";
		line[2]="---------2---1---------0----";
		line[3]="-----0-3-----1-------2------";
		line[4]="---3---------1-----3--------";
		line[5]="-1-----------1-0-3----------";
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p1.input.get(0).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p1.vLinesInput.get(0).get(i));
		}
		
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="||";
			}
			else
			{
				vline[i]="|";
			}
		}
		line[0] = "-3-------------";
		line[1] = "-3-------0-3---";
		line[2] = "-------0-------";
		line[3] = "-----3---------";
		line[4] = "---2-----------";
		line[5] = "-3-------------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p1.input.get(1).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p1.vLinesInput.get(1).get(i));
		}
		line[0] = "-0-----------";
		line[1] = "---1---1-----";
		line[2] = "-----0----0--";
		line[3] = "---------2---";
		line[4] = "-3-----------";
		line[5] = "-------------";
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="|";
			}
			else
			{
				vline[i]="||";
			}
		}
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p1.input.get(2).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p1.vLinesInput.get(2).get(i));
		}
		
		
	}
	
	@Test
	public void testP4()
	{
		String[] line = new String[6];
		String[] vline = new String [12];
		for(int i = 0 ; i < 12; i++)
		{
			if(i % 2==0)
			{
				vline[i]="||";
			}
			else
			{
				vline[i]="|";
			}
		}
		line[0]="1------------";
		line[1]="-----------0-";
		line[2]="---------2---";
		line[3]="-----0-3-----";
		line[4]="---3---------";
		line[5]="-1-----------";
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p4.input.get(0).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p4.vLinesInput.get(0).get(i));
		}
		
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="|";
			}
			else
			{
				vline[i]="||";
			}
		}
		line[0] = "-2------------";
		line[1] = "-1---------1--";
		line[2] = "---------0----";
		line[3] = "-------2------";
		line[4] = "-----3--------";
		line[5] = "-0-3----------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p4.input.get(1).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p4.vLinesInput.get(1).get(i));
		}
		line[0] = "-3-------------";
		line[1] = "-3-------0-3---";
		line[2] = "-------0-------";
		line[3] = "-----3---------";
		line[4] = "---2-----------";
		line[5] = "-3-------------";
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="||";
			}
			else
			{
				vline[i]="|";
			}
		}
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p4.input.get(2).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p4.vLinesInput.get(2).get(i));
		}
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="|";
			}
			else
			{
				vline[i]="||";
			}
		}
		line[0] = "-0----------";
		line[1] = "---1---1----";
		line[2] = "-----0----0-";
		line[3] = "---------2--";
		line[4] = "-3----------";
		line[5] = "------------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p4.input.get(3).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p4.vLinesInput.get(3).get(i));
		}
		
	}	
	@Test
	public void testP6()
	{
		String[] line = new String[6];
		String[] vline = new String [12];
		for(int i = 0 ; i < 12; i++)
		{
			if(i % 2==0)
			{
				vline[i]="||";
			}
			else
			{
				vline[i]="|";
			}
		}
		line[0]="1------------";
		line[1]="-----------0-";
		line[2]="---------2---";
		line[3]="-----0-3-----";
		line[4]="---3---------";
		line[5]="-1-----------";
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p6.input.get(0).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p6.vLinesInput.get(0).get(i));
		}
		
		for(int i = 0; i < 12;i++)
		{
				vline[i]="|";
		}
		line[0] = "-2------------";
		line[1] = "-1---------1--";
		line[2] = "---------0----";
		line[3] = "-------2------";
		line[4] = "-----3--------";
		line[5] = "-0-3----------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p6.input.get(1).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p6.vLinesInput.get(1).get(i));
		}
		line[0] = "-3-------------";
		line[1] = "-3-------0-3---";
		line[2] = "-------0-------";
		line[3] = "-----3---------";
		line[4] = "---2-----------";
		line[5] = "-3-------------";
		for(int i = 0; i < 12;i++)
		{
			vline[i]="|";
		}
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p6.input.get(2).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p6.vLinesInput.get(2).get(i));
		}
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="|";
			}
			else
			{
				vline[i]="||";
			}
		}
		line[0] = "-0----------";
		line[1] = "---1---1----";
		line[2] = "-----0----0-";
		line[3] = "---------2--";
		line[4] = "-3----------";
		line[5] = "------------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p6.input.get(3).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p6.vLinesInput.get(3).get(i));
		}
		
	}
	@Test
	public void testP7()
	{
		String[] line = new String[6];
		String[] vline = new String [12];
		for(int i = 0 ; i < 12; i++)
		{
			vline[i]="||";	
		}
		line[0]="1------------";
		line[1]="-----------0-";
		line[2]="---------2---";
		line[3]="-----0-3-----";
		line[4]="---3---------";
		line[5]="-1-----------";
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p7.input.get(0).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p7.vLinesInput.get(0).get(i));
		}
		
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="||";
			}
			else
			{
				vline[i]="|";
			}
		}
		line[0] = "-2------------";
		line[1] = "-1---------1--";
		line[2] = "---------0----";
		line[3] = "-------2------";
		line[4] = "-----3--------";
		line[5] = "-0-3----------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p7.input.get(1).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p7.vLinesInput.get(1).get(i));
		}
		line[0] = "-3-------------";
		line[1] = "-3-------0-3---";
		line[2] = "-------0-------";
		line[3] = "-----3---------";
		line[4] = "---2-----------";
		line[5] = "-3-------------";
		for(int i = 0; i < 12;i++)
		{
			vline[i]="|";
		}
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p7.input.get(2).get(i));
		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p7.vLinesInput.get(2).get(i));
		}
		for(int i = 0; i < 12;i++)
		{
			if(i % 2==0)
			{
				vline[i]="|";
			}
			else
			{
				vline[i]="||";
			}
		}
		line[0] = "-0----------";
		line[1] = "---1---1----";
		line[2] = "-----0----0-";
		line[3] = "---------2--";
		line[4] = "-3----------";
		line[5] = "------------";
				
		for(int i=0; i < 6; i++)
		{
			assertEquals(line[i],p7.input.get(3).get(i));

		}
		for(int i=0; i < 12; i++)
		{
			assertEquals(vline[i],p7.vLinesInput.get(3).get(i));
		}
		
	}
}
