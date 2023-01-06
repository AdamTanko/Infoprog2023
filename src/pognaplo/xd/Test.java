package pognaplo.xd;

import pognaplo.kek.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class Test
{
    public static void main(String[] args) throws ParseException
    {
        LocalDate dt =null;
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy");
             dt = LocalDate.parse("28-13-2023", formatter);

        } catch (DateTimeException | NullPointerException ignored)
        {

        }
        System.out.println(dt);
    }


}
