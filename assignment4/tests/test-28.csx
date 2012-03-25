## Test 28
 Created: Dec 2002

 Check  procedures calling other procedures

##

class p28csx {
 
  void proc1() {
	print("\n", "Procedure 1 entered");
	print("\n", "Procedure 1 exited");
  }

  void proc2() {
	print("\n", "Procedure 2 entered");
	proc1();
	print("\n", "Procedure 2 exited");
  }


 void main() {
  int x;
  int y;

  print("Testing Program p28csx", "\n");
  
   proc1();

   proc2();

   print("Test completed", "\n");

  }
}
