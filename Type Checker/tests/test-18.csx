## Test 18
 Created: Dec 2002

  Checks for proper working of input/output statements
   (used by all test programs)

 Assume input is: -5, -13, 20
##

class p18csx{

 void main(){
  int  x;
  int  y;
  int  z;

  print("Testing Program p18csx\n");
  read(x, y, z);
  if ((x != ~5) || (y != ~13) || (z != 20))
    print("\nERROR: Read error\n");


  x = 1000;  y = 2000; z = 3000;
  print("Testing print:  Ouput is ", x, z, y, "\n");
  
  print("Test completed\n");
 }

}

