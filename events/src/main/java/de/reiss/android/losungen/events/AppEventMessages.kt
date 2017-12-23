package de.reiss.android.losungen.events

sealed class AppEventMessage

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

class AppStyleChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()
