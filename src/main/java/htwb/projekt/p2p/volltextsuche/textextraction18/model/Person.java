package htwb.projekt.p2p.volltextsuche.textextraction18.model;

public class Person {
	private String name;
	private String affiliation;
	
	public Person() {
	}

	public Person(String name, String affiliation) {
		super();
		this.name = name;
		this.affiliation = affiliation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public String toString() {
		return name +" "+affiliation; 
	}
	
	

}
