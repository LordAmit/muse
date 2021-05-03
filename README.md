# Muse

## Update Notice

Muse repository description is currently going through update. There are two branches that require attention in terms of latest development. In chronologically ascending order, these are:

- [master](https://github.com/LordAmit/muse/tree/master) Contains the Muse version used for the [TOPS paper](https://dl.acm.org/doi/10.1145/3439802)
- [435Ext20](https://github.com/LordAmit/muse/tree/435Ext20) Contains the Muse version used for the [ICSE'21 Demonstrations paper](https://conf.researchr.org/details/icse-2021/icse-2021-Demonstrations/14/Demo-Mutation-based-Evaluation-of-Security-focused-Static-Analysis-Tools-for-Android). This is built on top of `master` branch, so it contains all the improvements `master` from there.

The website is also being updated accordingly.

---

Muse (alternatively µSE) is a mutation-based soundness evaluation framework which systematically evaluates Android static analysis tools to discover, document, and fix, flaws, by leveraging the well-founded practice of mutation analysis.

More information about Muse can be found in our [TOPS'21 paper](https://arxiv.org/pdf/2102.06829.pdf) and on our [website](https://muse-security-evaluation.github.io/#overview).

The original Muse tool was written by Richie Bonnett, done as a part of W&M's Software Engineering course, CSCI 435, year 2018, details of which can be found in [USENIX Security'18 paper](http://www.cs.wm.edu/~rfbonett/pubs/usenix18.pdf).

## Compilation

The source code of Muse is available in [Code](https://gitlab.com/WM-CSCI435-F18/android-muse/tree/master/code) section of this repository. It is maintained using the open source Eclipse Java IDE. To compile, simply clone this repo, and then import the project as a Java project in Eclipse.

## Pre compiled Binary

Alternatively, you can use the Muse.jar file by downloading it from the releases section.

## Usage

Muse relies on [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus). You will need the `libs4ast` folder of that project in order to run Muse.

To run Muse, use the following command, specifying the required arguments:

```sh
java -jar Muse-1.0.0.jar <libs4ast> <AppSourceCode> <AppName> <OutputPath> <OperatorType>
```

If running Muse within a IDE like Eclipse, import only the Muse folder within the code subdirectory, or else you might get a java.lang.SecurityException error when running Muse.java

### Arguments

Provide the following list of required arguments when running Muse:

1. ``libs4ast``:  path of the lib4ast folder, from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast)
2. ``AppSourceCode``: path of the Android app source code folder, which you want to apply mutation on;
3. ``AppName``:  Name of the App;
4. ``Output``: Path of the folder where the mutants will be created;
5. `OperatorType`: Type of operator to be used while creating mutants. Currently supported arguments are: SOURCE, SINK, TAINT, REACHABILITY, TAINTSINK, and COMPLEXREACHABILITY.

### Example

```sh
java -jar Muse-1.0.0.jar MDroidPlus/libs4ast/ /tmp/AppFoo/src/ AppFoo /tmp/mutants/ TAINTSINK
```

This will create a folder called `AppFoo` under `/tmp/mutants` where the mutated source files will be stored.

## Muse Processor Helper Utility

We have created the Muse processor utility using python, which makes it easier to mutate android projects by generating relevant shell script files. To work with it, you need to replace the file paths, and edit the file called `input_folders`. In the file, you need to provide the list of directories containing Android projects. The provided `input_folders` already contains sample values.

## Cite

If you use Muse for academic purposes, please cite:

Amit Seal Ami, Kaushal Kafle, Kevin Moran, Adwait Nadkarni, and Denys Poshyvanyk. 2021. Systematic Mutation-Based Evaluation of the Soundness of Security-Focused Android Static Analysis Techniques. ACM Trans. Priv. Secur. 24, 3, Article 15 (April 2021), 37 pages. DOI:[https://doi.org/10.1145/3439802](https://doi.org/10.1145/3439802)

```bib
@article{10.1145/3439802,
author = {Ami, Amit Seal and Kafle, Kaushal and Moran, Kevin and Nadkarni, Adwait and Poshyvanyk, Denys},
title = {Systematic Mutation-Based Evaluation of the Soundness of Security-Focused Android Static Analysis Techniques},
year = {2021},
issue_date = {April 2021},
publisher = {Association for Computing Machinery},
address = {New York, NY, USA},
volume = {24},
number = {3},
issn = {2471-2566},
url = {https://doi.org/10.1145/3439802},
doi = {10.1145/3439802},
journal = {ACM Trans. Priv. Secur.},
month = feb,
articleno = {15},
numpages = {37},
keywords = {CryptoPAn, Network trace anonymization, semantic attacks}
}
```

## Future Work

Currently Muse is oriented towards the evaluation of static analysis tools for Android data leak detection. We intend to expand Muse by developing additional security operators and mutation schemes for tools with other goals, e.g. SSL verification.

This repository is for a refactoring and expansion of the original muse tool built by Richie Bonnett, which is available [here](https://github.com/rfbonett/muse).

### Team Members

- Amit Seal Ami
- Liz Weech
- Yang Zhang
