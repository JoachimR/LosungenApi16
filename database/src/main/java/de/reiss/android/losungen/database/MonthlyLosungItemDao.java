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
