Block-Structured Symbol Table
==========

This project, containing a set of java classes and a test driver, is used to implement a
block-structed symbol table.

Although there is a testInput file, the test driver is interactive, which means the program
operates from command line. The testInput file is the set of test data. And testOutput is
generated in response to the testInput file.


## How to use:

1.Type commands listed below at command line
    
      "make",       to recompiles classes.
      "make test",  to recompilers classes and run the program.
      "make clean", to removes all class files.

2.The correct commands(ingoring case) for testing the operation of symbol table
  
      open a new scope:                     open/o
      close the top scope:                  close/c
      dump the contents of symbol table:    dump/d
      insert new symbol:                    insert/i parameter1(string type) parameter2(int type)
      lookup the entry in the top scope:    lookup/l parameter(string type)(you could enter more than one parameter to lookup)
      lookup the entry in the whole table:  global/g parameter(string type)(you could enter more than one parameter to global lookup)
      exit the test:                        quit/q


External documentation:

1.http://download.oracle.com/javase/6/docs/api

2.http://www.cs.uky.edu/~raphael/courses/CS541.html
