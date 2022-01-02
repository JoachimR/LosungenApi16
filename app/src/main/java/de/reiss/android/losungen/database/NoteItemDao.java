package de.reiss.android.losungen.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface NoteItemDao {

    @Query("SELECT * FROM NoteItem")
    List<NoteItem> all();

    @Query("SELECT * FROM NoteItem WHERE date = :date")
    NoteItem byDate(Date date);

    @Query("SELECT * FROM NoteItem WHERE date BETWEEN :from AND :to")
    List<NoteItem> range(Date from, Date to);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(NoteItem... items);

    @Delete
    int delete(NoteItem... item);

    @Query("DELETE FROM NoteItem")
    void clear();

}
