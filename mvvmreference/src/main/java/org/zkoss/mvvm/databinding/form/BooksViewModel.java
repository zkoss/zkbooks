package org.zkoss.mvvm.databinding.form;

import org.zkoss.bind.annotation.*;

import java.util.*;

public class BooksViewModel {

	private List<Book> books;
	private Book currentBook;
	private boolean editable;

	private String[][] BOOKS = {
			{"The Very Hungry Caterpillar", "Eric Carle", "Children,Classics,Animals"},
			{"The New Way Things Work", "David Macaulay", "Education,Science,Computers"},
			{"The DASH Diet Younger You", "Marla Heller", "Health,Fitness,Diets"}
			};
	@Init
	public void init() {
		books = new LinkedList<Book>();
		for (String[] book : BOOKS) {
			books.add(initBook(book[0], book[1], book[2]));
		}
		currentBook = books.get(0);
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean getEditable() {
		return editable;
	}
	public void setCurrentBook(Book book) {
		currentBook = book;
	}
	public Book getCurrentBook() {
		return currentBook;
	}
	private Book initBook(String name, String author, String categories) {
		Book book = new Book();
		book.setName(name);
		book.setAuthor(author);
		String[] cates = categories.split(",");
		Set<Category> sets = new HashSet<Category>();
		for (String cate : cates) {
			Category c = new Category();
			c.setName(cate);
			sets.add(c);
		}
		book.setCategories(sets);
		return book;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> book) {
		books = book;
	}
	
	@SmartNotifyChange("editable")
	@Command("editable")
	public void doEditable(@BindingParam("editable") boolean editable) {
		this.editable = editable;
	}

	@Command("cancel")
	@NotifyChange("currentBook")
	public void onCancel() {
		//just trigger loading fx.resetEmptyStringValue
	}
	
	@Command("save")
	@NotifyChange({"currentBook", "editable"})
	public void onSave() {
		this.editable = false;
	}

	@Command("addCategory")
	public void doAddCategory(@BindingParam("form") Book form,
			@BindingParam("cateName") String cateName) {
		Set<Category> categories = (Set<Category>) form.getCategories();
		categories.add(new Category(cateName));
		//because categories is a ZK-made proxy object instead of a standard java collection, so ZK can notify change when you call add() or remove()
		//check addCallbacks() in FormProxyHandler
	}

	@Command("removeCategory")
	public void doRemoveCategory(@BindingParam("form") Book form,
			@BindingParam("category") Category cate) {
		Set<Category> categories = (Set<Category>) form.getCategories();
		categories.remove(cate);
	}
}
