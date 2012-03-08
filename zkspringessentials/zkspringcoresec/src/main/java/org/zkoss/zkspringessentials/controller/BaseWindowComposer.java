/**
 * 
 */
package org.zkoss.zkspringessentials.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.ComposerExt;

/**
 * @author ashish
 *
 */
public class BaseWindowComposer  implements Composer, ComposerExt {

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.ComposerExt#doBeforeCompose(org.zkoss.zk.ui.Page, org.zkoss.zk.ui.Component, org.zkoss.zk.ui.metainfo.ComponentInfo)
	 */
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) throws Exception {
		// TODO Auto-generated method stub
		return compInfo;
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.ComposerExt#doBeforeComposeChildren(org.zkoss.zk.ui.Component)
	 */
	public void doBeforeComposeChildren(Component comp) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.ComposerExt#doCatch(java.lang.Throwable)
	 */
	public boolean doCatch(Throwable ex) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.ComposerExt#doFinally()
	 */
	public void doFinally() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.Composer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub

	}

}
