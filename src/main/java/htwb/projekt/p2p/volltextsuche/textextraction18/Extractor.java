package htwb.projekt.p2p.volltextsuche.textextraction18;

import java.time.LocalDate;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.FileWirter;
import htwb.projekt.p2p.volltextsuche.textextraction18.misc.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeachSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;

public class Extractor {

	public static void main(String[] args) {
		System.out.println(outArgs(args));
		XMLExtract extractedXML = null;
		String extractedString = "";
		for (String string : args) {
			try {
				extractedXML = XMLParser.readXML(string);
				extractedString = extractedXML.toString();

			} catch (Exception e) {
				extractedString = "Can not load XML-File: " + string + "\n" + e.getMessage();
			}
			String presidentTitleofSpeach = "Er\u00f6ffnungsrede";
			String presidentName = SpeachSearch.searchPresidentName(extractedXML.getProtocoll());
			String presidentAffiliation = SpeachSearch.searchPresidentAffiliation(extractedXML.getProtocoll());
			LocalDate presidentSpeachDate = extractedXML.getDate();
			String presidentSpeachTextSplit = presidentAffiliation + " " + presidentName + ":";
			String presidentSpeachText = SpeachSearch.searchPresidentText(extractedXML.getProtocoll());

			Speach presidentSpeach = new Speach(presidentTitleofSpeach, presidentName, presidentAffiliation,
					presidentSpeachDate, presidentSpeachText);
			FileWirter.write(presidentSpeach.toJSON(), presidentSpeachDate.toString());
		}

	}

	private static String outArgs(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append("Your Input: ");
			sb.append(args[i]);
			sb.append(" ");
		}
		String erg = sb.toString();
		if (erg.isBlank()) {
			erg = "Nothing to read!";
		}
		return erg;
	}

}
