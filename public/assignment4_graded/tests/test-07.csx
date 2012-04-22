## Test 07
 Created: Dec 2002
 
  Test proper operation of operators for integer values
##

class p07csx{

 void main(){

  int i1;
  int i2;
  int i3;
  int i4;
  int i5;
  int i6;
  int i7;
  int i8;
 
  print("Testing program p07csx\n");
 			
   i1 = 10;                             //10
   i2 = ~20;                            //-20
   i3 = i1*i2;                          //-200
   i4 = i3/i1 + 1;                      //-19
   i5 = (i1 -1)/i1 + 1;                 //1
   i6 = 1 + 4 -3 - 2 - 1 - (~10) + (~9);//0
   i7 = i4 - i2;                         //1
   i8 = i3/i2;                           //10
   
	print(i1, "\t", i2, "\t", i3, "\t", i4, "\n",
	      i5, "\t", i6, "\t", i7, "\t", i8, "\n");
 }
}
