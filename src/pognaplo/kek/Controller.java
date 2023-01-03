package pognaplo.kek;

import pognaplo.frontend.MainWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Controller
{
    public static void setFilepath(String filepath)
    {
        Controller.filepath = filepath;
    }

    public static String getFilepath()
    {
        return filepath;
    }

    private static String filepath = "txt/naplo.txt";
    public static final ArrayList<Bejegyzes> naplo = new ArrayList<>();

    private static final String[] header = {"dátum", "kezdő időpont", "záró időpont", "esemény"};

    public static void beolv()
    {
        try
        {
            Scanner sc = new Scanner(new File(filepath));
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-L-y").withLocale(new Locale("hu","HU"));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            int linecounter = 0;
            while (sc.hasNext())
            {
                try
                {
                    linecounter++;
                    String[] tokens = sc.nextLine().split(",");
                    Bejegyzes b = new Bejegyzes(
                            LocalDate.parse(tokens[0], formatter),
                            LocalTime.parse(tokens[1], timeFormatter),
                            LocalTime.parse(tokens[2], timeFormatter),
                            tokens[3]
                    );
                    naplo.add(b);

                } catch (DateTimeException e)
                {
                    MainWindow.errBox("Rossz bemenet a " + linecounter + ". sorban", "Rossz bemenet");
                }

            }
            sc.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean isUnique(Bejegyzes b)
    {
        for (Bejegyzes b2 :
                naplo)
        {
            if (b2.getDatum().equals(b.getDatum()))
            {
                if (b2.getKezdoIdopont().equals(b.getKezdoIdopont()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static JTable listItems()
    {
        if (naplo.size() == 0)
        {
            beolv();
        }


        naplo.sort(new BejegyzesDatumComparitor());
        String[][] bejegyzesek = new String[naplo.size()][4];
        int yes = 0;
        for (Bejegyzes b :
                naplo)
        {
            bejegyzesek[yes++] = b.toArray();

        }

        JTable jt = new JTable(bejegyzesek, header);
        jt.setBounds(50, 40, 200, 0);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }
    //naplo.sort(new BejegyzesKezdoIdopontComparitor());

    public static JTable findBasedOnDate(LocalDate dt)
    {
        if (naplo.size() == 0)
        {
            beolv();
        }
        String[][] bejegyzesek = new String[naplo.size()][4];
        int idx = 0;
        for (Bejegyzes b :
                naplo)
        {
            if (b.getDatum().equals(dt))
            {
                bejegyzesek[idx++] = b.toArray();
            }
        }

        JTable jt = new JTable(bejegyzesek, header);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }

    private static int writeToFile()
    {
        try
        {
            File file = new File(filepath);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);


            fw.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static boolean validate(Bejegyzes b)
    {

        return false;
    }

}
