package tutorial;


import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class SearchProductComposer extends SelectorComposer<Window> {

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
	
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		//initialize data
		productListbox.setModel(new SimpleListModel<Book>(bookService.findAll()));
	}
	
	@Listen("onClick = #searchButton")
	public void search(){
		productListbox.setModel(new SimpleListModel<Book>(bookService.search(keywordBox.getValue())));
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
