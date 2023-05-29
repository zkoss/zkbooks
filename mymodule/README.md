# A Reusable Module Example

This project serves as an example to demonstrate the modularization of a ZK project. It showcases the ability to create a reusable module by encapsulating various stuffs, including 
* ZK custom components
* ZUL pages
* static resources

## Benefits

By utilizing this reusable module approach, you can reap the following benefits:

1. **Reusable feature**: The module can be shared among multiple projects, allowing for code reuse and reducing duplication efforts.
2. **Parallel development**: The module can be developed separately from the main projects, enabling parallel development workflows. This fosters better collaboration and faster iteration cycles.

Feel free to explore this project to gain insights into how to effectively modularize your ZK projects and leverage the advantages of reusable modules.

# How to use in a zul
Include this project as a dependency.
* including a zul page or static resources:
`~.mymodule.zul/` (see [Classpath Web Resource Path](https://www.zkoss.org/wiki/ZK_Developer%27s_Reference/UI_Composing/ZUML/Include_a_Page#Classpath_Web_Resource_Path))
* use custom components: `<username>`directly in a zul without any declaration, since it's declared in `lang-addon.xml`. (see [Language Addon](https://www.zkoss.org/wiki/ZK_Client-side_Reference/Language_Definition#Language_Addon))


# Deploy as Shared Library
You can put this jar in a shared library folder when deploying, separately with your WAR, e.g. put it in Tomcat's `lib` folder.