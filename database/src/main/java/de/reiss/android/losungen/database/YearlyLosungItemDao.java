package de.reiss.android.losungen.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface YearlyLosungItemDao {

    @Query("SELECT * FROM YearlyLosungItem")
    List<YearlyLosungItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM YearlyLosungItem" +
            " INNER JOIN LanguageItem ON YearlyLosungItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND YearlyLosungItem.date = :date")
    YearlyLosungItem byDate(int languageId, Date date);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(YearlyLosungItem... items);

    @Delete
    void delete(YearlyLosungItem... item);

    @Query("DELETE FROM YearlyLosungItem")
    void clear();

}
