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

    @Query("SELECT * FROM MonthlyLosungItem")
    List<MonthlyLosungItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM MonthlyLosungItem" +
            " INNER JOIN LanguageItem ON MonthlyLosungItem.languageId = LanguageItem.id" +
            " WHERE LanguageItem.id = :languageId AND MonthlyLosungItem.date = :date")
    MonthlyLosungItem byDate(int languageId, Date date);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(MonthlyLosungItem... items);

    @Delete
    void delete(MonthlyLosungItem... item);

    @Query("DELETE FROM MonthlyLosungItem")
    void clear();

}
