CSX Code Generator
============


## Description
This project implements the code generator for CSX programs.


## Instructions to compile the program

   	make

The command above compiles or recompiles classes. The ".class" files are placed
in subdirectory classes.


## Instructions to run the program
	
   	make cg INPUT=<csx file>

The command above will generate JVM code if parse and type check are successful.
The generated JVM code are placed in file name.j, where name is the identifier
that names the CSX class.

   	make asm INPUT=<name.j>

The command above will generate name.class by using Jasmin, where name is
also the CSX program's class name, and name.j is the file that placed the
generated JVM code.

   	make run INPUT=<name>

The command above will run the CSX program.






