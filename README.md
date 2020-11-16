# Muse
Muse (alternatively µSE) is a mutation-based soundness evaluation framework which systematically evaluates Android static analysis tools to discover, document, and fix, flaws, by leveraging the well-founded practice of mutation analysis. More information about Muse can be found in our [USENIX Security'18 paper](http://www.cs.wm.edu/~rfbonett/pubs/usenix18.pdf) and on our [website](https://muse-security-evaluation.github.io/#overview). 

This repository is a refactoring of the original Muse tool written by Richie Bonnett, done as a part of W&M's Software Engineering course, CSCI 435, Fall 2020.

## Compilation
The source code of Muse is available in [Code](https://gitlab.com/WM-CSCI435-F18/android-muse/tree/master/code) section of this repository. It is maintained using the open source Eclipse Java IDE. To compile, simply clone this repo, and then import the project as a Java project in Eclipse.

## Pre compiled Binary
Alternatively, you can use the Muse.jar file by downloading it from the releases section. 

## Usage
Muse relies on [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus). You will need the `libs4ast` folder of that project in order to run Muse. 

You will also need to set your JAVA_HOME environment variable.

To run Muse, use the following command, all arguments will be specified in a configuration file you provide. `(arg)` specifies required arguments:
```
java -jar Muse-1.0.0.jar (<ConfigFilePath>)
```

If running Muse within a IDE like Eclipse, import only the Muse folder within the code subdirectory, or else you might get a java.lang.SecurityException error when running Muse.java. If Muse.java will not run, or displays an error , ensure JRE System Library appears alongside JUnit5 and Maven Dependencies as a library, if not add it with Build Path > Add Libraries > JRE System Library > Environments > JavaSE-12 > Apply and Close > Finish.

### Arguments
Provide the following list of required arguments when running Muse: 
1. ``ConfigFilePath``: This is the path to the config.properties file (not just the directory containg it) that Muse uses to read arguments. These arguments defined in the config.properties file include:
- ``libs4ast``:  Path of the lib4ast folder, from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast);
- ``appSrc``: Path of the Android app source code folder, which you want to apply mutation on;
- ``appName``:  Name of the App;
- ``output``: Path of the folder where the mutants will be created;
- `operatorType`: Type of operator to be used while creating mutants. Currently supported arguments are: TAINTSINK, TAINTSOURCE, REACHABILITY, SCOPESINK, SCOPESOURCE, and COMPLEXREACHABILITY.

### Example
```
java -jar Muse-1.0.0.jar /config.properties
```

The `config.properties` file is defined as:
```
lib4ast: MDroidPlus/libs4ast/
appSrc: /tmp/AppFoo/src/
appName: AppFoo
output: /tmp/mutants/
operatorType: SCOPESINK

//REQUIRED FOR LOGANALYZE
insertionLog: *path*
executionLog: *path*

//REQUIRED FOR CUSTOM LEAKS
//source: *string*
//sink: *string*
//varDec: *string*
```

This will create a folder called `AppFoo` under `/tmp/mutants/` where the mutated source files will be stored. 

## How to run in Eclipse
To run in eclipse click Run Test (dropdown) > Run Configuration > (right click) Java Application > New Configuration. Left click arguments tab, and in Program Arguments give the path to the config.properties file you want to use. A new Run Configuration can be created for each, so you can have multiple config.properties files with different names and parameters. The sample_config.properties file is merely a template for the arguments. It is best to create a new, separate config.properties file in a convenient location. The path must be to the config file itself, not the directory containing it. To run the code, simply run the configuration that was just created. To confirm that the code ran correctly, check the mutated source code (located in the output directory specified in the config file). There should be code injected into the files as expected. Be aware of the additional required parameters for loganalyze and custom leaks.


## How to run in Eclipse as GUI
To run in eclipse as a standalone GUI program: click Run Test (dropdown) > Run Configuration > (right click) Java Application > New Configuration. Left click arguments tab, and in Program Arguments give sole argument: `gui` 

The GUI itself will then provide steps to run µSE itself with an easy file-selection process.


## Additional Features
To access documentation about the additional features that Muse offers please access the Muse wiki to learn more about how to utilize these features.

## Muse Processor Helper Utility
We have created the Muse processor utility using python, which makes it easier to mutate android projects by generating relevant shell script files. To work with it, you need to replace the file paths, and edit the file called `input_folders`. In the file, you need to provide the list of directories containing Android projects. The provided `input_folders` already contains sample values. 


## Cite
If you use Muse for academic purposes, please cite: 

Bonett, R., Kafle, K., Moran, K., Nadkarni, A., & Poshyvanyk, D. (2018, August). Discovering Flaws in Security-Focused Static Analysis Tools for Android using Systematic Mutation. In 27th USENIX Security Symposium (USENIX Security 18). USENIX Association.

## Future Work
Currently Muse is oriented towards the evaluation of static analysis tools for Android data leak detection. We intend to expand Muse by developing additional security operators and mutation schemes for tools with other goals, e.g. SSL verification. 

This repository is for a refactoring and expansion of the original muse tool built by Richie Bonnett.

## Current Team Members
- Amit Seal Ami
- Phillip Watkins
- Kevin Cortright
- Nicholas di Mauro
- Michael Foster
- Pablo Solano
- John Clapham
  
## Past Team Members
- Scott Murphy
- Kyle Gorham
- Ian Wolff
- Jeff Petit-Freres
- Will Elliot


- Liz Weech
- Yang Zhang
