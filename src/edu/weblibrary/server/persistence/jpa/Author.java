package edu.weblibrary.server.persistence.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="author")
@AttributeOverride(name="birthYear", column=@Column(name="birth_year"))
public class Author {
	private int id;
	private String name;
	private String surname;	
	private int birthYear;	
	private String patronymic;
	
	private java.util.Collection<Book> books;
	
	public Author() { }	

	public Author(int id, String name, String surname, String patronymic, int birthYear) {
		this.id = id;
		this.setName(name);
		this.setSurname(surname);
		this.setBirthYear(birthYear);
		this.setPatronymic(patronymic);
	}
	
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	// !
	@OneToMany(mappedBy="author")
	public java.util.Collection<Book> getBooks() {
		return this.books;
	}
	
	public void setBooks(java.util.Collection<Book> books) {
		this.books = books;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}
	
	public void setBirthYear(int birth_year) {
		this.birthYear = birth_year;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getPatronymic() {
		return patronymic;
	}
}