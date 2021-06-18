package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeachSearch;

class RegexTest {
	private static final Logger LOG = Logger.getLogger(SpeachSearch.class.getName());
	private String first = "Zusatztagesordnungspunkt 1:\r\n" + "Antrag der Fraktion BÜNDNIS 90/DIE GRÜ-\r\n"
			+ "NEN: Einsetzung von Ausschüssen\r\n"
			+ "(Drucksache 18/102) . . . . . . . . . . . . . . . . . . . . 75 D";

	private String namewith = "Michael Grosse-Brömer (CDU/CSU)  . . . . . . 76 A";
	private String gruen = "Britta Haßelmann (BÜNDNIS 90/\r\n" + "\r\n"
			+ "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 79 A";
	private String minister = "Dr. Thomas de Maizière, Bundesminister \r\n"
			+ "BMVg  . . . . . . . . . . . . . . . . . . . . . . . . . . . . 80 D";

	private String punkt = "Zusatztagesordnungspunkt 1:\r\n" + "Antrag der Fraktion BÜNDNIS 90/DIE GRÜ-\r\n"
			+ "NEN: Einsetzung von Ausschüssen\r\n"
			+ "(Drucksache 18/102) . . . . . . . . . . . . . . . . . . . . 75 D\r\n"
			+ "Michael Grosse-Brömer (CDU/CSU)  . . . . . . 76 A\r\n"
			+ "Dr. Petra Sitte (DIE LINKE)  . . . . . . . . . . . . . 76 D\r\n"
			+ "Thomas Oppermann (SPD)  . . . . . . . . . . . . . . 78 A\r\n" + "Britta Haßelmann (BÜNDNIS 90/\r\n"
			+ "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 79 A\r\n" + "\r\n"
			+ "Tagesordnungspunkt 2:\r\n" + "Antrag der Bundesregierung: Fortsetzung\r\n"
			+ "der Beteiligung bewaffneter deutscher\r\n" + "Streitkräfte an der von den Vereinten Na-\r\n"
			+ "tionen geführten Friedensmission in\r\n" + "Südsudan (UNMISS) auf Grundlage der\r\n"
			+ "Resolution 1996 (2011) des Sicherheitsrates\r\n" + "der Vereinten Nationen vom 8. Juli 2011\r\n"
			+ "und Folgeresolutionen, zuletzt 2109 (2013)\r\n" + "vom 11. Juli 2013\r\n"
			+ "(Drucksache 18/71) . . . . . . . . . . . . . . . . . . . . . 80 C";

	@Test
	void test() {
		assert (RegexPattern.PERSON.pattern.matcher(first).find());
		assert (RegexPattern.PERSON.pattern.matcher(namewith).find());
		assert (RegexPattern.PERSON.pattern.matcher(gruen).find());
		assert (RegexPattern.PERSON.pattern.matcher(minister).find());
		assert (RegexPattern.TOC_NAMES.pattern.matcher(punkt).find());

	}

	@Test
	void testCreateMap() {
		String[] strings = RegexPattern.TOC_NAMES.pattern.split(punkt);
		assert (strings.length > 0);
		ArrayList<String> names = new ArrayList<String>();
		for (int j = 0; j < strings.length; j++) {
			if (RegexPattern.PERSON.pattern.matcher(strings[j]).find()) {
				names.add(strings[j]);
			}
		}
		assert (names.isEmpty() == false);
	}

	@Test
	void testDrucksacheRegex() {
		String[] strings = RegexPattern.TOC_NAMES.pattern.split(punkt);
		assert (strings.length > 0);
//		LOG.log(Level.INFO, stringArrayToString(strings));
		Pattern drucksache = Pattern.compile("\\r*\\n*.*Drucksache");
		for (int i = 0; i < strings.length; i++) {
			if(drucksache.matcher(strings[i]).find()) {
				LOG.log(Level.INFO, drucksache.split(strings[i])[0]);
			}
		}
		
	}
	
	private String stringArrayToString(String[] strings) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			sb.append(i).append(":").append(strings[i]).append(System.lineSeparator());			
		}
		return sb.toString();
	}
}
