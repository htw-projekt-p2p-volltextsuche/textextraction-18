package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;
import htwb.projekt.p2p.volltextsuche.textextraction18.search.SpeachSearch;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Person {
    private static final Logger LOG = Logger.getLogger(Person.class.getName());
    private String name;
    private String affiliation;

    public Person() {
    }

    public Person(String name, String affiliation) {
        this.name = prettyUp(name);
        this.affiliation = prettyUp(affiliation);
    }

    public String getName() {
        return prettyUp(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = prettyUp(affiliation);
    }

    public Pattern getRegexFromPerson() {
        String name = getName();
        if (RegexPattern.ISPARTY.pattern.matcher(getAffiliation()).find()) {
            String party = getAffiliation();
            if (Pattern.compile("\\/").matcher(party).find()){
                party = getAffiliation().replaceAll("\\/\\s","/");
                party = party.replaceAll("\\(.\\/.\\)", "$1/$2");
            }
            String regex = name + "\\s*\\(.+\\r*\\n*.+\\):";
//            LOG.log(Level.INFO, regex);
            return Pattern.compile(regex);
        } else {
            String regex = name + "\\s*,.+\\r*\\s*.+:";
//            LOG.log(Level.INFO, regex);
            return Pattern.compile(regex);
        }
    }

    @Override
    public String toString() {
        return name + " " + affiliation;
    }

    private String prettyUp(String text) {
        text = text.replaceAll(RegexPattern.TWO_LINEBREAKS.pattern.pattern(), " ");
        text = text.replaceAll("(.)-\n(.)", "$1$2");
        text = text.replaceAll("(.)\n(.)", "$1 $2");
        return text;
    }


}
