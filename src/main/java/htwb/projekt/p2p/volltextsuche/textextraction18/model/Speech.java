package htwb.projekt.p2p.volltextsuche.textextraction18.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.projekt.p2p.volltextsuche.textextraction18.enums.RegexPattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Speech {

    @JsonIgnore
    private UUID id;

    private String title;

    private String speaker;

    private String affiliation;

    private String date;

    private String text;

    public Speech() {
    }

    public Speech(String title, String speaker, String affiliation, LocalDate date, String text) {
        this.id = UUID.randomUUID();
        this.title = reformatTitle(title);
        this.speaker = speaker;
        this.affiliation = prettyUpAffiliation(affiliation);
        this.date = formatDateToISO8601String(date);
        this.text = prettyUpText(text);
    }

    private String formatDateToISO8601String(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public UUID getId() {
        return id;
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

    public void setDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = date.format(formatter);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        text = prettyUpText(text);
        this.text = text;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String prettyUpText(String text) {
        text = text.replaceAll(RegexPattern.TWO_LINEBREAKS.pattern.pattern(), " ");
        text = text.replaceAll("(.)-\\n(.)", "$1$2");
        text = text.replaceAll("(.)-\\r*\\n*(.)", "$1$2");
        text = text.replaceAll("(.)\\n(.)", "$1 $2");
        text = text.replaceAll("\\n", "");
        return text;
    }

    private String prettyUpAffiliation(String text) {
        text = prettyUpText(text);
        Matcher m = Pattern.compile("\\(.+\\)").matcher(text);
        if (m.find()) {
            text = text.substring(1, text.length() - 1);
        }
        return text;
    }

    private String reformatTitle(String title) {
        title = prettyUpText(title);
        Matcher m = Pattern.compile("Eröffnungsrede").matcher(title);
        if (!m.find()) {
            title = title.substring(4);
        }
        m = Pattern.compile("punkt\\s\\d+:").matcher(title);
        if (m.find()) {
            title = title.substring(m.end());
        }
        m = Pattern.compile("\\s\\.\\s").matcher(title);
        if (m.find()) {
            title = title.substring(0, m.start());
        }
        m = Pattern.compile("Drucksache").matcher(title);
        if (m.find()) {
            title = title.substring(0, m.start());
        }
        return title;
    }

}
