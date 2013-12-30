package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.ui.AuxAction;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.UserActionManager;
import org.zkoss.zss.ui.impl.DefaultUserActionManagerCtrl;

/**
 * This class demonstrate usage of "custom user action handler".
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class CustomHandlerComposer extends SelectorComposer<Component> {
	
	@Wire
	private Spreadsheet ss;

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//initialize custom handlers
		UserActionManager actionManager = ss.getUserActionManager();
		actionManager.registerHandler(
				DefaultUserActionManagerCtrl.Category.AUXACTION.getName(),
				AuxAction.NEW_BOOK.getAction(), new NewBookHandler());
		actionManager.registerHandler(
				DefaultUserActionManagerCtrl.Category.AUXACTION.getName(),
				AuxAction.SAVE_BOOK.getAction(), new SaveBookHandler());
	}
}



