package tutorial;


import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class SearchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Textbox keywordBox;
	@Wire
	private Listbox carListbox;
	@Wire
	private Label nameLabel;
	@Wire
	private Label companyLabel;
	@Wire
	private Label priceLabel;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Image previewImage;
	
	
	private CarService carService = new CarServiceImpl();
	
	@Listen("onClick = #searchButton")
	public void search(){
		String keyword = keywordBox.getValue();
		List<Car> result = carService.search(keyword);
		carListbox.setModel(new ListModelList<Car>(result));
	}
	
	@Listen("onSelect = #carListbox")
	public void showDetail(){
		Car selected = carListbox.getSelectedItem().getValue();
		previewImage.setSrc(selected.getPreview());
		nameLabel.setValue(selected.getName());
		companyLabel.setValue(selected.getCompany());
		priceLabel.setValue(selected.getPrice().toString());
		descriptionLabel.setValue(selected.getDescription());
	}
}
