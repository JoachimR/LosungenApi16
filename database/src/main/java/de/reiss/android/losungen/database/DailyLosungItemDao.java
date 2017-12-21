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
public interface DailyLosungItemDao {

    @Query("SELECT * FROM DailyLosungItem")
    List<DailyLosungItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM DailyLosungItem" +
            " INNER JOIN LanguageItem ON DailyLosungItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND DailyLosungItem.date = :date")
    DailyLosungItem byDate(int languageId, Date date);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM DailyLosungItem" +
            " INNER JOIN LanguageItem ON DailyLosungItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND DailyLosungItem.date BETWEEN :from AND :to")
    List<DailyLosungItem> range(int languageId, Date from, Date to);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(DailyLosungItem... items);

    @Delete
    void delete(DailyLosungItem... item);

    @Query("DELETE FROM DailyLosungItem")
    void clear();

}
