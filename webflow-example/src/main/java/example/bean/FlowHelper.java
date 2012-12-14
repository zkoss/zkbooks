/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mschroen
 */
public class FlowHelper implements Serializable{
    
    
    private List myList = new LinkedList();
    private boolean mybool;
    private int myint;
    
    private String value1 = "item value1";
    
    

    public boolean isMybool() {
        return mybool;
    }

    public void setMybool(boolean mybool) {
        this.mybool = mybool;
    }

    public int getMyint() {
        return myint;
    }

    public void setMyint(int myint) {
        this.myint = myint;
    }

    public List getMyList() {
    	System.out.println("getmylist, object: "+ this+",list size "+myList.size());
        return myList;
    }

    public void setMyList(List myList) {
    	System.out.println("setmylist, object: "+ this+", list size: "+myList.size());
        this.myList = myList;
    }

    private void readObject(ObjectInputStream ois)
    		throws IOException, ClassNotFoundException {
    	ois.defaultReadObject();
    	System.out.println("read "+ this+", list size: "+myList.size());
    }

    private void writeObject(ObjectOutputStream oos)
    		throws IOException {
    	oos.defaultWriteObject();
    	System.out.println("write "+ this+", list size: "+myList.size());
    }

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
		System.out.println(">>>setValue1 "+this+">"+value1);
	}
	
	public void kick(){
		System.out.println(">>>>Kick me");
	}

	public void addItem(String item){
		if(myList!=null){
			myList.add(item);
		}
	}
}
