
abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;

	static void genIndent(int indent) {
		for (int i = 1; i <= indent; i += 1) {
			System.out.print("\t");
		}
	} // genIndent

	ASTNode() {
		linenum = -1;
		colnum = -1;
	} // ASTNode()

	ASTNode(int line, int col) {
		linenum = line;
		colnum = col;
	} // ASTNode(line, col)

	boolean isNull() {
		return false; // often redefined in a subclass
	} // isNull()

	void Unparse(int indent) {
		// This routine is normally redefined in a subclass
	} // Unparse()
} // class ASTNode

class nullNode extends ASTNode {
// This class definition probably doesn't need to be changed
	nullNode() {
		super();
	}

	boolean isNull() {
		return true;
	}

	void Unparse(int indent) {
		// no action
	}
} // class nullNode

class csxLiteNode extends ASTNode {
// This node is used to root CSX lite programs 
	
	csxLiteNode(classNode pclass) {
		progClass = pclass;
	} // csxLiteNode() 

	void Unparse(int indent) {
		//System.out.println(linenum + ":");
		progClass.Unparse(0);
		//System.out.println(linenum + ":" + " } EOF");
	} // Unparse()

	private final classNode progClass;
} // class csxLiteNode

class classNode extends ASTNode {

	classNode(identNode id, memberDeclsNode memb, int line, int col) {
		super(line, col);
		className = id;
		members = memb;
	} // classNode

    void Unparse(int indent) {
      System.out.print(linenum + ":" + " class ");
      className.Unparse(0);
      System.out.println(" {");
      members.Unparse(0);
      System.out.println(linenum + ":" + " } EOF");
    }

	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
    memberDeclsNode () {
      super();
    }
	memberDeclsNode(declNode f, methodDeclsNode m, memberDeclsNode mm, int line, int col) {
		super(line, col);
		fields = f;
		methods = m;
        moremembers = mm;
	}


    void Unparse(int indent) {
      fields.Unparse(indent);
      moremembers.Unparse(indent);
      methods.Unparse(indent);
    }


    static nullMemberDeclsNode NULL = new nullMemberDeclsNode();
	private declNode fields;
    private memberDeclsNode moremembers;
	private methodDeclsNode methods;
} // class memberDeclsNode

class nullMemberDeclsNode extends memberDeclsNode {
  nullMemberDeclsNode() {};
  boolean isNull() {return true;}
  void Unparse(int indent) {}
}

class fieldDeclsNode extends ASTNode {
	fieldDeclsNode() {
		super();
	}
	fieldDeclsNode(declNode d, fieldDeclsNode f, int line, int col) {
		super(line, col);
		thisField = d;
		moreFields = f;
	}

    void Unparse(int indent) {
      thisField.Unparse(indent);
      moreFields.Unparse(indent);
    }

	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode thisField;
	private fieldDeclsNode moreFields;
} // class fieldDeclsNode

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
} // class nullFieldDeclsNode

// abstract superclass; only subclasses are actually created
abstract class declNode extends ASTNode {
	declNode() {
		super();
	}
	declNode(int l, int c) {
		super(l, c);
	}
    static nullDeclNode NULL = new nullDeclNode();
} // class declNode

class nullDeclNode extends declNode {
    nullDeclNode() {}
    boolean isNull() {return true;}
    void Unparse(int ident) {}
}

class varDeclNode extends declNode {
	varDeclNode(identNode id, typeNode t, exprNode e, int line, int col) {
		super(line, col);
		varName = id;
		varType = t;
		initValue = e;
	}
    
    void Unparse(int indent) {
      System.out.print(linenum + ":");
      varType.Unparse(indent+1);
      System.out.print(" ");
      varName.Unparse(0);
      if (initValue.isNull()) {}
      else {
        System.out.print(" = ");
        initValue.Unparse(0);
      }
      System.out.println(";");
    }


	private final identNode varName;
	private final typeNode varType;
	private final exprNode initValue;
} // class varDeclNode

class constDeclNode extends declNode {
	constDeclNode(identNode id,  exprNode e, int line, int col) {
		super(line, col);
		constName = id;
		constValue = e;
	}

	private final identNode constName;
	private final exprNode constValue;
} // class constDeclNode

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line, col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	}

	private final identNode arrayName;
	private final typeNode elementType;
	private final intLitNode arraySize;
} // class arrayDeclNode

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode() {
		super();
	}
	typeNode(int l, int c) {
		super(l, c);
	}
	static nullTypeNode NULL = new nullTypeNode();
} // class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
} // class nullTypeNode

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("int ");
    }
} // class intTypeNode

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("bool ");
    }
} // class boolTypeNode

class charTypeNode extends typeNode {
	charTypeNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("char ");
    }
} // class charTypeNode

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {}
} // class voidTypeNode

class optionalSemiNode extends ASTNode {
  optionalSemiNode() {
    super();
  }
  optionalSemiNode(int semi, int line, int col) {
    super(line, col);
    semisym = semi;
  }
  private int semisym;
  static nullOptionalSemiNode NULL = new nullOptionalSemiNode();
} // optionalSemiNode class

class nullOptionalSemiNode extends optionalSemiNode {
  nullOptionalSemiNode() {};
  boolean isNull() {return true;}
  void Unparse(int indent) {}
} // nullOptionalSemiNode class

class methodDeclsNode extends ASTNode {
	methodDeclsNode() {
		super();
	}
	methodDeclsNode(methodDeclNode m, methodDeclsNode ms,
			int line, int col) {
		super(line, col);
		thisDecl = m;
	 moreDecls = ms;
	}
	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullMethodDeclsNode 

class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t,
			fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		name = id;
		args = a;
		returnType = t;
		decls = f;
		stmts = s;
	}

	private final identNode name;
	private final argDeclsNode args;
	private final typeNode returnType;
	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class methodDeclNode 

// abstract superclass; only subclasses are actually created
abstract class argDeclNode extends ASTNode {
	argDeclNode() {
		super();
	}
	argDeclNode(int l, int c) {
		super(l, c);
	}
}

class argDeclsNode extends ASTNode {
	argDeclsNode() {}
	argDeclsNode(argDeclNode arg, argDeclsNode args,
			int line, int col) {
		super(line, col);
		thisDecl = arg;
		moreDecls = args;
	}
	static nullArgDeclsNode NULL = new nullArgDeclsNode();

	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgDeclsNode 

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		elementType = t;
	}

	private final identNode argName;
	private final typeNode elementType;
} // class arrayArgDeclNode 

class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		argType = t;
	}

	private final identNode argName;
	private final typeNode argType;
} // class valArgDeclNode 

// abstract superclass; only subclasses are actually created
abstract class stmtNode extends ASTNode {
	stmtNode() {
		super();
	}
	stmtNode(int l, int c) {
		super(l, c);
	}
	static nullStmtNode NULL = new nullStmtNode();
}

class nullStmtNode extends stmtNode {
	nullStmtNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullStmtNode 

class stmtsNode extends ASTNode {
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col) {
		super(line, col);
		thisStmt = stmt;
		moreStmts = stmts;
	}
	stmtsNode() {}

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	} 

	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}

} // class nullStmtsNode 

class asgNode extends stmtNode {
	asgNode(nameNode n, exprNode e, int line, int col) {
		super(line, col);
		target = n;
		source = e;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		target.Unparse(0);
		System.out.print(" = ");
		source.Unparse(0);
		System.out.println(";");
	}

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, stmtNode s2, int line, int col) {
		super(line, col);
		condition = e;
		thenPart = s1;
		elsePart = s2;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if (");
		condition.Unparse(0);
		System.out.println(")");
		thenPart.Unparse(indent+1);
		// No else parts in CSX-lite
	}

	private final exprNode condition;
	private final stmtNode thenPart;
	private final stmtNode  elsePart;
} // class ifThenNode whileNode extends stmtNode {
class whileNode extends stmtNode {
	whileNode(exprNode i, exprNode e, stmtNode s, int line, int col) {
		super(line, col);
	 label = i;
	 condition = e;
	 loopBody = s;
	}

	private final exprNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 

class readNode extends stmtNode {
	readNode() {}
	readNode(nameNode n, readNode rn, int line, int col) {
		super(line, col);
		 targetVar = n;
		 moreReads = rn;
	}

	static nullReadNode NULL = new nullReadNode();
	private nameNode targetVar;
	private readNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullReadNode 

class printNode extends stmtNode {
	printNode() {super();}
	printNode(exprNode val, printNode pn, int line, int col) {
		super(line, col);
		outputValue = val;
		morePrints = pn;
	}

	static nullPrintNode NULL = new nullPrintNode();

	private exprNode outputValue;
	private printNode morePrints;
} // class printNode 

class nullPrintNode extends printNode {
	nullPrintNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullPrintNode 

class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		args = a;
	}

    void Uparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        methodName.Unparse(0);
        System.out.print("(");
        if (args.isNull()){}
        else {
            args.Unparse(0);
        }
        System.out.println(")");
    }

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line, col);
		returnVal = e;
	}
    
    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        System.out.println("return " + ";");
    }

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		decls = f;
		stmts = s;
	}

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        System.out.println("break " + label + ";");
    }
	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        System.out.println("continue " + label + ";");
    }

	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {}
	argsNode(exprNode e, argsNode a, int line, int col) {
		super(line, col);
		argVal = e;
		moreArgs = a;
	}

    void Unparse(int indent) {
      argVal.Unparse(0);
      if (moreArgs.isNull()) {}
      else {
        System.out.print(",");
        moreArgs.Unparse(0);
      }
    }

	static nullArgsNode NULL = new nullArgsNode();
	private exprNode argVal;
	private argsNode moreArgs;
} // class argsNode 

class nullArgsNode extends argsNode {
	nullArgsNode() {
		// empty constructor
	}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgsNode 

// abstract superclass; only subclasses are actually created
abstract class exprNode extends ASTNode {
	exprNode() {
		super();
	}
    exprNode(int l, int c) {
      super(l, c);
    }
	static nullExprNode NULL = new nullExprNode();
}

class nullExprNode extends exprNode {
	nullExprNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullExprNode 

class booleanOpNode extends exprNode {
    booleanOpNode(exprNode e, int op, relationOpNode r, int line, int col) {
      super(line, col);
      expr = e;
      term = r;
      operatorCode = op;
    }

    static void printOp(int op) {
      switch (op) {
        case sym.COR:
          System.out.print(" || ");
          break;
        case sym.CAND:
          System.out.print(" && ");
          break;
        case -1:
          break;
        default:
          throw new Error("printOp: case not found");
      }
    }

    void Unparse(int indent) {
      //System.out.print("(");
      expr.Unparse(0);
      printOp(operatorCode);
      term.Unparse(0);
      //System.out.print(")");
    }

    private final exprNode expr;
    private final relationOpNode term;
    private final int operatorCode;
} // booleanOpNode class

class relationOpNode extends exprNode {
    relationOpNode(exprNode f1, int op, exprNode f2, int line, int col) {
        super(line, col);
        firstFactor = f1;
        secondFactor = f2;
        operatorCode = op;

    }

    static void printOp(int op) {
      switch (op) {
        case sym.LT:
          System.out.print(" < ");
          break;
        case sym.GT:
          System.out.print(" > ");
          break;
        case sym.LEQ:
          System.out.print(" <= ");
          break;
        case sym.GEQ:
          System.out.print(" >= ");
          break;
        case sym.EQ:
          System.out.print(" = ");
          break;
        case sym.NOTEQ:
          System.out.print(" != ");
          break;
        case -1:
          break;
        default:
          throw new Error("printOp: case not found");
      }
    }

    void Unparse(int indent) {
        //System.out.print("(");
        firstFactor.Unparse(0);
        printOp(operatorCode);
        secondFactor.Unparse(0);
        //System.out.print(")");
    }

    private final exprNode firstFactor;
    private final exprNode secondFactor;
    private final int operatorCode;
} // class relationOpNode

class factorNode extends exprNode {
    factorNode(exprNode e1, int op, exprNode e2, int line, int col) {
		super(line, col);
		operatorCode = op;
		factor = e1;
		pri = e2;
	}
    
    static void printOp(int op) {
      switch (op) {
        case sym.PLUS:
          System.out.print(" + ");
          break;
        case sym.MINUS:
          System.out.print(" - ");
          break;
        case -1:
          break;
        default:
          throw new Error("printOp: case not fount");
      }
    }

    void Unparse(int indent) {
      if (operatorCode == -1) {
        //System.out.print("(");
        factor.Unparse(0);
        printOp(operatorCode);
        pri.Unparse(0);
        //System.out.print(")");
      }
      else {
        System.out.print("(");
        factor.Unparse(0);
        printOp(operatorCode);
        pri.Unparse(0);
        System.out.print(")");
      }
    }

    private final exprNode factor;
    private final exprNode pri;
    private final int operatorCode;
} // class factorNode

class binaryOpNode extends exprNode {
	binaryOpNode(exprNode e1, int op, exprNode e2, int line, int col) {
		super(line, col);
		operatorCode = op;
		leftOperand = e1;
		rightOperand = e2;
	}

	static void printOp(int op) {
		switch (op) {
            case sym.TIMES:
                System.out.print(" * ");
                break;
            case sym.SLASH:
                System.out.print(" / ");
                break;
            case -1:
                break;
			default:
				throw new Error("printOp: case not found");
		}
	}

	void Unparse(int indent) {
      if (operatorCode == -1) {
        leftOperand.Unparse(0);
        rightOperand.Unparse(0);
      }
      else {
		System.out.print("(");
		leftOperand.Unparse(0);
		printOp(operatorCode);
		rightOperand.Unparse(0);
		System.out.print(")");
      }
	}

	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		operatorCode = op;
	}

    static void printOp(int op) {
        switch (op) {
          case sym.NOT:
            System.out.print(" !");
            break;
          case -1:
            break;
          default:
            throw new Error("printOp: case not found");
        }
    }

    void Unparse(int indent) {
        printOp(operatorCode);
        operand.Unparse(0);
    }

	private final exprNode operand;
	private final int operatorCode; // Token code of the operator
} // class unaryOpNode 

class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		resultType = t;
	}

    void Unparse(int indent) {
      System.out.print("(");
      resultType.Unparse(0);
      System.out.print(")");
      operand.Unparse(0);
    }

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		methodArgs = a;
	}

    void Unparse(int indent) {
      methodName.Unparse(0);
      System.out.print(" ( ");
      if (methodArgs.isNull()) {}
      else {
        methodArgs.Unparse(0);
      }
      System.out.print(" ) ");
    }

	private final identNode methodName;
	private final argsNode methodArgs;
} // class fctCallNode 

class identNode extends exprNode {
	identNode(String identname, int line, int col) {
		super(line, col);
		idname   = identname;
	}

	void Unparse(int indent) {
		System.out.print(idname);
	}

	private final String idname;
} // class identNode 

class nameNode extends exprNode {
    nameNode() {
      super();
    }
	nameNode(identNode id, exprNode expr, int line, int col) {
		super(line, col);
		varName = id;
		subscriptVal = expr;
	}

	void Unparse(int indent) {
      varName.Unparse(0); // Subscripts not allowed in CSX Lite
      if (subscriptVal.isNull()) {}
      else {
        System.out.print("[");
        subscriptVal.Unparse(0);
        System.out.print("]");
	  }
    }

    identNode returnVar() {
      return varName;
    }

    static nullNameNode NULL = new nullNameNode(); 
	private identNode varName;
	private exprNode subscriptVal;
} // class nameNode 

class nullNameNode extends nameNode {
    nullNameNode() {}
   	boolean   isNull() {return true;}
	void Unparse(int indent) {}
}

class strLitNode extends exprNode {
    strLitNode(String val, int line, int col) {
      super(line, col);
      strval = val;
    }

    void Unparse(int indent) {
      System.out.print(strval);
    }

    private final String strval;
} // class strLitNode

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line, col);
		intval = val;
	}

    void Unparse(int indent) {
      System.out.print(intval);
    }
	private final int intval;
} // class intLitNode 

class charLitNode extends exprNode {
	charLitNode(String val, int line, int col) {
		super(line, col);
		charval = val;
	}

    void Unparse(int indent) {
      System.out.print(charval);
    }

	private final String charval;
} // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {
      System.out.print("true");
    }
} // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col);
	}
    void Unparse(int indent) {
      System.out.print("false");
    }
} // class falseNode 

class exprUnitNode extends exprNode {
	exprUnitNode(exprNode e, int line, int col) {
		super(line, col);
		expr = e;
	}

	void Unparse(int indent) {
		System.out.print("(");
        expr.Unparse(0);
        System.out.print(")");
	}

	private final exprNode expr;
} // class exprUnitNode 
