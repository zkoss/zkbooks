package org.zkoss.reference.developer.mvvm.collection;

import java.util.ArrayList;
import java.util.List;

public class MenuVM {

	String message;
	List<Node> nodes;
	
	public MenuVM(){
		nodes = new ArrayList<Node>();
		nodes.add(createNode("Item A",0,0));
		nodes.add(createNode("Item B",1,1));
		nodes.add(createNode("Item C",2,2));
//		nodes.add(createNode("Item D",3,3));
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	Node createNode(String name,int children,int nested){
		Node n = new Node(name);
		if(nested>0){
			for(int i=0;i<children;i++){
				n.addChild(createNode(name+"_"+i,i==children-1?0:children,nested-1));
			}
		}
		return n;
	}


//	public String getMessage() {
//		return message;
//	}

//	@Command @NotifyChange("message")
//	public void menuClicked(@BindingParam("node") Node node ){
//		message = "clicked "+node.getName();
//	}
	
	static public class Node{
		List<Node> children;
		String name;
		
		public Node(String name){
			this.name = name;
			children = new ArrayList<Node>();
		}
		
		public void addChild(Node node){
			children.add(node);
		}
		
		public List<Node> getChildren(){
			return children;
		}
		
		public String getName(){
			return name;
		}
		
	}
	
}
