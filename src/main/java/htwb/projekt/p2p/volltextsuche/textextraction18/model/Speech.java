package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.json.JSONObject;

public class Speech {
	private UUID uuid;
	private String title;
	private String speaker;
	private String affiliation;
	private String date;
	private String text;

	public Speech(String title, String speaker, String affiliation, LocalDate date, String text) {
		this.uuid = UUID.randomUUID();
		this.title = title;
		this.speaker = speaker;
		this.affiliation = affiliation;
		this.date = formatDateToISO8601String(date);
		this.text = text;
	}

	private String formatDateToISO8601String(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");		
		return date.format(formatter);
	}

	public UUID getId() {
		return uuid;
	}

	public void setId(UUID id) {
		this.uuid = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void toJSON() {
		JSONObject json = new JSONObject();
		json.append("id", uuid);
		json.append("title", title);
		json.append("speaker", speaker);
		json.append("affiliation", affiliation);
		json.append("date", date);
		json.append("text", text);

	}

}
