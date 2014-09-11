package org.bollore.edi;

import java.util.ArrayList;

public class Message {

	public String reference_number;
	public ArrayList<org.bollore.edi.Segment> segments;
	public Integer nb_segments; // Inutile si on fait un arraylist.size
	
	
	
}
