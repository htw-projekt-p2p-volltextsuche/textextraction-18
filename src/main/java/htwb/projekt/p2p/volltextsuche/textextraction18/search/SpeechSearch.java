package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SpeechSearch {
    private static final Logger LOG = Logger.getLogger(SpeechSearch.class.getName());

    private TitlePersonMap map;

    public SpeechSearch() {
        map = new TitlePersonMap();
    }

    public TitlePersonMap getMap(String text) {
        String[] agendaList = getAgendaItems(text);
        List<String> titleList = getAgenda(agendaList);
        titleList.forEach(x -> map.putTitle(x));
        map = createMap(agendaList);
        return map;
    }

    public String[] getAgendaItems(String text) {
        text = splitProtocol(text)[0];
        String[] arr = RegexPattern.TOC_NAMES.pattern.split(text);
        arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
//        arr = concatAgendaItems(arr);
        return arr;
    }

    public List<String> getAgenda(String[] toc) {
        List<String> list = new ArrayList<>();
        for (String s : toc) {
            if (s.contains("ordnung")) {
                list.add(s);
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
                                    ArrayList<Person> personList = new ArrayList<>();
                                    for (int k = i + 1; k < j; k++) {
                                        if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
                                            if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
                                                Person p = createPersonfromString(toc[k]);
                                                personList.add(p);
                                            }
                                        }
                                    }
                                    String title = addOrderString(toc[i], i);
                                    map.addToMap(title, personList);
                                    i = j - 1;
                                    j = toc.length - 1;
                                }
                            }
                        }
                        // pattern "Fragestunde:"
//					} else if (RegexPattern.QUESTION_TIME.pattern.matcher(toc[i]).find()) {

                    } else if (RegexPattern.AGENDA.pattern.matcher(toc[i]).find()) {
                        if (RegexPattern.PERSON.pattern.matcher(toc[i + 1]).find()) {
                            for (j = i + 1; j < toc.length; j++) {
                                if (RegexPattern.TITLE.pattern.matcher(toc[j]).find()) {
                                    ArrayList<Person> personList = new ArrayList<>();
                                    String personString = RegexPattern.AGENDA.pattern.split(toc[i])[1];
                                    Person person = createPersonfromString(personString);
                                    personList.add(person);
                                    for (int k = i + 1; k < j; k++) {
                                        if (!RegexPattern.TITLE.pattern.matcher(toc[k]).find()) {
                                            if (RegexPattern.PERSON.pattern.matcher(toc[k]).find()) {
                                                Person p = createPersonfromString(toc[k]);
                                                personList.add(p);
                                            }
                                        }
                                    }
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
        return map;
    }

    public Person createPersonfromString(String text) {
        String[] person = null;
        String affiliation = "";
        if (RegexPattern.PERSON_PARTY.pattern.matcher(text).find()) {
            person = RegexPattern.PERSON_PARTY.pattern.split(text);
            affiliation = person[1].split("(?<=(\\)))")[0];
            affiliation = affiliation.replaceAll("(.)\\n(.)", "$1$2");
            affiliation = affiliation.strip();

        } else if (RegexPattern.PERSON_AFFILIATION.pattern.matcher(text).find()) {
            person = RegexPattern.PERSON_AFFILIATION.pattern.split(text);
            affiliation = person[1].split("\\s+\\.+")[0];
            affiliation = affiliation.replaceAll("(.)\\n(.)", "$1$2");
            affiliation = affiliation.strip();
        }
        String name = person[0];
        if (Pattern.compile("I+").matcher(name).find()) {
            name = name.substring(name.lastIndexOf("I") + 2);
        }
        return new Person(name, affiliation);

    }

    public List<Speach> addToListFromMap(TitlePersonMap map, String text, LocalDate date) {
        List<Speach> speechList = new ArrayList<>();
        TreeMap<String, ArrayList<Person>> treeMap = map.getMap();
        for (Map.Entry<String, ArrayList<Person>> entry : treeMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                Person person = entry.getValue().get(i);
                Speach speach = new Speach(entry.getKey(), person.getName(), person.getAffiliation() , date, getSpeechText(person, text));
                speechList.add(speach);
            }
        }
        return speechList;
    }

    private String getSpeechText(Person p, String text) {
        if (p.getRegexFromPerson().matcher(text).find()) {
            text = p.getRegexFromPerson().split(text)[1];
            text = RegexPattern.BREAKPOINT.pattern.split(text)[0];
        }
//        LOG.log(Level.INFO, "create speech from " + p.toString());
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

    public String[] splitProtocol(String text) {
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
        String nameWithPresident = splitProtocol(text)[1].split(":")[0];
        String[] strAr = RegexPattern.PRESIDENT.pattern.split(nameWithPresident);
        String affiliation = "";
        if (strAr.length == 3) {
            affiliation = strAr[0] + strAr[1];

        } else if (strAr.length == 2) {
            affiliation = strAr[0];
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
        String afterToc = splitProtocol(text)[1];
        String presidentText = Pattern.compile(regex).split(afterToc)[1];
        String[] postText = RegexPattern.BREAKPOINT.pattern.split(presidentText);
        StringBuilder erg = new StringBuilder();
        for (int i = 1; i < postText.length; i++) {
            erg.append(postText[i]);
        }
        return erg.toString();
    }
}