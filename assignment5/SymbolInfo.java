/**************************************************
*  class used to hold information associated w/
*  Symbs (which are stored in SymbolTables)
*
****************************************************/

import java.util.*;

class SymbolInfo extends Symb {
 public Kinds kind; // Should always be Var in CSX-lite
                    // Should be Var, Value, Array, ScalarParm, ArrayParm,
                    //           Method and Label in CSX
 public Types type; // Should always be Integer or Boolean in CSX-lite
                    // Should be Integer, Boolean, Character, String, Void,
                    //           Error and Unknown in CSX

 public int varIndex; // Index used to address a variable

 public int size;

 public static int hasWrittenMain = 0;

 public static String className;

 public String fieldInfo;

 public List <Types>listType = new LinkedList<Types>();

 public List <Kinds>listKind = new LinkedList<Kinds>();

 public List <Types>parmListType = new LinkedList<Types>();

 public List <Kinds>parmListKind = new LinkedList<Kinds>();

 public List <Types>backupParmListType = new LinkedList<Types>();

 public List <Kinds>backupParmListKind = new LinkedList<Kinds>();

 public SymbolInfo(String id, Kinds k, Types t){
	super(id);
	kind = k; type = t;};
 public SymbolInfo(String id, int k, int t){
	super(id);
	kind = new Kinds(k); type = new Types(t);};
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";};
}


