package tutorial;


import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

@SuppressWarnings("serial")
public class SearchProductController extends SelectorComposer<Component> {

	@Wire
	private Listbox productListbox;
	@Wire
	private Label nameLabel;
	@Wire
	private Label priceLabel;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Image thumbImage;
	@Wire
	private Textbox keywordBox;
	
	
	private BookService bookService = new BookServiceImpl();
	
	@Listen("onClick = #searchButton")
	public void search(){
		String keyword = keywordBox.getValue();
		List<Book> result = bookService.search(keyword);
		productListbox.setModel(new ListModelList<Book>(result));
	}
	
	@Listen("onSelect = #productListbox")
	public void showDetail(){
		Book selectedBook = productListbox.getSelectedItem().getValue();
		thumbImage.setSrc(selectedBook.getThumbnail());
		nameLabel.setValue(selectedBook.getName());
		priceLabel.setValue(selectedBook.getPrice().toString());
		descriptionLabel.setValue(selectedBook.getDescription());
		
	}
}
