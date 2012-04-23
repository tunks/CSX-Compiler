class test {
    void main() {
        char c[3];
        char d[3];
        c = "abc";
        d = c;
        d[2] = 'f';
        c[2] = d[2];
        print(c[2]);
    }
}
