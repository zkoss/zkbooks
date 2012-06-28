package tutorial;


import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
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
	private Hlayout detailArea;
	
	static final private String IMG_PATH = "img/";
	
	private BookService bookService = new BookService();
	
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		//initialize data
		productListbox.setModel(new SimpleListModel<Book>(bookService.findAll()));
	}
	
	public void search(){
		
	}
	
	@Listen("onSelect = #productListbox")
	public void showDetail(SelectEvent event){
		Book selectedBook = (Book)event.getSelectedObjects().iterator().next();
		detailArea.setVisible(true);
		thumbImage.setSrc(IMG_PATH+"/"+selectedBook.getThumbnail());
		nameLabel.setValue(selectedBook.getName());
		priceLabel.setValue("$"+selectedBook.getPrice());
		descriptionLabel.setValue(selectedBook.getDescription());
		
	}
}
