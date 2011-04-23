package edu.weblibrary.server.persistence.jpa;


import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="book")
@AttributeOverride(name="arrivalDate", column=@Column(name="arrival_date"))
public class Book {
	@Id
	private int id;
	
	@Column(name="author_id", insertable=false, updatable=false)
	private int authorId;	
	
	@Column(name="title")
	private String title;
	
	@Column(name="publishing")
	private String publishing;
	
	@Column(name="publication_year")
	private int publicationYear;
	
	@Column(name="price")
	private float price;
	
	@Column(name="number")
	private int number;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name="arrival_date")
	private Date arrivalDate;
	
	@Column(name="keywords")
	private String keywords;
	
	@ManyToOne
	private Author author;
		
	public Book() { }
	
	public Book(int id, int authorId, String title, String publishing, int publicationYear, float price, 
			int number, Date arrivalDate, String keywords)	{
		this.id = id;
		this.authorId = authorId;
		this.title = title;
		this.publishing = publishing;
		this.publicationYear = publicationYear;
		this.price = price;
		this.number = number;
		this.arrivalDate = arrivalDate;
		this.keywords = keywords;		
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setPublishing(String publishing) {
		this.publishing = publishing;
	}
	public String getPublishing() {
		return publishing;
	}
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
	public int getPublicationYear() {
		return publicationYear;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getPrice() {
		return price;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public Author getAuthor() {
		return this.author;
	}
	
	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getAuthorId() {
		return authorId;
	}	
}
