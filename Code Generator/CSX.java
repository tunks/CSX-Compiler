import java.io.*;
import java_cup.runtime.*;

class CSX {

  public static void
  main(String args[]) throws java.io.IOException, Exception {

    System.out.println("********************");
    System.out.println("** Author: Chen Liu");
    System.out.println("********************");

	if (args.length != 1) {
       		System.out.println(
			"Error: Input file must be named on command line." );
		System.exit(-1);
    	}

	
    	java.io.FileInputStream yyin = null;

    	try {
    		yyin = new java.io.FileInputStream(args[0]);
    	} catch (FileNotFoundException notFound){
       		System.out.println ("Error: unable to open input file.");
		System.exit(-1);
    	}

    String asmName; // name of asmFile

    PrintStream	asmFile; // File to which asm code will be generated

    Scanner.init(yyin); // Initialize Scanner class for parser

    parser csxParser = new parser();

    System.out.println ("\n\n" + "Begin CSX compilation of " +
			args[0] + ".\n");
    Symbol root=null;

    try {
        root = csxParser.parse(); // do the parse
	
    } catch (SyntaxErrorException e){
	System.out.println ("Compilation terminated due to syntax errors.");
	System.exit(0);
    }

    boolean    ok;
    ok = ((classNode)root.value).isTypeCorrect();
    if (ok) {
      String className = ((classNode)root.value).returnClassName();
      asmName = className+".j";

	asmFile = new PrintStream(new FileOutputStream(asmName));
	if (((classNode)root.value).codegen(asmFile)) {
		asmFile.close();
                System.out.println("CSX compilation successfully completed.");
                System.out.println("Translated program is in "+asmName+".");
	} else {
                System.out.println(
		  "CSX compilation halted due to code generation errors.");
               }

     } else   System.out.println("\nCSX compilation halted due to type errors.");
	

    return;
    }
}
	
	
