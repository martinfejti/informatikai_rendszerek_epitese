package org.ait.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.ait.model.Person;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class PersonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// a JPAExampleDB név a persistence.xml-ből jön
	@PersistenceContext(unitName = "JPAExampleDB")
	private EntityManager entityManager;

	// beépített tranzakció kezelőt használunk, most nincs autocommit
	@Resource
	private UserTransaction userTransaction;

	// ez a lista fogja tárolni az összes személyt
	private List<Person> allPersons;

	// ez fogja tárolni az újonnan létrehozott személyt
	private Person person = new Person();

	@PostConstruct
	public void init() {
		queryAllPersons();
	}

	private void queryAllPersons() {
		// a sql sztringben a model osztály nevét kell írni
		String q = "SELECT o from Person o";
		Query query = entityManager.createQuery(q);
		allPersons = query.getResultList();
	}

	public void newPerson() throws Exception {
		Person p = new Person();
		person.setId(p.getId());
		
		userTransaction.begin();
		entityManager.persist(person);
		userTransaction.commit();
		
		queryAllPersons();
		
		// egy új person-t hozunk létre, hogy az id változzon
		person = new Person();
	}
	
	public void modifyPerson() throws Exception {
		userTransaction.begin();
		entityManager.merge(person);
		userTransaction.commit();		
	}
	
    public void onRowSelect(SelectEvent event) {
    	person = (Person) event.getObject();
    }
	
	/***********************************************************************
	 * innen jönnek a generált getter és setter-ek
	 */
	public List<Person> getAllPersons() {
		return allPersons;
	}

	public void setAllPersons(List<Person> allPersons) {
		this.allPersons = allPersons;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
