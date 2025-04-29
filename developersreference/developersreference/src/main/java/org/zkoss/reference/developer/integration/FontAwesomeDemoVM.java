package org.zkoss.reference.developer.integration;

import java.io.*;
import java.util.*;

public class FontAwesomeDemoVM {

    private List<String> iconCssNames = new LinkedList<>();
    private List<String> brandIconCssNames = new LinkedList<>();

    public FontAwesomeDemoVM() {
        try {
            extractIconNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String iconPrefix = "z-icon-";
    /**
     * Extracts icon names from the font-awesome.txt file
     */
private void extractIconNames() throws IOException {
    boolean isBrandSection = false;
    boolean insideSection = false;

    try (InputStream input = getClass().getClassLoader().getResourceAsStream("web/zul/less/font/_variables.less");
         BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String trimmedLine = line.trim();

            if (trimmedLine.equals(".fa-icons() {")) {
                isBrandSection = false;
                insideSection = true;
                continue;
            } else if (trimmedLine.equals(".fa-brand-icons() {")) {
                isBrandSection = true;
                insideSection = true;
                continue;
            } else if (trimmedLine.equals("}")) {
                insideSection = false;
                continue;
            }

            if (insideSection && line.contains(":")) {
                String iconName = line.substring(0, line.indexOf(":")).trim();
                if (!iconName.isEmpty()) {
                    if (isBrandSection) {
                        brandIconCssNames.add(iconPrefix + iconName);
                    } else {
                        iconCssNames.add(iconPrefix + iconName);
                    }
                }
            }
        }
    }
}

    public List<String> getIconCssNames() {
        return iconCssNames;
    }

    public List<String> getBrandIconCssNames() {
        return brandIconCssNames;
    }
}