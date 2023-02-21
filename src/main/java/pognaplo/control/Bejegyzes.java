package pognaplo.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Bejegyzes
{
    private final LocalDate datum;

    private final LocalTime kezdoIdopont;
    private final LocalTime zaroIdopont;
    private final String esemeny;






    public Bejegyzes(LocalDate datum, LocalTime kezdoIdopont, LocalTime zaroIdopont, String esemeny)
    {
        this.datum = datum;
        this.kezdoIdopont = kezdoIdopont;
        this.zaroIdopont = zaroIdopont;
        this.esemeny = esemeny;
    }


    public LocalDate getDatum()
    {
        return datum;
    }

    public LocalTime getKezdoIdopont()
    {
        return kezdoIdopont;
    }

    public LocalTime getZaroIdopont()
    {
        return zaroIdopont;
    }


    public String[] toArray() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        return new String[]{datum.format(formatter), kezdoIdopont.format(timeFormatter), zaroIdopont.format(timeFormatter), esemeny};
    }

    @Override
    public String toString() {
        return this.datum.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ',' + this.kezdoIdopont.toString() + ',' + this.zaroIdopont.toString() + ',' + this.esemeny +'\n';
    }
}
