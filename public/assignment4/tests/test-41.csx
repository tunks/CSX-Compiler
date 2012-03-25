## Test 41
  Created: Dec 2002

 Check nested calls to functions

##

class p41csx {


  int sum(int i, int j, int k) {
	return i+j+k;
  }


 void main() {
  print("Testing Program p41csx", "\n");
  

  print("1+2+3+4+5+6+7 = ", sum(1,sum(2,3,sum(4,5,6)),7) , "\n");

  print("Test completed", "\n");

 }
}
