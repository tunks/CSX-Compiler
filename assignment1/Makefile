####
# Makefile for project 1
# Charles Fischer
# Modified 1/2011 Raphael Finkel
#
# All classes are compiled into the ./classes directory
#
# The default is to make all .class files.
#
#    clean    -- removes all generated files
#
#    test     -- Tests proj1 by running P1.class
#
###

CLASSPATH = ./classes
VPATH = $(CLASSPATH)
JCFLAGS = -Xlint:all -deprecation -classpath $(CLASSPATH) -d $(CLASSPATH)
JFLAGS = -classpath $(CLASSPATH)

%.class: %.java
	javac $(JCFLAGS) $<

all : P1.class EmptySTException.class SymbolTable.class \
		TestSym.class Symb.class DuplicateException.class

P1.class : EmptySTException.class SymbolTable.class \
		TestSym.class Symb.class DuplicateException.class
TestSym.class : Symb.class

SymbolTable.class : EmptySTException.class \
		Symb.class TestSym.class DuplicateException.class


###
# test
###

test: P1.class
	java $(JFLAGS) P1

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
	rm -f $(CLASSPATH)/*.class

