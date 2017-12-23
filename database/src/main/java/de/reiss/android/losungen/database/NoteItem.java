package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
