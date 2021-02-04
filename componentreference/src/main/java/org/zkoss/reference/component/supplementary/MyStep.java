package org.zkoss.reference.component.supplementary;

/**
 * This class represents the business data related to one step. You usually render it on a {@link org.zkoss.zkmax.zul.Step}
 */
public class MyStep {
    private String title;

    public MyStep(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
