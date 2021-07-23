package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speech;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;

/**
 * class to search all speeches in a text block
 * 
 * @author SteSad
 *
 * TODO should be optimized for better performance
 */
public class SpeechSearch {
	private static final Logger LOG = LogManager.getLogger(SpeechSearch.class);

	private TitlePersonMap map;

	public SpeechSearch() {
		map = new TitlePersonMap();
	}

	/**
	 * get a {@link TitlePersonMap} with all titles and the associated persons
	 * 
	 * @param text
	 * @return {@link TitlePersonMap}
	 */
	public TitlePersonMap getMap(String text) {
		String[] agendaList = getAgendaItems(text);
		List<String> titleList = getAgenda(agendaList);
		titleList.forEach(x -> map.putTitle(x));
		map = createMap(agendaList);
		return map;
	}

	/**
	 * get an array of entries in the table of contents
	 * @param text
	 * @return String[]
	 */
	public String[] getAgendaItems(String text) {
		Matcher m = RegexPattern.OPENING.pattern.matcher(text);
		if (m.find()) {
			text = text.substring(0, m.start());
			String[] arr = RegexPattern.TOC_NAMES.pattern.split(text);
			arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
			return arr;
		} else
			return null;
	}

	/**
	 * get all title from given string array
	 * @param toc - table of contents as string array
	 * @return List with titles
	 */
	public List<String> getAgenda(String[] toc) {
		List<String> list = new ArrayList<>();
		for (String s : toc) {
			if (s.contains("ordnung")) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * iterate over the table of contents and persist for each title all persons they speech
	 * @param toc - table of contents as string array
	 * @return {@link TitlePersonMap}
	 */
	public TitlePersonMap createMap(String[] toc) {
		int i, j;
		//iterate over the toc
		for (i = 0; i < toc.length; i++) {
			if (i + 1 < toc.length) {
				//check if a toc entry contains a title and a person
				Matcher m = RegexPattern.AGENDA.pattern.matcher(toc[i]);
				if (m.find()) {
					ArrayList<Person> personList = new ArrayList<>();
					Matcher n = RegexPattern.PERSON.pattern.matcher(toc[i]);
					if (n.find()) {
						String title = toc[i].substring(0, m.end());
						title = addOrderString(title, i);
						String personString = toc[i].substring(m.end());
						Person p = createPersonfromString(personString);
						if (p != null) {
							personList.add(p);
						}
						//add all followed persons
						for (j = i + 1; j < toc.length; j++) {
							if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
								for (int k = i + 1; k < j; k++) {
									if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
										if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
											Person person = createPersonfromString(toc[k]);
											if (person != null) {
												personList.add(person);
											}
										}
									}
								}
								//continue the loop on toc with the next title 
								map.addToMap(title, personList);
								i = j - 1;
								j = toc.length - 1;
							}
						}
					}
					//if a title does not contain a person, add all persons after the title  
				} else {
					m = RegexPattern.TITLE.pattern.matcher(toc[i]);
					if (m.find()) {
						if (RegexPattern.PERSON.pattern.matcher(toc[i + 1]).find()) {
							if (!RegexPattern.TITLE.pattern.matcher(toc[i + 1]).find()) {
								for (j = i + 1; j < toc.length; j++) {
									if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
										ArrayList<Person> personList = new ArrayList<>();
										for (int k = i + 1; k < j; k++) {
											if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
												if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
													Person p = createPersonfromString(toc[k]);
													if (p != null) {
														personList.add(p);
													}
												}
											}
										}
										//continue the loop on toc with the next title 
										String title = addOrderString(toc[i], i);
										map.addToMap(title, personList);
										i = j - 1;
										j = toc.length - 1;
									}
								}
							}
						}
					}
				}

			}
		}
		return map;
	}

	/**
	 * create a {@link Person} from a given string
	 * 
	 * @param text
	 * @return {@link Person}
	 */
	public Person createPersonfromString(String text) {
//        LOG.info(text);
		String name = "";
		String city = null;
		String affiliation = "";
		Matcher special = Pattern.compile(".?\\(.+\\)\\s+\\(").matcher(text);
		Matcher party = RegexPattern.PERSON_PARTY.pattern.matcher(text);
		Matcher member = RegexPattern.PERSON_AFFILIATION.pattern.matcher(text);
		//special deputies with their county
		if (special.find()) {
			name = text.substring(0, special.start());
			city = text.substring(special.start(), special.end() - 2).strip();
			affiliation = text.substring(special.end() - 1);
			special = Pattern.compile("\\)").matcher(affiliation);
			if (special.find()) {
				affiliation = affiliation.substring(0, special.start() + 1);
			}
			Matcher m = Pattern.compile("\\)\\s+\\.").matcher(affiliation);
			if (m.find()) {
				affiliation = affiliation.substring(0, m.start() + 1);
			}
			affiliation = affiliation.strip();
		// normal deputies
		} else if (party.find()) {
			name = text.substring(0, party.start());

			affiliation = text.substring(party.start());
			affiliation = affiliation.replaceAll("(.)\\n(.)", "$1$2");
			Matcher m = Pattern.compile("\\)\\s+\\.").matcher(affiliation);
			if (m.find()) {
				affiliation = affiliation.substring(0, m.start() + 1);
			}
			affiliation = affiliation.strip();
		// deputies with special task
		} else if (member.find()) {
			name = text.substring(0, member.start());
			affiliation = text.substring(member.start());
			affiliation = affiliation.replaceAll("(.)\\n(.)", "$1$2");
			affiliation = affiliation.substring(2);
			Matcher parl = Pattern.compile("Parl\\s*\\.\\s").matcher(affiliation);
			if (parl.find()) {
				affiliation = affiliation.substring(parl.end());
			}
			Matcher m = Pattern.compile(".?\\s.?").matcher(affiliation);
			if (m.find()) {
				affiliation = affiliation.substring(0, m.start() + 1);
			}
			affiliation = affiliation.strip();
		} else
			return null;
		Matcher matcher = Pattern.compile("I+").matcher(name);
		if (matcher.find()) {
			name = name.substring(name.lastIndexOf("I") + 2);
		}
		if (city == null) {
//            LOG.info(name + " " + affiliation);
			return new Person(name, affiliation);
		} else {
//            LOG.info(name + " " + city + " " + affiliation);
			return new Person(name, city, affiliation);
		}

	}

	/**
	 * create a {@link List} of {@link Speech}es from given {@link TitlePersonMap} with his text and date
	 * @param map
	 * @param text
	 * @param date
	 * @return {@link List}<{@link Speech}>
	 */
	public List<Speech> addToListFromMap(TitlePersonMap map, String text, LocalDate date) {
		List<Speech> speechList = new ArrayList<>();
		TreeMap<String, ArrayList<Person>> treeMap = map.getMap();
		for (Map.Entry<String, ArrayList<Person>> entry : treeMap.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				Person person = entry.getValue().get(i);
				Matcher matcher = Pattern.compile("\\)").matcher(person.getName());
				if (!matcher.find()) {
					String speechText = getSpeechText(person, text);
					Speech speech = new Speech(entry.getKey(), person.getName(), person.getAffiliation(), date,
							speechText);
					speechList.add(speech);
				}
			}
		}
		return speechList;
	}

	/**
	 * persists a string, 
	 * which begins with regex from given {@link Person} and 
	 * ends with the next speech begin find by the universal regex
	 * 
	 * @param p
	 * @param text
	 * @return String speech
	 */
	private String getSpeechText(Person p, String text) {
		Matcher start = p.getRegexFromPerson().matcher(text);
		if (start.find()) {
			String newText = p.getRegexFromPerson().split(text)[1];
			int begin = 0;
			int end;
			Matcher last = RegexPattern.BREAKINGPOINT.pattern.matcher(newText);
			if (last.find()) {
				end = last.start();
			} else
				end = begin + 10;
			text = newText.substring(begin, end);
		} else
			text = "";
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

	/**
	 * splits the protocol in two parts:
	 * table of contents
	 * text block with speeches
	 * @param text
	 * @return
	 */
	public String[] splitProtocol(String text) {
		Matcher matcher = RegexPattern.OPENING.pattern.matcher(text);
		String[] parts = RegexPattern.OPENING.pattern.split(text);
		if (parts.length < 2) {
			throw new IllegalArgumentException("Can not split with Pattern: " + RegexPattern.OPENING.pattern);
		}
		return parts;
	}

	public String searchPresidentName(String text) {
		String nameWithPresident = splitProtocol(text)[1].split(":")[0];
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
		String affiliation = "";
		Matcher startRegex = RegexPattern.OPENING.pattern.matcher(text);
		if (startRegex.find()) {
			String subText = text.substring(startRegex.end());
			Matcher endRegex = RegexPattern.PRESIDENT.pattern.matcher(subText);
			if (endRegex.find()) {
				affiliation = subText.substring(0, endRegex.end());
			}
		}
		return affiliation;
	}

	public String searchPresidentText(String text, String regex) {
		regex = regex.replaceAll("\\s\\s", " ");
		String afterToc = splitProtocol(text)[1];
		String presidentText = Pattern.compile(regex).split(afterToc)[1];
		return RegexPattern.BREAKPOINT.pattern.split(presidentText)[0];
	}

	public String searchPresidentPostText(String text, String regex) {
		regex = regex.replaceAll("\\s\\s", " ");
		Matcher m = Pattern.compile(regex).matcher(text);
		String postText = "";
		if (m.find()) {
			postText = text.substring(m.end());
		}
		return postText;
	}
}
