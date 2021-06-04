package htwb.projekt.p2p.volltextsuche.textextraction18.xml;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import htwb.projekt.p2p.volltextsuche.textextraction18.model.XMLExtract;

public class XMLParser {
	private static final Logger LOG = Logger.getLogger(XMLParser.class.getName());
	
	
	private static Document stringFilenameToDoc(String filename) {
		try {

			// creating a constructor of file class and parsing an XML file
			File file = new File(filename);
			// an instance of factory that gives a document builder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			return doc;
		} catch (Exception e) {
			return null;
		}

	}

	public static XMLExtract readXML(String filename) {
		Document doc = stringFilenameToDoc(filename);
		String period = null;
		LocalDate date = null;
		String protocoll = null;
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
					protocoll = node.getTextContent();
				}
			}
		}
		XMLExtract xmlExtract = new XMLExtract(period, date, protocoll);
		return xmlExtract;
	}

}
