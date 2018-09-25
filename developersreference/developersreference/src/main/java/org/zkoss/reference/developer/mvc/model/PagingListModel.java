package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.AbstractListModel;

import java.util.List;

/**
 * The model only contains the specified page size data at any moment instead of the complete data from the data source.
 * @author Hawk
 *
 */
public class PagingListModel<T> extends AbstractListModel<T> {

	private static final long serialVersionUID = 1015524612541054426L;

	private final int pageSize;
	private int totalSize; //the actual number of the data, not the cached page size
	private List<T> cachedPage;
	private int currentPageIndex;
	private PagingDataProvider<T> dataProvider;

	public PagingListModel(int pageSize, PagingDataProvider<T> dataProvider) {
		if (pageSize <= 0)
			throw new IllegalArgumentException("page size should be positive integer");
		this.pageSize = pageSize;
		totalSize = dataProvider.getTotalSize();
		cachedPage = dataProvider.getData(this.pageSize, 0);
		this.dataProvider = dataProvider;
	}

	public T getElementAt(int index) {
		int offset = index % pageSize; //offset index in a page
		int targetPageIndex = index / pageSize;

		if (currentPageIndex != targetPageIndex){
			System.out.println(">>> page fault, fetching from DB page index:" + targetPageIndex);
			cachedPage = dataProvider.getData(pageSize, targetPageIndex);
			currentPageIndex = targetPageIndex;
		}
		System.out.println(" getElementAt:" + index);
		return cachedPage.get(offset);
	}

	public int getSize() {
		return totalSize;
	}

	/**
	 * an interface to abstract the way to get data. Not to depend on specific entity type or implementation.
	 * @param <T>
	 */
	interface PagingDataProvider<T>{
		List<T> getData(int pageSize, int targetPageIndex);
		int getTotalSize();
	}
}
