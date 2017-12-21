package de.reiss.android.losungen.events

sealed class AppEventMessage

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

class FontSizeChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()
