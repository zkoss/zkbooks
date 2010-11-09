package demo.web.ui.ctrl;

import static org.zkoss.zk.ui.event.Events.ON_CLICK;

import java.text.SimpleDateFormat;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Spinner;

import demo.model.ProductDAO;
import demo.model.bean.Product;
import demo.web.ui.ShoppingCartCtrl;

/**
 * @author zkessentials store
 * 
 *         This is the controller for the product view as referenced in
 *         index.zul
 * 
 */
public class ProductViewCtrl extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4327599559929787819L;

	private Grid prodGrid;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		ProductDAO prodDao = new ProductDAO();
		List<Product> prods = prodDao.findAllAvailable();

		ListModelList prodModel = new ListModelList(prods);
		prodGrid.setModel(prodModel);
		prodGrid.setRowRenderer(new RowRenderer() {
			public void render(Row row, Object data) throws Exception {
				final Product prod = (Product) data;

				Image img = new Image(prod.getImgPath());
				img.setWidth("70px");
				img.setHeight("70px");
				img.setParent(row);
				new Label(prod.getName()).setParent(row);
				new Label("" + prod.getPrice()).setParent(row);
				new Label("" + prod.getQuantity()).setParent(row);
				new Label(new SimpleDateFormat("yyyy/MM/dd hh:mm").format(prod
						.getCreateDate())).setParent(row);
				initOperation(prod).setParent(row);
			}

			private Cell initOperation(final Product prod) {
				Cell cell = new Cell();
				final Spinner spinner = new Spinner(1);
				spinner.setConstraint("min 1 max " + prod.getQuantity());
				spinner.setParent(cell);

				Button button = new Button("add");
				button.setImage("/image/ShoppingCart-16x16.png");
				button.setParent(cell);

				final Label errorLb = new Label();
				errorLb.setParent(cell);

				button.addEventListener(ON_CLICK, new EventListener() {
					public void onEvent(Event event) throws Exception {
						ShoppingCartCtrl ctrl = ShoppingCartViewCtrl
								.getInstance(desktop);
						try {
							ctrl.addItem(prod, spinner.getValue());
							errorLb.setValue("");
						} catch (WrongValueException e) {
							errorLb.setValue(e.getMessage());
						}
					}
				});
				return cell;
			}
		});

	}
}
