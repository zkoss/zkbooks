package org.zkoss.reference.developer.uipattern;

import org.slf4j.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Map;

public class ErrorHandlingComposer extends SelectorComposer<Component> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingComposer.class);

    @WireVariable
    private Map<String, Object> requestScope;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        // 2 ways to get the error
        //via execution.getAttribute()
        Execution execution = Executions.getCurrent();
        Exception ex1 = (Exception) execution.getAttribute("javax.servlet.error.exception");

        //via requestScope map
        Exception ex2 = (Exception) requestScope.get("javax.servlet.error.exception");

        logger.error("", ex1);
        //log source zul, so that when you see the error in the log, you can locate the page
        logger.error("from " + getPage().getRequestPath());
    }
}