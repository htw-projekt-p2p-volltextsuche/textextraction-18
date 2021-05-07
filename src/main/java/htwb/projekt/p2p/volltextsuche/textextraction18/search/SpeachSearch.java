package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.RegexPattern;

public class SpeachSearch {
	public static String deleteTableOfContents(String text) {
		String[] parts = text.split(RegexPattern.OPENING.pattern);
		System.out.println(parts.length);
//		if(parts.length != 2) {
//			throw new IllegalArgumentException("Can not split with Pattern: " + RegexPattern.OPENING.pattern);
//		}
		System.out.println(parts[1].split(":")[0]);
		return parts[0];
		
	}
	
	
	
	public static String searchSpeachSnippet(String text) {
		return null;
	}

	public static String searchTitle(String text) {
		return null;
	}

	public static String searchSpeaker(String text) {
		return null;
	}

	public static String searchAffiliation(String text) {
		return null;
	}

	public static String searchDate(String text) {
		return null;
	}

	public static String searchText(String text) {
		return null;
	}
}
