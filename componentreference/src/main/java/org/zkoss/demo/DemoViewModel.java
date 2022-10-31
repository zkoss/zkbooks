package org.zkoss.demo;

import org.slf4j.*;
import org.zkoss.bind.annotation.*;
import org.zkoss.xml.XMLs;
import org.zkoss.zk.ui.*;

import java.nio.file.*;

public class DemoViewModel {
    private static final Logger logger = LoggerFactory.getLogger(DemoViewModel.class);
    private boolean codeVisible;
    static final String KEY_PATH = "path";

    public DemoViewModel() {
        loadZulContent();
    }

    private void loadZulContent(){
        //relative path to web context
        String path = Executions.getCurrent().getParameter(KEY_PATH);
        if (path == null){
            logger.error("a path for demo.zul is required");
            return;
        }
        try {
            String zulContent = readFileAsString(WebApps.getCurrent().getRealPath(path));
            zulContent = XMLs.escapeXML(zulContent);
            Executions.getCurrent().getDesktop().setAttribute("zulContent", zulContent);
        } catch (Exception e) {
            logger.error("demo.zul", e);
            throw new RuntimeException(e);
        }
    }

    public static String readFileAsString(String fileName)throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @Command @NotifyChange("codeVisible")
    public void toggleCodeVisibility(){
        codeVisible = !codeVisible;
    }

    public boolean isCodeVisible() {
        return codeVisible;
    }

    public void setCodeVisible(boolean codeVisible) {
        this.codeVisible = codeVisible;
    }
}
