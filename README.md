# Overview
Runnable codes speak louder than static code snippets.

This repository contains example source code used in ZK documentations including:
* [ZK Developer's Reference](http://books.zkoss.org/wiki/ZK_Developer%27s_Reference)
* [ZK Component Reference](http://books.zkoss.org/wiki/ZK_Component_Reference)
* [ZK MVVM Reference](http://books.zkoss.org/zk-mvvm-book/9.5/index.html)
* [ZK Client-side Reference](http://books.zkoss.org/wiki/ZK_Client-side_Reference)


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


