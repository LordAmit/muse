# Muse "Use Cases"
*Basically a document that team members can use to get up to speed. Includes basic use case for Muse and details description.*


## Muse Purpose
The overall purpose of Muse is to mutate android application source code and then use this mutated source code to check the soundness of static analysis tools. Tools that claim to be able to recognize these mutations should be able to recognize mutations in the outputted code Muse creates. If some leaks are not  recognized, then the analysis tool needs to be patched.

## Basic Use Case
**Name:** 
Running Muse

**Description:** 
User wants to run Muse to then test static analysis tool

**Basic Flow:**
- User runs Muse with command line arguments
  - `<libs4ast> <AppSourceCode> <AppName> <OutputPath> <OperatorType>` 
- Muse processes source code, marking places to input mutations
- Muse goes through points of mutation and injects mutant code
- Muse rewrites these changes to the output location
- User runs mutated file against static analysis tool


## Muse Inputs
- `<libs4ast>` - path to the lib4ast folder, from MDroidPlus
- `<AppSourceCode>` - path to the Android application source code folder, to which mutation will be applied
- `<AppName>` - Name of the Android application
- `<Output>` - path of the folder where the mutants will be created and saved after
- `<OperatorType>` - the type of operation the be used when creating mutated files. Currently must be one of: TAINTSOURCE, TAINTSINK, SCOPESOURCE, REACHABILITY, SCOPESINK, and COMPLEXREACHABILITY


## OperatorType Descriptions

Different `<OperatorType>` settings can be used when running source. Below is a description of each of these `<OperatorTypes>`

**TAINTSOURCE:**
- User runs Muse with the TAINTSOURCE `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - while not inStaticContext and not inAnonymousClass
- Muse runs respective operator to changes for each mutation
  - Source statement `String dataLeAk%d = \"\";`
- Muse rewrites these changes to the output file

**TAINTSINK**
- User runs Muse with the TAINTSINK `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - While not inStaticContext and not inAnonymousClass
  - Then again looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - If inAnonymousClass become true
- Also runs TAINTSOURCE to mark places for source placement
- Muse runs respective operator to make changes for each mutation
  - Also inputs the Source statement `String dataLeAk%d = \"\";`
  - Sink statement `android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);`
- Applies rewriter changes to file

**REACHABILITY**
- User runs Muse with the REACHABILITY `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Marks changes for Classes and Interfaces, Anonymous Classes, and Blocks
- Muse runs the respective operator to make changes for each marked mutation
  - Source statement `String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();`
  - Sink statement `Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);`
- Muse rewrites these changes to the output file


**COMPLEXREACHABILITY**
- User runs Muse with the COMPLEXREACHABILITY `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Currently only checks if it is not an Interface TypeDeclarations
  - Does not add any node changes
- Muse runs the respective operator to make changes for each marked node change 
  - Adds complex string DataLeak.getLeak()
- Muse rewrites these changes to the output file


**SCOPESOURCE**
- User runs Muse with the SCOPESOURCE `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Marks changes for all non static and non private/protected, creates node change for both declaration and method body
  - Looking for TYPE_DECLARATION and ANONYMOUS_CLASS_DECLARATION
- Muse runs the respective operator to make changes for each marked node change
  - Contains insertions for declaration and method body
  - Source statement method body `dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();`
  - Source statement declaration `String dataLeAk%d = \"%d\";`
- Muse rewrites these changes to the output file


**SCOPESINK**
- User runs Muse with the SCOPESINK `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Marks change for sink at each field declaration in every method where the classes match
- Also runs SCOPESOURCE to place marks for sources
- Muse then runs the respective operators to make changes for all marked node changes
  - Sink statement `"android.util.Log.d(\"leak-%s-%s\", dataLeAk%s);`
- Muse rewrites these change to the output file


