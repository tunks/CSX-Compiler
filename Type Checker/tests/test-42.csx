## Test 42
  Created: Dec 2002

 Test that args of caller can be properly accessed during a call

##

class p42csx {


 int sum(int i, int j) {
	return i+j;
 }

 int plus1(int k) {
	return sum(1,k);
 }

 void main() {
  print("Testing Program p42csx", "\n");
  

  print("100+1 = ", plus1(100) , "\n");

  print("Test completed", "\n");

 }
}
