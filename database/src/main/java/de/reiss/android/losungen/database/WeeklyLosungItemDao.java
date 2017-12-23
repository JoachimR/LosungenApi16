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
public interface WeeklyLosungItemDao {

    @Query("SELECT * FROM WeeklyLosungDatabaseItem")
    List<WeeklyLosungDatabaseItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM WeeklyLosungDatabaseItem" +
            " INNER JOIN LanguageItem ON WeeklyLosungDatabaseItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId" +
            " AND WeeklyLosungDatabaseItem.startdate <= :date " +
            " AND WeeklyLosungDatabaseItem.enddate >= :date" +
            " ORDER BY WeeklyLosungDatabaseItem.startdate" +
            " LIMIT 1")
    WeeklyLosungDatabaseItem byDate(int languageId, Date date);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(WeeklyLosungDatabaseItem... items);

    @Delete
    void delete(WeeklyLosungDatabaseItem... item);

    @Query("DELETE FROM WeeklyLosungDatabaseItem")
    void clear();

}
