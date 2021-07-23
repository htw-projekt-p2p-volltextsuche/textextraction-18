package test;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeechSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexTest {
    private static final Logger LOG = Logger.getLogger(SpeechSearch.class.getName());
    private SpeechSearch search;
    private String inputString;
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

    private String question = "Dr. Matthias Miersch (SPD) . . . . . . . . . . . . . . 117 D\r\n" + "\r\n"
            + "Josef Göppel (CDU/CSU)  . . . . . . . . . . . . . . . 118 C\r\n" + "Tagesordnungspunkt 6:\r\n"
            + "Fragestunde\r\n" + "(Drucksache 18/87). . . . . . . . . . . . . . . . . . . . . 119 C\r\n" + "\r\n"
            + "Mündliche Frage 1\r\n" + "Lisa Paus (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "Einfluss des Staatsministers a. D. Eckart\r\n" + "von Klaeden auf Entscheidungen im Be-\r\n"
            + "reich Elektromobilität\r\n" + "Antwort\r\n" + "Dr. Maria Böhmer, Staatsministerin \r\n" + "\r\n"
            + "BK . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 119 D\r\n" + "Zusatzfrage\r\n"
            + "Lisa Paus (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 119 D\r\n"
            + "\r\n" + "Mündliche Frage 2\r\n" + "Lisa Paus (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "Etwaige Loyalitätskonflikte des Staats-\r\n" + "ministers a. D. Eckart von Klaeden im\r\n"
            + "dienstlichen Kontakt zu der Investment-\r\n" + "bank Goldman Sachs\r\n" + "Antwort\r\n"
            + "Dr. Maria Böhmer, Staatsministerin \r\n" + "\r\n"
            + "BK . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 120 A\r\n" + "Zusatzfragen\r\n"
            + "Lisa Paus (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 120 B\r\n"
            + "Volker Beck (Köln) (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 120 C\r\n" + "Dr. Gerhard Schick (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 120 D\r\n" + "\r\n"
            + "Mündliche Frage 3\r\n" + "Annette Groth (DIE LINKE)\r\n"
            + "Schritte der Bundesregierung zur Informa-\r\n" + "tion der Öffentlichkeit über den Stand der\r\n"
            + "Verhandlungen zum geplanten Freihandels-\r\n" + "abkommen zwischen der EU und den USA\r\n"
            + "Antwort\r\n" + "Ernst Burgbacher, Parl. Staatssekretär \r\n" + "\r\n"
            + "BMWi  . . . . . . . . . . . . . . . . . . . . . . . . . . . . 121 B\r\n" + "Zusatzfragen\r\n"
            + "Annette Groth (DIE LINKE) . . . . . . . . . . . . . 121 D\r\n"
            + "Ralph Lenkert (DIE LINKE) . . . . . . . . . . . . . 122 A\r\n"
            + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 122 B\r\n"
            + "Sigrid Hupach (DIE LINKE) . . . . . . . . . . . . . 122 C\r\n"
            + "Heike Hänsel (DIE LINKE) . . . . . . . . . . . . . . 122 D\r\n" + "Britta Haßelmann (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 123 A\r\n"
            + "Harald Ebner (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 123 C\r\n"
            + "Petra Pau (DIE LINKE) . . . . . . . . . . . . . . . . . 123 D\r\n" + "\r\n"
            + "Deutscher Bundestag – 18. Wahlperiode – 3. Sitzung. Berlin, Donnerstag, den 28. November 2013 III\r\n"
            + "Mündliche Frage 4\r\n" + "Annette Groth (DIE LINKE)\r\n" + "Einbeziehung der Bundesregierung in die\r\n"
            + "Verhandlungen über das geplante Freihan-\r\n" + "delsabkommen zwischen EU und USA\r\n" + "Antwort\r\n"
            + "Ernst Burgbacher, Parl. Staatssekretär \r\n" + "\r\n"
            + "BMWi  . . . . . . . . . . . . . . . . . . . . . . . . . . . . 124 A\r\n" + "Zusatzfragen\r\n"
            + "Annette Groth (DIE LINKE)  . . . . . . . . . . . . . 124 B\r\n"
            + "Sigrid Hupach (DIE LINKE) . . . . . . . . . . . . . 124 C\r\n" + "Volker Beck (Köln) (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 124 D\r\n"
            + "Ralph Lenkert (DIE LINKE)  . . . . . . . . . . . . . 125 A\r\n" + "\r\n" + "Mündliche Frage 5\r\n"
            + "Peter Meiwald (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "Schäden durch den Erdölaustritt aus dem\r\n" + "Kavernenfeld in Etzel\r\n" + "Antwort\r\n"
            + "Ernst Burgbacher, Parl. Staatssekretär \r\n" + "\r\n"
            + "BMWi  . . . . . . . . . . . . . . . . . . . . . . . . . . . . 125 B\r\n" + "Zusatzfragen\r\n"
            + "Peter Meiwald (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 125 C\r\n"
            + "Herbert Behrens (DIE LINKE)  . . . . . . . . . . . 125 D\r\n"
            + "Pia Zimmermann (DIE LINKE) . . . . . . . . . . . 126 A\r\n" + "Julia Verlinden (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 126 B\r\n"
            + "Ralph Lenkert (DIE LINKE)  . . . . . . . . . . . . . 126 C\r\n" + "\r\n" + "Mündliche Frage 11\r\n"
            + "Agnieszka Brugger (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "Beteiligung von US-Stützpunkten in Deutsch-\r\n" + "land an extralegalen Hinrichtungen\r\n"
            + "Antwort\r\n" + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 127 A\r\n" + "Zusatzfragen\r\n"
            + "Agnieszka Brugger (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 127 A\r\n"
            + "Heike Hänsel (DIE LINKE) . . . . . . . . . . . . . . 127 C\r\n" + "Katja Keul (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 127 D\r\n"
            + "Jan van Aken (DIE LINKE) . . . . . . . . . . . . . . 128 A\r\n"
            + "Stefan Liebich (DIE LINKE) . . . . . . . . . . . . . 128 B\r\n" + "Volker Beck (Köln) (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 128 B\r\n"
            + "Sylvia Kotting-Uhl (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 128 D\r\n"
            + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 129 A\r\n" + "Harald Ebner (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 129 C\r\n"
            + "Dr. Alexander S. Neu (DIE LINKE)  . . . . . . . 129 C\r\n" + "Mündliche Frage 12\r\n"
            + "Uwe Kekeritz (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n" + "\r\n"
            + "Entscheidung über die Ansiedlung des US-\r\n" + "Afrikakommandos in Deutschland\r\n" + "\r\n"
            + "Antwort\r\n" + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 130 A\r\n" + "\r\n" + "Zusatzfragen\r\n"
            + "Uwe Kekeritz (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 130 B\r\n"
            + "Stefan Liebich (DIE LINKE) . . . . . . . . . . . . . 130 D\r\n"
            + "Heike Hänsel (DIE LINKE) . . . . . . . . . . . . . . 131 A\r\n" + "\r\n" + "Mündliche Frage 15\r\n"
            + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n" + "\r\n"
            + "Vorlage aller völkerrechtlichen Vereinba-\r\n" + "rungen mit den ehemals westalliierten Sta-\r\n"
            + "tionierungsstaaten\r\n" + "\r\n" + "Antwort\r\n" + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 131 C\r\n" + "\r\n" + "Zusatzfragen\r\n"
            + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 131 D\r\n" + "Katja Keul (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN) . . . . . . . . . . . . . . . . . . . . . 132 A\r\n"
            + "Heike Hänsel (DIE LINKE) . . . . . . . . . . . . . . 132 B\r\n" + "\r\n" + "Mündliche Frage 16\r\n"
            + "Heike Hänsel (DIE LINKE)\r\n" + "\r\n" + "Medienberichte über die Koordinierung\r\n"
            + "von US-Drohneneinsätzen von deutschem\r\n" + "Staatsgebiet aus\r\n" + "\r\n" + "Antwort\r\n"
            + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 132 C\r\n" + "\r\n" + "Zusatzfragen\r\n"
            + "Heike Hänsel (DIE LINKE) . . . . . . . . . . . . . . 132 D\r\n" + "\r\n" + "Mündliche Frage 19\r\n"
            + "Inge Höger (DIE LINKE)\r\n" + "\r\n" + "Internationale Konferenz für eine massen-\r\n"
            + "vernichtungswaffenfreie Zone Naher und\r\n" + "Mittlerer Osten\r\n" + "\r\n" + "Antwort\r\n"
            + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 133 B\r\n" + "\r\n" + "Zusatzfragen\r\n"
            + "Inge Höger (DIE LINKE)  . . . . . . . . . . . . . . . 133 C\r\n" + "\r\n"
            + "IV Deutscher Bundestag – 18. Wahlperiode – 3. Sitzung. Berlin, Donnerstag, den 28. November 2013\r\n"
            + "Mündliche Frage 23\r\n" + "Marieluise Beck (Bremen) (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "\r\n" + "Perspektive für die Östliche Partnerschaf-\r\n" + "ten der EU\r\n" + "\r\n" + "Antwort\r\n"
            + "Cornelia Pieper, Staatsministerin \r\n" + "\r\n"
            + "AA  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 134 A\r\n" + "\r\n"
            + "Zusatzfragen\r\n" + "Marieluise Beck (Bremen) (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 134 B\r\n"
            + "Dr. Alexander S. Neu (DIE LINKE)  . . . . . . . 135 B\r\n" + "Friedrich Ostendorff (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 135 C\r\n" + "\r\n"
            + "Mündliche Frage 24\r\n" + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n"
            + "\r\n" + "Etwaige Vergabe von IT-Aufträgen an das\r\n" + "US-Unternehmen Computer Sciences Cor-\r\n"
            + "poration durch die Bundesregierung\r\n" + "Antwort\r\n" + "Dr. Ole Schröder, Parl. Staatssekretär \r\n"
            + "\r\n" + "BMI  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 136 A\r\n" + "\r\n"
            + "Zusatzfragen\r\n" + "Hans-Christian Ströbele (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 136 D\r\n" + "Uwe Kekeritz (BÜNDNIS 90/\r\n"
            + "\r\n" + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 137 B\r\n" + "\r\n"
            + "Mündliche Frage 26\r\n" + "Uwe Kekeritz (BÜNDNIS 90/\r\n" + "\r\n" + "DIE GRÜNEN)\r\n" + "\r\n"
            + "Beteiligung des US-Unternehmens Compu-\r\n" + "ter Sciences Corporation an der Entfüh-\r\n"
            + "rung des deutschen Staatsbürgers Khaled\r\n" + "el-Masri\r\n" + "\r\n" + "Antwort\r\n"
            + "Dr. Ole Schröder, Parl. Staatssekretär \r\n" + "\r\n"
            + "BMI  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 137 D\r\n" + "\r\n" + "Zusatzfragen\r\n"
            + "Uwe Kekeritz (BÜNDNIS 90/\r\n" + "\r\n"
            + "DIE GRÜNEN)  . . . . . . . . . . . . . . . . . . . . . 137 D\r\n" + "\r\n"
            + "Zusatztagesordnungspunkt 2:\r\n" + "\r\n" + "Vereinbarte Debatte: zu dem vorläufigen\r\n"
            + "Atomabkommen mit dem Iran. . . . . . . . . . . 138 A";

    @BeforeEach
    void setup() {
        search = new SpeechSearch();

        URL url = Thread.currentThread().getContextClassLoader().getResource("18003.xml");
        inputString = url.getFile();
    }

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
        Pattern drucksache = Pattern.compile("\\r*\\n*.*Drucksache");
        for (int i = 0; i < strings.length; i++) {
            if (drucksache.matcher(strings[i]).find()) {
            }
        }

    }

    @Test
    void testQuestionMatchTitle() {
        assert (RegexPattern.TITLE.pattern.matcher(question).find());
        assert (RegexPattern.PERSON.pattern.matcher(question).find());
        String[] strings = RegexPattern.TOC_NAMES.pattern.split(question);
        assert (strings.length > 0);
        for (int i = 0; i < strings.length; i++) {
            if (RegexPattern.TITLE.pattern.matcher(strings[i]).find()) {
                if (i + 1 < strings.length) {
                    assert (!RegexPattern.TITLE.pattern.matcher(strings[i + 1]).find());
                    LOG.log(Level.INFO, strings[i + 1]);

                    assert (RegexPattern.PERSON.pattern.matcher(strings[i + 1]).find());
                }
            }
        }
    }

    @Test
    void testpartyOrAffiliation() {
        String[] strings = RegexPattern.TOC_NAMES.pattern.split(question);
        List<Person> personList = new ArrayList<Person>();
        for (int i = 0; i < strings.length; i++) {
            if (RegexPattern.TITLE.pattern.matcher(strings[i]).find()) {
                if (i + 1 < strings.length) {
                    assert (!RegexPattern.TITLE.pattern.matcher(strings[i + 1]).find());

                    LOG.log(Level.INFO, strings[i + 1]);

                    assert (RegexPattern.PERSON.pattern.matcher(strings[i + 1]).find());
                    if (RegexPattern.PERSON.pattern.matcher(strings[i + 1]).find()) {
                        Person p = search.createPersonfromString(strings[i + 1]);
                        personList.add(p);

                    }
                }
            }
        }
        assert (personList != null);
        personList.forEach(x -> assertTrue(x != null));
    }

    @Test
    void testgetMap() {
        inputString = XMLParser.readXML(inputString).getProtocoll();
        String[] agendaList = search.getAgendaItems(inputString);
        assert agendaList.length > 0;
        List<String> titleList = search.getAgenda(agendaList);
        assert titleList.size() > 0;
        LOG.log(Level.INFO, Arrays.asList(agendaList).toString());
        TitlePersonMap map = search.createMap(agendaList);
        Integer sizeOfMap = map.getSize();
        assert sizeOfMap > 0;

//		LOG.log(Level.INFO, map.toString());
    }

    private String stringArrayToString(String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(i).append(":").append(strings[i]).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Test
    void testName() {
        String michael = "Michael Grosse-Brömer (CDU/CSU):";
        Person p = search.createPersonfromString(michael);
        assert michael.matches(p.getRegexFromPerson().pattern());
    }

    @Test
    void fileNameTest() {
        String fileName = "/mnt/d/GitRepos/textextraction-18/target/18003.xml";
        String subString = "";
        if (fileName.contains("/")) {
            subString = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        subString = subString.substring(0, subString.indexOf("."));
        assertEquals(subString, "18003");
    }

    @Test
    void extraSigninNameTest() {
        String name = "I Ralph Lenkert";
        if (Pattern.compile("I+").matcher(name).find()) {
            name = name.substring(name.lastIndexOf("I")+2);
        }
        LOG.log(Level.INFO, name);
        assertEquals("Ralph Lenkert", name);
    }

    @Test
    void matcher(){
        String name = "I Ralph Lenkert";
        assert Pattern.compile("I+").matcher(name).find();
    }

    @Test
    void matcher2(){
        String name = "Dr. Johann Wadephul (CDU/CSU):";
        assert Pattern.compile("Dr. Johann Wadephul\\s*\\(.+\\r*\\n*.+\\):").matcher(name).find();
    }

    @Test
    void PatternGroupTest(){
        String test = "Danke.\n" +
                "\n" +
                "(Beifall bei der LINKEN)\n" +
                "\n" +
                "Präsident Dr. Norbert Lammert:\n" +
                "Für die CDU/CSU-Fraktion erhält der Kollege Johann\n" +
                "\n" +
                "Wadephul das Wort.\n" +
                "\n" +
                "Dr. Johann Wadephul (CDU/CSU):\n" +
                "Herr Präsident! Meine sehr verehrten Damen und Her-\n" +
                "\n" +
                "ren! Spätestens nachdem der Bundestagspräsident noch\n" +
                "einmal auf den Minderheitenschutz im Deutschen Bun-\n" +
                "destag hingewiesen hat, ist es notwendig, darzulegen,\n" +
                "warum wir empfehlen, den Antrag heute nicht zu behan-\n" +
                "deln.";

        Matcher matcher = RegexPattern.BREAKINGPOINT.pattern.matcher(test);
        if(matcher.find()){
//            Matcher matcher = RegexPattern.BREAKINGPOINT.pattern.matcher(test);
            String regexTest = RegexPattern.BREAKINGPOINT.pattern.split(test)[1];
            String name = test.substring(matcher.start(), matcher.end());
            System.out.println(regexTest);
            System.out.println(name);

        } else LOG.log(Level.INFO, "NOPE");
    }

    @Test
    void specialNameAndAffilationTest(){
        String nameAndAffiliation = " Carsten Schneider (Erfurt) (SPD)  . . . . . . . . . 948";
        String name = "";
        String affiliation = "";
        Matcher m = Pattern.compile(".?\\(.+\\)\\s+\\(").matcher(nameAndAffiliation);
        if(m.find()){
            name = nameAndAffiliation.substring(0,m.start());
            affiliation = nameAndAffiliation.substring(m.end()-1);
            m = Pattern.compile("\\)").matcher(affiliation);
            if (m.find()){
                affiliation = affiliation.substring(0,m.start()-1);
                affiliation.strip();
            }
        }
    }

    @Test
    void parlTest(){
        String person = "Ulrich Kelber, Parl. Staatssekretär \n" +
                "BMJV  . . . . . . . . . . . . . . . . . . . . . . . . . . . 9648 D";
        String name = "";
        String city = null;
        String affiliation = "";
        Matcher special = Pattern.compile(".?\\(.+\\)\\s+\\(").matcher(person);
        Matcher party = RegexPattern.PERSON_PARTY.pattern.matcher(person);
        Matcher member = RegexPattern.PERSON_AFFILIATION.pattern.matcher(person);
        if (member.find()) {
            name = person.substring(0, member.start());
            affiliation = person.substring(member.start());
            affiliation = affiliation.replaceAll("(.)\\n(.)", "$1$2");
            affiliation = affiliation.substring(2);
//            LOG.info(affiliation);
            Matcher parl = Pattern.compile("Parl\\.\\s").matcher(affiliation);
            if (parl.find()) {
                affiliation = affiliation.substring(parl.end());
                LOG.info(affiliation);
            }
            Matcher m = Pattern.compile(".?\\s+.?").matcher(affiliation);
            if (m.find()) {
                affiliation = affiliation.substring(0, m.start()+1);
//                LOG.info(affiliation);
            }
            affiliation = affiliation.strip();
        }
//        LOG.info(name + " " + affiliation);
        assert name.equals("Ulrich Kelber");
//        assert affiliation.equals("Staatssekretär");
    }

    @Test
    void prettyUpAffilationTest(){
        String spd = "(SPD)";
        Matcher m = Pattern.compile("\\(.+\\)").matcher(spd);
        if (m.find()){
//            LOG.info(spd);
            spd = spd.substring(1,spd.length()-1);
        }
//        LOG.info(spd);
        assert spd.equals("SPD");
    }

}
