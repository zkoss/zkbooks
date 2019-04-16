package org.zkoss.reference.developer.testing;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.impl.*;
import org.zkoss.zk.ui.metainfo.*;
import org.zkoss.zk.ui.sys.IdGenerator;

/**
 * Generate an UUID upon a component's ID first if exists. If not, then use static ID generator.
 * With this generator, you should specify id on those components you need to verify their state during testing.<br/>
 * Benefit:<br/>
 * Unlike {@link StaticIdGenerator}, component creation order can't affect this generator.<br/>
 * Limitation: <br/>
 * It can't handle same ID under the different space owner.
 * @author hawk
 */
public class ComponentIdFirstGenerator implements IdGenerator {

	static StaticIdGenerator staticIdGenerator = new StaticIdGenerator();
	@Override
	public String nextComponentUuid(Desktop desktop, Component comp,
                                    ComponentInfo compInfo) {
		String id = comp.getId();
		if (id.isEmpty()){
			id = getIdFromComponentInfo(comp, compInfo);
		}
		if (id.isEmpty()){
			return staticIdGenerator.nextComponentUuid(desktop, comp, compInfo);
		}else{
			return id;
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
		return staticIdGenerator.nextPageUuid(page);
	}

	@Override
	public String nextDesktopId(Desktop desktop) {
		return staticIdGenerator.nextDesktopId(desktop);
	}

}
