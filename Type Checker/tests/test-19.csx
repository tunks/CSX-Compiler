## Test 19
 Created: Dec 2002

 Check that control flows correctly in complex nested if statements

##

class p19csx{

 void main(){
  const cI1 = 1;
  const cI9 = 9;
  const cbt = TRUE;
  const cbf = FALSE;

  int  vI1;
  int  vI9;
  bool vbt;
  bool vbf;
  int  flowcount;

  print("Testing Program p19csx\n");

  vI1 = 1; vI9 = 9; vbt = TRUE; vbf = FALSE;
  flowcount = 0;

  if (cbf)              // FALSE 
	print("\n====> Wrong Control Flow 1\n");
  else if (vbf || vbt) {             // TRUE
	flowcount = flowcount+1;
	if (vbf != (vI1 != 1))  {   // FALSE 
		print("===> Wrong Control Flow 2\n");
		if (20 > 10) {   // TRUE
			if (cbf && vI1 != 0)    // FALSE
		 	   print("\n ==> Wrong Control Flow 3\n");
			else 
			   if (vI9 < cI1 - vI1)   // FALSE
			     print("\n===> Wrong Control Flow 4\n");
		} else 
		   print("\n===> Wrong Control Flow 5\n");
	} else 
		if (TRUE)     // TRUE
		    if ((vI9 == 9) && (cbf != TRUE))  // TRUE
		      flowcount = flowcount + 1;
		    else
		      print("\n===> Wrong Flow Control 6\n");
		else
	            print("\n==> Wrong Flow Control 7\n");
   }

   if (flowcount != 2) 
       print("\n ERROR : Incorrect paths followed \n");
	
   print("Test completed\n");

 }

}

