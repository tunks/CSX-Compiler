## Test 16
 Created: Dec 2002
 
  Test simple nested while loops
##

class p16csx{

 void main(){
  int i=1;
  int j=1;
  int sum=0;
 
  print("Testing program p16csx\n");

  while (i <= 100) {
	j=1;
  	while (j <= 100) {
		j = j+1;
		sum = sum + 1;
  	}
	i = i+1;
  }
 			

  print("The value of sum is ", sum, "\n");
 }

}

