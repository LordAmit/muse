# Muse "Use Cases"
*Basically a document that team members can use to get up to speed. Includes basic use case for Muse and details description.*


## Muse Purpose:
The overall purpose of Muse is to mutate android application source code and then use this mutated source code to check the soundness of static analysis tools. Tools that claim to be able to recognize these mutations should be able to recognize mutations in the outputted code Muse creates. If some leaks are not  recognized, then the analysis tool needs to be patched.

### Basic Use Case
**Name:** 
Run Muse

**Description:** 
Researcher wants to run Muse and test static analysis tool

**Basic Flow:**
- User runs Muse with command line arguments
  - <libs4ast> <AppSourceCode> <AppName> <OutputPath> <OperatorType>
- Muse processes source code, marking places to input mutations
- Muse goes through points of mutation and injects mutant code
- Muse rewrites these changes to the output location
- User runs mutated file against static analysis tool


### OperatorType Descriptions

Different `<OperatorType>` settings can be used when running source. Below is a description of each of these `<OperatorTypes>`

**SOURCE:**
- User runs Muse with the SOURCE `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - while not inStaticContext and not inAnonymousClass
- Muse runs respective operator to changes for each mutation
  - Source statement `String dataLeAk%d = \"\";`
- Muse rewrites these changes to the output file

**SINK**
- User runs Muse with the SINK `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - While not inStaticContext and not inAnonymousClass
  - Then again looks for ASTNode `TYPE_DECLARATION` and `ANONYMOUS_CLASS_DECLARATION`
    - If inAnonymousClass become true
- Muse runs respective operator to make changes for each mutation
  - Sink statement `android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);`

**TAINT**
- User runs Muse with the TAINT `<OperatorType>`
- Muse runs the respective schema to mark places for mutation insertion
  - Marks changes for Classes and Interfaces, Anonymous Classes, and Blocks
- Muse runs the respective operator to make changes for each marked mutation
  - Source statement `String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();`
  - Sink statement `Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);`

