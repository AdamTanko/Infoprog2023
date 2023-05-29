package pognaplo.control;

import pognaplo.exceptions.InvalidDateException;
import pognaplo.exceptions.InvalidTimeException;
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

    public static ArrayList<DiaryEntry> diary = new ArrayList<>();

    private static StringBuilder errors = new StringBuilder();

    /**
     * Reads the contents of naplo.txt into the diary {@link ArrayList}.
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
                        diary.add(new DiaryEntry(tryParseDate(tokens[0]),
                                LocalTime.parse(tokens[1], timeFormatter),
                                LocalTime.parse(tokens[2], timeFormatter),
                                tokens[3]));
                    }
                } catch (DateTimeException ignored) {
                    errors.append("Rossz bemenet a ").append(linecounter).append(". sorban\n");
                } catch (StringIndexOutOfBoundsException ignored) {
                    errors.append("Túl hosszú leírás a ").append(linecounter).append(". sorban\n");

                } catch (InvalidDateException ignored) {
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


    public static boolean isUnique(DiaryEntry b) {
        for (DiaryEntry b2 : diary) {
            if (b2.getDate().equals(b.getDate()) &&
                    (!(b2.getEndTime().isBefore(b.getStartTime()) ||
                            b.getEndTime().isBefore(b2.getStartTime())))) {
                return false;
            }
        }
        return true;
    }


    public static JTable listItems() {
        if (diary.isEmpty()) beolv(true);
        diary.sort(new DiaryEntryDateComparitor());
        String[][] entries = new String[diary.size()][4];
        int yes = 0;
        for (DiaryEntry b : diary) {
            entries[yes++] = b.toArray();

        }

        JTable jt = new JTable(entries, HEADER);
        jt.setBounds(50, 40, 200, 0);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }


    public static JTable findBasedOnDate(LocalDate dt) {
        beolv(false);
        String[][] entries = new String[diary.size()][4];
        int idx = 0;
        for (DiaryEntry b : diary) {
            if (b.getDate().equals(dt)) {
                entries[idx++] = b.toArray();
            }
        }
        if (entries.length == 0) {
            return null;
        }
        JTable jt = new JTable(entries, HEADER);
        jt.setDefaultEditor(Object.class, null);
        return jt;
    }


    /**
     * Writes the contents of the diary {@link ArrayList} back into naplo.txt.
     */
    public static int writeToFile() {
        try {
            File file = new File(FILEPATH);
            FileWriter fw = new FileWriter(file);

            for (DiaryEntry b :
                    diary) {
                fw.write(b.toString());
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public static void deleteExpiredItems() {
        if (diary.isEmpty()) {
            beolv(false);
        }
        LocalDateTime now = LocalDateTime.now();
        Iterator<DiaryEntry> iterator = diary.iterator();
        int deletedItems = 0;
        while (iterator.hasNext()) {
            DiaryEntry b = iterator.next();
            if (now.toLocalDate().equals(b.getDate())) {
                if (now.toLocalTime().isAfter(b.getEndTime())) {
                    iterator.remove();
                    deletedItems++;

                }
            } else if (now.toLocalDate().isAfter(b.getDate())) {
                iterator.remove();
                deletedItems++;

            }
        }
        JOptionPane.showMessageDialog(null, deletedItems + " number of entries have been deleted", "Deletion succesful", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean isValid(String... input) {
        try {
            LocalDate dt = tryParseDate(input[0]);
            LocalTime startTime = null;
            LocalTime endTime = null;
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("H:mm");
            try {
                startTime = LocalTime.parse(input[1], tf);
                endTime = LocalTime.parse(input[2], tf);
            } catch (DateTimeException ignored) {
                errors.append("Rossz idő\n");

            }
            if (startTime == null || endTime == null) {
                throw new NullPointerException("");
            }
            if (endTime.isBefore(startTime)) {
                throw new InvalidTimeException("");
            }
            if (input[3].length() > 250) {
                throw new StringIndexOutOfBoundsException("");
            }
            if (!isUnique(new DiaryEntry(dt, startTime, endTime, input[3]))) {
                return false;
            }
        } catch (InvalidDateException ignored) {
            errors.append("Invalid date\n");

            return false;
        } catch (NullPointerException ignored) {
            errors.append("An error occurred while trying to parse the date or the time\n");
            return false;
        } catch (StringIndexOutOfBoundsException ignored) {
            errors.append("Event description too long\n");
            return false;
        } catch (InvalidTimeException e) {
            errors.append("The end time cannot be before the start time\n");
            return false;
        }


        return true;
    }

    public static LocalDate tryParseDate(String input) throws DateTimeException, NullPointerException, InvalidDateException {
        final String REGEX = "[-,.]";
        try {
            Integer.parseInt(input.split(REGEX)[1]);
        } catch (NumberFormatException ignored) {
            // what is this even??? - AT
            HashMap<String, Integer> months = new HashMap<>();
            months.put("JAN", 1);
            months.put("JANUAR", 1);
            months.put("JANUARY", 1);
            months.put("JANUÁR", 1);
            months.put("FEB", 2);
            months.put("FEBRUAR", 2);
            months.put("FEBRUARY", 2);
            months.put("FEBRUÁR", 2);
            months.put("MAR", 3);
            months.put("MÁR", 3);
            months.put("MARCH", 3);
            months.put("MARCIUS", 3);
            months.put("MÁRCIUS", 3);
            months.put("APR", 4);
            months.put("ÁPR", 4);
            months.put("APRIL", 4);
            months.put("APRILIS", 4);
            months.put("ÁPRILIS", 4);
            months.put("MAJ", 5);
            months.put("MAY", 5);
            months.put("MÁJ", 5);
            months.put("MAJUS", 5);
            months.put("MÁJUS", 5);
            months.put("JUN", 6);
            months.put("JUNE", 6);
            months.put("JÚNIUS", 6);
            months.put("JUL", 7);
            months.put("JÚL", 7);
            months.put("JULY", 7);
            months.put("JULIUS", 7);
            months.put("JÚLIUS", 7);
            months.put("AUG", 8);
            months.put("AUGUSZTUS", 8);
            months.put("AUGUST", 8);
            months.put("SZEP", 9);
            months.put("SZEPTEMBER", 9);
            months.put("SEP", 9);
            months.put("SEPTEMBER", 9);
            months.put("OKT", 10);
            months.put("OKTOBER", 10);
            months.put("OCTOBER", 10);
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
            throw new InvalidDateException("");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy");

        return LocalDate.parse(input, formatter);
    }
}
