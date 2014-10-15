package tutorial.richlet;

import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.RichletConfig;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import tutorial.Car;
import tutorial.CarService;
import tutorial.CarServiceImpl;

public class SearchRichlet extends GenericRichlet {

	private CarService carService = new CarServiceImpl();

	@Override
	public void service(Page page) throws Exception {
//		if ("/search/admin".equals(page.getRequestPath())){
//			//build admin UI
//		}else{
//			//build normal UI
//		}
		//build the user interface
		Component rootComponent = buildUserInterface();
		rootComponent.setPage(page);
	}
	
	private Component buildUserInterface(){

		//build search area
		final Textbox keywordBox = new Textbox();
		Button searchButton = new Button("Search");
		searchButton.setImage("/img/search.png");
		
		Hbox searchArea = new Hbox();
		searchArea.setAlign("center");
		searchArea.appendChild(new Label("Keyword:"));
		searchArea.appendChild(keywordBox);
		searchArea.appendChild(searchButton);
		
		
		//build Car List Area
		Listhead listhead = new Listhead();
		listhead.appendChild(new Listheader("Model"));
		listhead.appendChild(new Listheader("Make"));
		Listheader priceHeader = new Listheader("Price");
		priceHeader.setWidth("20%");
		listhead.appendChild(priceHeader);
		final Listbox carListbox = new Listbox();
		carListbox.setHeight("160px");
		carListbox.setEmptyMessage("No car found in the result");
		carListbox.setItemRenderer(new CarRenderer());
		carListbox.appendChild(listhead);
		
		//build Detail Area
		final Label modelLabel = new Label();
		final Label makeLabel = new Label();
		final Label priceLabel = new Label();
		final Label descriptionLabel = new Label();
		Vbox vbox = new Vbox();
		vbox.appendChild(modelLabel);
		vbox.appendChild(makeLabel);
		vbox.appendChild(priceLabel);
		vbox.appendChild(descriptionLabel);
		
		final Image previewImage = new Image();
		previewImage.setWidth("250px");
		Hbox detailArea = new Hbox();
		detailArea.setStyle("margin-top:20px");
		detailArea.appendChild(previewImage);
		detailArea.appendChild(vbox);
		
		//add event listeners
		searchButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			//search
			@Override
			public void onEvent(Event event) throws Exception {
				String keyword = keywordBox.getValue();
				List<Car> result = carService.search(keyword);
				carListbox.setModel(new ListModelList<Car>(result));
			}
			
		});
		
		carListbox.addEventListener(Events.ON_SELECT, new EventListener<SelectEvent>() {
			//show selected item's detail
			@Override
			public void onEvent(SelectEvent event) throws Exception {
				//get selection from listbox's model
				Set<Car> selection = ((Selectable<Car>)carListbox.getModel()).getSelection();
				if (selection!=null && !selection.isEmpty()){
					Car selected = selection.iterator().next();
					previewImage.setSrc(selected.getPreview());
					modelLabel.setValue(selected.getModel());
					makeLabel.setValue(selected.getMake());
					priceLabel.setValue(selected.getPrice().toString());
					descriptionLabel.setValue(selected.getDescription());
				}
			}
		});
		
		Window window = new Window();
		window.setTitle("Search");
		window.setWidth("600px");
		window.setBorder("normal");
		window.appendChild(searchArea);
		window.appendChild(carListbox);
		window.appendChild(detailArea);
		
		return window;
	}
	

	@Override
	public void init(RichletConfig config) {
		//initialize resources, e.g. get initial parameters
	}
	
	@Override
	public void destroy() {
		//destroy resources
	}
}

class CarRenderer implements ListitemRenderer<Car>{

	@Override
	public void render(Listitem listitem, Car car, int index) throws Exception {
		listitem.appendChild(new Listcell(car.getModel()));
		listitem.appendChild(new Listcell(car.getMake()));
		Listcell priceCell = new Listcell();
		priceCell.appendChild(new Label("$"));
		priceCell.appendChild(new Label(car.getPrice().toString()));
		listitem.appendChild(priceCell);
	}
}

