package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class FileWirter {
	public static void write(JSONObject json, String filename) {
		File file = new File(filename + ".json");
		FileWriter writer = null;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer = new FileWriter(file);
			writer.append(jsonAsString(json));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static String jsonAsString(JSONObject object) {
		return object.toString(6);
	}

}
