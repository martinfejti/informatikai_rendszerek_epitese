package org.ait.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long id;
	
	@NotNull(message="Keresztnév nem lehet üres")
    @Size(min = 3, max = 14, message="Keresztnév 3 és 14 karakter lehet")
	private String firstName;
	
	@NotNull(message="Vezetéknév nem lehet üres")
    @Size(min = 3, max = 14, message="Vezetéknév 3 és 14 karakter lehet")
	private String lastName;
	
	// ez azért kell hogy java.util.Date ként kezelje és ne java.sql.Date -ként!
	@Temporal(TemporalType.TIMESTAMP)
	@Past(message="születési időnek a múltban kell lennie")
	private Date birthDay;

	public Person() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

}

