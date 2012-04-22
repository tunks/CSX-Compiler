class mytest {
	int a;
	int longInt = 9999999999999999999999999;
	const constString = "test file";
	char charArray[10];

	char first(int f1, char f2) {
		int a;
		a = f2;  // error different type
		charArray = "abcde"; // error different size
		if (a - 'A' < f1)
			return 'a';
		else
			return 'A';
	}

	void second(int f[]) {
		int b;
		char c;
		c = first(b, first(b,c));
		b = f[3];
		return;
	}

	void main() {
		int temp = a;
		int intArray[5];
		label: while (temp >= 'Z') {
			if (temp != 'a' && temp <= 'z') {
				int intArray2[5];
				temp = temp + 'a';
				intArray = intArray2;
				continue temp;  // error temp isn't a label
			} 
			else {
				bool b = temp == 'B';
				int intArray2[10];
				intArray = intArray2; // error different size
				break label;
			}
		}
		second(charArray); // error different type
		a = second(intArray); // error void method
		print("This is a test file", constString, intArray); // error int array
	}

## The last method, which is not main
##
	int lastOne() {
		label: while(false) {
			print(false);
		}  // error no break or continue
		return;  // error no return value
	}
}
