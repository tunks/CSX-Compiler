import java.util.*;

abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;
    int size;

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

    static void kindMustBe(int testType, int testKind, int requiredKind, String errorMsg) {
        if ((testType != Types.Error) && (testKind != requiredKind)) {
            System.out.println(errorMsg);
            typeErrors++;
        }
    } // kindMustBe

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

    @Override public String toString() {
        return(this.getClass().getName());
    } // toString

    void findClass(String str) {
        if (this.toString() == str)
// grader: less error-prone always to use braces on an "if". -0
            findResult = true;
    } // findClass

    String error() {
        return "Error (line " + linenum + "): ";
    } // error

    public static SymbolTable st = new SymbolTable();

    protected String parentNode;

    protected String brotherNode;

    static boolean findResult;

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
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup("main");
        if (id == null) {
            System.out.println(error() + "Don't declare main method in the class.");
            typeErrors++;
        }
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
	} // memberDeclsNode

    void checkTypes() {
        fields.checkTypes();
        methods.checkTypes();
        moreMembers.checkTypes();
    } // checkTypes
    void Unparse(int indent) {
      fields.Unparse(indent);
      moreMembers.Unparse(indent);
      methods.Unparse(indent);
    } // Unparse

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
        moreFields.checkTypes();
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
    void checkTypes() {}
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
                    error() + "The two hand sides of assignment"
                    + " don't have same type.");
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
        constValue.checkTypes();
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(constName.idname);
        if (id == null) {
            id = new SymbolInfo(constName.idname,
                new Kinds(Kinds.Value), constValue.type);
            constName.type = constValue.type;
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
} // class constDeclNode

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line, col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
        size = arraySize.returnIntVal();
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
        id = (SymbolInfo) st.localLookup(arrayName.idname);
        if (id == null) {
            id = new SymbolInfo(arrayName.idname,
                new Kinds(Kinds.Array), elementType.type);
            arrayName.type = elementType.type;
            id.size = size;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            arrayName.idinfo = id;
            if (id.size <= 0) {
                System.out.println(error() + "The size of " + id.name() + " is not greater than zero.");
                typeErrors++;
                arrayName.type = new Types(Types.Error);
            }
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
        if (name.idname.compareTo("main") == 0) {
            if (returnType.type.val != Types.Void) {
                System.out.println(error() + "The main method must be void.");
                typeErrors++;
            }
            if (!args.isNull()) {
                System.out.println(error() + "Main method shouldn't have parameters.");
                typeErrors++;
            }
        }
        id = (SymbolInfo) st.localLookup("main");
        if (id != null) {
            System.out.println(error() + "method " + name.idname + " declared after main method.");
            typeErrors++;
        }
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
        args.parentNode = name.idname;
        args.checkTypes();
        decls.checkTypes();
        stmts.parentNode = name.idname;
        stmts.checkTypes();
        try {
            st.closeScope();
        } catch (EmptySTException e) {
            /* can't happen */
        }
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

    void checkTypes() {
        thisDecl.parentNode = this.parentNode;
        moreDecls.parentNode = this.parentNode;
        thisDecl.checkTypes();
        moreDecls.checkTypes();
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

    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.globalLookup(this.parentNode);
        if (id != null) {
            id.listKind.add(new Kinds(Kinds.ArrayParm));
            id.listType.add(new Types(elementType.type.val));
        } else {
            System.out.println(error() + "Can't find the method symbol.");
        }
        id = (SymbolInfo) st.localLookup(argName.idname);
        if (id == null) {
            id = new SymbolInfo(argName.idname,
                new Kinds(Kinds.ArrayParm), elementType.type);
            argName.type = elementType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            argName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            argName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes

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

    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.globalLookup(this.parentNode);
        if (id != null) {
            id.listKind.add(new Kinds(Kinds.ScalarParm));
            id.listType.add(new Types(argType.type.val));
        } else {
            System.out.println(error() + "Can't find the method symbol.");
        }
        id = (SymbolInfo) st.localLookup(argName.idname);
        if (id == null) {
            id = new SymbolInfo(argName.idname,
                new Kinds(Kinds.ScalarParm), argType.type);
            argName.type = argType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            argName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            argName.type = new Types(Types.Error);
        } // id != null
    } //checkTypes

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

    void findClass(String str) {
        thisStmt.findClass(str);
        moreStmts.findClass(str);
    } // findClass

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	} // Unparse
    void checkTypes() {
        thisStmt.brotherNode = moreStmts.brotherNode = this.brotherNode;
        thisStmt.parentNode = moreStmts.parentNode = this.parentNode;
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
    void findClass(String str) {}
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
        //System.out.println("target " + target.type.toString() + "  source " + source.type.toString());
        if (target.kind.val == Kinds.Value) {
            System.out.println(error() + "Assignments to constant identifiers is invalid.");
            typeErrors++;
            target.type = new Types(Types.Error);
        } else if (target.type.val == Types.Character && target.kind.val == Kinds.Array && source.type.val == Types.String) {
            if (target.returnVarName().idinfo.size != source.size) {
                System.out.println(error() + "The string literal and character array have different size.");
                typeErrors++;
                target.type = new Types(Types.Error);
            }
        } else if (target.kind.val == Kinds.Array && source.kind.val == Kinds.Array) {
            if (target.type.val != source.type.val || target.size != source.size) {
                System.out.println(error() + "Only same size and component type may be assigned.");
// grader: spelling: assigned.  -0
                typeErrors++;
            }
        } else if (source.kind.val == Kinds.Value && source.type.val == Types.Character && target.kind.val == Kinds.Array) {
            if (target.size != 1 || target.type.val != Types.Character) {
                    System.out.println(error() + "Only same size and component type may be assigned.");
// grader: spelling: assigned.  -0
                    typeErrors++;
            }
        }
        else {
            typesMustBeEqual(source.type.val, target.type.val,
                error() + "The two hand sides of assignment"
                + " don't have same type.");
        }
    } // checkTypes

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class elseNode extends stmtNode {
    elseNode(stmtNode s, int line, int col) {
      super(line,col);
      elsestmt = s;
    } // elseNode
    elseNode() {}
        
    void findClass(String str) {
        elsestmt.findClass(str);
    } // findClass

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.println("else ");
        elsestmt.Unparse(indent+1);
    } // Unparse

    void checkTypes() {
        elsestmt.parentNode = this.parentNode;
        elsestmt.checkTypes();
    } // checkTypes

    static nullElseNode NULL = new nullElseNode();
    private stmtNode elsestmt;
} // class elseNode

class nullElseNode extends elseNode {
    nullElseNode() {}
    boolean  isNull() {return true;}
    void findClass(String str) {}
    void Unparse(int indent) {}
    void checkTypes() {}
} // class nullElseNode

class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, elseNode el, int line, int col) {
		super(line, col);
		condition = e;
		thenPart = s1;
		elsePart = el;
	} // ifThenNode
        
    void findClass(String str) {
        thenPart.findClass(str);
        elsePart.findClass(str);
    } // findClass

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if " + "(");
		condition.Unparse(0);
		System.out.println(") ");
		thenPart.Unparse(indent+1);
        elsePart.Unparse(indent);
	} // Unparse

    void checkTypes() {
        if (this.brotherNode == "while") {
            thenPart.brotherNode = "while";
            elsePart.parentNode = "while";
        }
        thenPart.parentNode = this.parentNode;
        elsePart.parentNode = this.parentNode;
        condition.checkTypes();
        typeMustBe(condition.type.val, Types.Boolean,
            error() + "The control expression of an" +
            " if must be a bool.");
        thenPart.checkTypes();
        elsePart.checkTypes();
    } // checkTypes

	private final exprNode condition;
	private final stmtNode thenPart;
	private final elseNode elsePart;
} // class ifThenNode

class whileNode extends stmtNode {
	whileNode(identNode i, exprNode e, stmtNode s, int line, int col) {
		super(line, col);
	 label = i;
	 condition = e;
	 loopBody = s;
	} // whileNode

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
    } // Unparse

    void checkTypes() {
        loopBody.brotherNode = "while";
        loopBody.parentNode = this.parentNode;
        if (!label.isNull()) {
          
            SymbolInfo id;
            id = (SymbolInfo) st.localLookup(label.idname);
            if (id == null) {
                id = new SymbolInfo(label.idname,
                    new Kinds(Kinds.Label), new Types(Types.Void));
                label.type = new Types(Types.Void);
                try {
                    st.insert(id);
                } catch (DuplicateException d) {
                    /* can't happen */
                } catch (EmptySTException e) {
                    /* can't happen */
                }
                label.idinfo = id;
            } else {
                System.out.println(error() + id.name() + " is already declared.");
                typeErrors++;
                label.type = new Types(Types.Error);
            }
            if (loopBody.toString() == "blockNode") {
                findResult = false;
                loopBody.findClass("breakNode");
                loopBody.findClass("continueNode");
                if (findResult == false) {
                    System.out.println(error() + "The break or continue statement must appear within"
                        + " the body of the while statement that is selected by the label.");
                    typeErrors++;
                }
                else {
                    findResult = false;
                }
            } else if (loopBody.toString() != "breakNode" && loopBody.toString() != "continueNode") {
                System.out.println(error() + "The break or continue statement must appear within"
                    + " the body of the while statement that is selected by the label.");
                typeErrors++;
            }
        }
        condition.checkTypes();
        typeMustBe(condition.type.val, Types.Boolean,
            error() + "The control expression of an" +
            " while must be a bool.");
        loopBody.checkTypes();
    } // checkTypes

	private final identNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 

class readListNode extends stmtNode {
    readListNode(readNode r, int line, int col) {
        super(line, col);
        read = r;
    } // readListNode

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("read" + " (");
        read.Unparse(0);
        System.out.println(" );");
    } // Unparse

    void checkTypes() {
        read.checkTypes();
    } // checkTypes

    private final readNode read;
} // class readListNode 

class readNode extends stmtNode {
	readNode() {}
	readNode(nameNode n, readNode rn, int line, int col) {
		 super(line, col);
		 targetVar = n;
		 moreReads = rn;
	} //readNode

    void Unparse(int indent) {
        targetVar.Unparse(indent);
        if (moreReads.isNull()) {}
        else {
            System.out.print(", ");
            moreReads.Unparse(indent);
        }
    } // Unparse

    void checkTypes() {
        targetVar.checkTypes();
        if (targetVar.type.val != Types.Integer) {
            typeMustBe(targetVar.type.val, Types.Character,
                error() + "Only int and char values may be read.");
        }
        moreReads.checkTypes();
    } // checkTypes

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
   } //printListNode

   void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("print" + " ( ");
        print.Unparse(0);
        System.out.println(" );");
   } // Unparse

   void checkTypes() {
        print.checkTypes();
   } //checkTypes

   private final printNode print;
} // class printListNode

class printNode extends stmtNode {
	printNode() {}
	printNode(exprNode val, printNode pn, int line, int col) {
		super(line, col);
		outputValue = val;
 		morePrints = pn;
	} // printNode

    void Unparse(int indent) {
        genIndent(indent);
        outputValue.Unparse(0);
        if (morePrints.isNull()) {}
        else {
            System.out.print(", ");
            morePrints.Unparse(0);
        }
    } // Unparse

    void checkTypes() {
        outputValue.checkTypes();
        morePrints.checkTypes();

        //System.out.println(outputValue.type.toString());
        if (outputValue.kind.val == Kinds.Array) {
            typeMustBe(outputValue.type.val, Types.Character,
                error() + "For arrays, only char arrays may be written.");
        } else if (outputValue.type.val == Types.String) {
        } else if (outputValue.type.val != Types.Boolean && outputValue.type.val != 
                Types.Character) {
                    typeMustBe(outputValue.type.val, Types.Integer,
                error() + "For values, only int, char, and bool values may be printed.");
        } 
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
	} // callNode

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        methodName.Unparse(0);
        System.out.print("(");
        args.Unparse(0);
        System.out.println(")" + ";");
    } // Unparse

    void checkTypes() {
        boolean isChangeTop = false;
        methodName.parentNode = "callNode";
        methodName.checkTypes();
        if (methodName.type.val != Types.Error) {
            args.parentNode = methodName.idname;
            args.checkTypes();
            typeMustBe(methodName.type.val, Types.Void,
                error() + "Only procedures may be called in statements.");
            SymbolInfo id;
            id = (SymbolInfo) st.globalLookup(methodName.idname);
            while (id != null && id.kind.val != Kinds.Method) {
                st.changeTop();
                id = (SymbolInfo) st.globalLookup(methodName.idname);
                isChangeTop = true;
            }
            if (isChangeTop) {
                st.restoreTop();
            }
            if (id.listType.size() == id.parmListType.size()) {
                Iterator iListType = id.listType.iterator();
                Iterator iListKind = id.listKind.iterator();
                Iterator iParmListType = id.parmListType.iterator();
                Iterator iParmListKind = id.parmListKind.iterator();
                while (iListType.hasNext()) {
                    String listType = iListType.next().toString();
                    String parmListType = iParmListType.next().toString();
                    if (listType != parmListType) {
                        System.out.println(error() +"The type of parameters doesn't match.");
                        System.out.println("\t\t " + "required: " + listType);
                        System.out.println("\t\t " + "found   : " + parmListType);
                        typeErrors++;
                        break;
                    }
                }
                while (iListKind.hasNext()) {
                    String listKind = iListKind.next().toString();
                    String parmListKind = iParmListKind.next().toString();
                    if (listKind == "ScalarParm" && parmListKind == "Array") {
                        System.out.println(error() +"The kind of parameters doesn't match.");
                        System.out.println("\t\t " + "required: " + listKind);
                        System.out.println("\t\t " + "found   : " + parmListKind);
                        typeErrors++;
                        break;
                    } else if (listKind == "ArrayParm" && parmListKind != "Array") {
                        System.out.println(error() +"The kind of parameters doesn't match.");
                        System.out.println("\t\t " + "required: " + listKind);
                        System.out.println("\t\t " + "found   : " + parmListKind);
                        typeErrors++;
                        break;
                    }
                }
                id.parmListType.clear();
                id.parmListKind.clear();
            } else {
                System.out.println(error() + "The number of parameters doesn't match.");
                typeErrors++;
            }
        }
    } // checkTypes

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line, col);
		returnVal = e;
	} //returnNode
    
    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("return ");
        returnVal.Unparse(0);
        System.out.println(";");
    } // Unparse

    void checkTypes() {
        returnVal.checkTypes();
        SymbolInfo id;
        id = (SymbolInfo) st.globalLookup(this.parentNode);
        if (returnVal.isNull()) {
            typeMustBe(id.type.val, Types.Void,
                error() + "Return statements without an expression may"
                + " only appear in procedures(with a void result type).");
        } else {
            if (id.type.val == Types.Void) {
                System.out.println(error() + "Return statements with an"
                    + " expression may only appear in functions(with a "
                    + "non-void result type).");
                typeErrors++;
            } else {
                typesMustBeEqual(returnVal.type.val, id.type.val,
                    error() + "The return expression don't have the same type with"
                    + " the function.");
            }
        }
    }

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, optionalSemiNode o, int line, int col) {
		super(line, col);
		decls = f;
		stmts = s;
        os = o;
        stmts.brotherNode = this.brotherNode;
	} // blockNode

    void findClass(String str) {
        stmts.findClass(str);
    } // findClass

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
    } // Unparse

    void checkTypes() {
        stmts.brotherNode = this.brotherNode;
        st.openScope();
        decls.checkTypes();
        stmts.checkTypes();
        try {
            st.closeScope();
        } catch (EmptySTException e) {
            /* can't happen */
// grader: then put in a bug warning. -0
        }
    } // checkTypes

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
    private final optionalSemiNode os;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	} // breakNode

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("break ");
        label.Unparse(0);
        System.out.println(";");
    } // Unparse

    void checkTypes() {
        label.checkTypes();
        if (this.brotherNode == "while") {
            kindMustBe(label.type.val, label.kind.val, Kinds.Label, error()
                + "The break label doesn't match while label.");
        }
    } // checkTypes

	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	} // continueNode

    void Unparse(int indent) {
        System.out.print(linenum + ":");
        genIndent(indent);
        System.out.print("continue ");
        label.Unparse(0);
        System.out.println(";");
    } // Unparse

    void checkTypes() {
        label.checkTypes();
        if (this.brotherNode == "while") {
            kindMustBe(label.type.val, label.kind.val, Kinds.Label, error()
                + "The continue label doesn't match while label.");
        }
    } // checkTypes

	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {}
	argsNode(exprNode e, argsNode a, int line, int col) {
		super(line, col);
		argVal = e;
		moreArgs = a;
	} // argsNode

    void Unparse(int indent) {
      argVal.Unparse(0);
      if (moreArgs.isNull()) {}
      else {
        System.out.print(",");
        moreArgs.Unparse(0);
      }
    } // Unparse

    void checkTypes() {
        boolean isChangeTop = false;
        argVal.parentNode = this.parentNode;
        moreArgs.parentNode = this.parentNode;
        argVal.checkTypes();
        SymbolInfo id;
        id = (SymbolInfo) st.globalLookup(argVal.parentNode);
        while (id != null && id.kind.val != Kinds.Method) {
            st.changeTop();
            id = (SymbolInfo) st.globalLookup(argVal.parentNode);
            isChangeTop = true;
        }
        if (isChangeTop) {
            st.restoreTop();
        }
        if (id != null) {
            id.parmListType.add(argVal.type);
            id.parmListKind.add(argVal.kind);
        } else {
            /* It can't happen */
        }
        moreArgs.checkTypes();
    } // checkTypes

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
    void checkTypes() {}
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
    int size;
	static nullExprNode NULL = new nullExprNode();
    protected Types type; // Used for typechecking: the type of this node
    protected Kinds kind; // Used for typechecking: the kind of this node
} // abstract class exprNode

class nullExprNode extends exprNode {
	nullExprNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
} // class nullExprNode 

class booleanOpNode extends exprNode {
    booleanOpNode(exprNode e, int op, exprNode r, int line, int col) {
      super(line, col);
      expr = e;
      term = r;
      operatorCode = op;
    } // booleanOpNode

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
    } // Unparse

    void checkTypes() {
        expr.checkTypes();
        term.checkTypes();
        if (expr.isNull()) {}
        else {
            mustBe(operatorCode == sym.COR
                ||operatorCode == sym.CAND);
            typeMustBe(expr.type.val, Types.Boolean,
                error() + "Only bool values may be applied.");
            typeMustBe(term.type.val, Types.Boolean,
                error() + "Only bool values may be applied.");
        }
        type = new Types(Types.Boolean);
        size = expr.size;
        //kind = expr.kind;
     } // checkTypes

    private final exprNode expr;
    private final exprNode term;
    private final int operatorCode;
} // booleanOpNode class

class relationOpNode extends exprNode {
    relationOpNode(exprNode f1, int op, exprNode f2, int line, int col) {
        super(line, col);
        firstFactor = f1;
        secondFactor = f2;
        operatorCode = op;

    } // relationOpNode

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
    } // printOp

    void checkTypes() {
        firstFactor.checkTypes();
        secondFactor.checkTypes();
        if (secondFactor.isNull()) {}
        else {
            mustBe(operatorCode == sym.LT || operatorCode == sym.GT
                || operatorCode == sym.LEQ || operatorCode == sym.GEQ
                || operatorCode == sym.EQ || operatorCode == sym.NOTEQ);
            if (firstFactor.type.val != Types.Integer && firstFactor.type.val 
                    != Types.Character && firstFactor.type.val != Types.Boolean) {
              typeMustBe(firstFactor.type.val, Types.Integer,
                  error() + "Only int, char and bool values may be applied in"
                  + " relational operators.");
            }
            if (secondFactor.type.val != Types.Integer && secondFactor.type.val 
                    != Types.Character && secondFactor.type.val != Types.Boolean) {
              typeMustBe(secondFactor.type.val, Types.Integer,
                    error() + "Only int, char and bool values may be applied in"
                  + " relational operators.");
            }
        }
        type = new Types(Types.Boolean);
        size = firstFactor.size;
        //kind = firstFactor.type;
    } // checkTypes

    void Unparse(int indent) {
        //System.out.print("(");
        firstFactor.Unparse(0);
        printOp(operatorCode);
        secondFactor.Unparse(0);
        //System.out.print(")");
    } // Unparse

    private final exprNode firstFactor;
    private final exprNode secondFactor;
    private final int operatorCode;
    public int size;
} // class relationOpNode

class factorNode extends exprNode {
    factorNode(exprNode e1, int op, exprNode e2, int line, int col) {
		super(line, col);
		operatorCode = op;
		factor = e1;
		pri = e2;
	} // factorNode
    
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
    } // Unparse

    void checkTypes() {
        factor.checkTypes();
        pri.checkTypes();
        if (pri.isNull()) {}
        else {
            mustBe(operatorCode == sym.PLUS
                || operatorCode == sym.MINUS);
            if (factor.type.val != Types.Integer && 
                factor.type.val != Types.Character) {
                typeMustBe(factor.type.val, Types.Integer,
                    error() + "LeftOperand must be int or char type.");
            }
            if (pri.type.val != Types.Integer &&
                pri.type.val != Types.Character) {
                typeMustBe(pri.type.val, Types.Integer,
                    error() + "RightOperand must be int or char type.");
            }
        }
        type = new Types(Types.Integer);
        size = factor.size;
    } // checkTypes

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
	} // binaryOpNode

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
	} // Unparse

    void checkTypes() {
        leftOperand.checkTypes();
        rightOperand.checkTypes();
        if (rightOperand.isNull()) {}
        else {
            mustBe(operatorCode == sym.SLASH
                || operatorCode == sym.TIMES);
            if (leftOperand.type.val != Types.Integer && 
                leftOperand.type.val != Types.Character) {
                typeMustBe(leftOperand.type.val, Types.Integer,
                    error() + "LeftOperand must be int or char type.");
            }
            if (rightOperand.type.val != Types.Integer &&
                rightOperand.type.val != Types.Character) {
                typeMustBe(rightOperand.type.val, Types.Integer,
                    error() + "RightOperand must be int or char type.");
            }
        }
        type = new Types(Types.Integer);
        size = leftOperand.size;
    } // checkTypes

	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		operatorCode = op;
	} // unaryOpNode

    static void printOp(int op) {
        switch (op) {
          case sym.NOT:
            System.out.print("!");
            break;
          case -1:
            break;
          default:
            throw new Error("printOp: case not found");
// grader: don't throw raw exceptions. -1
        }
    }

    void Unparse(int indent) {
        printOp(operatorCode);
        operand.Unparse(0);
    } // Unparse

    void checkTypes() {
        operand.checkTypes();
        type = new Types(Types.Boolean);
        if (operatorCode == sym.NOT) {
            typeMustBe(operand.type.val, Types.Boolean,
                error() + "Only bool values may be applied"
                + " in logical operators.");
            if (operand.type.val != Types.Boolean) {
                type = new Types(Types.Error);
            }
        }
        size = operand.size;
    } // checkTypes

	private final exprNode operand;
	private final int operatorCode; // Token code of the operator
} // class unaryOpNode 

class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		resultType = t;
	} // castNode

    void Unparse(int indent) {
      System.out.print("(");
      resultType.Unparse(0);
      System.out.print(")");
      operand.Unparse(0);
    } // Unparse

    void checkTypes() {
        resultType.checkTypes();
        operand.checkTypes();
        if (operand.type.val != Types.Integer && operand.type.val != Types.Character && operand.type.val != Types.Boolean) {
            typeMustBe(operand.type.val, Types.Integer,
                error() + "Only int, char and bool type may"
                + " be type-cast.");
        }
        type = resultType.type;
    } // checkTypes

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		methodArgs = a;
	} // fctCallNode

    void Unparse(int indent) {
      methodName.Unparse(0);
      System.out.print(" ( ");
      if (methodArgs.isNull()) {}
      else {
        methodArgs.Unparse(0);
      }
      System.out.print(" ) ");
    } // Unparse

    void checkTypes() {
      int numberOfBackUp= 0;
      int numberOfPrevBackUp = 0;
      boolean isChangeTop = false;
      methodName.parentNode = "callNode";
      methodName.checkTypes();
      type = methodName.type;
      kind = methodName.kind;
      if (methodName.type.val == Types.Void) {
            typeMustBe(methodName.type.val, Types.Integer,
                error() + "Only non-void result type"
                + " may be called in expression.");
            type = new Types(Types.Error);
       }
       SymbolInfo id;
       id = (SymbolInfo) st.globalLookup(methodName.idname);
       while (id != null && id.kind.val != Kinds.Method) {
            st.changeTop();
            id = (SymbolInfo) st.globalLookup(methodName.idname);
            isChangeTop = true;
       }
       if (isChangeTop) {
             st.restoreTop();
       }
       if (id != null && id.parmListType.size() != 0) { // back up previous parmlist
            Iterator iParmListType = id.parmListType.iterator();
            Iterator iParmListKind = id.parmListKind.iterator();
            numberOfPrevBackUp = id.backupParmListType.size();
            while(iParmListType.hasNext()) {
                id.backupParmListType.add((Types)iParmListType.next());
                id.backupParmListKind.add((Kinds)iParmListKind.next());
            }
            numberOfBackUp = id.parmListType.size();
            id.parmListType.clear();
            id.parmListKind.clear();
       }
       methodArgs.parentNode = methodName.idname;
       methodArgs.checkTypes();
       if (id.listType.size() == id.parmListType.size()) {
            Iterator iListType = id.listType.iterator();
            Iterator iListKind = id.listKind.iterator();
            Iterator iParmListType = id.parmListType.iterator();
            Iterator iParmListKind = id.parmListKind.iterator();
            while (iListType.hasNext()) {
                String listType = iListType.next().toString();
                String parmListType = iParmListType.next().toString();
                if (listType != parmListType) {
                    System.out.println(error() +"The type of parameters doesn't match.");
                    System.out.println("\t\t " + "required: " + listType);
                    System.out.println("\t\t " + "found   : " + parmListType);
                    typeErrors++;
                    break;
                }
            }
            while (iListKind.hasNext()) {
                String listKind = iListKind.next().toString();
                String parmListKind = iParmListKind.next().toString();
// grader: parmListKind could be final, as could 17 other variables.  -0
                if (listKind == "ScalarParm" && parmListKind == "Array") {
                    System.out.println(error() +"The kind of parameters doesn't match.");
                    System.out.println("\t\t " + "required: " + listKind);
                    System.out.println("\t\t " + "found   : " + parmListKind);
                    typeErrors++;
                    break;
                } else if (listKind == "ArrayParm" && parmListKind != "Array") {
                    System.out.println(error() +"The kind of parameters doesn't match.");
                    System.out.println("\t\t " + "required: " + listKind);
                    System.out.println("\t\t " + "found   : " + parmListKind);
                    typeErrors++;
                    break;
                }
            }

        } else {
            System.out.println(id.parmListType.size());
            System.out.println(error() + "The number of parameters doesn't match.");
            typeErrors++;
        }
        id.parmListType.clear();
        id.parmListKind.clear();
        if (numberOfBackUp != 0) { // restore the backup parmlist
            for (int i = 0; i < numberOfBackUp; i++) {
                id.parmListType.add(id.backupParmListType.get(numberOfPrevBackUp + i));
                id.parmListKind.add(id.backupParmListKind.get(numberOfPrevBackUp + i));
            }
            for (int i = 0; i < numberOfBackUp; i++) {
                id.backupParmListType.remove(numberOfPrevBackUp);
                id.backupParmListKind.remove(numberOfPrevBackUp);
            }
        }
    } // checkTypes

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
        boolean isChangeTop = false;
        SymbolInfo id;
        //mustBe(kind.val == Kinds.Var); // In CSX-lite all IDs should be vars!
        id = (SymbolInfo) st.localLookup(idname);
        if (id == null) {
            id = (SymbolInfo) st.globalLookup(idname);
        }
        if (id == null) {
          System.out.println(error() + " is not declared.");
// grader: print idname.  -1
          typeErrors++;
          type = new Types(Types.Error);
        } else {
            while (id != null && this.parentNode == "callNode" && id.kind.val != Kinds.Method) {
// grader: don't use == to compare strings!  That's pointer comparison.  -1
            //while (id != null && id.kind.val != Kinds.Method) {
                st.changeTop();
                id = (SymbolInfo) st.globalLookup(idname);
                isChangeTop = true;
            }
            if (isChangeTop) {
                st.restoreTop();
            }
            if (id != null) {
// grader: more readable to put positive case first.  -0
                type = id.type;
                kind = id.kind;
                size = id.size;
                idinfo = id; // Save ptr to correct symbol table entry
            } else {
                System.out.println(error() + idname + " is not declared.");
                typeErrors++;
                type = new Types(Types.Error);
            }
        } // id != null
        //size = id.size;
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
// grader: avoid empty branches of "if".  -0
      else {
        System.out.print("[");
        subscriptVal.Unparse(0);
        System.out.print("]");
	  }
    } // nameNode

    void checkTypes() {
        varName.checkTypes();
        subscriptVal.checkTypes();
        if (subscriptVal.isNull() != true) {
            if (subscriptVal.type.val != Types.Integer 
                && subscriptVal.type.val != Types.Character) {
// grader: These nested "if"s can be combined. -0
                    System.out.println(error() + "Only int or char type"
                        + " may be used to index arrays.");
                    typeErrors++;
                    type = new Types(Types.Error);
             }
        }
        if (type.val != Types.Error) {
            type = varName.type;
            kind = varName.kind;
        }
        if (subscriptVal.isNull() != true) {
// grader: silly.  Never compare against true or false. -1
            kind = new Kinds(Kinds.Var);
        }
        size = varName.size;
    } // checkTypes


    identNode returnVarName() {
      return varName;
    } // returnVarName


    static nullNameNode NULL = new nullNameNode(); 
	private identNode varName;
	private exprNode subscriptVal;
} // class nameNode 

class nullNameNode extends nameNode {
    nullNameNode() {}
// grader: best to document empty constructors. -0
   	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
// grader: best to document empty methods.  -0
}

class strLitNode extends exprNode {

    strLitNode(String fullstring, String stringval, int line, int col) {
      super(line, col, new Types(Types.String),
          new Kinds(Kinds.Value));
      fullstr = fullstring;
      strval = stringval;
      size = strval.length();
    } // strLitNode

    /*strLitNode(String val, int line, int col) {
      super(line, col, new Types(Types.String),
          new Kinds(Kinds.Value));
      strval = val;
    } // strLitNode */

    void Unparse(int indent) {
      System.out.print(strval);
    }

    void checkTypes() {
        // All strLits are automatically type-correct
    } // checkTypes

    private final String fullstr;
// grader: you never use fullstr.  -0
    private final String strval;
} // class strLitNode

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line, col, new Types(Types.Integer),
            new Kinds(Kinds.Value));
		intval = val;
	} // intLitNode

    int returnIntVal() {
        return intval;
    }

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
		super(line, col, new Types(Types.Character),
            new Kinds(Kinds.Value));
		charval = val;
	} // charLitNode

    void Unparse(int indent) {
      System.out.print(charval);
    } // Unparse

    void checkTypes() {
        // All charLits are automatically type-correct
    } // checkTypes

	private final String charval;
} // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col, new Types(Types.Boolean),
            new Kinds(Kinds.Value));
	} // trueNode

    void Unparse(int indent) {
      System.out.print("true");
    } // Unparse

    void checkTypes() {
        // All trueNodes are automatically type-correct
    } // checkTypes
} // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col, new Types(Types.Boolean),
            new Kinds(Kinds.Value));
	} // falseNode

    void Unparse(int indent) {
        System.out.print("false");
    } // Unparse

    void checkTypes() {
        // All falseNodes are automatically type-correct
    } // checkTypes
} // class falseNode 

class exprUnitNode extends exprNode {
	exprUnitNode(exprNode e, int line, int col) {
		super(line, col);
		expr = e;
	} //exprUnitNode

	void Unparse(int indent) {
		System.out.print("(");
        expr.Unparse(0);
        System.out.print(")");
	} // Unparse

    void checkTypes() {
        expr.checkTypes();
        type = expr.type;
    } // checkTypes

	private final exprNode expr;
} // class exprUnitNode 

