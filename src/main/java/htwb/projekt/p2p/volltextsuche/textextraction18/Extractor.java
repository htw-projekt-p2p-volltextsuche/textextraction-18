package htwb.projekt.p2p.volltextsuche.textextraction18;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.JSONFileWriter;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeechSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Extractor {
    private static final Logger LOG = Logger.getLogger(Extractor.class.getName());

    public static void main(String[] args) {
        System.out.println(outArgs(args));
        SpeechSearch search = new SpeechSearch();
        XMLExtract extractedXML = null;
        String extractedString = "";
        for (String arg : args) {
            if (Pattern.compile("001\\.xml").matcher(arg).find()) {
                continue;
            }
            System.out.println("Try to extract from: " + arg);
            try {
                extractedXML = XMLParser.readXML(arg);
                extractedString = extractedXML.getProtocoll();

            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Can not load XML-File: " + arg + "\n" + e.getMessage());
            }
            String presidentTitleofSpeach = "Eröffnungsrede";
            String presidentName = search.searchPresidentName(extractedString);
            String presidentAffiliation = search.searchPresidentAffiliation(extractedString.strip());
            LocalDate presidentSpeachDate = extractedXML.getDate();
            String presidentSpeachTextSplit = presidentAffiliation + presidentName + ":\\n+";
            String presidentSpeachText = search.searchPresidentText(extractedString, presidentSpeachTextSplit);

            Speach speach = new Speach(presidentTitleofSpeach, presidentName, presidentAffiliation, presidentSpeachDate, presidentSpeachText);

            TitlePersonMap map = search.getMap(extractedString);
            map = map.prettyUpEntries();
            map = map.clearEmptyEntries();
//            LOG.log(Level.INFO, extractedString);
            List<Speach> speachList = search.addToListFromMap(map, search.searchPresidentPostText(extractedString, presidentSpeachTextSplit), extractedXML.getDate());
            speachList.add(speach);

            JSONFileWriter.write(speachList, arg);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Scan...");
            }
        }
    }

    private static String outArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append("Your Input: ");
            sb.append(args[i]);
            sb.append(System.lineSeparator());
        }
        String erg = sb.toString();
        if (erg.isBlank()) {
            erg = "Nothing to read!";
        }
        return erg;
    }

}
