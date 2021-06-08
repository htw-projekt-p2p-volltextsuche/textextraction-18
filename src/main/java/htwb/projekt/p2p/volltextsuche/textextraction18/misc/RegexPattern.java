package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

public enum RegexPattern {
	OPENING("(?<=Uhr)(\\r|\\n|(\\n)*)(?=(Vize|Alters)?(P|p)r\u00e4sident(en|in|innen)?)"),
	BREAKPOINT("\\)\n\bPr\u00e4sident|\bVizepr\u00e4sident"),
	PRESIDENT_BREAKPOINT("(?=\\(Beifall bei)"),
	PRESIDENT("(?<=((Vize|Alters)?(P|p)r\u00e4sident(en|in|innen)?))"),
	SPEACH_BEGIN("\\):"),
	EOT("Die Sitzung ist geschlossen."),
	TOC_NAMES("\\d\\s[A-Z]\\n+"),
	TWO_LINEBREAKS(System.lineSeparator()+System.lineSeparator());
	public final String pattern;
	
	private RegexPattern(String pattern) {
		this.pattern = pattern;
	}
}
