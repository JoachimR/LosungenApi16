package de.reiss.android.losungen.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LanguageItemDao {

    @Query("SELECT * FROM LanguageItem")
    List<LanguageItem> all();

    @Query("SELECT * FROM LanguageItem WHERE language = :language")
    LanguageItem find(String language);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(LanguageItem item);

    @Insert
    long[] insertAll(LanguageItem... items);

    @Delete
    void delete(LanguageItem... item);

    @Query("DELETE FROM LanguageItem")
    void clear();

}
