package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONFileWriter {
    private static final Logger LOG = Logger.getLogger(JSONFileWriter.class.getName());
    private static FileWriter writer;
    private static JsonMapper mapper;

    public static void write(ArrayNode node, String name) {
        mapper = new JsonMapper();
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
        try {
            writer = new FileWriter(jsonFile);
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                LOG.log(Level.INFO, "writed File: " + name);
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
}
