package demo.web.ui.ctrl;

import static org.zkoss.zk.ui.event.Events.ON_CLICK;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listfooter;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import demo.model.bean.CartItem;
import demo.model.bean.Product;
import demo.web.AddToCartCallback;
import demo.web.OverQuantityException;
import demo.web.ShoppingCart;
import demo.web.ui.OrderCtrl;
import demo.web.ui.ShoppingCartCtrl;

/**
 * @author zkessentials store
 * 
 *         This is the controller for the shopping cart view
 * 
 */
public class ShoppingCartViewCtrl extends GenericForwardComposer implements
		ShoppingCartCtrl {

	private static final long serialVersionUID = -3893636956237252970L;

	private static final String KEY_SHOPPING_CART = "KEY_SHOPPING_CART";
	private Listbox shoppingCartListbox;
	private ListModelList _cartModel;
	private Button submitOrderBtn;
	private Button clearBtn;
	private Listfooter subtotalFooter;
	private Image cartItemImage;
	private Textbox descTxb;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		desktop.setAttribute(KEY_SHOPPING_CART, this);

		_cartModel = new ListModelList(getShoppingCart(session).getItems()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6398166037116720994L;

			public boolean remove(Object o) {
				CartItem item = (CartItem) o;
				getShoppingCart(session).remove(item.getProduct().getId());
				return super.remove(o);
			}

			public void clear() {
				super.clear();
				getShoppingCart(session).clear();
			}
		};

		shoppingCartListbox.setModel(_cartModel);
		shoppingCartListbox.setItemRenderer(new ListitemRenderer() {
			public void render(Listitem listItem, Object data) throws Exception {

				final CartItem cartItem = (CartItem) data;
				final Product prod = cartItem.getProduct();
				listItem.setValue(cartItem);

				new Listcell(prod.getName()).setParent(listItem);
				new Listcell("" + prod.getPrice()).setParent(listItem);
				new Listcell("" + cartItem.getAmount()).setParent(listItem);

				initSubTotal(cartItem, new Listcell()).setParent(listItem);
				initCancelBtn(cartItem, prod, new Listcell()).setParent(
						listItem);
			}

			private Listcell initSubTotal(CartItem cartItem, Listcell cell) {
				float price = cartItem.getProduct().getPrice();
				float subTotal = price * cartItem.getAmount();

				Label subTotalLb = new Label("$ " + subTotal);
				subTotalLb.setStyle("word-wrap: word-break");
				subTotalLb.setParent(cell);
				return cell;
			}

			private Listcell initCancelBtn(final CartItem cartItem,
					final Product prod, Listcell cell) {
				Button button = new Button();
				button.setImage("/image/DeleteCross-16x16.png");
				button.addEventListener(ON_CLICK, new EventListener() {
					public void onEvent(Event event) throws Exception {
						_cartModel.remove(cartItem);
						refresh();
					}
				});
				button.setParent(cell);
				return cell;
			}
		});
		refresh();
	}

	private void refresh() {
		subtotalFooter.setLabel("$: " + this.getCurrentSubTotal());
		boolean shouldDisable = _cartModel.size() == 0;
		submitOrderBtn.setDisabled(shouldDisable);
		clearBtn.setDisabled(shouldDisable);
	}

	public void onClick$clearBtn() {
		_cartModel.clear();
		refresh();
	}

	public void onClick$submitOrderBtn() {
		OrderCtrl orderCtrl = DatabindingOrderViewCtrl.getInstance(desktop);
		orderCtrl.submitNewOrder(getShoppingCart(session).getItems(), descTxb
				.getValue());
		descTxb.setValue("");
		onClick$clearBtn();
	}

	@SuppressWarnings("unchecked")
	public void onSelect$shoppingCartListbox(SelectEvent event) {
		Listitem item = (Listitem) new ArrayList(event.getSelectedItems())
				.get(0);
		CartItem cartItem = (CartItem) item.getValue();
		cartItemImage.setSrc(cartItem.getProduct().getImgPath());
	}

	public void addItem(Product prod, int amount) {
		try {
			getShoppingCart(session).add(prod, amount, new AddToCartCallback() {
				public void afterAdd(boolean hasAddNewItem, CartItem item) {
					if (hasAddNewItem) {
						_cartModel.add(item);
					} else {
						int index = _cartModel.indexOf(item);
						_cartModel.set(index, item);
					}
				}
			});
			refresh();
		} catch (OverQuantityException e) {
			throw new WrongValueException(e);
		}
	}

	private static ShoppingCart getShoppingCart(Session session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute("ShoppingCart");
		if (cart == null) {
			session.setAttribute("ShoppingCart", cart = new ShoppingCart());
		}
		return cart;
	}

	public static ShoppingCartCtrl getInstance(Desktop desktop) {
		return (ShoppingCartCtrl) desktop.getAttribute(KEY_SHOPPING_CART);
	}

	public float getCurrentSubTotal() {
		return getShoppingCart(session).getTotalPrice();
	}
}