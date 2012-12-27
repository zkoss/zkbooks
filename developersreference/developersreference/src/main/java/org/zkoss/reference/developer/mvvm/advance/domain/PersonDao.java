package org.zkoss.reference.developer.mvvm.advance.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

// Mimic a DAO object that queries data from a database. 
public class PersonDao {
	private int currentID = 0;
	private List <Person> personList;
	
	private String[] names = {"Aaron", "Alan", "Andrew", "Brian","Carl", "David","Elias", "Felix", "Forbes","Thomas",
			"Jack", "William", "Daniel","Matthew", "James", "Joseph", "Harry"};
	private String[] surnames = {"Smith", "Brown", "Lee", "Wilson","Martin", "Patel","Taylor","Wong","Campbell","Williams"
			,"Thompson", "Jones", "Licon", "Jaffeson", "Washton", "Clinton", "Bush"};
	private Random random = new Random(Calendar.getInstance().getTimeInMillis());
	
	public void generateData(){
		generateData(200);
	}
	public void generateData(int maxSize){
		personList = new ArrayList<Person>();
		for (int i = 0; i<maxSize ; i++){
			personList.add(createNewPerson());
		}
	}
	
	public Person createNewPerson(){
		Person p = new Person();
		p.setId(currentID);
		p.setAge(random.nextInt(80));
		p.setFirstName(names[random.nextInt(names.length)]);
		p.setLastName(surnames[random.nextInt(surnames.length)]);
		
		currentID++;
		return p;
	}
	
	public List<Person> findAll() {
		return personList;
	}

	public List<Person> findAll(int offset, int max) {
		List<Person> section = new ArrayList<Person>();
		int last = (offset+max) > personList.size() ? personList.size() : offset+max;  
		for (int i = offset ; i< last ; i++){
			section.add(personList.get(i));
		}
		return section;
	}
	
	public int findAllSize() {
		return personList.size();
	}	
	
	public void delete(Person person){
		for (int i = 0 ; i < personList.size() ; i++){
			Person p = personList.get(i);
			if (p.equals(person)){
				personList.remove(i);
				break;
			}
		}
	}
	
	public void add(Person person){
		person.setId(personList.get(personList.size()-1).getId()+1);
		personList.add(person);
	}
	
	
//	public Order save(Order newOrder) throws HibernateException{
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.save(newOrder);
//		session.flush();
//		return newOrder;
//	}
	
}