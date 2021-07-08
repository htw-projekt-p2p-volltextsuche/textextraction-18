package htwb.projekt.p2p.volltextsuche.textextraction18.search;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Person;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speech;
import htwb.projekt.p2p.volltextsuche.textextraction18.model.TitlePersonMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechSearch {
    private static final Logger LOG = LogManager.getLogger(SpeechSearch.class);

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
        Matcher m = RegexPattern.OPENING.pattern.matcher(text);
        if (m.find()) {
            text = text.substring(0, m.start());
            String[] arr = RegexPattern.TOC_NAMES.pattern.split(text);
            arr = Arrays.copyOfRange(arr, 0, arr.length - 1);
            return arr;
        } else return null;
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
                                map.addToMap(title, personList);
                                i = j - 1;
                                j = toc.length - 1;
                            }
                        }
                    }
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


    public Person createPersonfromString(String text) {
//        LOG.info(text);
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
        } else return null;
        String name = (person[0] == null) ? "NULL" : person[0];
        Matcher matcher = Pattern.compile("I+").matcher(name);
        if (matcher.find()) {
            name = name.substring(name.lastIndexOf("I") + 2);
        }
        return new Person(name, affiliation);

    }

    public List<Speech> addToListFromMap(TitlePersonMap map, String text, LocalDate date) {
        List<Speech> speechList = new ArrayList<>();
        TreeMap<String, ArrayList<Person>> treeMap = map.getMap();
        for (Map.Entry<String, ArrayList<Person>> entry : treeMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                Person person = entry.getValue().get(i);
                Matcher matcher = Pattern.compile("\\)").matcher(person.getName());
                if (!matcher.find()) {
                    String speechText = getSpeechText(person, text);
                    Speech speech = new Speech(entry.getKey(), person.getName(), person.getAffiliation(), date, speechText);
                    speechList.add(speech);
                }
            }
        }
        return speechList;
    }

    private String getSpeechText(Person p, String text) {
        Matcher start = p.getRegexFromPerson().matcher(text);
        if (start.find()) {
            String newText = p.getRegexFromPerson().split(text)[1];
            int begin = 0;
            int end;
            Matcher last = RegexPattern.BREAKINGPOINT.pattern.matcher(newText);
            if (last.find()) {
                end = last.start();
            } else end = begin + 10;
            text = newText.substring(begin, end);
        } else text = "";
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
