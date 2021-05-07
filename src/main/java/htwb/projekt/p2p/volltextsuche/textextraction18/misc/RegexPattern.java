package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

public enum RegexPattern {
//	BEGIN("\\d{4}\\s[\r\n]"); <- year followed by newline
	OPENING("(?<=Uhr)(\\r|\\n)(?=(Pr\u00e4sident|Vizepr\u00e4sident))"),
	BREAKPOINT("\\)[\r\n]\bPr\u00e4sident|\bVizepr\u00e4sident");
	
	public final String pattern;
	
	private RegexPattern(String pattern) {
		this.pattern = pattern;
	}
}
