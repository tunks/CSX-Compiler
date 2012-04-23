class test {
    int add(int a, int b) {
        return a+b;
    }
    void test() {
        int a = 1;
        if (a>0)
            print("This is a test");
        else
            return;
    }
    void main() {	
        int a = 40;
        int b = 12;
        b = add(a, b);
        print(b);
        test();
        return;
    }
}
