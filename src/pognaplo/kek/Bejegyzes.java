package pognaplo.kek;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Bejegyzes
{
    private LocalDate datum;

    private LocalTime kezdoIdopont;
    private LocalTime zaroIdopont;
    private String leiras;

    public Bejegyzes()
    {
    }

    public Bejegyzes(LocalDate datum, LocalTime kezdoIdopont, LocalTime zaroIdopont, String leiras)
    {
        this.datum = datum;
        this.kezdoIdopont = kezdoIdopont;
        this.zaroIdopont = zaroIdopont;
        this.leiras = leiras;
    }

    public LocalDate getDatum()
    {
        return datum;
    }

    public void setDatum(LocalDate datum)
    {
        this.datum = datum;
    }

    public LocalTime getKezdoIdopont()
    {
        return kezdoIdopont;
    }

    public void setKezdoIdopont(LocalTime kezdoIdopont)
    {
        this.kezdoIdopont = kezdoIdopont;
    }

    public LocalTime getZaroIdopont()
    {
        return zaroIdopont;
    }

    public void setZaroIdopont(LocalTime zaroIdopont)
    {
        this.zaroIdopont = zaroIdopont;
    }

    public String getLeiras()
    {
        return leiras;
    }

    public void setLeiras(String leiras)
    {
        if (leiras.length() > 250)
        {
            System.err.println("Tul hosszu leiras");
        } else
        {
            this.leiras = leiras;
        }
    }


    public String[] toArray() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        return new String[]{datum.format(formatter), kezdoIdopont.format(timeFormatter), zaroIdopont.format(timeFormatter), leiras};
    }
}
