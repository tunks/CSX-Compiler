## Test 23
 Created: Dec 2002

 Check relational operators

##

class p23csx{

 void main(){
  int  i;
  int  j;
  bool b;

  print("Testing Program p23csx\n");

  i = 1;
  j = 2;
  b = true;

  if (i < j)
	b = b && true;
  else {
     print("ERROR: Less Than operator not working");
     b = false;
  }
  if (i <= j && i <= 1 && j <= 2 && i <= 1000000
	&& ~123456 <= 123456)
	b = b && true;
  else {
     print("ERROR: <= operator not working");
     b = false;
  }
  if (j > i)
	b = b && true;
  else {
     print("ERROR: Greater Than operator not working");
     b = false;
  }
  if (j >= i && j >= 2 && 1 >= i && j >= ~123456 && 0 >= ~111111) 
	b = b && true;
  else {
     print("ERROR: >= operator not working");
     b = false;
  }
  if (i == j) {
     print("ERROR: Equal operator not working");
      b = false;
  } else
     b = b && true ;
  if (i != j) 
	b = b && true;
  else {
     print("ERROR: Less Than operator not working");
     b = false;
  }

  if (b == FALSE) 
	print("ERRORS found in program");

   print("Test completed\n");
 }

}

