package org.zkoss.reference.component.data.tree;

public class PackageDataUtil {
    private static DirectoryTreeNode<PackageData> root;

    static {
        root = new DirectoryTreeNode<PackageData>(null,
                new DirectoryTreeNodeCollection<PackageData>() {
                    private static final long serialVersionUID = 9019022379404376015L;

                    {
                        add(new DirectoryTreeNode<PackageData>(new PackageData(
                                "/doc", "Release Notes and License")));
                        add(new DirectoryTreeNode<PackageData>(new PackageData(
                                "/dist", null),
                                new DirectoryTreeNodeCollection<PackageData>() {
                                    private static final long serialVersionUID = 3541713473898615987L;

                                    {
                                        add(new DirectoryTreeNode<PackageData>(
                                                new PackageData("/lib", null),
                                                new DirectoryTreeNodeCollection<PackageData>() {
                                                    private static final long serialVersionUID = 7225750712385675090L;

                                                    {
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "/zkforge",
                                                                        null),
                                                                new DirectoryTreeNodeCollection<PackageData>() {
                                                                    private static final long serialVersionUID = 1204356757289701541L;

                                                                    {
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "ckez.jar",
                                                                                        "CKeditor",
                                                                                        "1709KB")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "timelinez.jar",
                                                                                        "Timeline",
                                                                                        "283KB")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "timeplotz.jar",
                                                                                        "Timeplot",
                                                                                        "112KB")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "gmapsz.jar",
                                                                                        "Google Maps",
                                                                                        "95KB")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "zuljsp.jar",
                                                                                        "JSP",
                                                                                        "129KB")));
                                                                    }
                                                                }

                                                                , true));

                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "/ext",
                                                                        null),
                                                                new DirectoryTreeNodeCollection<PackageData>() {
                                                                    private static final long serialVersionUID = 4142744663101866804L;

                                                                    {
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "commons-fileupload.jar",
                                                                                        "Upload Features")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "commons-io.jar",
                                                                                        "Upload Features")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "jcommon.jar",
                                                                                        "Chart Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "jfreechar.jar",
                                                                                        "Chart Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "jasperreports.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "itext.jarjxl.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "poi.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "commons-collections.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "commons-logging.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "commons-digester.jar",
                                                                                        "Jasperreport related Component")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "bsh.jar",
                                                                                        "Scripting in Java interpreter for zscript (BeanShell)")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "js.jar",
                                                                                        "Scripting in JavaScript (Rhino)")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "groovy.jar",
                                                                                        "Scripting in Groovy")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "jruby.jar",
                                                                                        "Scripting in Ruby (JRuby)")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "jython.jar",
                                                                                        "Scripting in Python (Jython)")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "Filters.jar",
                                                                                        "Captcha Component.")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "mvel.jar",
                                                                                        "Evaluate the expressions (MVEL)")));
                                                                        add(new DirectoryTreeNode<PackageData>(
                                                                                new PackageData(
                                                                                        "ognl.jar",
                                                                                        "Evaluate the expressions (OGNL)")));
                                                                    }
                                                                }));

                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zcommon.jar",
                                                                        "ZK Core Jar File",
                                                                        "413KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zcommon-el.jar",
                                                                        "ZK Core Jar File",
                                                                        "100KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zel.jar",
                                                                        "ZK Core Jar File",
                                                                        "151KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zhtml.jar",
                                                                        "ZK Core Jar File",
                                                                        "57KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zml",
                                                                        "ZK Core Jar File",
                                                                        "57KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zk.jar",
                                                                        "ZK Core Jar File",
                                                                        "1056KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zkbind.jar",
                                                                        "ZK Core Jar File",
                                                                        "270KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zkplus.jar",
                                                                        "ZK Core Jar File",
                                                                        "122KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zul.jar",
                                                                        "ZK Core Jar File",
                                                                        "1311KB")));
                                                        add(new DirectoryTreeNode<PackageData>(
                                                                new PackageData(
                                                                        "zweb.jar",
                                                                        "ZK Core Jar File",
                                                                        "196KB")));

                                                    }
                                                }

                                        ));

                                        add(new DirectoryTreeNode<PackageData>(
                                                new PackageData("/src",
                                                        "Jar Format Source Code")));
                                        add(new DirectoryTreeNode<PackageData>(
                                                new PackageData("/xsd",
                                                        "XSD files for Development")));
                                        add(new DirectoryTreeNode<PackageData>(
                                                new PackageData("/WEB-INF",
                                                        "Configuration Files")));
                                    }
                                }

                        ));

                    }
                }, true); // dist opened
    }

    public static DirectoryTreeNode<PackageData> getRoot() {
        return root;
    }
}
