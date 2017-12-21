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

    @Query("SELECT * FROM WeeklyLosungItem")
    List<WeeklyLosungItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM WeeklyLosungItem" +
            " INNER JOIN LanguageItem ON WeeklyLosungItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND WeeklyLosungItem.date BETWEEN :startDate AND :endDate")
    WeeklyLosungItem byDate(int languageId, Date startDate, Date endDate);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(WeeklyLosungItem... items);

    @Delete
    void delete(WeeklyLosungItem... item);

    @Query("DELETE FROM WeeklyLosungItem")
    void clear();

}
