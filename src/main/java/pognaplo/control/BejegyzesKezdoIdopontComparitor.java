package pognaplo.control;

import java.util.Comparator;

public class BejegyzesKezdoIdopontComparitor implements Comparator<Bejegyzes>
{
    @Override
    public int compare(Bejegyzes o1, Bejegyzes o2)
    {
        if (o1.getKezdoIdopont().isBefore(o2.getKezdoIdopont()))
        {
            return -1;
        } else if (o2.getKezdoIdopont().isBefore(o1.getKezdoIdopont()))
        {
            return 1;
        } else
        {
            return 0;
        }
    }
}
