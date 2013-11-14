CSX Compiler
============

This project implements a compiler for programming language CSX
(computer science experimental) in Java.

There are five modules in this project, corresponding to five stages of
a compiler.


## Symbol Table

Block-structure symbol table is built to associate each identifier in
the program's source code with its information (e.g., type, scope
level).


## Scanner

The scanner module is based on [JFlex](http://jflex.de) scanner-generation
tool.


## Parser

The parse module uses [Java CUP](http://www.cs.princeton.edu/~appel/modern/java/CUP/)
parse generator and builds an abstract syntax tree. The abstract syntax
tree is used by the type checker and code generator.


## Type Checker

The type checker operates on the abstract syntax tree built by the CSX
parser. The type checker can produce error messages and type errors, and
returns a boolean value indicating whether the abstract syntax tree has
any type or scoping errors.


## Code Generator

The code generator module generates JVM assembler code for CSX programs.
It then assembles the generated JVM instructions using the [Jasmin](http://jasmin.sourceforge.net) 
assembler. Jasmin produces a standard ".class" file, which will be
executed using Java.
