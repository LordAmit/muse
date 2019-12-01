# Muse
Muse (alternatively µSE) is a mutation-based soundness evaluation framework which systematically evaluates Android static analysis tools to discover, document, and fix, flaws, by leveraging the well-founded practice of mutation analysis. More information about Muse can be found in our [USENIX Security'18 paper](http://www.cs.wm.edu/~rfbonett/pubs/usenix18.pdf) and on our [website](https://muse-security-evaluation.github.io/#overview). 

This repository is a refactoring of the original Muse tool written by Richie Bonnett, done as a part of W&M's Software Engineering course, CSCI 435, year 2019.

## Compilation
The source code of Muse is available in [Code](https://gitlab.com/WM-CSCI435-F18/android-muse/tree/master/code) section of this repository. It is maintained using the open source Eclipse Java IDE. To compile, simply clone this repo, and then import the project as a Java project in Eclipse.

## Pre compiled Binary
Alternatively, you can use the Muse.jar file by downloading it from the releases section. 

## Usage
Muse relies on [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus). You will need the `libs4ast` folder of that project in order to run Muse. 

To run Muse, use the following command, all arguments will be specified in a configuration file you provide. `(arg)` specifies required arguments:
```
java -jar Muse-1.0.0.jar (<ConfigFilePath>)
```

If running Muse within a IDE like Eclipse, import only the Muse folder within the code subdirectory, or else you might get a java.lang.SecurityException error when running Muse.java

### Arguments
Provide the following list of required arguments when running Muse: 
1. ``ConfigFilePath``: This is the path to the config.properties file that Muse uses to read arguments. These arguments defined in the config.properties file include:
- ``libs4ast``:  Path of the lib4ast folder, from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast);
- ``appSrc``: Path of the Android app source code folder, which you want to apply mutation on;
- ``appName``:  Name of the App;
- ``output``: Path of the folder where the mutants will be created;
- `operatorType`: Type of operator to be used while creating mutants. Currently supported arguments are: TAINTSOURCE, TAINTSINK, SCOPESOURCE, REACHABILITY, SCOPESINK, and COMPLEXREACHABILITY.

### Examples
```
java -jar Muse-1.0.0.jar /config.properties
```

The `config.properties` file is defined as:
```
lib4ast = MDroidPlus//libs4ast//
appSrc = //tmp/AppFoo//src//
appName = AppFoo
output = //tmp//mutants//
operatorType = SCOPESINK
```

This will create a folder called `AppFoo` under `/tmp/mutants` where the mutated source files will be stored. 

## Additional Features
To access documentation about the additional features that Muse offered use the link provided below:

- Custom Leak Strings
- Log Analyzer

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
- Scott Murphy
- Kyle Gorham
- Ian Wolff
- Jeff Petit-Freres
- Will Elliot
  
## Past Team Members
- Liz Weech
- Yang Zhang