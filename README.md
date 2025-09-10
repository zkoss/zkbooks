# Overview
Runnable codes speak louder than static code snippets.

This repository contains example source code used in ZK documentations including:
* [ZK Developer's Reference](http://books.zkoss.org/wiki/ZK_Developer%27s_Reference)
* [ZK Component Reference](http://books.zkoss.org/wiki/ZK_Component_Reference)
* [ZK MVVM Reference](http://books.zkoss.org/zk-mvvm-book/9.5/index.html)
* [ZK Client-side Reference](http://books.zkoss.org/wiki/ZK_Client-side_Reference)
* [ZK Style Customization Guide](https://books.zkoss.org/wiki/ZK_Style_Customization_Guide)

# Directory Structure

- `componentreference/` — Example code for ZK Component Reference documentation
- `developersreference/developersreference/` — Example code for ZK Developer's Reference
- `developersreference/integration.cdi/` - Example code for ZK Developer's Reference / Integration / CDI
- `developersreference/integration.ejb/` - Example code for ZK Developer's Reference / Integration / EJB
- `developersreference/integration.hibernate/` - Example code for ZK Developer's Reference / Integration / Hibernate
- `developersreference/integration.jpa/` - Example code for ZK Developer's Reference / Integration / JPA
- `developersreference/integration.spring/` - Example code for ZK Developer's Reference / Integration / Spring
- `developersreference/integration.spring.security/` - Example code for ZK Developer's Reference / Integration / Spring Security
- `mvvmreference/` — Example code for ZK MVVM Reference.
- `clientreference/` — Example code for ZK Client-side Reference.
- `mymodule/` — Example code for a reusable module
- `styleguide/` — Example code for ZK Style Customization Guide
- `csp-filter/` — Example code for ZK Developer's Reference / Security Tips / Content Security Policy / Enhancing Security with Strict-Dynamic


# How to Run a Project
Each project has a Maven jetty plugin configured, just run the goal below:

`mvn jetty:run`

* Notice: Each project is a Maven project, so you need to install [Maven](https://maven.apache.org/) first to run.


# Naming Conventions
## Chapter Name as Folder Name
Folder is named by a chapter or a subsection.
*  chapter name - [`eventHandling`](https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/Event%20Handling)
* subsection name - [`mvc/controller`](https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/MVC/Controller)
## Page Name as File Name
 a zul file is named by a page
 e.g. , `eventQueue.zul` is for [Use Event Queues](https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/UI%20Patterns/Long%20Operations/Use%20Event%20Queues)


## Branches
master - for the latest ZK

# Project Tags

- `componentreference/`: components, UI, ZK components, essential components (Button, Label, Image, Menu, Toolbar, Popup, Timer), input components (Textbox, Combobox, Datebox, Bandbox, Spinner, Checkbox, Radiogroup, Colorbox, Intbox, Doublebox, Decimalbox, Timebox, Calendar, Signature, CKEditor), container components (Window, Panel, Groupbox, Tabbox, Drawer, Fragment, Inputgroup), layout components (Hlayout, Vlayout, Hbox, Vbox, Box, Borderlayout, Portallayout, Cardlayout, Columnlayout, Rowlayout, Splitter, Linelayout, Organigram, Goldenlayout), event handling (event listeners, afterSize, visibility change, chatroom), multimedia components (Audio, Video, Camera, Cropper, Barcode, Barcodescanner, PDFViewer), diagrams and charts (Gantt, Bar Chart, Pie Chart, Google Map, Advanced Marker), supplementary components (Stepbar, Coachmark, Paging, Auxhead, Cell, Frozen), base components (Div, Span, XulElement, InputElement), mobile support, PWA (Progressive Web App), image and file upload/download, customization (custom renderer, custom template, custom JS), ZK component features, UI patterns
- `developersreference/`: developer guide, backend, architecture, 
  - mvc, model-view-controller, controller, view, model
  - integration, CDI, EJB, Hibernate, JPA, Spring, JDBC, datasource, font-awesome
  - security, CSP, content security policy, XSS, HTML escape, widget accessibility
  - performance, ROD (Render on Demand), cache, stubonly, client performance, optimization
  - event handling, event queue, event listening, desktop events, chat events
  - utilities, browser detection, logging
  - testing, test tips
  - debugging, error message, desktop debugging
  - theming, theme preview, theme switch
  - accessibility, label-input, accessible widgets
  - internationalization, i18n, locale, labels, messages, timezone
  - responsive design, mobile, adaptive UI
  - server push, async update
  - advanced, module, META-INF
- `mvvmreference/`: MVVM pattern, data binding, form binding, collection binding, command binding, global command binding, validator, converter, template injection, shadow component, command parameter, annotation, view model, dynamic binding, View Model
- `clientreference/`: client-side, JavaScript, client API


