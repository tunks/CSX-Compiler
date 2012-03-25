## Test 29
 Created: Dec 2002

  Check scoping and local variable access

##

class p29csx {

 int  x;
 int  y;
 int  z;

 void  local1() {
    const x = 10;
    const y = 20;
    int   z;

    print("In procedure local1: Expecting 10 and 20\n");
    print("   X = ", x, "   Y = ", y, "\n");
    z = 10000;     // should not be visible outside 
 }

 void main() {
  print("Testing Program p29csx\n");

  x = 100;
  y = 200;
  z = ~1;
  print(" X = ", x, " Y = ", y, " Z = ", z,  "\n");
  local1();
  // Z should not change; same with x and y 
  print(" X = ", x, " Y = ", y, " Z = ", z,  "\n");
   
  print("Test completed\n");
 }
}
