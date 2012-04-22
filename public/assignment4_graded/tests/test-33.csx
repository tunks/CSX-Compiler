## Test 33
 Created: Dec 2002

  Checks print statements & simple proc calls

##

class p33csx {
 

 int  x;
 int  y;
 bool li;

 void lessthanzero(int x1, int y1) {
    li = (x1 < 0) && (y1 < 0);
 }

 void main() {
  print("Testing Program p33csx\n");
 
  x = ~20; y  = ~10;
  print(" Output ", "X = ", x, " * ", "Y = ", y, " = ", (x*y), "\n");
  lessthanzero(x, y);
  print(" Output: Are both X(", x, ") and Y(", y, ") less than zero??", "\n",
         "   Answer is : ", li, "\n"); 
  
  y = 4; x = 8;
  print(" Output ", "X = ", x, " / ", "Y = ", y, " = ", (x/y), "\n");
  y = 4; x = 2;
  print(" Output ", "X = ", x, " / ", "Y = ", y, " = ", (x/y), "\n");

  print("Test completed\n");
 }
}
