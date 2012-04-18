class Kinds{
 public static final int Var = 0;
 public static final int Value = 1;
 public static final int Other = 2;
 public static final int Array = 3;
 public static final int ScalarParm = 4;
 public static final int ArrayParm = 5;
 public static final int Method = 6;
 public static final int Label = 7;

 Kinds(int i){val = i;}
 Kinds(){val = Other;}

 public String toString() {
        switch(val){
          case 0: return "Var";
          case 1: return "Value";
          case 2: return "Other";
          case 3: return "Array";
          case 4: return "ScalarParm";
          case 5: return "ArrayParm";
          case 6: return "Method";
          case 7: return "Label";
          default: throw new RuntimeException();
        }
 }

 int val;
}

