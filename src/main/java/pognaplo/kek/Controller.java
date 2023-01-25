package main.java.pognaplo.kek;

import main.java.pognaplo.exceptions.RosszDatumException;
import main.java.pognaplo.exceptions.RosszIdoException;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Controller
{

    public static final ImageIcon ICON = new ImageIcon("images/Infoprog logo kicsi.png");
    public static final String TITLE = "Napló";

    public static void setFilepath(String filepath)
    {
        Controller.FILEPATH = filepath;
    }

    public static String getFilepath()
    {
        return FILEPATH;
    }

    private static String FILEPATH = "txt/naplo.txt";
    public static final ArrayList<Bejegyzes> naplo = new ArrayList<>();

    private static final String[] header = {"dátum", "kezdő időpont", "záró időpont", "esemény"};

    public static void beolv()
    {
        try
        {
            Scanner sc = new Scanner(new File(FILEPATH));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            int linecounter = 0;
            while (sc.hasNext())
            {
                try
                {
                    linecounter++;
                    String[] tokens = sc.nextLine().split(",");
                    if (isValid(tokens))
                    {
                        naplo.add(new Bejegyzes(tryParseDate(tokens[0]),
                                LocalTime.parse(tokens[1], timeFormatter),
                                LocalTime.parse(tokens[2], timeFormatter),
                                tokens[3],
                                true));
                    }
                } catch (DateTimeException ignored)
                {
                    JOptionPane.showMessageDialog(null, "Rossz bemenet a " + linecounter + ". sorban", "Rossz bemenet", JOptionPane.ERROR_MESSAGE);
                } catch (StringIndexOutOfBoundsException ignored)
                {
                    JOptionPane.showMessageDialog(null, "Tul hosszu leiras a " + linecounter + ". sorban", "Rossz bemenet", JOptionPane.ERROR_MESSAGE);
                } catch (RosszDatumException ignored) {
                    JOptionPane.showMessageDialog(null, "Rossz datum a " + linecounter + ". sorban", "Rossz datum", JOptionPane.ERROR_MESSAGE);
                }
            }
            sc.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hitelesiti a beadaott bejegyzes egyeniseget
     *
     * @param b - egy bejegyzes
     * @return - igaz, ha
     */
    public static boolean isUnique(Bejegyzes b)
    {
        for (Bejegyzes b2 : naplo)
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
        for (Bejegyzes b : naplo)
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
        for (Bejegyzes b : naplo)
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

    public static int writeToFile()
    {
        try
        {
            File file = new File(FILEPATH);
            FileWriter fw = new FileWriter(file);

            for (Bejegyzes b :
                    naplo)
            {
                fw.write(b.toString());

            }

            fw.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public static void deletExpiredItems(LocalDateTime ldt)
    {
        if (naplo.size() == 0)
        {
            beolv();
        }
        int deletedItems = 0;
        for (int i = 0; i < naplo.size(); i++)
        {
            Bejegyzes b = naplo.get(i);
            if (ldt.toLocalDate().equals(b.getDatum()))
            {
                if (ldt.toLocalTime().isAfter(b.getZaroIdopont()))
                {
                    naplo.remove(b);
                    deletedItems++;
                }
            } else if (ldt.toLocalDate().isAfter(b.getDatum()))
            {
                naplo.remove(b);
                deletedItems++;
            }
        }


    }

    public static boolean isValid(String dateS, String kezdoidoS, String zaroidoS, String leiras)
    {
        try
        {
            LocalDate dt = tryParseDate(dateS);
            if (dt == null)
            {
                throw new RosszDatumException("");
            }
            LocalTime kezdoIdo = null;
            LocalTime zaroIdo = null;
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("H:mm");
            try
            {
                kezdoIdo = LocalTime.parse(kezdoidoS, tf);
                zaroIdo = LocalTime.parse(zaroidoS, tf);
            } catch (DateTimeException ignored)
            {
                JOptionPane.showMessageDialog(null, "Rossz ido", "", JOptionPane.ERROR_MESSAGE);
            }
            if (kezdoIdo == null || zaroIdo == null)
            {
                throw new NullPointerException("");
            }
            if (zaroIdo.isBefore(kezdoIdo))
            {
                throw new RosszIdoException("");
            }
            if (leiras.length() > 250)
            {
                throw new StringIndexOutOfBoundsException("");
            }
            if (!isUnique(new Bejegyzes(dt, kezdoIdo, zaroIdo, leiras)))
            {
                return false;
            }
        } catch (RosszDatumException ignored)
        {
            JOptionPane.showMessageDialog(null, "Rossz datum", "Rossz datum", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NullPointerException ignored)
        {
            JOptionPane.showMessageDialog(null, "Hiba tortent a datum vagy az ido beolvasasanal", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (StringIndexOutOfBoundsException ignored)
        {
            JOptionPane.showMessageDialog(null, "tull hossz leiras", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RosszIdoException e)
        {
            JOptionPane.showMessageDialog(null, "A zaro ido nem lehet a kezdo ido elott", "Rossz Ido", JOptionPane.ERROR_MESSAGE);
        }


        return true;
    }

    public static boolean isValid(String[] input)
    {
        String dateS = input[0];
        String kezdoidoS = input[1];
        String zaroidoS = input[2];
        String leiras = input[3];
        return isValid(dateS, kezdoidoS, zaroidoS, leiras);
    }

    public static LocalDate tryParseDate(String input) throws DateTimeException, NullPointerException, RosszDatumException
    {
        try
        {
            Integer.parseInt(input.split("-")[1]);
        } catch (NumberFormatException ignored)
        {
            HashMap<String, Integer> months = new HashMap<>();
            months.put("JAN", 1);
            months.put("JANUAR", 1);
            months.put("JANUÁR", 1);
            months.put("FEB", 2);
            months.put("FEBRUAR", 2);
            months.put("FEBRUÁR", 2);
            months.put("MAR", 3);
            months.put("MÁR", 3);
            months.put("MARCIUS", 3);
            months.put("MÁRCIUS", 3);
            months.put("APR", 4);
            months.put("ÁPR", 4);
            months.put("APRILIS", 4);
            months.put("ÁPRILIS", 4);
            months.put("MAJ", 5);
            months.put("MÁJ", 5);
            months.put("MAJUS", 5);
            months.put("MÁJUS", 5);
            months.put("JUN", 6);
            months.put("JÚNIUS", 6);
            months.put("JUL", 7);
            months.put("JÚL", 7);
            months.put("JULIUS", 7);
            months.put("JÚLIUS", 7);
            months.put("AUG", 8);
            months.put("AUGUSZTUS", 8);
            months.put("SZEP", 9);
            months.put("SZEPTEMBER", 9);
            months.put("SEP", 9);
            months.put("SEPTEMBER", 9);
            months.put("OKT", 10);
            months.put("OKTOBER", 10);
            months.put("OKTÓBER", 10);
            months.put("NOV", 11);
            months.put("NOVEMBER", 11);
            months.put("DEC", 12);
            months.put("DECEMBER", 12);

            String yes = input.split("-")[1].toUpperCase();
            String no = months.get(input.split("-")[1].toUpperCase()).toString();
            input = input.toUpperCase().replace(yes, no);
        }

        Year year = Year.parse(input.split("-")[2]);
        if (!year.isLeap() && input.split("-")[1].equals("2") && input.split("-")[0].equals("29"))
        {
            throw new RosszDatumException("");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy");
        LocalDate dt = LocalDate.parse(input, formatter);

        if (dt == null)
        {
            System.out.println("Nem sikerult parseolni");
        }
        return dt;
    }
}
