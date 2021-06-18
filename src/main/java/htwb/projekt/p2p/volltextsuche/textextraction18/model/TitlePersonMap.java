package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;

/**
 * @author stefa
 *
 */
public class TitlePersonMap {
	private TreeMap<String, ArrayList<Person>> map;
	private ArrayList<Person> personList;

	public TitlePersonMap() {
		this.map = new TreeMap<String, ArrayList<Person>>();
		this.personList = new ArrayList<Person>();
	}
	
	public TitlePersonMap(TreeMap<String, ArrayList<Person>> map) {
		this.map = map;
	}

	public void putTitle(String title) {
		map.put(title, null);
	}

	public void addPerson(Person p) {
		personList.add(p);
	}

	public void addToMap(String title, ArrayList<Person> pList) {
		if (map.containsKey(title)) {
			map.replace(title, pList);
		} else {
			map.put(title, pList);
		}
	}

	public TreeMap<String, ArrayList<Person>> getMap() {
		return map;
	}

	public String getMapTitle() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, ArrayList<Person>> entry : map.entrySet()) {
			sb.append("--------------------------------------------");
			sb.append(entry.getKey()).append(System.lineSeparator());
			sb.append("--------------------------------------------");
		}
		return sb.toString();
	}
	
	public TitlePersonMap clearEmptyEntries() {
		TitlePersonMap outputMap = new TitlePersonMap();
		TreeMap<String, ArrayList<Person>> inputMap = this.getMap();
		for (Entry<String, ArrayList<Person>> entry : inputMap.entrySet()) {
			if(entry.getValue() != null) {
					if(!entry.getValue().isEmpty()) {
						outputMap.addToMap(entry.getKey(), entry.getValue());
				}
			}
		}
		return outputMap;
	}
	
	public TitlePersonMap prettyUpEntries() {
		TitlePersonMap outputMap = new TitlePersonMap();
		TreeMap<String, ArrayList<Person>> inputMap = this.getMap();
		for (Entry<String, ArrayList<Person>> entry : inputMap.entrySet()) {
			String title = entry.getKey();
			title = prettyUpTitle(title);
			title = prettyUpText(title);
			ArrayList<Person> personList = new ArrayList<Person>();
			
			
			
			
			outputMap.addToMap(title, entry.getValue());			
		}
		return outputMap;
	}
	
	public int getSize() {
		return this.getMap().size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, ArrayList<Person>> entry : map.entrySet()) {
			sb.append(System.lineSeparator()).append("###").append(System.lineSeparator());
			sb.append("title: ").append(entry.getKey()).append(System.lineSeparator());
			if (entry.getValue() != null) {
				sb.append("nameList: ").append(entry.getValue());
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	private String prettyUpText(String text) {
		text = text.replaceAll(RegexPattern.TWO_LINEBREAKS.pattern.pattern(), " ");
		text = text.replaceAll("(.)-\n(.)", "$1$2");
		text = text.replaceAll("(.)\n(.)", "$1 $2");
		return text;
	}
	
	private String prettyUpTitle(String title) {
		if(RegexPattern.DRUCKSACHE.pattern.matcher(title).find()) {
			title = RegexPattern.DRUCKSACHE.pattern.split(title)[0];
		}		
		return title;
	}
}
