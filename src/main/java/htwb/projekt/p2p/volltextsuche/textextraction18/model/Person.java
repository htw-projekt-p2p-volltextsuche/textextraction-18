package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;

import java.util.regex.Pattern;

public class Person {
    private String name;
    private String city;
    private String affiliation;

    public Person() {
    }

    public Person(String name, String affiliation) {
        this.name = prettyUp(name);
        this.affiliation = prettyUp(affiliation);
    }

    public Person(String name, String city, String affiliation) {
        this.name = prettyUp(name);
        this.city = prettyUp(city);
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Pattern getRegexFromPerson() {
        String nameRegex = this.getName();
        String affilationRegex;
        if (RegexPattern.ISPARTY.pattern.matcher(getAffiliation()).find()) {
            if (this.getCity() != null)
                affilationRegex = "\\s+\\(.+\\)\\s+\\(.+\\r*\\n*.+\\):\\n";
            else affilationRegex = "\\s+\\(.+\\r*\\n*.+\\):\\n";
        } else {
            affilationRegex = "\\s*,.+\\r*\\s*.+:\\n";
        }
        return Pattern.compile(nameRegex + affilationRegex);
    }

    @Override
    public String toString() {
        return name + " " + city == null ? "" : city + " " + affiliation;
    }

    private String prettyUp(String text) {
        text = text.replaceAll(RegexPattern.TWO_LINEBREAKS.pattern.pattern(), " ");
        text = text.replaceAll("(.)-\n(.)", "$1$2");
        text = text.replaceAll("(.)\n(.)", "$1 $2");
        return text;
    }


}
