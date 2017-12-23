package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"languageId", "startdate"}, unique = true)},
        foreignKeys = @ForeignKey(entity = LanguageItem.class, parentColumns = "id", childColumns = "languageId"))
public class WeeklyLosungDatabaseItem implements LosungDatabaseItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int languageId;

    public Date startdate;
    public Date enddate;
    public String holiday;
    public String text;
    public String source;

    public WeeklyLosungDatabaseItem() {
    }

    @Ignore
    public WeeklyLosungDatabaseItem(int languageId, Date startdate, Date enddate, String holiday,
                                    String text, String source) {
        this.languageId = languageId;
        this.startdate = startdate;
        this.enddate = enddate;
        this.holiday = holiday;
        this.text = text;
        this.source = source;
    }

}
