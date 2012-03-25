## Test 32
 Created: Dec 2002

 Check recursive fct calls using you know what

##

class p32csx {
 
 int  cnt;

 int fac(int n) {
    if (n <= 1) 
      return 1;
    else 
      return fac(n-1)*n;
 }

 void main() {
  print("Testing Program p32csx", "\n");
  
   cnt = 1;

   while (cnt <= 9) {
   	print("Factorial of ", cnt, " is ", fac(cnt), "\n");
	cnt = cnt+1;
   }

   print("Test completed", "\n");

 }
}
