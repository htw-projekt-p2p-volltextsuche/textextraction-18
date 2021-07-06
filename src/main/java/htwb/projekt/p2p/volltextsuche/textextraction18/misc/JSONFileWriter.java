package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import htwb.projekt.p2p.volltextsuche.textextraction18.Extractor;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speech;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONFileWriter {
    private static final Logger LOG = LogManager.getLogger(Extractor.class);
    private static FileWriter writer;

    public static void write(List<Speech> speechList, String name) {
        JsonMapper mapper = new JsonMapper();
        name = fileWithDirectoryToFileName(name);
        File jsonFile = new File(name);
        if (!jsonFile.exists() && !jsonFile.isDirectory()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            jsonFile.delete();
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayNode node = createArrayNodeFromList(speechList);
        try {
            writer = new FileWriter(jsonFile);
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                LOG.debug("wrote File: " + name);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String fileWithDirectoryToFileName(String fileName) {
        String subString = "";
        if (fileName.contains("/")) {
            subString = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        subString = subString.substring(0, subString.indexOf("."));

        return subString + ".json";
    }

    private static ArrayNode createArrayNodeFromList(List<Speech> speechList) {
        JsonMapper mapper = new JsonMapper();
        return mapper.convertValue(speechList, ArrayNode.class);
    }
}
