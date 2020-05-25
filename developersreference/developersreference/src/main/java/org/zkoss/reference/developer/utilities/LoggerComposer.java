package org.zkoss.reference.developer.utilities;

import org.slf4j.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

public class LoggerComposer extends SelectorComposer {
    Logger logger = LoggerFactory.getLogger(LoggerComposer.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        logger.debug("this is debug message");
        logger.info("this is info message", comp);
    }
}
