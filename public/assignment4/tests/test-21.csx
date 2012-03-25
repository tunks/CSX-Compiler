## Test 21
 Created: Dec 2002

 Check control flow in a deeply nested loop

##

class p21csx{

 void main(){
  int  loopcount;
  bool flag;

  print("Testing Program p21csx\n");

  flag = TRUE;
  loopcount = 0;

  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
  while (flag == TRUE) {
   loopcount = loopcount + 1;
	flag = FALSE;
  } } } } } } } } } }

 if (loopcount != 10)
    print("\nERROR : Not all loops exectuted properly: ", loopcount, "\n");

  print("Test completed\n");

 }

}
 
