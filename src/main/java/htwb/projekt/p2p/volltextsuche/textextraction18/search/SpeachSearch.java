package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.RegexPattern;

public class SpeachSearch {

	private static final Logger LOG = Logger.getLogger(SpeachSearch.class.getName());

	public static String[] getToc(String text) {
		text = splitProtocoll(text)[0];
		String[] arr = text.split(RegexPattern.TOC_NAMES.pattern);
		arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
		return arr;
	}

	public static List<String> getAgenda(String[] toc) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < toc.length; i++) {
			if(toc[i].contains("ordnungspunkt")) {
				list.add(toc[i]);
			}
		}
		return list;
	}

	public static String[] splitProtocoll(String text) {
		String[] parts = text.split(RegexPattern.OPENING.pattern);
		if (parts.length < 2) {
			throw new IllegalArgumentException("Can not split with Pattern: " + RegexPattern.OPENING.pattern);
		}
		return parts;
	}

	public static String searchPresidentName(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		String[] strAr = nameWithPresident.split(RegexPattern.PRESIDENT.pattern);
		String name = "";
		if (strAr.length == 3) {
			name = strAr[2];

		} else if (strAr.length == 2) {
			name = strAr[1];
		}
		return name;
	}

	public static String searchPresidentAffiliation(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		String[] strAr = nameWithPresident.split(RegexPattern.PRESIDENT.pattern);
		String affiliation = "";
		if (strAr.length == 3) {
			affiliation = strAr[0] + strAr[1];

		} else if (strAr.length == 2) {
			affiliation = strAr[0];
		}
		return affiliation;
	}

	public static String searchPresidentText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		return presidentText.split(RegexPattern.PRESIDENT_BREAKPOINT.pattern)[0];
	}

	public static String searchPresidentPostText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		String[] postText = presidentText.split(RegexPattern.PRESIDENT_BREAKPOINT.pattern);
		String erg = "";
		for (int i = 1; i < postText.length; i++) {
			erg += postText[i];
		}
		return erg;
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
		// von SPEECH_BEGIN bis //BREAKPOINT
		return null;
	}
}
