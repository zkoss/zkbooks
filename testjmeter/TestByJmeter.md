# Preparation
apply [static ID generator](https://www.zkoss.org/wiki/ZK_Developer%27s_Reference/Testing/Testing_Tips#Approach_2:_Use_ID_Generator)

# ZK JMeter Plugin
* help you to perform stress tests on your ZK applications with JMeter easily
* automating the process of replacing parameter values with user variables 
* provides a JMX Visualizer which can help users to monitor server memory usage during the test

## Installation
1. Download the plugin from [Github](https://github.com/zkoss/zkjmeterplugin)
2. put the plugin jar file in the folder: `[JMETER_HOME]\lib\ext`

# Performance Testing

## Create a Test Plan

1. Create a Thread Group in Test Group.
2. Add HTTP Cookie Manager in Thread Group
3. Add HTTP Request Defaults in Thread Group.
4. Add Transaction Controller in Thread Group.
5. Edit HTTP Request Defaults, set the Server Name and Port Number. 
6. Create a ZK HTTP Proxy Server (Add > Non-Test Elements) in WorkBench, and edit it as follows 
7. Set Target Controller to Test Plan > Thread Group > Transaction Controller 
8. Start the ZK HTTP Proxy Server