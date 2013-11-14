## Test 37
  Created: Dec 2002

 Let's test nested calls

##

class p37csx {


 int sum3(int a, int b, int c) {
      return a+b+c;
 }

 void main() {
  print("Testing Program p37csx", "\n");
  

  // This should compute 1+2+...+9 = 45
  print("The grand sum is ",
        sum3(1, 2 , sum3(3 , 4 , sum3(5 , 6 , sum3(7 , 8 , 9)))), "\n");

  print("Test completed", "\n");

 }
}
