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
 public int size;

 public LinkedList <Types>listType = new LinkedList<Types>();
// grader: better to declare as List, even if you assign a LinkedList.  -0

 public LinkedList <Kinds>listKind = new LinkedList<Kinds>();

 public LinkedList <Types>parmListType = new LinkedList<Types>();

 public LinkedList <Kinds>parmListKind = new LinkedList<Kinds>();

 public LinkedList <Types>backupParmListType = new LinkedList<Types>();

 public LinkedList <Kinds>backupParmListKind = new LinkedList<Kinds>();

 public SymbolInfo(String id, Kinds k, Types t){
	super(id);
	kind = k; type = t;};
 public SymbolInfo(String id, int k, int t){
	super(id);
	kind = new Kinds(k); type = new Types(t);};
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";};
}

