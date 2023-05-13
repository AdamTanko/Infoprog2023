package pognaplo.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * An entry in the diary.
 *
 */
public class DiaryEntry
{
    private final LocalDate date;

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String event;






    public DiaryEntry(LocalDate date, LocalTime startTime, LocalTime endTime, String event)
    {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
    }


    public LocalDate getDate()
    {
        return date;
    }

    public LocalTime getStartTime()
    {
        return startTime;
    }

    public LocalTime getEndTime()
    {
        return endTime;
    }


    public String[] toArray() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        return new String[]{date.format(formatter), startTime.format(timeFormatter), endTime.format(timeFormatter), event};
    }

    @Override
    public String toString() {
        return this.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ',' + this.startTime.toString() + ',' + this.endTime.toString() + ',' + this.event +'\n';
    }
}
