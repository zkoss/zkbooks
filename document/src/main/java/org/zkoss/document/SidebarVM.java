package org.zkoss.document;

import javax.servlet.ServletRequest;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Executions;

public class SidebarVM {

	private boolean floatingMenuVisible = true;
	private boolean isMobile = false;
	private String orientation;
	private String windowMode = "embedded";
	private String menuAreaWidth= "200px";
	private int selectedIndex = 0;

	@AfterCompose
	public void init(){
		// Detect if client is mobile device (such as Android or iOS devices)
		isMobile = Executions.getCurrent().getBrowser("mobile") !=null;
		if (isMobile){
			toFloatingMenu();
		}
	}

	@Command
	@NotifyChange("floatingMenuVisible")
	public void toggleMenu(){
		floatingMenuVisible = !floatingMenuVisible;
	}

	@Command
	@NotifyChange({"windowMode","menuAreaWidth","floatingMenuVisible"})
	public void updateClientInfo(@BindingParam("orientation") String orientation, @BindingParam("width")int width ){
		if (isMobile){
			if (!orientation.equals(this.orientation)){
				this.orientation = orientation;
				if (orientation.equals("protrait") || width < 800){
					toFloatingMenu();
				}else{
					toFixedMenu();
				}
			}
		}
	}
	
	private void toFloatingMenu(){
		floatingMenuVisible = false;
		windowMode = "overlapped";
		menuAreaWidth = "0px";
	}
	
	private void toFixedMenu(){
		floatingMenuVisible = true;
		windowMode = "embedded";
		menuAreaWidth = "200px";
	}
	
	@Command @NotifyChange("selectedIndex")
	public void select(@BindingParam("index") int index){
		this.selectedIndex = index;
	}
	public boolean isFloatingMenuVisible() {
		return floatingMenuVisible;
	}

	public void setFloatingMenuVisible(boolean floatingMenuVisible) {
		this.floatingMenuVisible = floatingMenuVisible;
	}

	public boolean isMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	public String getWindowMode() {
		return windowMode;
	}

	public void setWindowMode(String windowMode) {
		this.windowMode = windowMode;
	}

	public String getMenuAreaWidth() {
		return menuAreaWidth;
	}

	public void setMenuAreaWidth(String menuAreaWidth) {
		this.menuAreaWidth = menuAreaWidth;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	

}
