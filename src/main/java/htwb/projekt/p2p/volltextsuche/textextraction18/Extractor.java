package htwb.projekt.p2p.volltextsuche.textextraction18;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.Service;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeachSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;

public class Extractor {

	public static void main(String[] args) {
		System.out.println(outArgs(args));
		Service service = new Service();
		XMLExtract extractedXML = null;
		String extractedString = "";
		for (int i = 0; i < args.length; i++) {
			try {
				extractedXML = XMLParser.readXML(args[i]);
				extractedString = extractedXML.toString();

			} catch (Exception e) {
				extractedString = "Can not load XML-File: " + args[i] + "\n" + e.getMessage();
			}
			String presidentTitleofSpeach = "Er\u00f6ffnungsrede";
			String presidentName = SpeachSearch.searchPresidentName(extractedXML.getProtocoll());
			String presidentAffiliation = SpeachSearch.searchPresidentAffiliation(extractedXML.getProtocoll());
			LocalDate presidentSpeachDate = extractedXML.getDate();
			String presidentSpeachTextSplit = presidentAffiliation + " " + presidentName + ":";
			String presidentSpeachText = SpeachSearch.searchPresidentText(extractedXML.getProtocoll());

			Speach speach = new Speach();
			speach.setTitle(presidentTitleofSpeach);
			speach.setSpeaker(presidentName);
			speach.setAffiliation(presidentAffiliation);
			speach.setDate(presidentSpeachDate);
			speach.setText(presidentSpeachText);
			
			
			UUID speachID = service.save(speach);
//			System.out.println(service.getAll());
//			service.getAllAsList().forEach(x -> System.out.println(x.toString()));
			//TODO extract the others
//			String postString = SpeachSearch.searchPresidentPostText(extractedXML.getProtocoll());
			
			//Extract Title & Speaker
			String[] toc = SpeachSearch.getToc(extractedXML.getProtocoll());
			List<String> agenda = SpeachSearch.getAgenda(toc);
			for (int j = 0; j < agenda.size(); j++) {
				System.out.println(j+":"+agenda.get(j));
			}
			//TODO concat 2 agenda items by "in Verbindung mit"
			//TODO pretty up Titls
			//TODO SpeachSearch.getPersons(toc) <- may the other toc entries?
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
