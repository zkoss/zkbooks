package org.zkoss.reference.component.data.tree;

public class PackageData {
	private final String path;
	private final String description;
	private final String size;

	public PackageData(String path, String description) {
		this.path = path;
		this.description = description;
		this.size = null;
	}

	public PackageData(String path, String description, String size) {
		this.path = path;
		this.description = description;
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public String getDescription() {
		return description;
	}

	public String getSize() {
		return size;
	}
}
