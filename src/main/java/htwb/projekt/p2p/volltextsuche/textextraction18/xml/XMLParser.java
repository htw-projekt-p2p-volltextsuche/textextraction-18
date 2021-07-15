package htwb.projekt.p2p.volltextsuche.textextraction18.xml;

import htwb.projekt.p2p.volltextsuche.textextraction18.Extractor;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XMLParser {
	private static final Logger LOG = LogManager.getLogger(XMLParser.class);

	private static Document stringFilenameToDoc(String filename) {
		try {

			// creating a constructor of file class and parsing an XML file
			File file = new File(filename);
			// an instance of factory that gives a document builder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(file);
		} catch (Exception e) {
			return null;
		}

	}

	public static XMLExtract readXML(String filename) {
		Document doc = stringFilenameToDoc(filename);
		String period = null;
		LocalDate date = null;
		String protocol = null;
		if (doc != null) {
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getElementsByTagName("*");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				if (node.getNodeName().equalsIgnoreCase(TagNames.WAHLPERIODE.name())) {
					period = node.getTextContent();
				}
				if (node.getNodeName().equalsIgnoreCase(TagNames.DATUM.name())) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
					date = LocalDate.parse(node.getTextContent(), formatter);
				}
				if (node.getNodeName().equalsIgnoreCase(TagNames.TEXT.name())) {
					protocol = node.getTextContent();
				}
			}
		}
		return new XMLExtract(period, date, protocol);
	}

}
