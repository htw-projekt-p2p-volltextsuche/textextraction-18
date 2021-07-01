package htwb.projekt.p2p.volltextsuche.textextraction18;

import java.time.LocalDate;
import java.util.UUID;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.Service;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeachSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;

public class Extractor {

	public static void main(String[] args) {
		System.out.println(outArgs(args));
		Service service = new Service();
		SpeachSearch search = new SpeachSearch();
		XMLExtract extractedXML = null;
		String extractedString = "";
		for (int i = 0; i < args.length; i++) {
			try {
				extractedXML = XMLParser.readXML(args[i]);
				extractedString = extractedXML.toString();

			} catch (Exception e) {
				extractedString = "Can not load XML-File: " + args[i] + "\n" + e.getMessage();
			}
			String presidentTitleofSpeach = "Eröffnungsrede";
			String presidentName = search.searchPresidentName(extractedXML.getProtocoll());
			String presidentAffiliation = search.searchPresidentAffiliation(extractedXML.getProtocoll());
			LocalDate presidentSpeachDate = extractedXML.getDate();
			String presidentSpeachTextSplit = presidentAffiliation + " " + presidentName + ":";
			String presidentSpeachText = search.searchPresidentText(extractedXML.getProtocoll());

			Speach speach = new Speach();
			speach.setTitle(presidentTitleofSpeach);
			speach.setSpeaker(presidentName);
			speach.setAffiliation(presidentAffiliation);
			speach.setDate(presidentSpeachDate);
			speach.setText(presidentSpeachText);

			UUID speachID = service.save(speach);
			TitlePersonMap map = search.getMap(extractedXML.getProtocoll());
			map = map.clearEmptyEntries();
			map = map.prettyUpEntries();
			System.out.println(map);
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
