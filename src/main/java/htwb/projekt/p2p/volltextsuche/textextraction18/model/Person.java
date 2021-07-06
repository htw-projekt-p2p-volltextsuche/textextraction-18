package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;

import java.util.regex.Pattern;

public class Person {
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
        String nameRegex = this.getName();
        String affilationRegex;
        if (RegexPattern.ISPARTY.pattern.matcher(getAffiliation()).find()) {
            affilationRegex = "\\s*\\(.+\\r*\\n*.+\\):\\n";
        } else {
            affilationRegex = "\\s*,.+\\r*\\s*.+:\\n";
        }
        return Pattern.compile(nameRegex+affilationRegex);
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
