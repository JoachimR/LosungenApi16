package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"languageId", "date"}, unique = true)},
        foreignKeys = @ForeignKey(entity = LanguageItem.class, parentColumns = "id", childColumns = "languageId"))
public class DailyLosungDatabaseItem implements LosungDatabaseItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int languageId;

    public Date date;
    public String holiday;
    public String text1;
    public String source1;
    public String text2;
    public String source2;

    public DailyLosungDatabaseItem() {
    }

    @Ignore
    public DailyLosungDatabaseItem(int id, int languageId, Date date, String holiday, String text1,
                                   String source1, String text2, String source2) {
        this.id = id;
        this.languageId = languageId;
        this.date = date;
        this.holiday = holiday;
        this.text1 = text1;
        this.source1 = source1;
        this.text2 = text2;
        this.source2 = source2;
    }

    @Ignore
    public DailyLosungDatabaseItem(int languageId, Date date, String holiday, String text1,
                                   String source1, String text2, String source2) {
        this.languageId = languageId;
        this.date = date;
        this.holiday = holiday;
        this.text1 = text1;
        this.source1 = source1;
        this.text2 = text2;
        this.source2 = source2;
    }
}
