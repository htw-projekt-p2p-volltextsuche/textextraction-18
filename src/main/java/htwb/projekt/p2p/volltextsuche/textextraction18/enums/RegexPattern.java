package htwb.projekt.p2p.volltextsuche.textextraction18.enums;

import java.util.regex.Pattern;

public enum RegexPattern {
	OPENING(Pattern.compile("(?<=Uhr)(\\r|\\n|(\\n)*)(?=(Vize|Alters)?(P|p)räsident(en|in|innen)?)")),
	BREAKPOINT(Pattern.compile("\\)\n\bPräsident|\bVizepräsident")),
	PRESIDENT_BREAKPOINT(Pattern.compile("(?=\\(Beifall bei)")),
	PRESIDENT(Pattern.compile("(?<=((Vize|Alters)?(P|p)räsident(en|in|innen)?))")),
	SPEACH_BEGIN(Pattern.compile("\\):")),
	EOT(Pattern.compile("Die Sitzung ist geschlossen.")),
	TOC_NAMES(Pattern.compile("\\d\\s[A-Z]\\n*")),
	PERSON_PARTY(Pattern.compile("(?=(\\s\\())")),
	PERSON_AFFILIATION(Pattern.compile("(?=(\\,\\s))")),
	PERSON(Pattern.compile("(minister)|(\\(.+(\\r?\\n+)*.+\\))")),
	TITLE(Pattern.compile("ordnung")),
	DRUCKSACHE(Pattern.compile("\\r*\\n*.*Drucksache")),
	TWO_LINEBREAKS(Pattern.compile(System.lineSeparator()+System.lineSeparator()));
	
	public final Pattern pattern;
	
	private RegexPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
