package de.reiss.android.losungen.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface YearlyLosungItemDao {

    @Query("SELECT * FROM YearlyLosungDatabaseItem")
    List<YearlyLosungDatabaseItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM YearlyLosungDatabaseItem" +
            " INNER JOIN LanguageItem ON YearlyLosungDatabaseItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND YearlyLosungDatabaseItem.date = :date")
    YearlyLosungDatabaseItem byDate(int languageId, Date date);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(YearlyLosungDatabaseItem... items);

    @Delete
    void delete(YearlyLosungDatabaseItem... item);

    @Query("DELETE FROM YearlyLosungDatabaseItem")
    void clear();

}
