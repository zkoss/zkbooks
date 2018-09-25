/* PersonDAO.java

	Purpose:
		
	Description:
		
	History:
		2010/7/30, Created by Ian Tsai

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.reference.developer.mvc.model;

import java.util.*;

/**
 * @author Ian Tsai
 *
 */
public class PersonDAO {

	public static final Map<Integer, Person> DUMMY_PERSON_TABLE =
			Collections.synchronizedMap(new LinkedHashMap<Integer, Person>());

	private static volatile int uuid = 0;

	static {
		Person person = null;

		person = new Person(++uuid, "Ian", "Tsai", false);
		DUMMY_PERSON_TABLE.put(person.getId(), person);

		person = new Person(++uuid, "Tom", "Yah", true);
		DUMMY_PERSON_TABLE.put(person.getId(), person);

		person = new Person(++uuid, "Henri", "Chen", true);
		DUMMY_PERSON_TABLE.put(person.getId(), person);

		person = new Person(++uuid, "Jumper", "Chen", true);
		DUMMY_PERSON_TABLE.put(person.getId(), person);

		person = new Person(++uuid, "Ryan", "Wu", false);
		DUMMY_PERSON_TABLE.put(person.getId(), person);
	}

	/**
	 * 
	 * @return
	 */
	public static List<Person> getAll() {
		return new ArrayList<Person>(DUMMY_PERSON_TABLE.values());
	}

	private static final int size = 1000000;

	public int countAll() {
		return size;
	}

	private static final String[][] NAMES = new String[][] {
			new String[] { "Ian", "Tsai" },
			new String[] { "Tom", "Yeh" },
			new String[] { "Henri", "Chen" },
			new String[] { "Jumper", "Chen" },
			new String[] { "Robbie", "Cheng" }};

	public List<Person> getPersons(int pageSize, int pageIdx) {

		int currentId = uuid + (pageSize * pageIdx);
		ArrayList<Person> persons = new ArrayList<Person>(pageSize);
		for (int i = 0; i < pageSize; i++) {
			Person person = new Person(currentId, NAMES[currentId % 5][0]
					+ currentId, NAMES[currentId % 5][1] + currentId,
					currentId % 2 == 0);
			persons.add(person);
			currentId++;
		}
		return persons;
	}
}
