## Test 22
 Created: Dec 2002

 Check while loops

##

class p22csx{

 void main(){
  int  i;
  int  j;
  bool b;

  print("Testing Program p22csx\n");

  i = 1;
   while (i < 5) {
      print("Iteration ", i, "\n");
      i = i +  1;
   }

  i = 1;
   while ( i < 3) {
     j = 1;
     while (j < 3) {
	  print("( ", i, j, " )", "\n");
	  j = j + 1;
      }
      i = i +  1;
   }

  i = 1; j = 1;
  while ( i < 3) {
     i = i + 1;
     if (i  == 2) 
	j = j + 1;
  }

  print("i = ", i, " j = ", j, "\n");
  print("Test completed", "\n");
 }

}

