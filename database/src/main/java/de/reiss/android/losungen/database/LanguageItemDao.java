package de.reiss.android.losungen.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
