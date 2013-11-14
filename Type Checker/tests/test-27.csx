## Test 27
 Created: Dec 2002

 Check  simple parameterless functions

##

class p27csx {
 
 int proc1() {
	print("\n", "Procedure 1 entered");
	print("\n", "Procedure 1 exited");
	return 123;
 }

 bool proc2() {
	print("\n", "Procedure 2 entered");
	print("\n", "Procedure 2 exited");
	return true;
 }

 void main() {
  int x;
  int y;

  print("Testing Program p27csx", "\n");
  
   print(proc1());

   print(proc2());
   print("\n");

   print("Test completed", "\n");
  }
}

