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
public interface MonthlyLosungItemDao {

    @Query("SELECT * FROM MonthlyLosungDatabaseItem")
    List<MonthlyLosungDatabaseItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MonthlyLosungDatabaseItem" +
            " INNER JOIN LanguageItem ON MonthlyLosungDatabaseItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND MonthlyLosungDatabaseItem.date = :date")
    MonthlyLosungDatabaseItem byDate(int languageId, Date date);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(MonthlyLosungDatabaseItem... items);

    @Delete
    void delete(MonthlyLosungDatabaseItem... item);

    @Query("DELETE FROM MonthlyLosungDatabaseItem")
    void clear();

}
