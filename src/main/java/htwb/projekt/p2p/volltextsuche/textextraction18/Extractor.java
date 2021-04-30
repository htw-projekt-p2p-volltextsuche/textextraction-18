package htwb.projekt.p2p.volltextsuche.textextraction18;

import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;

public class Extractor {

	public static void main(String[] args) {
		System.out.println(outArgs(args));
		//TODO verify args
		for (String string : args) {
			System.out.println(XMLParser.readXML(string).toString());
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
		if(erg.isBlank()) {
			erg = "Nothing to read!";
		}
		return erg;
	}

}
