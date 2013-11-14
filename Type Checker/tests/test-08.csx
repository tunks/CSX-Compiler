## Test 08
 Created: Dec 2002
 
  Test proper working of operators for boolean values
##

class p08csx{

 void main(){

  bool b1;
  bool b2;
  bool b3;
  bool b4;
  bool b5;
  bool b6;
  bool b7;
  bool b8;
 
  print("Testing program p08csx\n");
 			
   b1 = true;                             //true
   b2 = false;                            //false
   b3 = b1 && b1;                         //true
   b4 = b2 && b2;                         //false
   b5 = b1 && b2;                         //false
   b6 = b2 && b1;                         //false

   print(b1, "\t", b2, "\n", b3, "\t", b4, "\t", b5, "\t", b6, "\n");

   b3 = b1 || b1;                          //true
   b4 = b2 || b2;                          //false
   b5 = b1 || b2;                          //true
   b6 = b2 || b1;                          //true

   print( b1, "\t", b2, "\n", b3, "\t", b4, "\t", b5, "\t", b6, "\n");

   b3 = ! b1;                          //false
   b4 = ! b2;                          //true
   
   print(b1, "\t", b2, "\t", b3, "\t", b4, "\n");

 }

}
