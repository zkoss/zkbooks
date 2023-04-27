package org.zkoss.reference.developer.testing;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.impl.*;
import org.zkoss.zk.ui.metainfo.*;

/**
 * Generate an UUID upon a component's ID first if exists. If not, then fall back to {@link StaticIdGenerator}.
 * With this generator, you should specify id on those components you need to verify their state during testing.
 * Multiple components with same ID under multiple space owners causes UUID collision. In such case, you need to specify
 * those ID space owners with ID.<br/>
 * Benefit:<br/>
 * Unlike {@link StaticIdGenerator}, component creation order can't affect id generation.<br/>
 * @author hawk
 */
public class ComponentIdFirstGenerator extends StaticIdGenerator {

	@Override
	public String nextComponentUuid(Desktop desktop, Component comp,
                                    ComponentInfo compInfo) {
		String id = comp.getId();
		if (id.isEmpty()){
			id = getIdFromComponentInfo(comp, compInfo);
		}
		if (id.isEmpty()){
			return super.nextComponentUuid(desktop, comp, compInfo);
		}else{

			return getSpaceOwnerId(comp) + id;
		}
	}

	protected String getIdFromComponentInfo(Component comp, ComponentInfo compInfo) {
		String id = "";

		if (compInfo != null && compInfo.getProperties() != null){
			for (Property p :compInfo.getProperties()){
				if ("id".equals(p.getName())){
					id = p.getValue(comp).toString();
					break;
				}
			}
		}
		return id;
	}

	protected String getSpaceOwnerId(Component comp){
		String id = "";
		if (comp.getSpaceOwner() instanceof  Component){
			Component spaceOwner = (Component) comp.getSpaceOwner();
			id = spaceOwner.getId();
		}
		return id;
	}

	@Override
	public String nextPageUuid(Page page) {
		return super.nextPageUuid(page);
	}

	@Override
	public String nextDesktopId(Desktop desktop) {
		return super.nextDesktopId(desktop);
	}
}
