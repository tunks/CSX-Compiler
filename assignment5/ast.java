import java.io.*;
import java.util.*;

abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;
    int size;

    static PrintStream afile; // File to generate JVM code into

    static int typeErrors = 0;     // Total number of type errors found
    static int cgErrors = 0;       // Total number of code gen errors

    static int numberOfLocals = 0; // Total number of local vars

    static int labelCnt = 0;       // Counter used to gen unique labels

	static void genIndent(int indent) {
		for (int i = 1; i <= indent; i += 1) {
			System.out.print("\t");
		}
	} // genIndent

    static void myAssert(boolean assertion) {
        if (! assertion)
             throw new RuntimeException();
    }

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
            findResult = true;
    } // findClass

    String error() {
        return "Error (line " + linenum + "): ";
    } // error

    // generate an instruction w/ 0 operand
    static void gen(String opcode) {
        afile.println("\t"+opcode);
    }

    // generate an instruction w/ 1 operand
    static void gen(String opcode, String operand) {
        afile.println("\t"+opcode+"\t"+operand);
    }

    static void gen(String opcode, int operand) {
        afile.println("\t"+opcode+"\t"+operand);
    }

    // generate an instruction w/ 2 operands
    static void gen(String opcode, String operand1, String operand2) {
        afile.println("\t"+opcode+"\t"+operand1+" "+operand2);
    }

    // generate an instruction w/ 2 operands
    static void gen(String opcode, String operand1, int operand2) {
        afile.println("\t"+opcode+"\t"+operand1+" "+operand2);
    }

    // build label of form labeln
    String buildlabel(int suffix) {
        return "label"+suffix;
    }

    // generate a label for an instruction
    void genlab(String label) {
        afile.println(label+":");
    }


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


    //  codegen translates the AST rooted by this node 
    //      into JVM code which is written in asmFile
    //  If no errors occur during code generation,
    //    TRUE is returned, and asmFile should contain a
    //      complete and correct JVM program.
    //  Otherwise, FALSE is returned and asmFile need not
    //    contain a valid program.

    boolean codegen(PrintStream asmfile) {
        throw new Error();
    } // This version of codegen should never be called

    //  cg translates its AST node into JVM code
    //  The code which is written in the shared PrintStream variable
    //    afile (set by codegen)

    void cg(){}; // This member is normally overridden in subclasses

} // abstract class ASTNode

class nullNode extends ASTNode {
// This class definition probably doesn't need to be changed
	nullNode() {super();};
	boolean isNull() {return true;};
	void Unparse(int indent) {};
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
        members.parentNode = "class";
        members.checkTypes();
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup("main");
        if (id == null) {
            System.out.println(error() + "Not declare main method in the class.");
            typeErrors++;
        }
    } // checkTypes

    boolean isTypeCorrect() {
        checkTypes();
        return (typeErrors == 0);
    } // isTypeCorrect

    void Unparse(int indent) {
      System.out.print(linenum + ":" + " class ");
      className.Unparse(0);
      System.out.println(" {");
      members.Unparse(0);
      System.out.println(linenum + ":" + " } EOF");
    } // Unparse

    boolean codegen(PrintStream asmfile) {
        afile = asmfile;
        cg();
        return (cgErrors == 0);
    } // codegen

    void cg() {
        SymbolInfo.className = className.idname;
        gen(".class","public",className.idname);
        gen(".super","java/lang/Object\n");

        members.cg();
    } // cg

	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
    memberDeclsNode () {
      super();
    }
	memberDeclsNode(declNode f, memberDeclsNode mm, int line, int col) {
		super(line, col);
		fields = f;
		methods = methodDeclsNode.NULL;
        moreMembers = mm;
	} // memberDeclsNode

    memberDeclsNode(methodDeclsNode m, int line, int col) {
        super(line, col);
        fields = declNode.NULL;
        moreMembers = memberDeclsNode.NULL;
        methods = m;
    }

    void checkTypes() {
        fields.parentNode = this.parentNode;
        moreMembers.parentNode = this.parentNode;
        fields.checkTypes();
        methods.checkTypes();
        moreMembers.checkTypes();
    } // checkTypes
    void Unparse(int indent) {
      fields.Unparse(indent);
      moreMembers.Unparse(indent);
      methods.Unparse(indent);
    } // Unparse

    void cg() {
        fields.cg();
        if (moreMembers.fields == null) {
            gen(".method"," public static","main([Ljava/lang/String;)V");
            //System.out.println(fields.getClass().getName());
            gen("invokestatic",SymbolInfo.className+"/main()V"); // className /main()V
            gen("return");
            gen(".limit", "stack", 2);
            gen(".end method\n");
            SymbolInfo.hasWrittenMain = 1;
            /*
             * Need to do dostore initValue
             */
        }
        moreMembers.cg();
        methods.cg();
    } // cg

    static nullMemberDeclsNode NULL = new nullMemberDeclsNode();
	private declNode fields;
    private memberDeclsNode moreMembers;
	private methodDeclsNode methods;
} // class memberDeclsNode

class nullMemberDeclsNode extends memberDeclsNode {
  nullMemberDeclsNode() {super();}
  boolean isNull() {return true;}
  void Unparse(int indent) {}
  void checkTypes() {}
  void cg() {}
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
    } // Unparse

    void checkTypes() {
        thisField.parentNode = this.parentNode;
        moreFields.parentNode = this.parentNode;
        thisField.checkTypes();
        moreFields.checkTypes();
    } // checkTypes

    void cg() {
        thisField.cg();
        moreFields.cg();
    } // cg

	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode thisField;
	private fieldDeclsNode moreFields;
} // class fieldDeclsNode

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode() {super();}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
    nullDeclNode() {super();}
    boolean isNull() {return true;}
    void Unparse(int ident) {}
    void checkTypes() {}
    void cg() {}
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
    } // Unparse

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

    void cg() {
        // Given this variable an index equal to numberOfLocals
        //   and remember index in ST
          if ("class".equals(this.parentNode)) {
              varName.idinfo.varIndex = -1;
              varName.idinfo.fieldInfo = SymbolInfo.className+"/"+varName.idname;
              gen(".field static",varName.idname,varType.returnType());
          }
          else {
              varName.idinfo.varIndex = numberOfLocals;
              // Increment numberOfLocals used in this prog
              numberOfLocals++;
          }
        if (!initValue.isNull()) {
            initValue.cg();
            if ("class".equals(this.parentNode)) {
                gen("putstatic",varName.idinfo.fieldInfo,varType.returnType());
            } else {
                gen("istore",varName.idinfo.varIndex);
            }
        }
    } // cg

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
    } // Unparse

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

    void cg() {
        if ("class".equals(this.parentNode)) {
          constName.idinfo.varIndex = -1;
          constName.idinfo.fieldInfo = SymbolInfo.className+"/"+constName.idname;
          if (constValue.type.val == Types.Integer)
            gen(".field static",constName.idname,"I");
          else if (constValue.type.val == Types.Boolean)
            gen(".field static",constName.idname,"Z");
          else if (constValue.type.val == Types.Character)
            gen(".field static",constName.idname,"C");
          else if (constValue.type.val == Types.String)
            gen(".field static",constName.idname,"S");
          /*
           * Need to do do check if "S" are correct.
           */
        }
        else {
            constName.idinfo.varIndex = numberOfLocals;
            numberOfLocals++;
        }
        constValue.cg();
        if ("class".equals(this.parentNode)) {
            //gen("putstatic",constName.idinfo.fieldInfo,varType.returnType());
            /*
             * Need to do do store value to field
             */
        } else {
            gen("istore",constName.idinfo.varIndex);
        }
    } // cg

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
    } // Unparse

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

    void cg() {
         arrayName.idinfo.varIndex = numberOfLocals;
         numberOfLocals++;
         arraySize.cg();
         gen("newarray", elementType.returnFullType());
         gen("astore", arrayName.idinfo.varIndex);
    } // cg

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
    String returnType(){return null;}
    String returnFullType() {return null;}
	static nullTypeNode NULL = new nullTypeNode();
    Types type; // Used for typechecking -- the type of this typeNode
} // class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {super();}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
} // class nullTypeNode

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line, col, new Types(Types.Integer));
	} // intTypeNode
    void Unparse(int indent) {
      genIndent(indent);
      System.out.print("int");
    } // Unparse
    String returnType() {
        return "I";
    }
    String returnFullType() {
        return "int";
    }
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
    String returnType() {
        return "Z";
    }
    String returnFullType() {
        return "boolean";
    }
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
    String returnType() {
        return "C";
    }
    String returnFullType() {
        return "char";
    }
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
    String returnType() {
        return "V";
    }
    String returnFullType() {
        return "void";
    }
} // class voidTypeNode

class optionalSemiNode extends ASTNode {
    optionalSemiNode() {
        super();
    }
    optionalSemiNode(int semi, int line, int col) {
        super(line, col);
        semisym = semi;
    }
    void Unparse(int indent) {
        if (semisym == sym.SEMI) {
          System.out.print(";");
        }
    }
    private int semisym;
    static nullOptionalSemiNode NULL = new nullOptionalSemiNode();
} // optionalSemiNode class

class nullOptionalSemiNode extends optionalSemiNode {
  nullOptionalSemiNode() {super();};
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
    } // Unparse

    void checkTypes() {
        thisDecl.checkTypes();
        moreDecls.checkTypes();
    } // checkTypes

    void cg() {
        thisDecl.cg();
        moreDecls.cg();
    } // cg

	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
    } // Unparse

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

    void cg() {
        storeNumOfLocals = numberOfLocals;
        numberOfLocals = 0;
        args.cg();
        if (args.isNull()) {
            name.idinfo.methodSigna = name.idname+"()"+returnType.returnType();
            gen(".method","public static",name.idinfo.methodSigna);
        }
        else {
            // Add args within ()
            calArgs = args;
            if (!calArgs.returnMoreDecls().isNull()) {
                argsType = calArgs.returnThisDecl().returnArgsType();
                calArgs = calArgs.returnMoreDecls();
            }
            while (!calArgs.returnMoreDecls().isNull()) {
                argsType += calArgs.returnThisDecl().returnArgsType();
                calArgs = calArgs.returnMoreDecls();
            }
            argsType += calArgs.returnThisDecl().returnArgsType();
            name.idinfo.methodSigna = name.idname+"("+argsType+")"+returnType.returnType();
            gen(".method","public static",name.idinfo.methodSigna);
        }
        decls.cg();
        stmts.cg();
        gen("return");
        if (numberOfLocals > 0) {
            gen(".limit","locals",numberOfLocals);
        }
        gen(".limit","stack",10);
        gen(".end","method\n");
        numberOfLocals = storeNumOfLocals;
    } // cg

    private String argsType;
    private argDeclsNode calArgs;
    private int storeNumOfLocals;
	
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
    String returnArgsType() {return null;};
}

class argDeclsNode extends ASTNode {
	argDeclsNode() {super();}
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
    } // Unparse

    void checkTypes() {
        thisDecl.parentNode = this.parentNode;
        moreDecls.parentNode = this.parentNode;
        thisDecl.checkTypes();
        moreDecls.checkTypes();
    } // checkTypes

    void cg() {
        thisDecl.cg();
        moreDecls.cg();
    } // cg

    argDeclNode returnThisDecl() {return this.thisDecl;}
    argDeclsNode returnMoreDecls() {return this.moreDecls;}

	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
    } // Unparse

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

    String returnArgsType() {
        return "["+elementType.returnType();
    } // returnArgsType

    void cg() {
        argName.idinfo.varIndex = numberOfLocals;
        numberOfLocals++;
    } // cg

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
    } // Unparse

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

    String returnArgsType() {
        return argType.returnType();
    } // returnArgsType

    void cg() {
        argName.idinfo.varIndex = numberOfLocals;
        numberOfLocals++;
    } // cg

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
	nullStmtNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
} // class nullStmtNode 

class stmtsNode extends ASTNode {
	stmtsNode() {super();}
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col) {
		super(line, col);
		thisStmt = stmt;
		moreStmts = stmts;
	}

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
    void cg() {
        thisStmt.cg();
        moreStmts.cg();
    } // cg

	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void findClass(String str) {}
    void cg() {}
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
	} // Unparse

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
                typeErrors++;
            }
        } else if (source.kind.val == Kinds.Value && source.type.val == Types.Character && target.kind.val == Kinds.Array) {
            if (target.size != 1 || target.type.val != Types.Character) {
                    System.out.println(error() + "Only same size and component type may be assigned.");
                    typeErrors++;
            }
        }
        else {
            typesMustBeEqual(source.type.val, target.type.val,
                error() + "The two hand sides of assignment"
                + " don't have same type.");
        }
    } // checkTypes

    void cg() {
        target.parentNode = "target";
        gen("; begin asg operation");
        target.cg();
          /*
           * Need to do do check array length
           */
        source.cg();
        if ("strLitNode".equals(source.getClass().getName()))
            gen("invokestatic", "CSXLib/convertString(Ljava/lang/String;)[C");

        if (target.returnVarName().idinfo.kind.val == Kinds.Array ||
            target.returnVarName().idinfo.kind.val == Kinds.ArrayParm) {
            if (target.returnSub().isNull())
               gen("astore", target.returnVarName().idinfo.varIndex);
            else {
              if (target.returnVarName().idinfo.type.val == Types.Integer)
                gen("iastore");
              else if (target.returnVarName().idinfo.type.val == Types.Character)
                gen("castore");
              else if (target.returnVarName().idinfo.type.val == Types.Boolean)
                gen("bastore");
            }
        }
        else {
            gen("istore", target.returnVarName().idinfo.varIndex);
        }
    } // cg

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class elseNode extends stmtNode {
    elseNode(stmtNode s, int line, int col) {
      super(line,col);
      elsestmt = s;
    } // elseNode
    elseNode() {super();}

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

    void cg() {
        elsestmt.cg();
    } // cg

    static nullElseNode NULL = new nullElseNode();
    private stmtNode elsestmt;
} // class elseNode

class nullElseNode extends elseNode {
    nullElseNode() {super();}
    boolean  isNull() {return true;}
    void findClass(String str) {}
    void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
        if ("while".equals(this.brotherNode)) {
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

    void cg() {
        gen("; if statement");
        condition.cg();
        gen("ifeq",buildlabel(++labelCnt));
        thisLabelCnt = labelCnt;
        labelCnt += 2;
        thenPart.cg();
        gen("goto",buildlabel(thisLabelCnt+1));
        gen("; else statement");
        genlab(buildlabel(thisLabelCnt));
        elsePart.cg();
        genlab(buildlabel(thisLabelCnt+1));
    } // cg

    private int thisLabelCnt;
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

    void cg() {
      gen("; while statement");
      labelCnt++;
      thisLabelCnt = labelCnt;
      if (!label.isNull()) {
          label.idinfo.headOfLoop = thisLabelCnt;
          label.idinfo.loopExit = thisLabelCnt+1;
      }
      genlab(buildlabel(thisLabelCnt));
      labelCnt += 2;
      condition.cg();
      gen("ifeq",buildlabel(thisLabelCnt+1));
      loopBody.cg();
      gen("goto",buildlabel(thisLabelCnt));
      genlab(buildlabel(thisLabelCnt+1));
    } // cg

    private int thisLabelCnt;
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
    void cg(){
        read.cg();
    } // cg

    private final readNode read;
} // class readListNode 

class readNode extends stmtNode {
	readNode() {super();}
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

    void cg() {
        gen("; read operation");
        if (targetVar.type.val == Types.Integer) {
            gen("invokestatic", "CSXLib/readInt()I");
        } else if (targetVar.type.val == Types.Character) {
            gen("invokestatic", "CSXLib/readChar()C");
        }
        if (targetVar.returnVarName().idinfo.varIndex != -1) {
            gen("istore", targetVar.returnVarName().idinfo.varIndex);
        } else {
            if (targetVar.type.val == Types.Integer) {
                gen("putstatic", targetVar.returnVarName().idinfo.fieldInfo, "I");
            } else if (targetVar.type.val == Types.Character) {
                gen("putstatic", targetVar.returnVarName().idinfo.fieldInfo, "C");
            }
        }
        moreReads.cg();
    } // cg

	static nullReadNode NULL = new nullReadNode();
	private nameNode targetVar;
	private readNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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

   void cg() {
        print.cg();
   } // cg

   private final printNode print;
} // class printListNode

class printNode extends stmtNode {
	printNode() {super();}
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

    void cg() {
        gen("; print operation");
        outputValue.cg();
        if (outputValue.type.val == Types.String) {
            gen("invokestatic","CSXLib/printString(Ljava/lang/String;)V");
        } else if (outputValue.type.val == Types.Integer) {
            gen("invokestatic","CSXLib/printInt(I)V");
        } else if (outputValue.type.val == Types.Character && 
            outputValue.kind.val != Kinds.Array && outputValue.kind.val != Kinds.ArrayParm) {
            gen("invokestatic","CSXLib/printChar(C)V");
        } else if (outputValue.type.val == Types.Boolean) {
            gen("invokestatic","CSXLib/printBool(Z)V");
        } else if (outputValue.kind.val == Kinds.Array || 
            outputValue.kind.val == Kinds.ArrayParm) {
            gen("invokestatic","CSXLib/printCharArray([C)V");
        }
        morePrints.cg();
    } //cg

	static nullPrintNode NULL = new nullPrintNode();
	private exprNode outputValue;
	private printNode morePrints;
} // class printNode 

class nullPrintNode extends printNode {
	nullPrintNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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

    void cg() {
      args.cg();
      gen("invokestatic",SymbolInfo.className+"/"+methodName.idinfo.methodSigna);
    } // cg

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
    } // checkTypes

    void cg() {
        if (returnVal.isNull()) {
            gen("return\n");
        } else {
            returnVal.cg();
            gen("ireturn\n");
        }
    } // cg

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

    void cg() {
        decls.cg();
        stmts.cg();
    } // cg

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
        if ("while".equals(this.brotherNode)) {
            kindMustBe(label.type.val, label.kind.val, Kinds.Label, error()
                + "The break label doesn't match while label.");
        }
    } // checkTypes

    void cg() {
        gen("goto",buildlabel(label.idinfo.loopExit));
    } // cg

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
        if ("while".equals(this.brotherNode)) {
            kindMustBe(label.type.val, label.kind.val, Kinds.Label, error()
                + "The continue label doesn't match while label.");
        }
    } // checkTypes

    void cg() {
        gen("goto",buildlabel(label.idinfo.headOfLoop));
    } // cg

	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {super();}
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

    void cg() {
        argVal.cg();
        moreArgs.cg();
    } // cg

	static nullArgsNode NULL = new nullArgsNode();
	private exprNode argVal;
	private argsNode moreArgs;
} // class argsNode 

class nullArgsNode extends argsNode {
	nullArgsNode() {
        super();
	}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
	nullExprNode() {super();}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
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
        if (expr.isNull()) {
            type = term.type;
            kind = term.kind;
        }
        else {
            mustBe(operatorCode == sym.COR
                ||operatorCode == sym.CAND);
            typeMustBe(expr.type.val, Types.Boolean,
                error() + "Only bool values may be applied.");
            typeMustBe(term.type.val, Types.Boolean,
                error() + "Only bool values may be applied.");
            type = new Types(Types.Boolean);
        }
        size = term.size;
        //kind = expr.kind;
     } // checkTypes

     void cg() {
        if (expr.isNull())
          term.cg();
        else {
          term.cg();
          if (operatorCode == sym.CAND) {
            gen("ifeq",buildlabel(++labelCnt));
            thisLabelCnt = labelCnt;
            labelCnt += 2;
            expr.cg();
            gen("ifeq",buildlabel(thisLabelCnt));
            gen("iconst_1");
            gen("goto",buildlabel(++thisLabelCnt));
            genlab(buildlabel(thisLabelCnt-1));
            gen("iconst_0");
            genlab(buildlabel(thisLabelCnt));
          }
          if (operatorCode == sym.COR) {
            gen("ifne",buildlabel(++labelCnt));
            thisLabelCnt = labelCnt;
            labelCnt += 2;
            expr.cg();
            gen("ifeq",buildlabel(++thisLabelCnt));
            genlab(buildlabel(thisLabelCnt-1));
            gen("iconst_1");
            gen("goto",buildlabel(++thisLabelCnt));
            genlab(buildlabel(thisLabelCnt-1));
            gen("iconst_0");
            genlab(buildlabel(thisLabelCnt));
          }
        }
     } // cg

    private int thisLabelCnt;
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
        if (secondFactor.isNull()) {
            type = firstFactor.type;
            kind = firstFactor.kind;
        }
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
            type = new Types(Types.Boolean);
        }
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

    void cg() {
        firstFactor.cg();
        if (!secondFactor.isNull()) {
            secondFactor.cg();
            gen("isub");
            if (operatorCode == sym.LT) {
              gen("iflt",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
            if (operatorCode == sym.GT) {
              gen("ifgt",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
            if (operatorCode == sym.LEQ) {
              gen("ifle",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
            if (operatorCode == sym.GEQ) {
              gen("ifge",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
            if (operatorCode == sym.EQ) {
              gen("ifeq",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
            if (operatorCode == sym.NOTEQ) {
              gen("ifne",buildlabel(++labelCnt));
              gen("iconst_0");
              gen("goto", buildlabel(++labelCnt));
              genlab(buildlabel(labelCnt-1));
              gen("iconst_1");
              genlab(buildlabel(labelCnt));
            }
        }
    } // cg

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
        if (pri.isNull()) {
            type = factor.type;
            kind = factor.kind;
        }
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
            type = new Types(Types.Integer);
        }
        size = factor.size;
    } // checkTypes

    void cg() {
        factor.cg();
        if (!pri.isNull()) {
            pri.cg();
            if (operatorCode == sym.PLUS)
              gen("iadd");
            else
              gen("isub");
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
        if (rightOperand.isNull()) {
            type = leftOperand.type;
            kind = leftOperand.kind;
        }
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
            type = new Types(Types.Integer);
        }
        size = leftOperand.size;
    } // checkTypes

    void cg() {
        leftOperand.cg();
        if (!rightOperand.isNull()) {
            rightOperand.cg();
            if (operatorCode == sym.SLASH)
              gen("idiv");
            else
              gen("imul");
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
	} // unaryOpNode

    static void printOp(int op) {
        switch (op) {
          case sym.NOT:
            System.out.print("!");
            break;
          case -1:
            break;
          default:
            break;
        }
    }

    void Unparse(int indent) {
        printOp(operatorCode);
        operand.Unparse(0);
    } // Unparse

    void checkTypes() {
        operand.checkTypes();
        if (operatorCode == -1) {
            type = operand.type;
            kind = operand.kind;
        }
        else if (operatorCode == sym.NOT) {
            typeMustBe(operand.type.val, Types.Boolean,
                error() + "Only bool values may be applied"
                + " in logical operators.");
            if (operand.type.val != Types.Boolean) {
                type = new Types(Types.Error);
            }
            type = new Types(Types.Boolean);
        }
        size = operand.size;
    } // checkTypes

    void cg() {
        operand.cg();
        if (operatorCode == sym.NOT) {
          gen("ifne",buildlabel(++labelCnt));
          gen("iconst_1");
          gen("goto",buildlabel(++labelCnt));
          genlab(buildlabel(labelCnt-1));
          gen("iconst_0");
          genlab(buildlabel(labelCnt));
        }

    } // cg

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

    void cg() {
        if ("boolTypeNode".equals(resultType.getClass().getName())) {
            /*
             * Need to do do finish
             */
        }
    } // cg

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

    void cg() {
        methodArgs.cg();
        gen("invokestatic",SymbolInfo.className+"/"+methodName.idinfo.methodSigna);
    } // cg

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
        id = (SymbolInfo) st.localLookup(idname);
        if (id == null) {
            id = (SymbolInfo) st.globalLookup(idname);
        }
        if (id == null) {
          System.out.println(error() + idname + " is not declared.");
          typeErrors++;
          type = new Types(Types.Error);
        } else {
            while (id != null && "callNode".equals(this.parentNode) && id.kind.val != Kinds.Method) {
                st.changeTop();
                id = (SymbolInfo) st.globalLookup(idname);
                isChangeTop = true;
            }
            if (isChangeTop) {
                st.restoreTop();
            }
            if (id == null) {
                System.out.println(error() + idname + " is not declared.");
                typeErrors++;
                type = new Types(Types.Error);
            } else {
                type = id.type;
                kind = id.kind;
                size = id.size;
                idinfo = id; // Save ptr to correct symbol table entry
            }
        } // id != null
        //size = id.size;
    } // checkTypes

    void cg() {
      System.out.println("test");
       if (this.idinfo.varIndex != -1) {
            gen("iload",this.idinfo.varIndex);
       } else {
            if (this.idinfo.type.val == Types.Integer)
               gen("getstatic",this.idinfo.fieldInfo, "I");
            else if (this.idinfo.type.val == Types.Character)
               gen("getstatic",this.idinfo.fieldInfo, "C");
            else if (this.idinfo.type.val == Types.Boolean)
               gen("getstatic",this.idinfo.fieldInfo, "Z");
       }
    } // cg

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
      if (subscriptVal.isNull() != true) {
        System.out.print("[");
        subscriptVal.Unparse(0);
        System.out.print("]");
	  }
    } // nameNode

    void checkTypes() {
        varName.checkTypes();
        subscriptVal.checkTypes();
        if (!subscriptVal.isNull() &&
            subscriptVal.type.val != Types.Integer 
                && subscriptVal.type.val != Types.Character) {
                    System.out.println(error() + "Only int or char type"
                        + " may be used to index arrays.");
                    typeErrors++;
                    type = new Types(Types.Error);
        }
        if (type.val != Types.Error) {
            type = varName.type;
            kind = varName.kind;
        }
        if (!subscriptVal.isNull()) {
            kind = new Kinds(Kinds.Var);
        }
        size = varName.size;
    } // checkTypes

    identNode returnVarName() {
      return varName;
    } // returnVarName
    exprNode returnSub() {
      return subscriptVal;
    }

    void cg() {
      if (subscriptVal.isNull()) {
          if (varName.idinfo.varIndex != -1) {
            if (varName.kind.val == Kinds.Array || varName.kind.val == Kinds.ArrayParm) {
              gen("aload",varName.idinfo.varIndex);
              if (varName.type.val == Types.Integer)
                gen("invokestatic","CSXLib/cloneIntArray([I)[I");
              else if(varName.type.val == Types.Character)
                gen("invokestatic","CSXLib/cloneCharArray([C)[C");
              else if(varName.type.val == Types.Boolean)
                gen("invokestatic","CSXLib/cloneBoolArray([Z)[Z");
            } else {
              if ("target".equals(this.parentNode) != true)
                gen("iload",varName.idinfo.varIndex);
            }
          } else {
            /*
             * Need to do do solve field: a[1] = 9;
             */
              if (varName.idinfo.type.val == Types.Integer)
                gen("getstatic",varName.idinfo.fieldInfo, "I");
              else if (varName.idinfo.type.val == Types.Character)
                gen("getstatic",varName.idinfo.fieldInfo, "C");
              else if (varName.idinfo.type.val == Types.Boolean)
                gen("getstatic",varName.idinfo.fieldInfo, "Z");
          }
      }
      else {
        gen("aload",varName.idinfo.varIndex);
        subscriptVal.cg();
        if ("target".equals(this.parentNode)){}
        else {
          if (type.val == Types.Integer)
            gen("iaload");
          else if (type.val == Types.Character)
            gen("caload");
          else if (type.val == Types.Boolean)
            gen("baload");
        }
      }
    } // cg


    static nullNameNode NULL = new nullNameNode(); 
	private identNode varName;
	private exprNode subscriptVal;
} // class nameNode 

class nullNameNode extends nameNode {
    nullNameNode() {super();}
   	boolean   isNull() {return true;}
	void Unparse(int indent) {}
    void checkTypes() {}
    void cg() {}
}

class strLitNode extends exprNode {
    strLitNode(String fullstring, String stringval, int line, int col) {
      super(line, col, new Types(Types.String),
          new Kinds(Kinds.Value));
      fullstr = fullstring;
      strval = stringval;
      size = strval.length();
    } // strLitNode

    void Unparse(int indent) {
      System.out.print(strval);
    } // Unparse

    void checkTypes() {
        // All strLits are automatically type-correct
    } // checkTypes

    void cg() {
      gen("ldc",fullstr);
    } // cg

    private identNode array;
    private final String fullstr;
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

    void cg() {
        gen("ldc",intval);
    } // cg

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

    void cg() {
        gen("ldc",charval.charAt(1)-'A'+65);
    } // cg

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

    void cg() {
        gen("iconst_1");
    } // cg
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

    void cg() {
        gen("iconst_0");
    } // cg
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

    void cg() {
        expr.cg();
    } // cg

	private final exprNode expr;
} // class exprUnitNode 

