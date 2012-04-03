#########################################
####
# Makefile for project 4
# Modified 2/2011 Raphael Finkel
#
# All classes are compiled into the ./classes directory
#
# The default is to make all .class files.
#
#    clean    -- removes all generated files
#
#    test     -- Tests proj4 by running P4.class with inputs test[123].lite
#
###

DESTPATH = ./classes
#COURSEDIR = /homes/raphael/courses/cs541/public
COURSEDIR = /Users/chenliu/vim-git/compiler-design/public
CUPPATH = $(COURSEDIR)/cup
CLASSPATH = $(DESTPATH):$(CUPPATH)/java-cup-10k.jar
VPATH = $(DESTPATH)
JCFLAGS = -Xlint:all -deprecation -classpath $(CLASSPATH) -d $(DESTPATH)
JFLAGS = -classpath $(CLASSPATH)
#TESTDIR = $(COURSEDIR)/proj4/tests
TESTDIR = ./tests
TESTFILE = $(wildcard *.csx)

%.class: %.java
	javac $(JCFLAGS) $<

.PHONY: all 
all: P4.class Yylex.class parser.class 

# don't use CUP 0.11a; I couldn't get it to work right.  -- Raphael 2/2011
sym.java parser.java: lite.cup 
	java $(JFLAGS) java_cup/Main < lite.cup
Yylex.java: lite.jlex
	jflex lite.jlex
parser.class: ASTNode.class Scanner.class SyntaxErrorException.class
Yylex.class: sym.class 
SymbolTable.class: Symb.class EmptySTException.class DuplicateException.class
Scanner.class: Yylex.class Types.class
SymbolInfo.class: Kinds.class
ASTNode.class: ast.java sym.class SymbolTable.class SymbolInfo.class
	javac $(JCFLAGS) ast.java
P4.class: Yylex.class Scanner.class parser.class ASTNode.class

test: all
	java $(JFLAGS) P4 $(TESTDIR)/test-00.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-01.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-02.csx >> TestResults 
	java $(JFLAGS) P4 $(TESTDIR)/test-03.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-04.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-05.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-06.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-07.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-08.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-09.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-10.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-11.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-12.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-13.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-14.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-15.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-16.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-18.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-19.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-20.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-21.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-22.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-23.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-24.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-25.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-26.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-27.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-28.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-29.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-30.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-31.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-32.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-33.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-36.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-37.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-38.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-39.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-40.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-41.csx >> TestResults
	java $(JFLAGS) P4 $(TESTDIR)/test-42.csx >> TestResults

###
# style check
###

HOW = basic,braces,clone,design,finalizers,migrating,unusedcode,imports,optimizations,strictexception,strings,sunsecure,typeresolution

style: 
	t=`pwd`; cd ~raphael/source/pmd-4.2.5/bin; \
		./pmd.sh $$t text $(HOW)

###
# lint check
###

lint: all
	jlint +all classes

###
# clean
###

clean: 
	rm -f $(DESTPATH)/*.class Yylex.java* sym.java parser.java
