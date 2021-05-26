package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.time.LocalDate;

public class XMLExtract {

	private String period;
	private LocalDate date;
	private String protocoll;

	public XMLExtract(String period, LocalDate date, String protocoll) {
		super();
		this.period = period;
		this.date = date;
		this.protocoll = protocoll;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getProtocoll() {
		return protocoll;
	}

	public void setProtocoll(String protocoll) {
		this.protocoll = protocoll;
	}

	public String getShortProtocoll(String protocoll) {
		String[] shortProtocoll = protocoll.split(":");
		return shortProtocoll[0];
	}

	@Override
	public String toString() {
		String shortProtocoll = getShortProtocoll(protocoll);
		StringBuilder sb = new StringBuilder();
		sb.append("Wahlperiode: ").append(period);
		sb.append(System.lineSeparator());
		sb.append("Datum: ").append(date.toString());
		sb.append(System.lineSeparator());
		sb.append("Protokollauszug: ").append(shortProtocoll);
		return sb.toString();
	}

}
