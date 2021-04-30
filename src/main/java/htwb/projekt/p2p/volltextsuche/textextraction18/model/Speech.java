package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.time.LocalDate;
import java.util.List;

public class Speech {
	private String person;
	private String party;
	private String period;
	private LocalDate date;
	private List<String> keywords;
	private String speech;
	
	public Speech(String person, String party, String period, LocalDate date, List<String> keywords, String rede) {
		this.person = person;
		this.party = party;
		this.period = period;
		this.date = date;
		this.keywords = keywords;
		this.speech = rede;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getRede() {
		return speech;
	}

	public void setRede(String rede) {
		this.speech = rede;
	}
	
	
	
	
}
