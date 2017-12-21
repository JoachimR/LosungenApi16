package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import de.reiss.android.losungen.model.LosungContent;

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
    public NoteItem(Date date, LosungContent content, String note) {
        this.date = date;
        this.text1 = content.getText1();
        this.source1 = content.getSource1();
        this.text2 = content.getText2();
        this.source2 = content.getSource2();
        this.note = note;
    }

}
