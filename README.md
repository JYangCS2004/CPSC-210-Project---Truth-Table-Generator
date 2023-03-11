# My Personal Project: Truth Table Calculator

### What will the application do?
In CPSC 121, we spent a good chunk of the semester learning about boolean algebra, or
logical expressions. Specifically, we were often required to show whether if 
and logical argument is of a valid form. We used two different methods to achieve this. 
We showed that an argument is valid by applying equivalency laws and rules of inference.
However, we can also use a truth table to show validity. The idea is to draw a truth table listing all possible combinations
of truth values from each logical statement in the argument.
An example of this is shown below:

![img.png](img.png)

For this term's 210 project, the task is to create a program that can generate such truth tables.
Moreover, the program should have the ability to determine the validity of the argument.
An argument can be shown to be invalid if there is a row in the truth table 
where the **premises are true and the conclusion is false**.
We also can use it to test logical equivalence between two different logical statements using
the truth table. **The program should be able to take in logical statements as strings (e.g. P ∨ Q.), and interpret
and evaluate based on given truth values.**
### Who will use it?
This program will be particularly useful for students taking CPSC 121 and PHIL 220. Drawing a truth table is an automatic, but
can be a time-consuming process, considering logical statements can get arbitrarily long. A complicated truth table can be cumbersome
to analyze. It would be nice to have a program to automate the process.

### Why is this project of interest to you?
We looked into many types of interesting algorithms in CPSC 110. I feel like this would be a nice extension to what we learned
in DrRacket. Additionally, I'm also currently re-learning this material in PHIL 220. This project could reinforce what I 
have learned in class, and as an extension to what I already know.

## User Stories
  - As a user, I want to be able to add an arbitrary number of premises to the argument.
  - As a user, I want to be shown the complete truth table that displays all the expressions inputted.
  - As a user, I want to be told the specific set of truth values that proves that an argument is invalid.
  - As a user, I want to be able to delete a premise from the set of logical statements.
  - As a user, I want to have the option to save the current statements in the argument any time during the application.
  - As a user, I want to have the option to load the set of statements that was last saved at any time during the program.