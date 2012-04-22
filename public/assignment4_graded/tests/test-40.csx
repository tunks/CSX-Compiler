## Test 40
  Created: Dec 2002

 Check  that fct return values are properly protected after return

##

class p40csx {

 int x;
 int y;

 int echo(int i) {
	return i;
 }


 void main() {
  print("Testing Program p40csx", "\n");
  

  print("1+2+3+4 = ", echo(1) +(echo(2) + (echo(3) + echo(4))), "\n");

  print("Test completed", "\n");

 }
}
