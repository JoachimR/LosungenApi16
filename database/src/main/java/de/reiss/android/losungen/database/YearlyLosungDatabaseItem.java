package de.reiss.android.losungen.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"languageId", "date"}, unique = true)},
        foreignKeys = @ForeignKey(entity = LanguageItem.class, parentColumns = "id", childColumns = "languageId"))
public class YearlyLosungDatabaseItem implements LosungDatabaseItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int languageId;

    public Date date;
    public String text;
    public String source;

    public YearlyLosungDatabaseItem() {
    }

    @Ignore
    public YearlyLosungDatabaseItem(int languageId, Date date, String text, String source) {
        this.languageId = languageId;
        this.date = date;
        this.text = text;
        this.source = source;
    }

}
