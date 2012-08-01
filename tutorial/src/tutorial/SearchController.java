package tutorial;


import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class SearchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Listbox productListbox;
	@Wire
	private Label nameLabel;
	@Wire
	private Label priceLabel;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Image previewImage;
	@Wire
	private Textbox keywordBox;
	
	
	private CarService bookService = new CarServiceImpl();
	
	@Listen("onClick = #searchButton")
	public void search(){
		String keyword = keywordBox.getValue();
		List<Car> result = bookService.search(keyword);
		productListbox.setModel(new ListModelList<Car>(result));
	}
	
	@Listen("onSelect = #productListbox")
	public void showDetail(){
		Car selectedBook = productListbox.getSelectedItem().getValue();
		previewImage.setSrc(selectedBook.getPreview());
		nameLabel.setValue(selectedBook.getName());
		priceLabel.setValue(selectedBook.getPrice().toString());
		descriptionLabel.setValue(selectedBook.getDescription());
	}
}
