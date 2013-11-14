## Test 24
 Created: Dec 2002
 
  Test global arrays
##

class p24csx{

 void main(){
  int i[10];
  bool b[2];
 
  print("Testing program p24csx\n");
 			
   i[0]=1;
   i[1]=10;
   i[2]=100;
   i[3]=1000;
   i[4]=10000;
   i[5]=100000;
   i[6]=1000000;
   i[7]=10000000;
   i[8]=100000000;
   i[9]=1000000000;
   
	print(i[0], "\t", i[1], "\t", i[2], "\t", i[3], "\t", i[4] , "\n");
	print(i[5], "\t", i[6], "\t", i[7], "\t", i[8], "\t", i[9] , "\n");


   b[0]=false;
   b[1]=true;

   print(b[0], "\t", b[1], "\n");
 }

}

