## Test 26
 Created: Dec 2002

 Check  simple parameterless procs

##

class p26csx{

 void proc1() {
	print("\n", "Procedure 1 entered");
	print("\n", "Procedure 1 exited");
 }

 void proc2() {
	print("\n", "Procedure 2 entered");
	print("\n", "Procedure 2 exited");
 }

 void main(){
  int x;
  int y;

  print("Testing Program p26csx", "\n");
  
  proc1();

  proc2();

  print("Test completed", "\n");
 }

}

