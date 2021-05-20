package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.RegexPattern;

public class SpeachSearch {
	public static String[] splitProtocoll(String text) {
		String[] parts = text.split(RegexPattern.OPENING.pattern);
		if(parts.length < 2) {
			throw new IllegalArgumentException("Can not split with Pattern: " + RegexPattern.OPENING.pattern);
		}
		return parts;
		
	}
	
	public static String searchPresidentName(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		return nameWithPresident.split(RegexPattern.PRESIDENT.pattern)[1];
	}
	
	public static String searchPresidentAffiliation(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		return nameWithPresident.split(RegexPattern.PRESIDENT.pattern)[0].split(" ")[0];
	}
	
	public static String searchPresidentText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		System.out.println(splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[0]);
		return presidentText.split(RegexPattern.PRESIDENT_BREAKPOINT.pattern)[0];
	}
	
	public static String searchPresidentPostText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		System.out.println(splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[0]);
		return presidentText.split(RegexPattern.PRESIDENT_BREAKPOINT.pattern)[1];
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
		//von SPEECH_BEGIN bis //BREAKPOINT
		return null;
	}
}
