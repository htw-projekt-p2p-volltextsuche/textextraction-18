package htwb.projekt.p2p.volltextsuche.textextraction18.enums;

import java.util.regex.Pattern;

public enum RegexPattern {
	OPENING(Pattern.compile("(?<=Uhr)(\\r|\\n|(\\n)*)(?=(Vize|Alters)?(P|p)räsident(en|in|innen)?)")),
	BREAKPOINT(Pattern.compile("(.+minister.+\\r*\\n*.+:\\n\\n)|((.+p|P)räsident.+:\\n)|(.+\\(.+\\):\\n)")),
	PRESIDENT_BREAKPOINT(Pattern.compile("(Sitzung|Sit-\\r*\\n*zung)\\r*\\n*\\s*ist eröffnet.")),
	PRESIDENT(Pattern.compile("(?<=((Vize|Alters)?(P|p)räsident(en|in|innen)?))")),
	SPEACH_BEGIN(Pattern.compile("\\):")),
	EOT(Pattern.compile("Die Sitzung ist geschlossen.")),
	TOC_NAMES(Pattern.compile("\\d\\s[A-Z]\\n*")),
	PERSON_PARTY(Pattern.compile("(?=(\\s\\())")),
	ISPARTY(Pattern.compile("(\\(.+\\/*.+\\))")),
	PERSON_AFFILIATION(Pattern.compile("(?=(\\,\\s))")),
	PERSON(Pattern.compile("(minister(\\s|in\\s))|(\\(.+(\\r?\\n+)*.+\\))")),
	TITLE(Pattern.compile("ordnung")),
	PRINTED_MATTER(Pattern.compile("\\r*\\n*.*Drucksache")),
	AGENDA(Pattern.compile("(?<=((G|g)eschäftsordnung))")),
	QUESTION_TIME(Pattern.compile("Fragestunde")),
	TWO_LINEBREAKS(Pattern.compile(System.lineSeparator()+System.lineSeparator()));
	
	public final Pattern pattern;
	
	private RegexPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
