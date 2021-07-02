package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;

public class SpeachSearch {
	private static final Logger LOG = Logger.getLogger(SpeachSearch.class.getName());

	private TitlePersonMap map;

	public SpeachSearch() {
		map = new TitlePersonMap();
	}

	public TitlePersonMap getMap(String text) {
		String[] agendaList = getAgendaItems(text);
		agendaList = concatAgendaItems(agendaList);
		List<String> titleList = getAgenda(agendaList);
		titleList.forEach(x -> map.putTitle(x));
		map = createMap(agendaList);
		return map;
	}

	public String[] getAgendaItems(String text) {
		text = splitProtocoll(text)[0];
		String[] arr = RegexPattern.TOC_NAMES.pattern.split(text);
		arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
		arr = concatAgendaItems(arr);
		return arr;
	}

	public String[] concatAgendaItems(String[] toc) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < toc.length; i++) {
			if (toc[i].contains("in Verbindung mit")) {
				if (i - 1 > 0) {
					if (list.contains(toc[i - 1]))
						list.remove(toc[i - 1]);
					list.add(i - 1, toc[i - 1] + toc[i]);
				}
			} else
				list.add(toc[i]);

		}
		return list.toArray(new String[list.size()]);
	}

	public List<String> getAgenda(String[] toc) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < toc.length; i++) {
			if (toc[i].contains("ordnung")) {
				list.add(toc[i]);
			}
		}
		return list;
	}

	public TitlePersonMap createMap(String[] toc) {
		int i, j;
		for (i = 0; i < toc.length; i++) {
			if (i + 1 < toc.length) {
				// find title in table of content
				if (RegexPattern.TITLE.pattern.matcher(toc[i]).find()) {
					// find next entry after title that is a person entry
					if (RegexPattern.PERSON.pattern.matcher(toc[i + 1]).find()) {
						if (!RegexPattern.TITLE.pattern.matcher(toc[i + 1]).find()) {
							// iterate from first entry that is a name until find new title
							for (j = i + 1; j < toc.length; j++) {
								if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
									ArrayList<Person> personlist = new ArrayList<Person>();
									for (int k = i + 1; k < j; k++) {
										if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
											if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
												Person p = createPersonfromString(toc[k]);
												personlist.add(p);
											}
										}
									}
									String title = addOrderString(toc[i], i);
									map.addToMap(title, personlist);
									i = j - 1;
									j = toc.length - 1;
									continue;
								}
							}
						}
						// pattern "Fragestunde:"
//					} else if (RegexPattern.QUESTION_TIME.pattern.matcher(toc[i]).find()) {

					} else if (RegexPattern.AGENDA.pattern.matcher(toc[i]).find()) {
						if (RegexPattern.PERSON.pattern.matcher(toc[i + 1]).find()) {
							for (j = i + 1; j < toc.length; j++) {
								if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
									ArrayList<Person> personlist = new ArrayList<Person>();
									String personString = RegexPattern.AGENDA.pattern.split(toc[i])[1];
									Person person = createPersonfromString(personString);
									personlist.add(person);
									for (int k = i + 1; k < j; k++) {
										if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
											if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
												Person p = createPersonfromString(toc[k]);
												personlist.add(p);
											}
										}
									}
									String title = addOrderString(toc[i], i);
									map.addToMap(title, personlist);
									i = j - 1;
									j = toc.length - 1;
									continue;
								}
							}
						}

					}
				}
			}
		}
		return map;
	}

	//FIXME NO entries for Question Time
	public Person createPersonfromString(String text) {
		String[] person = null;
		if (RegexPattern.PERSON_PARTY.pattern.matcher(text).find()) {
			person = RegexPattern.PERSON_PARTY.pattern.split(text);
			String affiliation = person[1].split("(?<=(\\)))")[0];
			affiliation = affiliation.replaceAll("(.)\n(.)", "$1$2");
			affiliation = affiliation.strip();
			return new Person(person[0], affiliation);

		} else if (RegexPattern.PERSON_AFFILIATION.pattern.matcher(text).find()) {
			person = RegexPattern.PERSON_AFFILIATION.pattern.split(text);
			String affiliation = person[1].split("\\s+\\.+")[0];
			affiliation = affiliation.replaceAll("(.)\n(.)", "$1$2");
			affiliation = affiliation.strip();
			return new Person(person[0], affiliation);
		} else
			return null;

	}

	public List<Speach> addToListFromMap(TitlePersonMap map, String text, LocalDate date){
		List<Speach> speachList = new ArrayList<>();
		TreeMap<String, ArrayList<Person>> treeMap = map.getMap();
		for (Map.Entry<String, ArrayList<Person>> entry : treeMap.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				Person person = entry.getValue().get(i);
				Speach speach = new Speach();
				speach.setDate(date);
				speach.setTitle(entry.getKey());
				speach.setSpeaker(person.getName());
				speach.setAffiliation(person.getAffiliation());
				speach.setText(getSpeachText(person, text));
				speachList.add(speach);
			}
		}
		return speachList;
	}

	private String getSpeachText(Person p, String text){
		LOG.log(Level.INFO, p.toString());
		text = p.getRegexFromPerson().split(text)[1];
		text = RegexPattern.BREAKPOINT.pattern.split(text)[0];
		return text;
	}


	private String addOrderString(String title, int index) {
		if (index < 10) {
			title = "000" + index + " " + title;
		} else if (index >= 10 && index < 100) {
			title = "00" + index + " " + title;
		} else if (index >= 100 && index < 1000) {
			title = "0" + index + " " + title;
		}
		return title;
	}

	public String[] splitProtocoll(String text) {
		String[] parts = RegexPattern.OPENING.pattern.split(text);
		if (parts.length < 2) {
			throw new IllegalArgumentException("Can not split with Pattern: " + RegexPattern.OPENING.pattern);
		}
		return parts;
	}

	public String searchPresidentName(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		String[] strAr = RegexPattern.PRESIDENT.pattern.split(nameWithPresident);
		String name = "";
		if (strAr.length == 3) {
			name = strAr[2];

		} else if (strAr.length == 2) {
			name = strAr[1];
		}
		return name;
	}

	public String searchPresidentAffiliation(String text) {
		String nameWithPresident = splitProtocoll(text)[1].split(":")[0];
		String[] strAr = RegexPattern.PRESIDENT.pattern.split(nameWithPresident);
		String affiliation = "";
		if (strAr.length == 3) {
			affiliation = strAr[0] + strAr[1];

		} else if (strAr.length == 2) {
			affiliation = strAr[0];
		}
		return affiliation;
	}

	public String searchPresidentText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		return RegexPattern.PRESIDENT_BREAKPOINT.pattern.split(presidentText)[0];
	}

	public String searchPresidentPostText(String text) {
		String presidentText = splitProtocoll(text)[1].split("Die Sitzung ist er\u00f6ffnet.")[1];
		String[] postText = RegexPattern.PRESIDENT_BREAKPOINT.pattern.split(presidentText);
		String erg = "";
		for (int i = 1; i < postText.length; i++) {
			erg += postText[i];
		}
		return erg;
	}

	public List<Speach> createSpeachListFromMap(TitlePersonMap titlePersonMap, String text) {
		List<Speach> speachList = null;
		TreeMap<String, ArrayList<Person>> map = titlePersonMap.getMap();

		return speachList;
	}
}
