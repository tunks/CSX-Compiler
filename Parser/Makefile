##########################################
####
# Makefile for project 3
# Modified 2/2011 Raphael Finkel
#
# All classes are compiled into the ./classes directory
#
# The default is to make all .class files.
#
#    clean    -- removes all generated files
#
#    test     -- Tests proj3 by running P3.class with input test.lite
#
###

DESTPATH = ./classes
CUPPATH = /Users/chenliu/vim-git/compiler-design/public/java_cup
CLASSPATH = $(DESTPATH):$(CUPPATH)/java-cup-10k.jar
VPATH = $(DESTPATH)
JCFLAGS = -Xlint:all -deprecation -classpath $(CLASSPATH) -d $(DESTPATH)
JFLAGS = -classpath $(CLASSPATH)
FILENAME = -g
FILENAME = test.lite

%.class: %.java
	javac $(JCFLAGS) $<

.PHONY: all 
all: P3.class Yylex.class test.lite parser.class 

# don't use CUP 0.11a; I couldn't get it to work right.  -- Raphael 2/2011
sym.java parser.java: lite.cup 
	java $(JFLAGS) java_cup/Main < lite.cup
Yylex.java: lite.jlex
	jflex lite.jlex
parser.class: ASTNode.class Scanner.class
Yylex.class: sym.class 
Scanner.class: Yylex.class
ASTNode.class: ast.java sym.class
	javac $(JCFLAGS) ast.java
P3.class: Yylex.class Scanner.class parser.class ASTNode.class

test: all
	java $(JFLAGS) P3 $(FILENAME)

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

