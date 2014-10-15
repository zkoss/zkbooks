package tutorial.richlet;

import java.util.Map;

import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import tutorial.Car;

class CarListTemplate implements Template {

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
		if (insertBefore ==null || parent != insertBefore.getParent()){
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
		//it's used for data binding.
		//we don't use it in this example.
		return null;
	}
}