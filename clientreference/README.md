# ZK Client Reference Example Project
This project contains example source code in [ZK Client-side Reference](https://www.zkoss.org/wiki/ZK_Client-side_Reference)

# How to run
run it with maven command:
`mvn jetty:run`

Then visit http://localhost:8080/reference.client with your browser.

# Widget Customization
See examples and configurations under `widgetCustomization`.

## Setup Steps
### 1. Install TypeScript compiler and [zk-types](https://www.npmjs.com/package/zk-types), TypeScript Declarations for the ZK Framework widgets.
`npm i -D typescript zk-types @types/jquery`

(`-D`: It indicates that the packages being installed are development dependencies. )
It will download and install the latest versions of typescript and zk-types packages into the `node_modules` directory.

### 2. Setup [tsconfig.json](widgetCustomization/tsconfig.json) 


## Compile

### compile all files
`npx tsc`

### compile single file
specify the file to compile in `include` in `tsconfig.json` first then run compile command