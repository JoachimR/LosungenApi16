package de.reiss.android.losungen.architecture.di

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.android.losungen.apk.ApkHandler
import de.reiss.android.losungen.language.LanguageRepository
import de.reiss.android.losungen.main.content.LosungRepository
import de.reiss.android.losungen.main.viewpager.ViewPagerRepository
import de.reiss.android.losungen.note.details.NoteDetailsRepository
import de.reiss.android.losungen.note.edit.EditNoteRepository
import de.reiss.android.losungen.note.export.NoteExportRepository
import de.reiss.android.losungen.note.list.NoteListRepository
import de.reiss.android.losungen.notification.NotificationHelper
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.preferences.AppPreferencesRepository
import de.reiss.android.losungen.rawdata.RawToDatabase
import de.reiss.android.losungen.widget.WidgetRefresher

@ApplicationScope
@Component(modules = [
    ContextModule::class,
    AndroidModule::class,
    DatabaseModule::class,
    PreferenceModule::class,
    ExecutorModule::class,
    NotesExportModule::class])
interface ApplicationComponent {

    val context: Context
    val clipboardManager: ClipboardManager

    val viewPagerRepository: ViewPagerRepository
    val losungRepository: LosungRepository
    val languageRepository: LanguageRepository
    val appPreferencesRepository: AppPreferencesRepository
    val editNoteRepository: EditNoteRepository
    val noteListRepository: NoteListRepository
    val noteDetailsRepository: NoteDetailsRepository
    val noteExportRepository: NoteExportRepository

    val widgetRefresher: WidgetRefresher

    val notificationHelper: NotificationHelper
    val appPreferences: AppPreferences

    val searchManager: SearchManager

    val apkHandler: ApkHandler
    val rawToDatabase: RawToDatabase

}
