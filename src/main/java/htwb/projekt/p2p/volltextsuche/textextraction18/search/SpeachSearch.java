package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
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

	// FIXME public for testing
	public String[] getAgendaItems(String text) {
		text = splitProtocoll(text)[0];
		String[] arr = RegexPattern.TOC_NAMES.pattern.split(text);
		arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
		arr = concatAgendaItems(arr);
		return arr;
	}

	private String[] concatAgendaItems(String[] toc) {
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

	private List<String> getAgenda(String[] toc) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < toc.length; i++) {
			if (toc[i].contains("ordnung")) {
				list.add(toc[i]);
			}
		}
		return list;
	}

	// FIXME
	public TitlePersonMap createMap(String[] toc) {
		int i, j;
		for (i = 0; i < toc.length; i++) {
			if (i + 1 < toc.length) {
				if (RegexPattern.TITLE.pattern.matcher(toc[i]).find()) {
//					LOG.log(Level.SEVERE, toc[i]);
					if (!RegexPattern.TITLE.pattern.matcher(toc[i + 1]).find()) {
						if (RegexPattern.PERSON.pattern.matcher(toc[i + 1]).find()) {
//						LOG.log(Level.SEVERE, toc[i + 1]);
							for (j = i + 1; j < toc.length; j++) {
								if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
//								LOG.log(Level.SEVERE, toc[j]);
//									LOG.log(Level.SEVERE, toc[i].split(":")[0] + toc[j].split(":")[0]);
									ArrayList<Person> personlist = new ArrayList<Person>();
									for (int k = i + 1; k < j; k++) {
										if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
											if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
												Person p = createPersonfromString(toc[k]);
//										LOG.log(Level.SEVERE, p.toString());
												personlist.add(p);
											}
										}
									}
									map.addToMap(toc[i], personlist);
									i = j-1;
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

	private Person createPersonfromString(String text) {
		String[] person = null;
		if (RegexPattern.PERSON_PARTY.pattern.matcher(text).find()) {
			person = RegexPattern.PERSON_PARTY.pattern.split(text);
			String affiliation = person[1].split("(?<=(\\)))")[0];
			return new Person(person[0], affiliation);

		} else if (RegexPattern.PERSON_AFFILIATION.pattern.matcher(text).find()) {
			person = RegexPattern.PERSON_AFFILIATION.pattern.split(text);
			String affiliation = person[1].split("\\s+\\.+")[0];
			affiliation = affiliation.trim();
			return new Person(person[0], affiliation);
		} else
			return null;

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

	public static String searchSpeachSnippet(String text) {
		return null;
	}

	public static String searchTitle(String text) {
		return null;
	}

	public static String searchSpeaker(String text) {
		return null;
	}

	public static String searchAffiliation(String text) {
		return null;
	}

	public static String searchDate(String text) {
		return null;
	}

	public static String searchText(String text) {
		// von SPEECH_BEGIN bis //BREAKPOINT
		return null;
	}
}
