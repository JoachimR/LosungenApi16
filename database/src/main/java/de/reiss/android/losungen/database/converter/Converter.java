package de.reiss.android.losungen.database.converter;


import org.jetbrains.annotations.Nullable;

import de.reiss.android.losungen.database.DailyLosungItem;
import de.reiss.android.losungen.database.NoteItem;
import de.reiss.android.losungen.model.LosungContent;
import de.reiss.android.losungen.model.DailyLosung;
import de.reiss.android.losungen.model.Note;

public class Converter {

    @Nullable
    public static DailyLosung itemToDailyLosung(String language,
                                                @Nullable DailyLosungItem item) {
        if (item == null) {
            return null;
        }
        return new DailyLosung(language,
                item.date,
                item.holiday,
                new LosungContent(item.text1, item.source1, item.text2, item.source2));
    }

    @Nullable
    public static Note itemToNote(@Nullable NoteItem item) {
        if (item == null) {
            return null;
        }
        return new Note(item.date,
                item.note,
                new LosungContent(item.text1, item.source1, item.text2, item.source2));
    }

}