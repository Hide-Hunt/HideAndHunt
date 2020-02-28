# Git Conventions

## Branches

-  At the beginning of a sprint, the common work should be pushed to a branch called sprintM with M being the id of the sprint.
- Each user story should be coded on it's own branch (see below).
- When working on user story with id n, create a branch called us-n, this branch can be merged into master when the us is finished by doing a pull request.
- When working on a subpart of a user story, you should create a branch following this naming convention: "us-n-[type]-[name]" where
  - Type is the type of work you're doing
    - **fe** for feature (also contains the testing part)
    - **bug** for bug fixing
  - Name is a simple name to identify what you're doing i.e: nfc-detection

### Example of a workflow



Alice creates branch **sprint4** and commits some dumb convention file that everybody will eventually get bored of following

*Sprint starts*

Bob checkout to branch **sprint4** and creates a branch **us-23**

Bob creates branch **us-23-fe-automatic-position-update** 

Bob makes some commits

Bob merges **us-23-fe-automatic-position-update** to **us-23**

...

Bob makes a PR for **us-23** => **master**

PR get's reviewed

???

Profit



## Commits

Follow SwEng guidelines and it should be good enough



# Code

## Variables

Names in **c**amelCase

```java
private int nodeID;
private String wayTooLongButUsefulToShowCamelCase = "Bon je crois qu'on a fait le tour"
```



## Constantes

Names in SNAKE_CASE

```java
public static final int MAX_CONCURRENT_THREADS = 10
```



## Classes, Enums et Interfaces

Names in **C**amelCase

```java
public class HaltingProblemSolver<T> implements Solver {
 //... 
}
```



## Fonctions

Names in **c**amelCase

```java
public class HaltingProblemSolver<T> implements Solver {

  public void solve(T turingMachine) {
    //I have an elegant algorithm but this document is too small to contain it
  }
  
  public String toString() {
    //...
    if(condition) { 
      //note how the accolades are formatted
    }
  }

}
```

