package tutorial;


import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

@SuppressWarnings("serial")
public class SearchProductComposer extends SelectorComposer<Component> {

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
	
//	@Override
//	public void doAfterCompose(Component comp) throws Exception {
//		super.doAfterCompose(comp);
//		//initialize component's data
//		productListbox.setModel(new ListModelList<Book>(bookService.findAll()));
//	}
//	
	@Listen("onClick = #searchButton")
	public void search(){
		String keyword = keywordBox.getValue();
		List<Book> result = bookService.search(keyword);
		productListbox.setModel(new ListModelList<Book>(result));
	}
	
	@Listen("onSelect = #productListbox")
	public void showDetail(SelectEvent event){
		Book selectedBook = (Book)event.getSelectedObjects().iterator().next();
		thumbImage.setSrc(selectedBook.getThumbnail());
		nameLabel.setValue(selectedBook.getName());
		priceLabel.setValue("$"+selectedBook.getPrice());
		descriptionLabel.setValue(selectedBook.getDescription());
		
	}
}
