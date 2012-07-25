package tutorial;

import java.util.LinkedList;
import java.util.List;

public class BookServiceImpl implements BookService{

	//data model
	private static List<Book> bookList= new LinkedList<Book>();
	
	//initialize book data
	static {
		bookList.add(
			new Book(1, 
			"ZK", 
			"ZK is the most popular open source SourceForge Ajax web framework that enables rich user interface for " +
			"web applications with no JavaScript and little programming.  This firstPress book will cover the following: " +
			"Shows you how to use this simplest Ajax framework to write real-world responsive web applications " +
			"Covers ZK's more than 70 XUL and 80 XHTML rich GUI components " +
			"Authoritatively written by cofounder/lead of the ZK project",
			"img/zk.jpg",
			12.49f));
		bookList.add(
				new Book(2, 
				"ZK Developer's Guide", 
				"ZK is an open-source web development framework that enables web applications to have the rich " +
				"user experiences and low development costs that desktop applications have had for years. " +
				"ZK includes an Ajax-based event-driven engine, rich sets of XML User Interface Language (XUL) " +
				"and XHTML components, and a markup language. The ZK rich client framework takes the so-called server-centric " +
				"approach: the content synchronization of components and the event pipelining between clients and " +
				"servers are automatically done by the engine and Ajax plumbing codes are completely transparent to " +
				"web application developers. Therefore, the end users get rich user interfaces with similar engaged interactivity and responsiveness to that of desktop applications, while for programmers, development remains similar in simplicity to that of desktop applications. This book is a Developer's Guide that steps you through the ZK framework with examples. It starts with installing and configuring ZK and takes you on to integrate it with other frameworks. By the time you are through the book you will be able to build an application on your own.",
				"img/zkDeveloperGuide.jpg",
				38.62f));
		bookList.add(
				new Book(3, 
				"Head First Java", 
				"Learning a complex new language is no easy task especially when it s an object-oriented computer programming language like Java. You might think the problem is your brain. It seems to have a mind of its own, a mind that doesn't always want to take in the dry, technical stuff you're forced to study. " +
				"The fact is your brain craves novelty. It's constantly searching, scanning, waiting for something unusual to happen. After all, that's the way it was built to help you stay alive. It takes all the routine, ordinary, dull stuff and filters it to the background so it won't interfere with your brain's real work--recording things that matter. How does your brain know what matters? It's like the creators of the Head First approach say, suppose you're out for a hike and a tiger jumps in front of you, what happens in your brain? Neurons fire. Emotions crank up. Chemicals surge."
				+"That's how your brain knows.",
				"img/headFirstJava.jpg",
				23.72f));
		bookList.add(
				new Book(4, 
				"Effective Java", 
				"Are you looking for a deeper understanding of the Java™ programming language so that you can write code that is clearer, more correct, more robust, and more reusable? Look no further! Effective Java™, Second Edition, brings together seventy-eight indispensable programmer’s rules of thumb: working, best-practice solutions for the programming challenges you encounter every day.",
				"img/effectiveJava.jpg",
				38.85f));
		bookList.add(
				new Book(5, 
				"Java, A Beginner's Guide", 
				"Learn the fundamentals of Java programming in no time from bestselling programming author Herb Schildt. Fully updated to cover Java Platform, Standard Edition 7 (Java SE 7), Java: A Beginner's Guide, Fifth Edition starts with the basics, such as how to compile and run a Java program, and then discusses the keywords, syntax, and constructs that form the core of the Java language. You'll also find coverage of some of Java's most advanced features, including multithreaded programming and generics. An introduction to Swing concludes the book. Get started programming in Java right away with help from this fast-paced tutorial.",
				"img/javaBeginner.jpg",
				24.09f));
		bookList.add(
				new Book(6, 
				"Sams Teach Yourself Java in 24 Hours", 
				"In just 24 lessons of one hour or less, you can learn how to create Java applications with the free NetBeans visual editing tools. Using a straightforward, step-by-step approach, popular author Rogers Cadenhead helps you master the skills and technology you need to create desktop and web programs, web services, and even an Android app in Java.",
				"img/teachYourself.jpg",
				20.99f));		
	}
	
	
	public List<Book> findAll(){
		return bookList;
	}
	
	public List<Book> search(String keyword){
		List<Book> result = new LinkedList<Book>();
		if (keyword==null || "".equals(keyword)){
			result = bookList;
		}else{
			for (Book b: bookList){
				if (b.getName().toLowerCase().contains(keyword.toLowerCase())){
					result.add(b);
				}
			}
		}
		return result;
	}
}
