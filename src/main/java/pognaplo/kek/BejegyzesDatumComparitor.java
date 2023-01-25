package pognaplo.kek;

import java.util.Comparator;

public class BejegyzesDatumComparitor implements Comparator<Bejegyzes>
{
    @Override
    public int compare(Bejegyzes o1, Bejegyzes o2)
    {
        if(o1.getDatum() == null || o2.getDatum() == null) {
            return 0;
        }
        if (o1.getDatum().compareTo(o2.getDatum()) != 0){
            return o1.getDatum().compareTo(o2.getDatum());
        } else{
            return o1.getKezdoIdopont().compareTo(o2.getKezdoIdopont());
        }

    }
}
