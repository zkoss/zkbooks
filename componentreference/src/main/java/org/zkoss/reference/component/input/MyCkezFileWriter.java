package org.zkoss.reference.component.input;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.zkforge.ckez.CkezFileWriter;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.UiException;

import java.io.File;


/**
 * writing a file to a specified path, out of web context root.
 */
public class MyCkezFileWriter implements CkezFileWriter {
    private File targetFolder;

    public MyCkezFileWriter(String targetFolderPath){
        this.targetFolder = new File(targetFolderPath);
    }
    public String writeFileItem(String uploadUrl, String realPath,
            FileItem item, String type) {

        String fileName = item.getName();
        if (Strings.isEmpty(fileName)) {
            throw new UiException("Empty filename: " + fileName);
        }
        if (!targetFolder.exists()) {
            throw new UiException("Folder not found: " + targetFolder);
        }
        fileName = FilenameUtils.getName(fileName);
        File file = new File(targetFolder, fileName);
        try {
            item.write(file);
        } catch (Exception var8) {
            throw new UiException("Failed to write file item", var8);
        }

        return uploadUrl + "/" + fileName;
    }
}


