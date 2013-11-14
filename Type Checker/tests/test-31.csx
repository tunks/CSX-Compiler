## Test 31
 Created: Dec 2002

 Check  procedure calls with parameters :
  Are the parameters properly scoped & accessed ?

##

class p31csx {
 
 int  x;
 int  y;

 int  n;
 int  fct;


 void proc1(int x1) {
	print("\n", "Procedure 1 entered");
	print("X1 = (10?) ", x1);
	print("\n", "Procedure 1 exited");
 }

 void proc2(int y1) {
	print("\n", "Procedure 2 entered");
	print(" Y1 = (15?) ", y1);
	print("\n", "Procedure 2 exited");
 }


 void main() {
   print("Testing Program p31csx", "\n");
   x = 10; 
   proc1(x);
   y = 15;
   proc2(y);

   print(" X = (10?)", x, " Y = (15?) ", y, "\n");


   print("Test completed", "\n");

 }
}
