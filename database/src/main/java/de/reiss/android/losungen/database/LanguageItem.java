package de.reiss.android.losungen.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
