package pognaplo.control;

import java.util.Comparator;

public class DiaryEntryDateComparitor implements Comparator<DiaryEntry>
{
    @Override
    public int compare(DiaryEntry o1, DiaryEntry o2)
    {
        if(o1.getDate() == null || o2.getDate() == null) {
            return 0;
        }
        if (!o1.getDate().isEqual(o2.getDate())){
            return o1.getDate().compareTo(o2.getDate());
        } else{
            return o1.getStartTime().compareTo(o2.getStartTime());
        }

    }
}
