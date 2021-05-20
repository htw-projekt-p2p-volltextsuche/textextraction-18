package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.json.JSONObject;

public class Speach {
	private UUID uuid;
	private String title;
	private String speaker;
	private String affiliation;
	private String date;
	private String text;

	public Speach(String title, String speaker, String affiliation, LocalDate date, String text) {
		this.uuid = UUID.randomUUID();
		this.title = title;
		this.speaker = speaker;
		this.affiliation = affiliation;
		this.date = formatDateToISO8601String(date);
		this.text = text;
	}

	private String formatDateToISO8601String(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
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

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", uuid);
		json.put("title", title);
		json.put("speaker", speaker);
		json.put("affiliation", affiliation);
		json.put("date", date);
		json.put("text", text);
		return json;
	}

}
