package org.bollore.edi.tests;

import org.bollore.edi.EDIException;
import org.bollore.edi.Utils;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

//	public void testFormatDate() {
//		fail("Not yet implemented");
//	}
//
//	public void testIsDate() {
//		fail("Not yet implemented");
//	}

	public void testFormatInput() throws EDIException{
		assertNull(Utils.formatInput(null,1,","));
		assertNull(Utils.formatInput("1",null,","));
		assertNull(Utils.formatInput("1",1,null));
		assertEquals("1",Utils.formatInput("1",1,","));
		assertEquals("1",Utils.formatInput("1   ",1,","));
		assertEquals("1",Utils.formatInput("    1   ",1,","));
		
		assertEquals(" 1,2,3 ",Utils.formatInput("1,2,3",3,","));
		assertEquals(" 1,2,3 ",Utils.formatInput("    1,2,3   ",3,","));
		assertEquals(" 1,2,3, ",Utils.formatInput("    1,2,3,   ",4,","));
		assertEquals(" 1,2,3, ",Utils.formatInput("    1,2,3,   ",4,","));
		System.out.println("A"+Utils.formatInput("1,2,3",5,",")+"A");
		assertEquals(" 1,2,3 , , ",Utils.formatInput("1,2,3",5,","));
	}
	public void testisDate() {
	
		assertEquals(true, Utils.isDate("12:24:13","hh:mm:ss"));
		assertFalse(Utils.isDate(null,"hh:mm:ss"));
		assertFalse(Utils.isDate("12:24:13",null));
		assertFalse(Utils.isDate(null,null));
		}

}
