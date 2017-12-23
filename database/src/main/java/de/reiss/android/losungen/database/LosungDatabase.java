package de.reiss.android.losungen.database;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

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
                .addMigrations(MIGRATION_0_1)
                .build();
    }

    static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.beginTransaction();

//            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
//                    + "`name` TEXT, PRIMARY KEY(`id`))");

            database.endTransaction();
        }
    };

    public abstract DailyLosungItemDao dailyLosungItemDao();

    public abstract WeeklyLosungItemDao weeklyLosungItemDao();

    public abstract MonthlyLosungItemDao monthlyLosungItemDao();

    public abstract YearlyLosungItemDao yearlyLosungItemDao();

    public abstract LanguageItemDao languageItemDao();

    public abstract NoteItemDao noteItemDao();

}