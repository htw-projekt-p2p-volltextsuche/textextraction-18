package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author stefa
 *
 */
public class TitlePersonMap {
	private HashMap<String, ArrayList<Person>> map;
	private ArrayList<Person> personList;

	public TitlePersonMap() {
		this.map = new HashMap<String, ArrayList<Person>>();
		this.personList = new ArrayList<Person>();
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

	public HashMap<String, ArrayList<Person>> getMap() {
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

}
