package de.reiss.android.losungen.note.export

sealed class NoteExportStatus

class NoPermissionStatus : NoteExportStatus()
class NoNotesStatus : NoteExportStatus()
class ExportErrorStatus(val directory: String, val fileName: String) : NoteExportStatus()
class ExportSuccessStatus(val directory: String, val fileName: String) : NoteExportStatus()
class ExportingStatus : NoteExportStatus()