package demo.web.ui.ctrl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import demo.web.UserCredentialManager;

/**
 * @author zkessentials store
 * 
 *         This is the controller for the login view as referenced in index.zul
 * 
 */
public class LoginViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 5730426085235946339L;

	private Textbox nameTxb;
	private Textbox passwordTxb;
	private Label mesgLbl;

	Window win;
	Button btn;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (UserCredentialManager.getIntance(session).isAuthenticated()) {
			execution.sendRedirect("index.zul");
		}
		nameTxb.setFocus(true);

	}

	public void onOK() {
		doLogin();
	}

	public void onCancel() {
		nameTxb.setValue("");
		passwordTxb.setValue("");
		nameTxb.setFocus(true);
	}

	public void onClick$confirmBtn() {
		doLogin();
	}

	private void doLogin() {
		UserCredentialManager mgmt = UserCredentialManager.getIntance(Sessions
				.getCurrent());
		mgmt.login(nameTxb.getValue(), passwordTxb.getValue());
		if (mgmt.isAuthenticated()) {
			execution.sendRedirect("index.zul");
		} else {
			mesgLbl.setValue("Your User Name or Password is invalid!");
		}
	}

}
