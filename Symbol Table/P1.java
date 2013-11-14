import java.io.*;
import java.util.Scanner;
class P1 {
   public static void main(String args[]){
     System.out.println(
       "Project 1 test driver. Enter any of the following commands:\n"+
       "  (Command prefixes are allowed)\n"+
       "\tOpen (a new scope)\n"+
       "\tClose (innermost current scope)\n"+
       "\tQuit (test driver)\n"+
       "\tDump (contents of symbol table)\n"+
       "\tInsert parameter1 parameter2(symbol,integer pair into symbol table)\n"+
       "\tLookup parameter(lookup symbol in top scope)\n"+
       "\tGlobal parameter(global lookup of symbol in symbol table)\n"+
     "");

		SymbolTable stable = new SymbolTable();
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);
     
    String str_read = null;

    //isr = new InputStreamReader(System.in);
    //br = new BufferedReader(isr);

	  PrintStream original = new PrintStream(System.out);      //save the original PrintStream

   
	 	try {

		   System.setOut(new PrintStream(new FileOutputStream("testOutput")));

			 System.out.println("/*=================================================================");
       original.println("/*===================================================================");
	   	 System.out.println("#    Author:        Chen Liu");
		   original.println("#    Author:        Chen Liu");
		   System.out.println("#    Email:         chen.liu@uky.edu");
		   original.println("#    Email:         chen.liu@uky.edu");
		   System.out.println("#    Description:   Compiler Design Assignment: Symbal Table Classes");
		   original.println("#    Description:   Compiler Design Assignment: Symbal Table Classes");
		   System.out.println("==================================================================*/\n");
		   original.println("====================================================================*/\n");

			 System.out.println("Please enter commends:");
		   original.println("Please enter commends:");

		   str_read = br.readLine();
       String str[] = str_read.split(" ");


       while (!str[0].equalsIgnoreCase("Quit")&&!str[0].equalsIgnoreCase("Q")){        //quit

				 System.out.println(str_read);    //output the user's commands to the testOutput file
      
         if (str[0].equalsIgnoreCase("Open")||str[0].equalsIgnoreCase("O")){           //open
            
						if (str.length != 1){
							System.out.println("Error: mismatched number of parameter, please retry.");
						  original.println("Error: mismatched number of parameter, please retry.");
						}
						else {
							stable.openScope();
						  System.out.println("New scope opened.");
							original.println("New scope opened");
						}
				 }
         else if (str[0].equalsIgnoreCase("Close")||str[0].equalsIgnoreCase("C"))      //close
            
						if (str.length != 1) {
							System.out.println("Error: mismatched number of parameter, please retry.");
              original.println("Error: mismatched number of parameter, please retry.");
						}
						else{
							try {
              stable.closeScope();
							System.out.println("Top scope closed.");
							original.println("Top scope closed.");
              } catch (EmptySTException e) {
                  //e.printStackTrace();
									System.out.println(e);
									original.println(e);
              }
						}
         else if (str[0].equalsIgnoreCase("Dump")||str[0].equalsIgnoreCase("D"))       //dump
            
					 if (str.length != 1) {
							System.out.println("Error: mismatched number of parameter, please retry.");
							original.println("Error: mismatched number of parameter, please retry.");
					 }
						else {
							stable.dump(System.out);
						  stable.dump(original);
						}
         else if (str[0].equalsIgnoreCase("Insert")||str[0].equalsIgnoreCase("I")){    //insert
            
            if ((str.length == 3)&&(isNumeric(str[2]))){       //check the number and type of parameter
              int i = Integer.parseInt(str[2]);
              TestSym tsym = new TestSym(str[1], i);
              try {
                stable.insert(tsym);
								System.out.println(tsym.toString() + " entered into symbol table.");
								original.println(tsym.toString() + " entered into symbol table.");
              } catch (EmptySTException e) {
								System.out.println(e);
								original.println(e);
              } catch (DuplicateException d) {
								System.out.println(d);
								original.println(d);
              }
            }
            else {
              System.out.println("Error: mismatched the number of parameter or the second parameter could not convert to Integer, please retry.");
              original.println("Error: mismatched the number of parameter or the second parameter could not converts to Ingeter, please retry.");
						}
         }
         else if (str[0].equalsIgnoreCase("Lookup")||str[0].equalsIgnoreCase("L")){    //lookup
           
					 for (int i =1; i < str.length; i++) {
             TestSym tsym = (TestSym)stable.localLookup(str[i]);
             if (tsym == null) {
               System.out.println(str[i] + " not found in top scope.");
               original.println(str[i] + " not found in top scope.");
						 }
             else {
               System.out.println(tsym.toString() + " found in top scope.");
               original.println(tsym.toString() + " found in top scope.");
						 }
					}
         }
         else if (str[0].equalsIgnoreCase("Global")||str[0].equalsIgnoreCase("G")){     //global
           
					 for (int i = 1; i < str.length; i++) {
             TestSym tsym = (TestSym)stable.globalLookup(str[i]);
             if (tsym == null) {
               System.out.println(str[i] + " not found in symbol table.");
               original.println(str[i] + " not found in symbol table.");
						 }
             else {
               System.out.println(tsym.toString() + " found in symbol table.");
               original.println(tsym.toString() + " found in symbol table.");
						 }
					 }
				}

        else {                                                                            //Input Error
           System.out.println("Error: " + str[0] + " is illegal, please retry.");
           original.println("Error: " + str[0] + " is illegal, please retry.");
				 }

         System.out.println("\nPlease enter commends:");
         original.println("\nPlease enter commends:");
        
         str_read = br.readLine();
         str = str_read.split(" ");
       }
     } catch (Exception e) {
       System.out.println("Fail to read commends!");
       original.println("Fail to read commends!");
     } finally {
       try {
        br.close();
        isr.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
     }  //try catch finally

     System.out.println(str_read);
     System.out.println("Testing done.");
     original.println("Testing done.");

   } // main

   public static boolean isNumeric(String str) {             //to check if str could be converted to integer
     try {
       Integer.parseInt(str);
       return true;
     } catch (Exception e) {
       return false;
     }
   }
} // class P1
