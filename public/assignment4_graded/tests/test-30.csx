## Test 30
 Created: Dec 2002

 Check recursive parameterless procedure calls
 (using factorial, of course)
##

class p30csx {

 int  n;
 int  fact;

 void factorial() {
    if (n == 0)
      fact = 1;
    else  {
      n = n-1;
      factorial();
      n = n + 1;
      fact = fact * n;
    }
 }
 
 void main() {

  print("Testing Program p30csx", "\n");
   
   n=6;
   factorial();

   print("\n", "Factorial of ", n, " is ", fact, "\n");
   print("Test completed", "\n");

  }
}
