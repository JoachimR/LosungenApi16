package de.reiss.android.losungen.database;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {
        LanguageItem.class,
        DailyLosungDatabaseItem.class,
        WeeklyLosungDatabaseItem.class,
        MonthlyLosungDatabaseItem.class,
        YearlyLosungDatabaseItem.class,
        NoteItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LosungDatabase extends RoomDatabase {

    public static LosungDatabase create(Application application) {
        return Room.databaseBuilder(application, LosungDatabase.class, "LosungDatabase.db")
                .allowMainThreadQueries() // for widgets
                .build();
    }

    public abstract DailyLosungItemDao dailyLosungItemDao();

    public abstract WeeklyLosungItemDao weeklyLosungItemDao();

    public abstract MonthlyLosungItemDao monthlyLosungItemDao();

    public abstract YearlyLosungItemDao yearlyLosungItemDao();

    public abstract LanguageItemDao languageItemDao();

    public abstract NoteItemDao noteItemDao();

}