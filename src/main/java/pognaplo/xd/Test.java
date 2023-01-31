package pognaplo.xd;

import pognaplo.frontend.ErrorDialog;
import pognaplo.kek.Controller;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class Test {
    public static void main(String[] args) {
        ArrayList<String> yes = new ArrayList<>();
        StringBuilder yesString = new StringBuilder();
        System.out.println(yesString.length());
        for (int i = 0; i < 20; i++)
        {
            yes.add("test " + i + "\n");
            yesString.append("test ").append(i).append("\n");
        }


        System.out.println(yesString);


//        ErrorDialog no = new ErrorDialog((yes.toString()));
    }


}
