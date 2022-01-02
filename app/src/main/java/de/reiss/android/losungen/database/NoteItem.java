package de.reiss.android.losungen.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import de.reiss.android.losungen.model.BibleTextPair;

@Entity(indices = {@Index(value = {"date"}, unique = true)})
public class NoteItem {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public Date date;
    public String holiday;
    public String text1;
    public String source1;
    public String text2;
    public String source2;
    public String note;

    public NoteItem() {
    }

    @Ignore
    public NoteItem(Date date,
                    BibleTextPair bibleTextPair,
                    String note) {
        this.date = date;
        this.text1 = bibleTextPair.getFirst().getText();
        this.source1 = bibleTextPair.getFirst().getSource();
        this.text2 = bibleTextPair.getSecond().getText();
        this.source2 = bibleTextPair.getSecond().getSource();
        this.note = note;
    }

}
