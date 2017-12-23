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
