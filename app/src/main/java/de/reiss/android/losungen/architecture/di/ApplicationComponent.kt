package de.reiss.android.losungen.architecture.di

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.android.losungen.apk.ApkHandler
import de.reiss.android.losungen.language.LanguageRepository
import de.reiss.android.losungen.main.daily.LosungRepository
import de.reiss.android.losungen.main.daily.viewpager.ViewPagerRepository
import de.reiss.android.losungen.main.single.monthly.MonthlyLosungRepository
import de.reiss.android.losungen.main.single.weekly.WeeklyLosungRepository
import de.reiss.android.losungen.main.single.yearly.YearlyLosungRepository
import de.reiss.android.losungen.note.details.NoteDetailsRepository
import de.reiss.android.losungen.note.edit.EditNoteRepository
import de.reiss.android.losungen.note.export.NoteExportRepository
import de.reiss.android.losungen.note.list.NoteListRepository
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.preferences.AppPreferencesRepository
import de.reiss.android.losungen.widget.daily.DailyWidgetTextRefresher
import de.reiss.android.losungen.widget.monthly.MonthlyWidgetTextRefresher
import de.reiss.android.losungen.widget.weekly.WeeklyWidgetTextRefresher
import de.reiss.android.losungen.widget.yearly.YearlyWidgetTextRefresher

@ApplicationScope
@Component(
    modules = [
        ContextModule::class,
        AndroidModule::class,
        DatabaseModule::class,
        PreferenceModule::class,
        ExecutorModule::class,
        NotesExportModule::class]
)
interface ApplicationComponent {

    val context: Context
    val clipboardManager: ClipboardManager

    val viewPagerRepository: ViewPagerRepository
    val losungRepository: LosungRepository
    val weeklyLosungRepository: WeeklyLosungRepository
    val monthlyLosungRepository: MonthlyLosungRepository
    val yearlyLosungRepository: YearlyLosungRepository
    val languageRepository: LanguageRepository
    val appPreferencesRepository: AppPreferencesRepository
    val editNoteRepository: EditNoteRepository
    val noteListRepository: NoteListRepository
    val noteDetailsRepository: NoteDetailsRepository
    val noteExportRepository: NoteExportRepository

    val dailyWidgetTextRefresher: DailyWidgetTextRefresher
    val monthlyWidgetTextRefresher: MonthlyWidgetTextRefresher
    val weeklyWidgetTextRefresher: WeeklyWidgetTextRefresher
    val yearlyWidgetTextRefresher: YearlyWidgetTextRefresher

    val appPreferences: AppPreferences

    val searchManager: SearchManager

    val apkHandler: ApkHandler
}
