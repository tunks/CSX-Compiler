## Test 14
 Created: Dec 2002
 
 Checks integer, boolean and relational operators
##

class p14csx{

 void main(){
  int   i1;
  int   i2;
  bool  b1;
  bool  b2;

  print("Testing program p14csx\n");
  i1 = ~2147483640;  i2 = 2147483647;

  if (i1 > i2 || i1 == i2 || i2 < i1 || i2 != i2)
	print("ERROR: Error in relational operators (integer)\n");

  i1 = i1 * 0;
  i2 = i2 * 1;
  b1 = FALSE;
  b2 = TRUE;
  b2 = b1 == TRUE && b2 == b1;
  
 if (b2)
	print("ERROR: In boolean expression\n");
 

 b1 = i1 < i2 && i1 == 0 && FALSE;
 if (b1)
      print("ERROR: In boolean/integer expression\n");

 i1 = (1 + 2) * 3 / (4 + 5);
 i2 = (1 + 2) * (3 / (4 + 5));
 if ((i1 != 1) || (i2 != 0))
	print("ERROR: Improper Integer division and associativity\n");
 print("\n");

 }

}

