## Test 09
 Created: Dec 2002
 
  Test integer relational operators
##

class p09csx{

 void main(){
  int i1;
  int i2;
  bool b1;
  bool b2;
  bool b3;
  bool b4;
  bool b5;
  bool b6;
 
  print("Testing program p09csx\n");
 			
   i1 = 10;                             // 10
   i2 = ~10;                            // -10
   b1 = i1 == i1;			// true
   b2 = i1 != i1;			// false
   b3 = i1 <= i1;			// true
   b4 = i1 <  i1;			// false
   b5 = i1 >= i1;			// true
   b6 = i1 >  i1;			// false
   
   print(b1, "\t", b2, "\t",b3, "\t", b4, "\n", b5, "\t", b6, "\n");

   b1 = i1 == i2;			// false
   b2 = i1 != i2;			// true
   b3 = i1 <= i2;			// false
   b4 = i1 <  i2;			// false
   b5 = i1 >= i2;			// true
   b6 = i1 >  i2;			// true
   
   print(b1, "\t", b2, "\t",b3, "\t", b4, "\n", b5, "\t", b6, "\n");

 }

}

