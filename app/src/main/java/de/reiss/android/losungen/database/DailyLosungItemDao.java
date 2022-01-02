package de.reiss.android.losungen.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyLosungItemDao {

    @Query("SELECT * FROM DailyLosungDatabaseItem")
    List<DailyLosungDatabaseItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM DailyLosungDatabaseItem" +
            " INNER JOIN LanguageItem ON DailyLosungDatabaseItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND DailyLosungDatabaseItem.date = :date")
    DailyLosungDatabaseItem byDate(int languageId, Date date);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM DailyLosungDatabaseItem" +
            " INNER JOIN LanguageItem ON DailyLosungDatabaseItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND DailyLosungDatabaseItem.date BETWEEN :from AND :to")
    List<DailyLosungDatabaseItem> range(int languageId, Date from, Date to);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(DailyLosungDatabaseItem... items);

    @Delete
    void delete(DailyLosungDatabaseItem... item);

    @Query("DELETE FROM DailyLosungDatabaseItem")
    void clear();

}
