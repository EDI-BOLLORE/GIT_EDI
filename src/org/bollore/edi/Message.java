package org.bollore.edi;

import java.util.ArrayList;

public class Message {

	public String reference_number;
	public ArrayList<org.bollore.edi.Segment> segments;

	public Message(String reference_number, ArrayList<Segment> segments) {
		super();
		this.reference_number = reference_number;
		this.segments = segments;
	}

	public Message(String reference_number) {
		super();
		this.reference_number = reference_number;
		this.segments = new ArrayList<org.bollore.edi.Segment>();
	}

}
