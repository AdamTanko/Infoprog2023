package pognaplo.control;

import pognaplo.exceptions.RosszDatumException;
import pognaplo.exceptions.RosszIdoException;
import pognaplo.frontend.ErrorDialog;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main operating class of the project
 */
public class Controller {
    private static final String[] HEADER = {"dátum", "kezdő időpont", "záró időpont", "esemény"};

    public static final ImageIcon ICON = new ImageIcon("images/Infoprog logo kicsi.png");

    private static String FILEPATH = "txt/naplo.txt";

    public static void setFilepath(String filepath) {
        Controller.FILEPATH = filepath;
    }

    public static String getFilepath() {
        return FILEPATH;
    }

    public static ArrayList<Bejegyzes> naplo = new ArrayList<>();

    private static StringBuilder errors = new StringBuilder();

    /**
     * Beolvassa a naplo.txt tartalmát a naplo {@link ArrayList}-be
     */
    public static void beolv(boolean displayErrorMsgs) {
        try {
            Scanner sc = new Scanner(new File(FILEPATH));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            int linecounter = 0;

            while (sc.hasNext()) {
                try {
                    linecounter++;
                    String[] tokens = sc.nextLine().split(",");
                    if (isValid(tokens)) {
                        naplo.add(new Bejegyzes(tryParseDate(tokens[0]),
                                LocalTime.parse(tokens[1], timeFormatter),
                                LocalTime.parse(tokens[2], timeFormatter),
                                tokens[3]));
                    }
                } catch (DateTimeException ignored) {
                    errors.append("Rossz bemenet a ").append(linecounter).append(". sorban\n");
                } catch (StringIndexOutOfBoundsException ignored) {
                    errors.append("Túl hosszú leírás a ").append(linecounter).append(". sorban\n");

                } catch (RosszDatumException ignored) {
                    errors.append("Rossz dátum a ").append(linecounter).append(". sorban\n");
                }
            }

            if ((!errors.isEmpty()) && displayErrorMsgs) {
                new ErrorDialog(errors.toString());
            }
            errors = new StringBuilder();
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isUnique(Bejegyzes b) {
        for (Bejegyzes b2 : naplo) {
            if (b2.getDatum().equals(b.getDatum()) &&
                    (!(b2.getZaroIdopont().isBefore(b.getKezdoIdopont()) ||
                            b.getZaroIdopont().isBefore(b2.getKezdoIdopont())))) {
                return false;
            }
        }
        return true;
    }


    public static JTable listItems() {

        if (naplo.isEmpty()) beolv(true);
        naplo.sort(new BejegyzesDatumComparitor());
        String[][] bejegyzesek = new String[naplo.size()][4];
        int yes = 0;
        for (Bejegyzes b : naplo) {
            bejegyzesek[yes++] = b.toArray();

        }

        JTable jt = new JTable(bejegyzesek, HEADER);
        jt.setBounds(50, 40, 200, 0);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }


    public static JTable findBasedOnDate(LocalDate dt) {
        beolv(false);
        String[][] bejegyzesek = new String[naplo.size()][4];
        int idx = 0;
        for (Bejegyzes b : naplo) {
            if (b.getDatum().equals(dt)) {
                bejegyzesek[idx++] = b.toArray();
            }
        }
        if (bejegyzesek.length == 0) {
            return null;
        }
        JTable jt = new JTable(bejegyzesek, HEADER);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }


    /**
     * Beírja a naplo.txt-be a naplo {@link ArrayList} tartalmát
     */
    public static int writeToFile() {
        try {
            File file = new File(FILEPATH);
            FileWriter fw = new FileWriter(file);

            for (Bejegyzes b :
                    naplo) {
                fw.write(b.toString());
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public static void deleteExpiredItems() {
        if (naplo.isEmpty()) {
            beolv(false);
        }
        LocalDateTime now = LocalDateTime.now();
        Iterator<Bejegyzes> iterator = naplo.iterator();
        int deletedItems = 0;
        while (iterator.hasNext()) {
            Bejegyzes b = iterator.next();
            if (now.toLocalDate().equals(b.getDatum())) {
                if (now.toLocalTime().isAfter(b.getZaroIdopont())) {
                    iterator.remove();
                    deletedItems++;

                }
            } else if (now.toLocalDate().isAfter(b.getDatum())) {
                iterator.remove();
                deletedItems++;

            }
        }
        JOptionPane.showMessageDialog(null, deletedItems + " bejegyzés lett törölve", "Törlés sikeres", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean isValid(String... input) {
        try {
            LocalDate dt = tryParseDate(input[0]);
            if (dt == null) {
                throw new RosszDatumException("");
            }
            LocalTime kezdoIdo = null;
            LocalTime zaroIdo = null;
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("H:mm");
            try {
                kezdoIdo = LocalTime.parse(input[1], tf);
                zaroIdo = LocalTime.parse(input[2], tf);
            } catch (DateTimeException ignored) {
                errors.append("Rossz idő\n");

            }
            if (kezdoIdo == null || zaroIdo == null) {
                throw new NullPointerException("");
            }
            if (zaroIdo.isBefore(kezdoIdo)) {
                throw new RosszIdoException("");
            }
            if (input[3].length() > 250) {
                throw new StringIndexOutOfBoundsException("");
            }
            if (!isUnique(new Bejegyzes(dt, kezdoIdo, zaroIdo, input[3]))) {
                return false;
            }
        } catch (RosszDatumException ignored) {
            errors.append("Rossz dátum\n");

            return false;
        } catch (NullPointerException ignored) {
            errors.append("Hiba történt a détum vagy az idő beolvasásánál\n");

            return false;
        } catch (StringIndexOutOfBoundsException ignored) {
            errors.append("Túll hosszú leírás\n");

            return false;
        } catch (RosszIdoException e) {
            errors.append("A záró idő nem lehet a kezdő idő előtt\n");
            return false;
        }


        return true;
    }

    public static LocalDate tryParseDate(String input) throws DateTimeException, NullPointerException, RosszDatumException {
        final String REGEX = "[-,.]";
        try {
            Integer.parseInt(input.split(REGEX)[1]);
        } catch (NumberFormatException ignored) {
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

            String yes = input.split(REGEX)[1].toUpperCase();
            String no = months.get(input.split(REGEX)[1].toUpperCase()).toString();
            input = input.toUpperCase().replace(yes, no);
        }

        Year year = Year.parse(input.split(REGEX)[2]);
        if (!year.isLeap() && input.split(REGEX)[1].equals("2") && input.split(REGEX)[0].equals("29")) {
            throw new RosszDatumException("");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy");
        LocalDate dt = LocalDate.parse(input, formatter);

        if (dt == null) {
            System.out.println("Nem sikerult parseolni");
        }
        return dt;
    }
}
