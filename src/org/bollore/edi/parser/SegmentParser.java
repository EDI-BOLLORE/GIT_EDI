package org.bollore.edi.parser;

import java.util.ArrayList;

import org.bollore.edi.*;

public class SegmentParser {

	
	public static String getSegmentCode(String segment) throws EDIParseException{
		String result=null;
		
		if(segment!=null&&segment.length()>=3){
			String temp=segment.substring(0,3);
			if(Utils.PatternCheck(temp,"[A-Z]{3}")){
				result=temp;
			} else {
				throw new EDIParseException("Le segment '"+segment+"' à parser ne comporte pas de code à 3 caractères alphabétiques");
			}
			
		} else {
			throw new EDIParseException("Le segment à parser est vide ou de longueur < 3");
		}
		return result;
		}
	
	
	
	public static Segment parse(String segment,String element_separator,String component_separator) throws EDIParseException{
		Segment result=new Segment();
		
		/**
		 * Prévoir le cas où element_separator et component_separator sont des caractères spéciaux pour les regex 
		 */
		
		
		result.code=getSegmentCode(segment);
		
		ArrayList<String> segments=Utils.PatternExtract(segment,"([A-Z0-9"+component_separator+"]"+element_separator+")", 1);
		
		for (int i = 1; i < segments.size(); i++) {
			result.elements.addAll(ElementParser.parseElement(segments.get(i), component_separator));
			//System.out.println(segments.get(i));
		}
				
		return result;
	}
		
		public static void main(String[] args) throws EDIParseException {
			//System.out.println(getSegmentCode("ABC DHEIODH"));
			//System.out.println(getSegmentCode(" ABC DHEIODH"));
			//System.out.println(getSegmentCode("1ABC DHEIODH"));
			Segment seg=parse("UNH+1234+CUSCAR:D:95B:UN","+",":");
			
			ArrayList<Element> elements=seg.elements;
			
			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).printElement();
			}
		}
		
	}

