package org.bollore.edi.tests;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.bollore.edi.Component;
import org.junit.Test;

public class ComponentTest extends TestCase{

	@Test
	public void testClone() {
		Component original = new Component("datatype", "1", "2", true, true,
				"label", "documentation");

		Component cloned = (Component) original.clone();
		//original.minLength = "3";
		
		assertTrue(original != cloned);
		assertTrue(original.getClass() == cloned.getClass());
		// 		Bug à corriger?
		//		assertTrue(original.equals(cloned));

	}

}
