package org.bollore.edi;

import java.util.ArrayList;

public class Message {

	//public Integer nb_segments;
	public String reference_number;
	public ArrayList<org.bollore.edi.Segment> segments;

	public Message(String reference_number, ArrayList<Segment> segments) {
		super();
		this.reference_number = reference_number;
		this.segments = segments;
		//this.nb_segments=segments.size();
	}

	public Message(String reference_number) {
		super();
		this.reference_number = reference_number;
		this.segments = new ArrayList<org.bollore.edi.Segment>();
	}
	
	
	
	public void addSegment(Segment segment) {
		//this.nb_segments++;
		this.segments.add(segment);
	}
	
	public String countSegments() {
		
		Integer temp_result=2;
		
		ArrayList<org.bollore.edi.Segment> segments=this.segments;
		
		for (int i = 0; i < segments.size(); i++) {
			if(!segments.get(i).isEmpty(segments.get(i))){
				temp_result++;
			}
		}
				
		return String.valueOf(temp_result);
		
	}

}
