package htwb.projekt.p2p.volltextsuche.textextraction18.enums;

public enum Affiliation {
	CDU("(CDU/CSU)"),
	LINKE("(DIE LINKE)"),
	SPD("(SPD)"),
	GRUENEN("(BÜNDNIS 90/(\r\n)+DIE GRÜNEN)"),
	BUNDESMINISTER("Bundesminister(in)?"),
	STATSMINISTER("Staatsminister(in)?");
	
	
	
	public final String pattern;
	
	private Affiliation(String pattern) {
		this.pattern = pattern;
	}

}
