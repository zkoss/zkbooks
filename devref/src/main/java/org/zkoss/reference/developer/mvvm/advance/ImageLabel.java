package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class ImageLabel extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7548714946540407501L;
	@Wire
	private Groupbox item;
//	@Wire
//	private Image labelImage;
	@Wire
	private Label titleLabel;
//	private Label descLabel;

	public ImageLabel() {
		Executions.createComponents("/advance/ImageLabel.zul", this, null);
		Selectors.wireComponents(this, this, false);
		Selectors.wireEventListeners(this, this);
//		labelImage.setWidth("20px");
//		labelImage.setHeight("20px");
	}

//	public String getImagePath() {
//		return labelImage.getSrc();
//	}

//	public void setImagePath(String imgPath) {
//		this.labelImage.setSrc(imgPath);
//	}

	public String getTitle() {
		return titleLabel.getValue();
	}

	public void setTitle(String title) {
		this.titleLabel.setValue(title);
	}

//	public String getDescription() {
//		return descLabel.getValue();
//	}
//
//	public void setDescription(String description) {
//		this.descLabel.setValue(description);
//	}

	InplaceEditor itemInplaceEditor;

	@Listen("onClick = #titleLabel")
	public void titleEdit() {
		if (itemInplaceEditor == null) {
			itemInplaceEditor = new InplaceEditor();
			itemInplaceEditor.setParent(item);
		} else {
			itemInplaceEditor.cancelEdit();
		}
	}

	// Editable Area after click the description
	public class InplaceEditor extends Div implements IdSpace {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8967484785417958374L;
		@Wire
		private Textbox editTitle, editDesc;
		

		public InplaceEditor() {
			Executions.createComponents("/advance/InplaceEditor.zul", this, null);
			Selectors.wireComponents(this, this, false);
			Selectors.wireEventListeners(this, this);
			editTitle.setValue(getTitle());
//			editDesc.setValue(getDescription());
		}

		@Listen("onClick = #submitBtn")
		public void submitTitleUpdate() {
			setTitle(editTitle.getValue());
//			setDescription(editDesc.getValue());
			Events.postEvent(new LabelEditEvent());
			cancelEdit();
		}

		@Listen("onClick = #cancelBtn")
		public void cancelEdit() {
			this.detach();
			itemInplaceEditor = null;
		}
	}

	// Customize Event
	public static final String ON_LABEL_EDIT = "onLabelEdit";

	// Editable Event which contains the content of title and description
	public class LabelEditEvent extends Event {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2338807610333176243L;

		public LabelEditEvent() {
			super(ON_LABEL_EDIT, ImageLabel.this);
		}

		public String getCurrentTitle() {
			return getTitle();
		}

//		public String getCurrentDesc() {
//			return getDescription();
//		}
	}
}
