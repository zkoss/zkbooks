package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.*;

public class FileInfoRenderer implements TreeitemRenderer<DefaultTreeNode<FileInfo>> {
    public void render(Treeitem item, DefaultTreeNode<FileInfo> data, int index) throws Exception {
        FileInfo fi = data.getData();
        Treerow tr = new Treerow();
        item.appendChild(tr);
        tr.appendChild(new Treecell(fi.path));
        tr.appendChild(new Treecell(fi.description));
    }
}
