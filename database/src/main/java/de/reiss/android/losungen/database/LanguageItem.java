package de.reiss.android.losungen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"language"}, unique = true)})
public class LanguageItem {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String language;

    public String name;

    public String languageCode;

    public LanguageItem() {
    }

    @Ignore
    public LanguageItem(String language, String name, String languageCode) {
        this.language = language;
        this.name = name;
        this.languageCode = languageCode;
    }
}
