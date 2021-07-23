package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * model class to temporary persists all persons of a title
 * 
 * @author SteSad
 *
 */
public class TitlePersonMap {
	private final TreeMap<String, ArrayList<Person>> map;
	private ArrayList<Person> personList;

	public TitlePersonMap() {
		this.map = new TreeMap<>();
		this.personList = new ArrayList<>();
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
	
	/**
	 * remove all entered title, if they have no persons
	 * @return
	 */
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
}
