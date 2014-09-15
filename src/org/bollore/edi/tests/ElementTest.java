package org.bollore.edi.tests;

import java.util.ArrayList;

import org.bollore.edi.Component;
import org.bollore.edi.Element;

import junit.framework.TestCase;

public class ElementTest extends TestCase {

	public void testGetComponent() {
		// fail("Not yet implemented");
	}

	public void testMaxRankComponentNonNull() {
		ArrayList<org.bollore.edi.Component> components = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c1 = new Component("String", "10", "1", true,
				true, "label1", "doc1", "value2");
		org.bollore.edi.Component c2 = new Component("String", "10", "1", true,
				true, "label2", "doc2", null);
		org.bollore.edi.Component c3 = new Component("String", "10", "1", true,
				true, "label3", "doc3", "value3");

		components.add(c1);
		components.add(c2);
		components.add(c3);

		org.bollore.edi.Element element = new Element("code_element", true,
				true, "label_element", "doc_element", components);

		assertTrue(element.MaxRankComponentNonNull() == 3);

		org.bollore.edi.Element element2 = new Element("code_element", true,
				true, "label_element", "doc_element", null);

		assertTrue(element2.MaxRankComponentNonNull() == 0);

	}

	public void testHasEmptyComponents() {

		ArrayList<org.bollore.edi.Component> components = new ArrayList<org.bollore.edi.Component>();
		ArrayList<org.bollore.edi.Component> components2 = new ArrayList<org.bollore.edi.Component>();

		org.bollore.edi.Component c1 = new Component("String", "10", "1", true,
				true, "label1", "doc1", "value2");
		org.bollore.edi.Component c2 = new Component("String", "10", "1", true,
				true, "label2", "doc2", null);
		org.bollore.edi.Component c3 = new Component("String", "10", "1", true,
				true, "label3", "doc3", "value3");

		components.add(c1);
		components.add(c2);
		components.add(c3);

		org.bollore.edi.Element element = new Element("code_element", true,
				true, "label_element", "doc_element", components);

		assertFalse(element.HasEmptyComponents());

		components2.add(c2);
		components2.add(c2);
		components2.add(c2);

		org.bollore.edi.Element element2 = new Element("code_element", true,
				true, "label_element", "doc_element", components2);

		assertTrue(element2.HasEmptyComponents());
	}

}