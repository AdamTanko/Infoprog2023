package pognaplo.main;

import java.util.ArrayList;


public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> yes = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            yes.add(i);
        }

        int idx = yes.size();
        System.out.println(yes.get(++idx));


//        ErrorDialog no = new ErrorDialog((yes.toString()));
    }


}
