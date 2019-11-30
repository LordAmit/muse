# Muse - Log Analyzer
The Log Analyzer extension for the Muse tool helps the user remove certain leaks from an application with ease. If the user wants to adjust the leaks in a mutated application, Muse can remove all leaks specified by the user through a universal log format. This feature can be utilized to recheck static analysis tools with a subset of leaks in a mutated application.

# Usage
To run the Log Analyzer Muse extension, the `logAnalyze` keyword should be specified on the command line. The `config.properties` file path should also define all values that Muse will use during execution. `(arg)` specifies required arguments:

```
java -jar Muse-1.0.0.jar logAnalyze (<ConfigFilePath>)
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

To run the Log Analyzer extension, the following values also need to be set:
  - `logPath`: The path to the log file that Muse will use to remove leaks.


### Example
```
java -jar Muse-1.0.0.jar logAnalyze /config.properties
```

The `config.properties` file is defined as:
```
lib4ast = MDroidPlus//libs4ast//
appSrc = //tmp/AppFoo//src//
appName = AppFoo
output = //tmp//mutants//
operatorType = SCOPESINK
logPath = //tempLog.txt
```

This will create a folder called `AppFoo` under `/tmp/mutants` where the mutated source files will be stored.
The `logPath` file should be of the following format:
  ```
leak-0: <ApplicationTest.<init>>
leak-1: ApplicationTest.ApplicationTest
  ``` 
  Any leaks listed in the format leak ID number following by the location of the leak in the mutated app will be read by the Log Analyzer and removed. The leaks for removal are defines in logPath file.

# Cite
If you use Muse for academic purposes, please cite: 

Bonett, R., Kafle, K., Moran, K., Nadkarni, A., & Poshyvanyk, D. (2018, August). Discovering Flaws in Security-Focused Static Analysis Tools for Android using Systematic Mutation. In 27th USENIX Security Symposium (USENIX Security 18). USENIX Association.

# Future Work
Currently Muse is oriented towards the evaluation of static analysis tools for Android data leak detection. We intend to expand Muse by developing additional security operators and mutation schemes for tools with other goals, e.g. SSL verification. 

This repository is for a refactoring and expansion of the original muse tool built by Richie Bonnett.

## Current Team Members
- Amit Seal Ami
- Scott Murphy
- Kyle Gorham
- Ian Wolff
- Jeff Petit-Freres
- Will Elliot

