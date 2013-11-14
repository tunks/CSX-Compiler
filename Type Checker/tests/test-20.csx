## Test 20
 Created: Dec 2002

 Checks relational operators

##

class p20csx{

 void main(){
  int  i;
  int  j;
  bool b;

  print("Testing Program p20csx\n");

  i = 1;
  j = 2;
  b = true;

  if (i < j) 
	b = b && true;
  else {
     print("ERROR: Less Than operator not working\n");
     b = false;
  }
  if (j > i) 
	b = b && true;
  else {
     print("ERROR: Greater Than operator not working\n");
     b = false;
  }
  if (i == j) {
     print("ERROR: Equal operator not working\n");
     b = false;
  } else
     b = b && true ;
  if (i != j)
	b = b && true;
  else {
     print("ERROR: Less Than operator not working\n");
     b = false;
  }

  if (b == FALSE) 
	print("ERRORS found in program\n");

   print("Test completed\n");
 }

}

