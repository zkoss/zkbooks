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
						"Head First Java", 
						"Learning a complex new language is no easy task especially when it s an object-oriented computer programming language like Java. You might think the problem is your brain. It seems to have a mind of its own, a mind that doesn't always want to take in the dry, technical stuff you're forced to study. " +
								"The fact is your brain craves novelty. It's constantly searching, scanning, waiting for something unusual to happen. After all, that's the way it was built to help you stay alive. It takes all the routine, ordinary, dull stuff and filters it to the background so it won't interfere with your brain's real work--recording things that matter. How does your brain know what matters? It's like the creators of the Head First approach say, suppose you're out for a hike and a tiger jumps in front of you, what happens in your brain? Neurons fire. Emotions crank up. Chemicals surge."
								+"That's how your brain knows.",
								"img/headFirstJava.jpg",
								23.72f));
		bookList.add(
				new Book(2, 
						"Effective Java", 
						"Are you looking for a deeper understanding of the Java™ programming language so that you can write code that is clearer, more correct, more robust, and more reusable? Look no further! Effective Java™, Second Edition, brings together seventy-eight indispensable programmer’s rules of thumb: working, best-practice solutions for the programming challenges you encounter every day.",
						"img/effectiveJava.jpg",
						38.85f));
		bookList.add(
				new Book(3, 
						"Java, A Beginner's Guide", 
						"Learn the fundamentals of Java programming in no time from bestselling programming author Herb Schildt. Fully updated to cover Java Platform, Standard Edition 7 (Java SE 7), Java: A Beginner's Guide, Fifth Edition starts with the basics, such as how to compile and run a Java program, and then discusses the keywords, syntax, and constructs that form the core of the Java language. You'll also find coverage of some of Java's most advanced features, including multithreaded programming and generics. An introduction to Swing concludes the book. Get started programming in Java right away with help from this fast-paced tutorial.",
						"img/javaBeginner.jpg",
						24.09f));
		bookList.add(
				new Book(4, 
						"Sams Teach Yourself Java in 24 Hours", 
						"In just 24 lessons of one hour or less, you can learn how to create Java applications with the free NetBeans visual editing tools. Using a straightforward, step-by-step approach, popular author Rogers Cadenhead helps you master the skills and technology you need to create desktop and web programs, web services, and even an Android app in Java.",
						"img/teachYourself.jpg",
						20.99f));
		bookList.add(
				new Book(5, 
						"Introduction to Java Programming, Comprehensive ", 
						"Regardless of major, students will be able to grasp concepts of problem-solving and programming — thanks to Liang’s fundamentals-first approach, students learn critical problem solving skills and core constructs before object-oriented programming.  Liang’s approach has been extended to application-rich programming examples, which go beyond the traditional math-based problems found in most texts. Students are introduced to topics like control statements, methods, and arrays before learning to create classes. Later chapters introduce advanced topics including graphical user interface, exception handling, I/O, and data structures. Small, simple examples demonstrate concepts and techniques while longer examples are presented in case studies with overall discussions and thorough line-by-line explanations. Increased data structures chapters make the Eighth Edition ideal for a full course on data structures.",
						"img/introJava.jpg",
						94.75f));
		bookList.add(
				new Book(6, 
						"Building Java Programs", 
						"Building Java Programs: A Back to Basics Approach, Second Edition, introduces novice programmers to basic constructs and common pitfalls by emphasizing the essentials of procedural programming, problem solving, and algorithmic reasoning. By using objects early to solve interesting problems and defining objects later in the course, Building Java Programs develops programming knowledge for a broad audience.",
						"img/build.jpg",
						95.58f));
		bookList.add(
				new Book(7, 
						"Beginning Programming with Java For Dummies", 
						"Java is a popular language for beginning programmers, and earlier editions of this fun and friendly guide have helped thousands get started. Now fully revised to cover recent updates for Java 7.0, Beginning Programming with Java For Dummies, 3rd Edition is certain to put more first-time programmers and Java beginners on the road to Java mastery.",
						"img/beginning.jpg",
						17.48f));
		bookList.add(
				new Book(8, 
						"Java For Dummies", 
						"Java is the platform-independent, object-oriented programming language used for developing web and mobile applications. The revised version offers new functionality and features that have programmers excited, and this popular guide covers them all. This book helps programmers create basic Java objects and learn when they can reuse existing code. It's just what inexperienced Java developers need to get going quickly with Java 2 Standard Edition 7.0 (J2SE 7.0) and Java Development Kit 7.0 (JDK 7).",
						"img/dummies.jpg",
						17.48f));
		bookList.add(
				new Book(9, 
						"Core Java, Volume I", 
						"This revised edition of the classic Core Java™, Volume I–Fundamentals, is the definitive guide to Java for serious programmers who want to put Java to work on real projects."+
						"Fully updated for the new Java SE 6 platform, this no-nonsense tutorial and reliable reference illuminates the most important language and library features with thoroughly tested real-world examples. The example programs have been carefully crafted to be easy to understand as well as useful in practice, so you can rely on them as an outstanding starting point for your own code.",
						"img/core.jpg",
						35.99f));
		bookList.add(
				new Book(10, 
						"Java 7: A Beginner's Tutorial", 
						"A Books24x7's TOP 10 title for 4 consecutive years!"+
						"Java is an easy language to learn. However, you need to master more than the language syntax to be a professional Java programmer. For one, object-oriented programming (OOP) skill is key to developing robust and effective Java applications. In addition, knowing how to use the vast collection of libraries makes development more rapid."+
						"This book introduces you to important programming concepts and teaches how to use the Java core libraries. It is a guide to building real-world applications, both desktop and Web-based. The coverage is the most comprehensive you can find in a beginner’s book.",
						"img/java7.jpg",
						28.36f));		
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
