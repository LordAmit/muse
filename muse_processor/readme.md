# Readme for Muse Processor

Muse Processor makes it easier to create mutated apps using the Muse framework. It is a helper utility that generates script files for you based on a combination of shell scripts and variables you edit. 

## Setup

To utilize it, edit the `input_folders` file first. 
In each line, a project name, and a project path is to be given. 
The project name is used as the name of the output folder. At the project path, put in the path to the base project to be used for mutation. 

For example,

`BMI_Calculator,/home/amit/muse/app/BMI_Calculator-master/`

means the output of the Muse Processor will be stored at a folder called BMI_Calculator. The project which will act as the base project for mutation can be found from the `/home/amit/muse/app/BMI_Calculator-master/` path. 

A line containing a `//` will be skipped. 

To run `muse_processor.py` file, you need to edit `path_to_muse_jar`, and `output_dir` variables. As it sounds, the path to muse jar points to the absolute address to the `Muse.jar` file. The `output_dir` points to the absolute path to the output directory.

That's it, you probably do not need to do anything else for setup. (you still need appropriate java, python etc in path)

## Running muse_processor
Assuming that you already configured muse_processor, you can directly execute `muse_processor.py` file. It will create a directory per line from the `input_folders`.  Assuming that the directory name is `babymonitor`, each directory will contain the following: 

```
├── schemas
│   ├── COMPLEXREACHABILITY
│   ├── REACHABILITY
│   ├── TAINTSINK
│   └── SCOPESINK
└── sh_files
    ├── all_schema.sh
    ├── babymonitor_COMPLEXREACHABILITY.sh
    ├── babymonitor_REACHABILITY.sh
    ├── babymonitor_SINK.sh
    ├── babymonitor_TAINTSINK.sh
    ├── clean_all.sh
    ├── clean_and_build_all.sh
    ├── clean_project_folder.sh
    └── gradle_permission.sh


```
The directories under schemas will contain the mutated version of apps after running the script files. 

Here is a short description for each script files that you need to execute in order:

1. `all_schema.sh`: This is actually a script file that calls other script files. 
  
    a)  `clean_project_folder.sh`: Used to execute `gradlew clean` command in the base directory. Useful since you do not need build data for creating mutants.
    
    b) `babymonitor_SINK.sh`: Used to execute Muse with necessary arguments so that the mutated version of babymonitor is created while TAINTSINK schema is applied. The same goes for the `*_REACHABILITY, *_TAINTSINK, *_COMPLEXREACHABILITY` schemes. 

2. `gradle_permission.sh`: Since the gradle files created through the Muse framework may or may not have executable permission, we need to execute this. It merely assigns executable permission for the gradle script in each mutated application folder. 

3. `clean_and_build_all.sh`: This script cleans and then builds each mutated application through gradlew. The basic aommands are: `gradlew clean; gradlew assembleDebug`.

4. `clean_all.sh`: This script is not mandatory, but you can use it after extracting the necessary mutated apk files to clean the build related data in mutated application.