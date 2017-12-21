package de.reiss.android.losungen.architecture.di

import dagger.Module
import dagger.Provides
import de.reiss.android.losungen.note.export.FileProvider
import de.reiss.android.losungen.note.export.NotesExporter

@Module
open class NotesExportModule {

    @Provides
    @ApplicationScope
    open fun fileProvider() = FileProvider()

    @Provides
    @ApplicationScope
    open fun notesExporter(fileProvider: FileProvider): NotesExporter = NotesExporter(fileProvider)

}