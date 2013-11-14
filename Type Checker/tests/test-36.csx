## Test 36
  Created: Dec 2002

 Let's try a Fibonacci sequence

##

class p36csx {

  int  cnt;

  const lim = 15; // Adjust this if execution is too slow to bear

  int fib(int n) {
    if (n <= 1) 
      return 1;
    else 
      return fib(n-1) + fib(n-2);
  }

 void main() {
  print("Testing Program p36csx", "\n");
  
   cnt = 0;

   while (cnt <= lim) {
   	print("Fibonacci of ", cnt, " is ", fib(cnt), "\n");
	cnt = cnt+1;
   }

   print("Test completed", "\n");
 }
}

