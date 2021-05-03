package htwb.projekt.p2p.volltextsuche.textextraction18;

import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
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
				extractedString = "Can not load XML-File: " + string;
			}
			System.out.println(extractedString);
		}
//		String xmlParseString = XMLParser.readXML(null);
//		System.out.println();

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
