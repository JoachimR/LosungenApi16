package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"languageId", "startdate"}, unique = true)},
        foreignKeys = @ForeignKey(entity = LanguageItem.class, parentColumns = "id", childColumns = "languageId"))
public class WeeklyLosungItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int languageId;

    public Date startdate;
    public Date enddate;
    public String holiday;
    public String text1;
    public String source1;
    public String text2;
    public String source2;

    public WeeklyLosungItem() {
    }

    @Ignore

    public WeeklyLosungItem(int id, int languageId, Date startdate, Date enddate, String holiday,
                            String text1, String source1, String text2, String source2) {
        this.id = id;
        this.languageId = languageId;
        this.startdate = startdate;
        this.enddate = enddate;
        this.holiday = holiday;
        this.text1 = text1;
        this.source1 = source1;
        this.text2 = text2;
        this.source2 = source2;
    }

}
