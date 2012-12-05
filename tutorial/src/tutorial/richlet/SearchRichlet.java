package tutorial.richlet;

import java.util.List;
import java.util.Map;

import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.RichletConfig;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.Template;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import tutorial.Car;
import tutorial.CarService;
import tutorial.CarServiceImpl;

public class SearchRichlet extends GenericRichlet {

	private CarService carService = new CarServiceImpl();

	public void service(Page page) throws Exception {
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
		carListbox.setTemplate("model", new ListboxTemplate());
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
		
		carListbox.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			//show selected item's detail
			@Override
			public void onEvent(Event event) throws Exception {
				Car selected = carListbox.getSelectedItem().getValue();
				previewImage.setSrc(selected.getPreview());
				modelLabel.setValue(selected.getModel());
				makeLabel.setValue(selected.getMake());
				priceLabel.setValue(selected.getPrice().toString());
				descriptionLabel.setValue(selected.getDescription());
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
		super.init(config);
		//initialize resources
	}
	
	@Override
	public void destroy() {
		super.destroy();
		//destroy resources
	}
}

class ListboxTemplate implements Template {

	@SuppressWarnings("rawtypes")
	public Component[] create(Component parent, Component insertBefore,
			VariableResolver resolver, Composer composer){
		
		Car car = (Car)resolver.resolveVariable("each");
		Listitem listitem = new Listitem();
		listitem.appendChild(new Listcell(car.getModel()));
		listitem.appendChild(new Listcell(car.getMake()));
		Listcell priceCell = new Listcell();
		priceCell.appendChild(new Label("$"));
		priceCell.appendChild(new Label(car.getPrice().toString()));
		listitem.appendChild(priceCell);
		
		 //append to the parent
        if (insertBefore ==null){
                parent.appendChild(listitem);
        }else{
                parent.insertBefore(listitem, insertBefore);
        }
        
		Component[] components = new Component[1];
		components [0] = listitem;

		return components;
	}

	@Override
	public Map<String, Object> getParameters() {
		// you could set variable name, we use default name, "each" 
		//parameters.put("var","car");
		return null;
	}
}
