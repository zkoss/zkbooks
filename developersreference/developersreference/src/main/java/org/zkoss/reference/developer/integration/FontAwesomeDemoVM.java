package org.zkoss.reference.developer.integration;

import java.io.*;
import java.util.*;

public class FontAwesomeDemoVM {

    public static final String FONT_AWESOME_FONT_PATH_JAR = "web/zul/less/font/";
    private List<String> iconCssNames = new LinkedList<>();
    private List<String> brandIconCssNames = new LinkedList<>();
    private List<String> animationNames = new LinkedList<>();

    public FontAwesomeDemoVM() {
        try {
            extractIconNames();
            extractAnimationNames();
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

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(FONT_AWESOME_FONT_PATH_JAR + "_variables.less");
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

    private void extractAnimationNames() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(FONT_AWESOME_FONT_PATH_JAR + "_animated.less");
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".@{fa-css-prefix}-")) {
                    String animName;
                    if (line.endsWith(",")) {
                        animName = line.substring(".@{fa-css-prefix}-".length(), line.indexOf(",")).trim();
                    } else {
                        animName = line.substring(".@{fa-css-prefix}-".length(), line.indexOf(" {")).trim();
                    }
                    if (!animName.contains("@") && !animationNames.contains(iconPrefix + animName)) {
                        animationNames.add(iconPrefix + animName);
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

    public List<String> getAnimationNames() {
        return animationNames;
    }
}