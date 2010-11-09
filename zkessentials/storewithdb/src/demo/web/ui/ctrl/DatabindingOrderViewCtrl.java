package demo.web.ui.ctrl;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import demo.model.OrderDAO;
import demo.model.bean.CartItem;
import demo.model.bean.Order;
import demo.web.UserCredentialManager;
import demo.web.ui.OrderCtrl;

/**
 * @author zkessentials store
 * 
 *         This is the databinding controller for the order view as referenced
 *         in index.zul
 * 
 */
public class DatabindingOrderViewCtrl extends GenericForwardComposer implements
		OrderCtrl {

	private static final long serialVersionUID = -8897252573046243483L;

	private static final String KEY_ORDER_VIEW_CTRL = "KEY_ORDER_VIEW_CTRL";

	private Listbox orderLibox;
	private Button cancelOrderBtn;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.setAttribute(KEY_ORDER_VIEW_CTRL, this);
		cancelOrderBtn.setDisabled(true);
	}

	public static OrderCtrl getInstance(Desktop desktop) {
		return (OrderCtrl) desktop.getAttribute(KEY_ORDER_VIEW_CTRL);
	}

	public List<Order> getOrders() {
		List<Order> orders = getOrderDAO().findByUser(getCurrentUserId());
		return orders;
	}

	private Long getCurrentUserId() {
		Long userId = UserCredentialManager.getIntance(session).getUser()
				.getId();
		return userId;
	}

	private static OrderDAO getOrderDAO() {
		return new OrderDAO();
	}

	public Order submitNewOrder(List<CartItem> items, String description) {
		// 1. insert a new order, and a bunch of order_item into the DB.
		Order order = getOrderDAO().createOrder(getCurrentUserId(), items,
				description);
		// 2. show this new Order at the UI.
		getOrderModel().add(order);
		return order;
	}

	private ListModelList getOrderModel() {
		return (ListModelList) orderLibox.getModel();
	}

	public void onClick$cancelOrderBtn() {
		if (orderLibox.getSelectedItem() == null) {
			return;
		}
		Order order = (Order) orderLibox.getSelectedItem().getValue();
		cancelOrder(order.getId());
	}

	public void onSelect$orderLibox() {
		if (cancelOrderBtn.isDisabled())
			cancelOrderBtn.setDisabled(false);
	}

	private void cancelOrder(Long orderId) {
		// 1. Do the update using DAO
		getOrderDAO().cancelOrder(orderId);
		// 2. update UI
		AnnotateDataBinder binder = (AnnotateDataBinder) page
				.getAttribute("binder");
		binder.loadAll();
	}

}
