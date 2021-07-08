package htwb.projekt.p2p.volltextsuche.textextraction18;

import htwb.projekt.p2p.volltextsuche.textextraction18.misc.JSONFileWriter;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speech;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeechSearch;
import htwb.projekt.p2p.volltextsuche.textextraction18.xml.XMLParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Extractor {
    private static final Logger LOG = LogManager.getLogger(Extractor.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.error("Nothing to read");
        }
        int speechCount = 0;
        int fileCount = 0;
        System.out.println(outArgs(args));
        SpeechSearch search = new SpeechSearch();
        XMLExtract extractedXML = null;
        String extractedString = "";
        for (String arg : args) {
            if (Pattern.compile("001\\.xml").matcher(arg).find()) {
                continue;
            }
            LOG.debug("Try to extract from: " + arg);
            try {
                extractedXML = XMLParser.readXML(arg);
                extractedString = extractedXML.getProtocoll();

            } catch (Exception e) {
                LOG.error("Can not load XML-File: " + arg + "\n" + e.getMessage());
            }
            if (extractedString == null) {
                LOG.warn("failure occured, " + arg + " will skip");
                continue;
            }
            String presidentTitleofSpeach = "Eröffnungsrede";
            String presidentName = search.searchPresidentName(extractedString);
            String presidentAffiliation = search.searchPresidentAffiliation(extractedString.strip());
            LocalDate presidentSpeachDate = extractedXML.getDate();
            String presidentSpeachTextSplit = presidentAffiliation + presidentName + ":\\n+";
            String presidentSpeachText = search.searchPresidentText(extractedString, presidentSpeachTextSplit);

            Speech speech = new Speech(presidentTitleofSpeach, presidentName, presidentAffiliation, presidentSpeachDate, presidentSpeachText);

            TitlePersonMap map = search.getMap(extractedString);
            map = map.clearEmptyEntries();
            List<Speech> speechList = search.addToListFromMap(map, search.searchPresidentPostText(extractedString, presidentSpeachTextSplit), extractedXML.getDate());
            speechList.add(speech);
            speechList = clearList(speechList);
            speechCount += speechList.size();
            JSONFileWriter.write(speechList, arg);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                LOG.error("Scan...");
            }
            fileCount++;
        }
        System.out.println("Finished, extracted " + speechCount + " Speeches in " + fileCount + " Files");
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

    private static List<Speech> clearList(List<Speech> speechList) {
        List<Speech> removeList = new ArrayList<>();
        for (Speech speech : speechList) {
            if (speech.getText() == null) removeList.add(speech);
            if (speech.getText().isBlank()) removeList.add(speech);
        }
        speechList.removeAll(removeList);
        speechList = removeDuplicates(speechList);
        return speechList;
    }

    private static List<Speech> removeDuplicates(List<Speech> speechList) {
        List<Speech> removeList = new ArrayList<>();
        for (int i = 0; i < speechList.size(); i++) {
            Speech speech = speechList.get(i);
            for (int j = 0; j < speechList.size(); j++) {
                Speech compare = speechList.get(j);
                if (!removeList.contains(compare) && !removeList.contains(speech)) {
                    if (speech.getId() != compare.getId()) {
                        if (speech.getSpeaker().equalsIgnoreCase(compare.getSpeaker())) {
                            if (speech.getText().equalsIgnoreCase(compare.getText())) {
//                                LOG.info("duplicate found: " + speech.getSpeaker());
                                removeList.add(compare);
                            }
                        }
                    }
                }
            }
        }
        speechList.removeAll(removeList);
        return speechList;
    }

}
