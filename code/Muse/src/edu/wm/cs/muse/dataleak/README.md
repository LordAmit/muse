# Muse - Custom Leaks

The custom leak feature in Muse allows for the user to define their own leaks for application mutation. Muse will use the custom source, sinks, and/or leak variable declarations when mutating the Android application.

## Usage
To define custom leaks in Muse execution, use the keyword `mutate` on the command line. 

```
java -jar Muse-1.0.0.jar mutate (<ConfigFilePath>)
```

### Arguments
The only arguments on the command line is the path to the `config.properties` file where all runtime values will be defined. To utilize the custom leak strings feature of Muse, the `source`, `sink`, and `varDec` values should be define in the properties file.

1. ``ConfigFilePath``: This is the path to the config.properties file that Muse uses to read arguments. These arguments defined in the config.properties file include:
- ``libs4ast``:  Path of the lib4ast folder, from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast);
- ``appSrc``: Path of the Android app source code folder, which you want to apply mutation on;
- ``appName``:  Name of the App;
- ``output``: Path of the folder where the mutants will be created;
- `operatorType`: Type of operator to be used while creating mutants. Currently supported arguments are: TAINTSOURCE, TAINTSINK, SCOPESOURCE, REACHABILITY, SCOPESINK, and COMPLEXREACHABILITY.

These values are required for using custom leak string in Muse

   - ``source``: Value of the custom source to be used in Muse
   - ``sink``: Value of the custom sink to be used in Muse
   - ``varDec``: Value of the custom variable declaration to be used in Muse


### Example

```
java -jar Muse-1.0.0.jar mutate /config.properties
```

The `config.properties` file is defined as:
```
lib4ast = MDroidPlus/libs4ast/
appSrc = /tmp/AppFoo/src/
appName = AppFoo
output = /tmp/mutants/
operatorType = SCOPESINK
source = sourceString
sink = sinkString
varDec = varDecString
```

This will create a folder called `AppFoo` under `/tmp/mutants/` where the mutated source files will be stored. 

This will also mutate the app using the custom leak strings defined by the user in the properties file.

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