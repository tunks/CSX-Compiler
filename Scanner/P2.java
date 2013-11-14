import java.io.*;

class P2 {

  public static void
  main(String args[]) throws java.io.IOException {

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

    // lex is a JLex-generated scanner that reads from yyin
	final Yylex lex = new Yylex(yyin);	

	System.out.println ("Begin test of CSX scanner.");

	Symbol token = lex.yylex();

	while ( token.sym != sym.EOF) {

      if (token.sym != sym.MULTICOM) {

		System.out.print( token.value.linenum + ":"
				+ token.value.colnum + " ");

		switch (token.sym) {

          case sym.STRLIT:
            System.out.println("\tString literal(" +
              ((CSXStringLitToken) token.value).stringText + ")");
            break;

          case sym.CHARLIT:
            System.out.println("\tChar literal(" +
              ((CSXCharLitToken) token.value).charValue + ")");
            break;

          case sym.IDENTIFIER:
            System.out.println("\tIdentifier  (" +
              ((CSXStringLitToken) token.value).stringText + ")");
            break;

		  case sym.INTLIT:
			System.out.println("\tInteger literal(" +
			  ((CSXIntLitToken) token.value).intValue + ")");
			break;

		  case sym.PLUS:
			System.out.println("\t+");
			break;

		  case sym.NOTEQ:
			System.out.println("\t!=");
			break;

          case sym.TIMES:
            System.out.println("\t*");
            break;

          case sym.LPAREN:
            System.out.println("\t(");
            break;

          case sym.RPAREN:
            System.out.println("\t)");
            break;

          case sym.LBRACKET:
            System.out.println("\t[");
            break;

          case sym.RBRACKET:
            System.out.println("\t]");
            break;

          case sym.EQ:
            System.out.println("\t==");
            break;

          case sym.SEMI:
            System.out.println("\t;");
            break;
		
		  case sym.MINUS:
			System.out.println("\t-");
			break;

		  case sym.SLASH:
			System.out.println("\t/");
			break;

          case sym.ASG:
            System.out.println("\t=");
            break;

          case sym.CAND:
            System.out.println("\t&&");
            break;

          case sym.COR:
            System.out.println("\t||");
            break;

          case sym.LT:
            System.out.println("\t<");
            break;

          case sym.GT:
            System.out.println("\t>");
            break;

          case sym.LEQ:
            System.out.println("\t<=");
            break;

		  case sym.GEQ:
			System.out.println("\t>=");
			break;

		  case sym.COMMA:
			System.out.println("\t,");
			break;

          case sym.NOT:
            System.out.println("\t!");
            break;

          case sym.LBRACE:
            System.out.println("\t{");
            break;

          case sym.RBRACE:
            System.out.println("\t}");
            break;

          case sym.COLON:
            System.out.println("\t:");
            break;

          case sym.rw_BOOL:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_BREAK:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_CHAR:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_CLASS:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_CONST:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_CONTINUE:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_ELSE:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_FALSE:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_IF:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_INT:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_READ:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_RETURN:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_TRUE:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_VOID:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_WHILE:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.rw_PRINT:
            System.out.println("\t" + ((CSXStringLitToken)token.value).stringText);
            break;

          case sym.UNKNOWN:
            System.out.println("***ERROR: invalid token (" + ((CSXStringLitToken)token.value).stringText + ")");
            break;

          default:
			System.out.println("unrecognized token type: " + token.value);
		} // switch(token.sym)
      }
	  token = lex.yylex(); // get next token
	}
	System.out.println("End test of CSX scanner.");
  } // main()

} // class P2

