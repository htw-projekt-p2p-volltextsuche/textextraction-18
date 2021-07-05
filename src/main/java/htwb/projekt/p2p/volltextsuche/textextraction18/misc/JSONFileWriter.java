package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONFileWriter {
    private static final Logger LOG = Logger.getLogger(JSONFileWriter.class.getName());
    private static FileWriter writer;

    public static void write(List<Speach> speachList, String name) {
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
        ArrayNode node = createArrayNodeFromList(speachList);
        //TODO split ArrayNode as String with("(?=(},")))
        try {
            writer = new FileWriter(jsonFile);
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("wrote File: " + name);
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

    private static ArrayNode createArrayNodeFromList(List<Speach> speachList) {
        JsonMapper mapper = new JsonMapper();
        return mapper.convertValue(speachList, ArrayNode.class);
    }
}
