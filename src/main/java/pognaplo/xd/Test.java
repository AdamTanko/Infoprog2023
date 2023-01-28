package pognaplo.xd;

import pognaplo.kek.Controller;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


public class Test {
    public static void main(String[] args) {
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
    }


}
