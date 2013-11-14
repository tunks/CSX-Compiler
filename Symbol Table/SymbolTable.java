import java.io.*;
import java.util.*;

class SymbolTable {

  private LinkedList<HashMap<String, Symb>> llist;
  private HashMap<String, Symb> hmap;

   SymbolTable() {
      
		 llist = new LinkedList<HashMap<String, Symb>>();
     hmap = new HashMap<String, Symb>();
     llist.addFirst(hmap);
   }

	 //open
   public void openScope() {
      
		 HashMap<String, Symb> addhmap = new HashMap<String, Symb>();
      llist.addFirst(addhmap);
   }

	 //close
   public void closeScope() throws EmptySTException {
      
		 if (llist.size() == 0)
        throw new EmptySTException("The list of scopes is empty!");
     else
        llist.remove();
   }

	 //insert
   public void insert(Symb s) throws EmptySTException, DuplicateException {
      
		 String iname = s.name();
		 if (llist.size() == 0)
       throw new EmptySTException("The list of scopes is empty, please input command \"open\" first!");
		 else if (llist == null)
       throw new EmptySTException("The list of scopes is empty!");
     else if (llist.getFirst().containsKey(iname.toUpperCase())) {
       throw new DuplicateException(iname + " already entered into top scope.");
     }
     else
       llist.getFirst().put(iname.toUpperCase(), s);
   }

	 //lookup
   public Symb localLookup(String s) {
     
		 if (llist.size()==0)
        return null;
     else if (llist.getFirst().containsKey(s.toUpperCase()))
        return llist.getFirst().get(s.toUpperCase());
     else
        return null;
   }

	 //global
   public Symb globalLookup(String s) {
     
		 int size = llist.size();
     int i;
     for (i = 0; i < size; i++) {
       if (llist.get(i).containsKey(s.toUpperCase()))
         return llist.get(i).get(s.toUpperCase());
     }
     return null;
   }

   public String toString() {
      return ""; // change this
   }

	 //dump
   void dump(PrintStream ps) {
		 int size = llist.size();
		 int i;
		 if (size == 0)
			 ps.println("The symbol table is empty!");
		 else {
			 ps.println("Contents of symbol table:");
       //original.println("Contents of symbol table:");
		   for (i = 0; i < size; i++){
			   ps.print("{");
			 	 Set<String> dset = llist.get(i).keySet();
			   Iterator it = dset.iterator();
			   while (it.hasNext()) {
				   String tmp = (String)it.next();
				   Symb stmp = llist.get(i).get(tmp);
				   TestSym tstmp = (TestSym)stmp;
				   ps.print(tstmp.name() + " = " + tstmp.toString());
				   if (it.hasNext())
					   ps.print(", ");
			   }
			   ps.print("}\n");
		   }
		 }
   }
} // class SymbolTable
