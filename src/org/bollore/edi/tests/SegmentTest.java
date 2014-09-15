package org.bollore.edi.tests;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.bollore.edi.Segment;
import org.junit.Test;

public class SegmentTest extends TestCase{

	@Test
	public void testClone() {
		Segment original = new Segment("name", "code", 1, 2, "description");
		Segment cloned = (Segment) original.clone();
		
		assertTrue(original != cloned);
		assertTrue(original.getClass() == cloned.getClass());
		assertFalse(original.equals(cloned));

	}

}
