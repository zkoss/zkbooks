package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.List;

/**
 * 
 * @author Hawk Chen
 * 
 */
public class PartialLoadingComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -7481683315099616293L;

	@Wire
	private Listbox personListbox;
	private PagingListModel<Person> model;
	private PersonDAO dao = new PersonDAO();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		personListbox.setItemRenderer(new ListitemRenderer<Person>() {
			public void render(Listitem lItem, Person person, int index) throws Exception {
				new Listcell(person.getFirstName()).setParent(lItem);
				new Listcell(person.getLastName()).setParent(lItem);
				new Listcell(person.isMarried() ? "O" : "X").setParent(lItem);
			}
		});
		PagingListModel.PagingDataProvider<Person> personsProvider = new PagingListModel.PagingDataProvider<Person>() {
			
			@Override
			public int getTotalSize() {
				return dao.countAll();
			}
			
			@Override
			public List<Person> getData(int pageSize, int targetPageIndex) {
				return dao.getPersons(pageSize, targetPageIndex);
			}
		};
		
		if (personListbox.getMold().equals("paging")){
			model = new PagingListModel<Person>(personListbox.getPageSize(), personsProvider);
		}else{
			model = new PagingListModel<Person>(50, personsProvider);
		}
		personListbox.setModel(model);
	}

}
