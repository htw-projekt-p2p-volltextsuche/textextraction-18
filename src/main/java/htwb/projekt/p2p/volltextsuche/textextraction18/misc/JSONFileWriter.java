package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import htwb.projekt.p2p.volltextsuche.textextraction18.Extractor;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speech;

/**
 * write json files
 * 
 * @author SteSad
 *
 */
public class JSONFileWriter {
	private static final Logger LOG = LogManager.getLogger(Extractor.class);
	private static FileWriter writer;

	/**
	 * writes a given {@link List}<{@link Speech}> in a json file, that has the
	 * given name
	 * 
	 * @param speechList
	 * @param name
	 */
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

	/**
	 * reduce the full classified file name in a simple file name
	 * 
	 * @param fileName
	 * @return String - file name
	 */
	private static String fileWithDirectoryToFileName(String fileName) {
		String subString = "";
		if (fileName.contains("/")) {
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		Matcher m = Pattern.compile("\\.xml").matcher(fileName);
		if (m.find()) {
			subString = fileName.substring(0, m.start());
		}

		return subString + ".json";
	}

	/**
	 * create json object from given {@link List}<{@link Speech}> 
	 * @param speechList
	 * @return {@link ArrayNode}
	 */
	private static ArrayNode createArrayNodeFromList(List<Speech> speechList) {
		JsonMapper mapper = new JsonMapper();
		return mapper.convertValue(speechList, ArrayNode.class);
	}
}
