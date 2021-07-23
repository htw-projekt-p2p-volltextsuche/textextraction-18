package htwb.projekt.p2p.volltextsuche.textextraction18.enums;

import java.util.regex.Pattern;
/**
 * compiled regular expressions represented as enum
 * 
 * @author SteSad
 *
 */
public enum RegexPattern {
    /**
     * opening of the speech from the interim president
     */
    OPENING(Pattern.compile("(Uhr(\\r|\\n|(\\n)*)(?=(Vize|Alters)?(P|p)räsident(en|in|innen)?))")),
    
    /**
     * first speech from the interim president
     */
    BREAKPOINT(Pattern.compile("(?=((.+minister.+\\r*\\n*.+:\\n\\n)|((.+p|P)räsident.+:\\n)|(.+\\(.+\\):\\n)))")),
    
    /**
     * universal regex for each speech begin   
     */
    BREAKINGPOINT(Pattern.compile("(.+,\\s.+minister.+\\r*\\n*.+:\\n)|((.+p|P)räsident.+:\\n)|(.+\\(.+\\):\\n)|(.+\\(.+/.+-\\r*\\n*.+\\):\\n)|(.+\\(.+\\r*\\n*.+\\):\\n)|(.+,.+kanzler.+:\\n)")),
    
    /**
     * introduction of the interim president speech
     */
    PRESIDENT_BREAKPOINT(Pattern.compile("(Sitzung|Sit-\\r*\\n*zung)\\r*\\n*\\s*ist eröffnet.")),
    
    /**
     *  affiliation of the interim president
     */
    PRESIDENT(Pattern.compile("((Vize|Alters)?(P|p)räsident(en|in|innen)?)")),
    
    /**
     * regex to split each entry in the table of content
     */
    TOC_NAMES(Pattern.compile("\\d\\s[A-Z]\\n+")),
    
    /**
     * affiliation in party 
     */
    PERSON_PARTY(Pattern.compile("(\\s\\()")),
    
    /**
     *  regex to check if a affiliation is a party
     */
    ISPARTY(Pattern.compile("(\\(.+/*.+\\))")),
    
    /**
     * affiliation in task
     */
    PERSON_AFFILIATION(Pattern.compile("(,\\s)")),
    
    /**
     *  regex to check the task in affiliation
     */
    PERSON(Pattern.compile("(minister(\\s|in\\s))|(.+\\(.+(\\r?\\n+)*.+\\)|(sekretär)|(kanzler))")),
    
    /**
     * to find a title of speeches 
     */
    TITLE(Pattern.compile("ordnung")),
    
    /**
     * special title in the table of contents  
     */
    AGENDA(Pattern.compile("(?<=((G|g)eschäftsordnung))")),
    
    /**
     * pattern to pretty up texts 
     */
    TWO_LINEBREAKS(Pattern.compile(System.lineSeparator() + System.lineSeparator()));

    public final Pattern pattern;

    RegexPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
