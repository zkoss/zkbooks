package example.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MyConversation implements Serializable{

	private List myList = new LinkedList();

	public List getMyList() {
		return myList;
	}

	public void setMyList(List myList) {
		this.myList = myList;
	}
	
	
}
