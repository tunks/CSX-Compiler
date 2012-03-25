
abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;

    static int typeErrors = 0; // Total number of type errors found

	static void genIndent(int indent) {
		for (int i = 1; i <= indent; i += 1) {
			System.out.print("\t");
		}
	} // genIndent

    static void mustBe(boolean assertion) {
        if (! assertion) {
            throw new RuntimeException();
        }
    } // mustBe

    static void typeMustBe(int testType, int requiredType, String errorMsg) {
        if ((testType != Types.Error) && (testType != requiredType)) {
            System.out.println(errorMsg);
            typeErrors++;
        }
    } // typeMustBe

    static void typesMustBeEqual(int type1, int type2, String errorMsg) {
        if ((type1 != Types.Error) && (type2 != Types.Error) && (type1 != type2)) {
            System.out.println(errorMsg);
            typeErrors++;
        }
    } // typesMustBeEqual

    String error() {
        return "Error (line " + linenum + "): ";
    } // error

    public static SymbolTable st = new SymbolTable();

	ASTNode() {
		linenum = -1;
		colnum = -1;
	} // ASTNode()

	ASTNode(int line, int col) {
		linenum = line;
		colnum = col;
	} // ASTNode(line, col)

	boolean isNull() {
		return false;
	} // isNull()

	void Unparse(int indent) {
		// This will normally need to be redefined in a subclass
	} // Unparse()

    void checkTypes() {}
        // This will normally need to be redefined in a subclass
} // abstract class ASTNode

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

// This node is used to root CSX lite programs 
class csxLiteNode extends ASTNode {
	
	csxLiteNode(fieldDeclsNode decls, stmtsNode stmts, int line, int col) {
		super(line, col);
        fields = decls;
        progStmts = stmts;
	} // csxLiteNode() 

	void Unparse(int indent) {
        System.out.println(linenum + ":" + "{");
        fields.Unparse(1);
        progStmts.Unparse(1);
        System.out.println(linenum + ":" + " } EOF");
	} // Unparse()

    void checkTypes() {
        fields.checkTypes();
        progStmts.checkTypes();
    } // checkTypes

    boolean isTypeCorrect() {
        checkTypes();
        return (typeErrors == 0);
    } // isTypeCorrect

	private final stmtsNode progStmts;
    private final fieldDeclsNode fields;
} // class csxLiteNode

// Root of all ASTs for CSX
class classNode extends ASTNode {

	classNode(identNode id, memberDeclsNode memb, int line, int col) {
		super(line, col);
		className = id;
		members = memb;
	} // classNode

    void checkTypes() {
        //className.checkTypes(); // class name never conflicts with any other
                                  // declaration
        members.checkTypes();
    }

    boolean isTypeCorrect() {
        checkTypes();
        return (typeErrors == 0);
    }

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
        moreMembers = mm;
	}

    void checkTypes() {
        fields.checkTypes();
        methods.checkTypes();
        moreMembers.checkTypes();
    }

    void Unparse(int indent) {
      fields.Unparse(indent);
      moreMembers.Unparse(indent);
      methods.Unparse(indent);
    }

    static nullMemberDeclsNode NULL = new nullMemberDeclsNode();
	private declNode fields;
    private memberDeclsNode moreMembers;
	private methodDeclsNode methods;
} // class memberDeclsNode

class nullMemberDeclsNode extends memberDeclsNode {
  nullMemberDeclsNode() {}
  boolean isNull() {return true;}
  void Unparse(int indent) {}
  void checkTypes() {}
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

    void checkTypes() {
        thisField.checkTypes();
        moreField.checkTypes();
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
    void checkTypes() {}
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
      genIndent(indent+1);
      varType.Unparse(0);
      System.out.print(" ");
      varName.Unparse(0);
      if (initValue.isNull()) {}
      else {
        System.out.print(" = ");
        initValue.Unparse(0);
      }
      System.out.println(";");
    }
    
    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(varName.idname);
        if (id == null) {
            id = new SymbolInfo(varName.idname,
                new Kinds(Kinds.Var), varType.type);
            varName.type = varType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            varName.idinfo = id;
            if (!initValue.isNull()) {
                initValue.checkTypes();
                typesMustBeEqual(varName.type.val, initValue.type.val,
                    error() + "Both the left and right"
                    + " hand sides of an assignment must"
                    + " have the same type.");
            }
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            varName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes

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

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        System.out.print("const ");
        constName.Unparse(0);
        System.out.print(" = ");
        constValue.Unparse(0);
        System.out.println(";");
    }

    void checkTypes() {
        constValue.checkType();
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(constName.idname);
        if (id == null) {
            id = new SymbolInfo(constName.idname,
                new Kinds(Kinds.Value), constValue.type);
            constType.type = constValue.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            constName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            constName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes()

	private final identNode constName;
	private final exprNode constValue;
    private final typeNode constType;
} // class constDeclNode

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line, col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        elementType.Unparse(0);
        System.out.print(" ");
        arrayName.Unparse(0);
        System.out.print("[");
        arraySize.Unparse(0);
        System.out.println("];");
    }

    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(varName.idname);
        if (id == null) {
            id = new SymbolInfo(arrayName.idname,
                new Kinds(Kinds.Array), elementType.type);
            arrayName.type = elementType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            arrayName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            arrayName.type = new Types(Types.Error);
        } // id != null
    } //checkTypes

	private final identNode arrayName;
	private final typeNode elementType;
	private final intLitNode arraySize;
} // class arrayDeclNode

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode() {
		super();
	}
	typeNode(int l, int c, Types t) {
		super(l, c);
        type = t;
	}
	static nullTypeNode NULL = new nullTypeNode();
    Types type; // Used for typechecking -- the type of this typeNode
} // class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullTypeNode

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line, col, new Types(Types.Integer));
	} // intTypeNode
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("int");
    } // Unparse
    void checkTypes() {
        // No type checking needed
    } // checkTypes
} // class intTypeNode

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col) {
		super(line, col, new Types(Types.Boolean));
	} // boolTypeNode
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("bool");
    } // Unparse
    void checkTypes() {
        // No type checking needed
    } // checkTypes
} // class boolTypeNode

class charTypeNode extends typeNode {
	charTypeNode(int line, int col) {
		super(line, col, new Types(Types.Character));
	} // charTypeNode
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("char");
    } // Unparse
    void checkTypes() {
        // No type checking needed
    } // checkTypes
} // class charTypeNode

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col) {
		super(line, col, new Types(Types.Void));
	} // voidTypeNode
    void Unparse(int indent) {
        genIndent(indent);
        System.out.print("void");
    }
    void checkTypes() {
        // No type checking needed
    } // checkTypes
} // class voidTypeNode

class optionalSemiNode extends ASTNode {
    optionalSemiNode() {
        super();
    }
    optionalSemiNode(int semi, int line, int col) {
        super(line, col);
        semisym = semi;
    }

    /* need to do(grade) */

    void Unparse(int indent) {
        if (semisym == sym.SEMI) {
          System.out.print(";");
        }
        //System.out.print("\n");
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

    void Unparse(int indent) {
        thisDecl.Unparse(0);
        moreDecls.Unparse(0);
    }

    void checkTypes() {
        thisDecl.checkTypes();
        moreDecls.checkTypes();
    }

	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullMethodDeclsNode 

class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t,
			fieldDeclsNode f, stmtsNode s, optionalSemiNode o, int line, int col) {
		super(line, col);
		name = id;
		args = a;
		returnType = t;
		decls = f;
		stmts = s;
        os = o;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent+1);
        returnType.Unparse(0);
        System.out.print(" ");
        name.Unparse(0);
        System.out.print(" ( ");
        args.Unparse(0);
        System.out.println(" ) {");
        decls.Unparse(indent+1);
        stmts.Unparse(indent+2);
        System.out.print(linenum + ":");
        genIndent(indent+1);
        System.out.print("}");
        os.Unparse(0);
        System.out.print("\n");
    }
    
    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(name.idname);
        if (id == null) {
            id = new SymbolInfo(name.idname,
                new Kinds(Kinds.Method), returnType.type);
            name.type = returnType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            name.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            name.type = new Types(Types.Error);
        }
        st.openScope();
        args.checkTypes();
        decls.checkTypes();
        stmts.checkTypes();
        st.closeScope();
    } // checkTypes

	private final identNode name;
	private final argDeclsNode args;
	private final typeNode returnType;
	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
    private final optionalSemiNode os;
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

    void Unparse(int indent) {
        thisDecl.Unparse(0);
        if (moreDecls.isNull()) {}
        else {
            System.out.print(" , ");
            moreDecls.Unparse(0);
        }
    }

	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullArgDeclsNode 

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		elementType = t;
	}

    void Unparse(int indent) {
        elementType.Unparse(0);
        System.out.print(" ");
        argName.Unparse(0);
        System.out.print("[ ]");
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
    
    void Unparse(int indent) {
        argType.Unparse(0);
        System.out.print(" ");
        argName.Unparse(0);
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
    void checkTypes() {}
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
	} // Unparse

    void checkTypes() {
        thisStmt.checkTypes();
        moreStmts.checkTypes();
    } // checkTypes

	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
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

    void checkTypes() {
        target.checkTypes();
        source.checkTypes();
        //mustBe(target.kind.var == Kinds.Var); //In CSX-lite all IDs should be vars!
        typesMustBeEqual(source.type.val, target.type.val,
            error() + "Both the left and right"
            + " hand sides of an assignment must"
            + " have the same type.");
    } // checkTypes

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class elseNode extends stmtNode {
    elseNode(stmtNode s, int line, int col) {
      super(line,col);
      elsestmt = s;
    }
    elseNode() {}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.println("else " + "{");
        elsestmt.Unparse(indent+1);
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.println("}");
    }

    static nullElseNode NULL = new nullElseNode();
    private stmtNode elsestmt;
} // class elseNode

class nullElseNode extends elseNode {
    nullElseNode() {}
    boolean  isNull() {return true;}
    void Unparse(int indent) {}
} // class nullElseNode

class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, elseNode el, int line, int col) {
		super(line, col);
		condition = e;
		thenPart = s1;
		elsePart = el;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if " + "(");
		condition.Unparse(0);
		System.out.println(") " + "{");
		thenPart.Unparse(indent+1);
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.println("}");
        elsePart.Unparse(indent);
	}

    void checkTypes() {
        condition.checkTypes();
        typeMustBe(condition.type.val, Types.Boolean,
            error() + "The control expression of an" +
            " if must be a bool.");
        thenPart.checkTypes();
        elsePart.checkTypes();
    }

	private final exprNode condition;
	private final stmtNode thenPart;
	private final elseNode elsePart;
} // class ifThenNode

class whileNode extends stmtNode {
	whileNode(exprNode i, exprNode e, stmtNode s, int line, int col) {
		super(line, col);
	 label = i;
	 condition = e;
	 loopBody = s;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        if (label.isNull()) {}
        else {
          label.Unparse(0);
          System.out.print(" : ");
        }
        System.out.print("while");
        System.out.print(" (");
        condition.Unparse(0);
        System.out.println(")");
        if (label.isNull()) {
            loopBody.Unparse(indent+1);
        } else {
            loopBody.Unparse(indent);
        }
    }

	private final exprNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 

class readListNode extends stmtNode {
    readListNode(readNode r, int line, int col) {
        super(line, col);
        read = r;
    }

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("read" + " (");
        read.Unparse(0);
        System.out.println(" );");
    }

    private final readNode read;
} // class readListNode 

class readNode extends stmtNode {
	readNode() {}
	readNode(nameNode n, readNode rn, int line, int col) {
		 super(line, col);
		 targetVar = n;
		 moreReads = rn;
	}

    void Unparse(int indent) {
        targetVar.Unparse(indent);
        if (moreReads.isNull()) {}
        else {
            System.out.print(", ");
            moreReads.Unparse(indent);
        }
    }

	static nullReadNode NULL = new nullReadNode();
	private nameNode targetVar;
	private readNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullReadNode 

class printListNode extends stmtNode {
   printListNode(printNode p, int line, int col) {
        super(line, col);
        print = p;
   }

   void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("print" + " ( ");
        print.Unparse(0);
        System.out.println(" );");
   }

   private final printNode print;
} // class printListNode

class printNode extends stmtNode {
	printNode() {}
	printNode(exprNode val, printNode pn, int line, int col) {
		super(line, col);
		outputValue = val;
		morePrints = pn;
	}

    void Unparse(int indent) {
        genIndent(indent);
        outputValue.Unparse(0);
        if (morePrints.isNull()) {}
        else {
            System.out.print(", ");
            morePrints.Unparse(0);
        }
    }

    void checkTypes() {
        outputValue.checkTypes();
        typeMustBe(outputValue.type.val, Types.Integer,
            error() + "Only int values may be printed.");
    } // checkTypes

	static nullPrintNode NULL = new nullPrintNode();
	private exprNode outputValue;
	private printNode morePrints;
} // class printNode 

class nullPrintNode extends printNode {
	nullPrintNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullPrintNode 

class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		args = a;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        methodName.Unparse(0);
        System.out.print("(");
        args.Unparse(0);
        System.out.println(")" + ";");
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
        genIndent(indent);
        System.out.print("return ");
        returnVal.Unparse(0);
        System.out.println(";");
    }

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, optionalSemiNode o, int line, int col) {
		super(line, col);
		decls = f;
		stmts = s;
        os = o;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.println("{");
        decls.Unparse(indent+1);
        stmts.Unparse(indent+1);
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("}");
        os.Unparse(0);
        System.out.print("\n");
    }

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
    private final optionalSemiNode os;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("break ");
        label.Unparse(0);
        System.out.println(";");
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
        genIndent(indent);
        System.out.print("continue ");
        label.Unparse(0);
        System.out.println(";");
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
      type = new Types();
      kind = new Kinds();
    } // exprNode
    exprNode(int l, int c, Types t, Kinds k) {
        super(l,c);
        type = t;
        kind = k;
    } // exprNode
	static nullExprNode NULL = new nullExprNode();
    protected Types type; // Used for typechecking: the type of this node
    protected Kinds kind; // Used for typechecking: the kind of this node
}

class nullExprNode extends exprNode {
	nullExprNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
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
          mustBe(false);
      } // switch(op)
    } // printOp

    void Unparse(int indent) {
      //System.out.print("(");
      expr.Unparse(0);
      printOp(operatorCode);
      term.Unparse(0);
      //System.out.print(")");
    }

    void checkTypes() {
        mustBe(operatorCode == sym.COR
            ||operatorCode == sym.CAND);
        expr.checkTypes();
        term.checkTypes();
        /* need to do */
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
          System.out.print(" == ");
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
          System.out.print(" ~ ");
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
            System.out.print("!");
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
		super(line, col, new Types(Types.Unknown), new Kinds(Kinds.Var));
		idname   = identname;
        nullFlag = false;
	} // identNode

    identNode(boolean flag) {
        super(0, 0, new Types(Types.Unknown), new Kinds(Kinds.Var));
        idname = "";
        nullFlag = flag;
    } // identNode

    boolean isNull() {return nullFlag;} // Is this node null?

    static identNode NULL = new identNode(true);

	void Unparse(int indent) {
		System.out.print(idname);
	} // Unparse

    void checkTypes() {
        SymbolInfo id;
        //mustBe(kind.val == Kinds.Var); // In CSX-lite all IDs should be vars!
        id = (SymbolInfo) st.localLookup(idname);
        if (id == null) {
          System.out.println(error() + idname + " is not declared.");
          typeErrors++;
          type = new Types(Types.Error);
        } else {
            type = id.type;
            idinfo = id; // Save ptr to correct symbol table entry
        } // id != null
    } // checkTypes

	public String idname;
    public SymbolInfo idinfo; // symbol table entry for this ident
    private final boolean nullFlag;
} // class identNode 

class nameNode extends exprNode {
    nameNode() {
      super();
    }
	nameNode(identNode id, exprNode expr, int line, int col) {
		super(line, col, new Types(Types.Unknown), new Kinds(Kinds.Var));
		varName = id;
		subscriptVal = expr;
	} // nameNode

	void Unparse(int indent) {
      varName.Unparse(0); // Subscripts not allowed in CSX Lite
      if (subscriptVal.isNull()) {}
      else {
        System.out.print("[");
        subscriptVal.Unparse(0);
        System.out.print("]");
	  }
    }

    void checkTypes() {
        varName.checkTypes();
        substriptVal.checkTypes();
        type = varName.type; // need to check type
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
    void checkTypes() {}
}

class strLitNode extends exprNode {
    /*strLitNode(String val, int line, int col) {
      super(line, col);
      strval = val;
    }*/
    strLitNode(String fullstring, string stringval, int line, int col) {
        // need to complete this constructor
    } // strLitNode

    void Unparse(int indent) {
      System.out.print(strval);
    }

    private String fullstr;
    private String strval;
} // class strLitNode

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line, col,new Types(Types.Integer),
            new Kinds(Kinds.Value));
		intval = val;
	} // intLitNode

    void Unparse(int indent) {
      System.out.print(intval);
    } // Unparse

    void checkTypes() {
        // All intLits are automatically type-correct
    } // checkTypes

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

class trueNode extends eprNode {
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

