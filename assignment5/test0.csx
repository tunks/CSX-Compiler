class test {
	int a = 65;
	const constString = true;
	char charArray[11];

	char first(int f1, char f2) {
		int a;
		a = f1;
		charArray = "Hello World";
		print("The alphabet of charArray are ",charArray,"\n",
				"Is your input greater than 'A'?\n");
		if (a - 'A' <= f1)
			f2 = 'Y';
		else
			f2 = 'N';
		return f2;
	}

	int second(int f[]) {
		int b;
		b = f[0]*100/2;
		return b;
	}

	void main() {
		int temp;
		char c;
		int intArray[5];
		print("\nTest first function:\n");
		print("Please input a char: ");
		read(c);
		print("Your input is \"",c,"\"\n");
		temp = (int)c;
		c = first(temp, c);
		print(c,"\n");
		temp = a;
		print("\nBelow are 26 English alphabets:\n");
		label: while (temp <= 'z') {
			if (temp <= 'Z' || temp >= 'a') {
				c = (char)temp;
				print(c," ");
			} 
			else {
				temp=temp+1;
				continue label;	
			}
			temp=temp+1;
		}
		print("\n\n","Test second function:","\n");
		print("Please input an integer: ");
		read(temp);
		intArray[0] = temp;
		a = second(intArray);
		print("The result of second function(times 50) is ", a);
	}
}

