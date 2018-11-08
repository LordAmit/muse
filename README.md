# Muse
Muse (alternatively µSE) is a mutation-based soundness evaluation framework which systematically evaluates Android static analysis tools to discover, document, and fix, flaws, by leveraging the well-founded practice of mutation analysis. More information about Muse can be found in our [USENIX Security'18 paper](http://www.cs.wm.edu/~rfbonett/pubs/usenix18.pdf) and on our [website](https://muse-security-evaluation.github.io/#overview). 

This repository is a refactoring of the original Muse tool written by Richie Bonnett, done as a part of W&M's Software Engineering course, CSCI 435.

# Compilation
Muse can be downloaded from this git repository, with a Maven Eclipse project found in the code directory. After downloading, navigate into the code subdirectory. Then Muse can be compiled with the following commands:
```
mvn clean
mvn package
```
The generated runnable jar can be found in: ``target/Muse-1.0.0.jar``

# Usage
Muse relies on [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus). You will need the libs4ast folder of that project in order to run Muse. 

To run Muse, use the following command, specifying the required arguments:
```
java -jar Muse-1.0.0.jar <libs4ast> <AppSourceCode> <AppName> <OutputPath>
```

If running Muse within a IDE like Eclipse, import only the Muse folder within the code subdirectory, or else you might get a java.lang.SecurityException error when running Muse.java

### Arguments
Provide the following list of required arguments when running Muse: 
1. ``libs4ast``:  path of the lib4ast folder, inherited from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast);
2. ``AppSourceCode``: path of the Android app source code folder;
3. ``AppName``: App main package name;
4. ``Output``: path of the folder where the mutants will be created;

### Example
```
java -jar Muse-1.0.0.jar MDroidPlus/libs4ast/ /tmp/AppFoo/src/ AppFoo /tmp/mutants/
```

### Output
The output directory will contain a folder with the source code for each generated mutant. 

# Cite
If you use Muse for academic purposes, please cite: 

Bonett, R., Kafle, K., Moran, K., Nadkarni, A., & Poshyvanyk, D. (2018, August). Discovering Flaws in Security-Focused Static Analysis Tools for Android using Systematic Mutation. In 27th USENIX Security Symposium (USENIX Security 18). USENIX Association.

# Future Work
Currently Muse is oriented towards the evaluation of static analysis tools for Android data leak detection. We intend to expand Muse by developing additional security operators and mutation schemes for tools with other goals, e.g. SSL verification. 

This repository is for a refactoring and expansion of the original muse tool built by Richie Bonnett.

## Team Members
- Amit Seal Ami
- Liz Weech
- Yang Zhang
