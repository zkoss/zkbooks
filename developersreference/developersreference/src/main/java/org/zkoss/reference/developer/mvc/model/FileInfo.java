package org.zkoss.reference.developer.mvc.model;

public class FileInfo {
    public String path;
    public String description;
    public FileInfo(String path, String description) {
        this.path = path;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
