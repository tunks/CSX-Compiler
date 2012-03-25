## Test 38
  Created: Dec 2002

 The 8 queens problem

##

class p38csx {


  const dim =8; // Dimensions of the chess board
  int a1=1;
  int b1=2;
  int c1=3;
  int d1=4;
  int e1=5;
  int f1=6;
  int g1=7;
  int h1=8;
  int a2;
  int b2;
  int c2;
  int d2;
  int e2;
  int f2;
  int g2;
  int h2;
  int count=0;
  bool flag1;
  bool flag2;
  bool flag3;
  bool flag4;
  bool flag5;
  bool flag6;
  bool flag7;
  
  bool compatible(int x1, int x2, int y1, int y2) {
	if(x2 == y2) 
		return FALSE;
	if(((y2 - x2) == (y1 - x1)) || ((x2 - y2) == (y1 - x1)))
		return FALSE;
	else    return TRUE;
  } // End of function compatible

  void main() {
   print("Testing Program p38csx (8 queens problem)", "\n");

   a2 = 1;
   b2 = 1;
   c2 = 1;
   d2 = 1;
   e2 = 1;
   f2 = 1;
   g2 = 1;
   h2 = 1;

   while (a2 <= 8) {
     while (b2 <= 8) {
       flag1 = compatible(a1,a2,b1,b2);
       if(flag1) {
       while (c2 <= 8) {
         flag1 = compatible(a1,a2,c1,c2);
         flag2 = compatible(b1,b2,c1,c2);
         if(flag1 && flag2) {
	     while (d2 <= 8) {
                flag1 = compatible(a1,a2,d1,d2); 
                flag2 = compatible(b1,b2,d1,d2); 
                flag3 = compatible(c1,c2,d1,d2); 
	        if(flag1 && flag2 && flag3) {
		   while (e2 <= 8) {
		      flag1 = compatible(a1,a2,e1,e2);
		      flag2 = compatible(b1,b2,e1,e2);
		      flag3 = compatible(c1,c2,e1,e2);
		      flag4 = compatible(d1,d2,e1,e2);
		      if(flag1 && flag2 && flag3 && flag4) {
			   while (f2 <= 8) {
		   	      flag1 = compatible(a1,a2,f1,f2);
		   	      flag2 = compatible(b1,b2,f1,f2);
		   	      flag3 = compatible(c1,c2,f1,f2);
		   	      flag4 = compatible(d1,d2,f1,f2);
		   	      flag5 = compatible(e1,e2,f1,f2);
			      if(flag1 && flag2 && flag3 && flag4 && flag5) {
			          while (g2 <= 8) {
		   	   	    flag1 = compatible(a1,a2,g1,g2);
		   	   	    flag2 = compatible(b1,b2,g1,g2);
		   	   	    flag3 = compatible(c1,c2,g1,g2);
		   	   	    flag4 = compatible(d1,d2,g1,g2);
		   	   	    flag5 = compatible(e1,e2,g1,g2);
		   	   	    flag6 = compatible(f1,f2,g1,g2);
				    if(flag1 && flag2 && flag3 &&
                                       flag4 && flag5 && flag6) {
				       while (h2 <= 8) {
					   flag1 = compatible(a1,a2,h1,h2);
		   	   	 	   flag2 = compatible(b1,b2,h1,h2);
		   	   	 	   flag3 = compatible(c1,c2,h1,h2);
		   	   	 	   flag4 = compatible(d1,d2,h1,h2);
		   	   	 	   flag5 = compatible(e1,e2,h1,h2);
		   	   	 	   flag6 = compatible(f1,f2,h1,h2);
		   	   	 	   flag7 = compatible(g1,g2,h1,h2);
					   if(flag1 && flag2 && flag3 &&
                                              flag4 && flag5 && flag6 &&
                                              flag7) {
					      count = count + 1;
					      print("\n", "Solution#", count,
                                                    " is:", "\n");
	                                      print(" ", a2, " ", b2, " ", c2,
                                                    " ", d2, " ", e2, " ", f2,
                                                    " ", g2, " ", h2, "\n");
					   }
					   h2 = h2 + 1;
				       } // For h2
				      }
				      g2 = g2 + 1;
				      h2 = 1;
			          } // For g2	
			      }
			      f2 = f2 + 1;
			      g2 = 1;
			      h2 = 1;
			   } // For f2
		      }
		   e2 = e2 + 1;
		   f2 = 1;
		   g2 = 1;
		   h2 = 1;
		   } // For e2
	        }
	     d2 = d2 + 1;
	     e2 = 1;
	     f2 = 1;
	     g2 = 1;
	     h2 = 1;
             } // End while for d2
         }
         c2 = c2 + 1;
         d2 = 1;
         e2 = 1;
         f2 = 1;
         g2 = 1;
         h2 = 1;
       } // End while for c2
       }
       b2 = b2 + 1;
       c2 = 1;
       d2 = 1;
       e2 = 1;
       f2 = 1;
       g2 = 1;
       h2 = 1;
      } // For b2
     a2 = a2 + 1;
     b2 = 1;
     c2 = 1;
     d2 = 1;
     e2 = 1;
     f2 = 1;
     g2 = 1;
     h2 = 1;
   } // For a2

 }
}
