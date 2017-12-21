package de.reiss.android.losungen.architecture

import de.reiss.android.losungen.architecture.AsyncLoadStatus.*

data class AsyncLoad<out T>(val loadStatus: AsyncLoadStatus,
                            val data: T?,
                            val message: String?) {

    companion object {

        fun <T> success(newData: T? = null, message: String? = null): AsyncLoad<T> =
                AsyncLoad(SUCCESS, newData, message)

        fun <T> error(oldData: T? = null, message: String? = null): AsyncLoad<T> =
                AsyncLoad(ERROR, oldData, message)

        fun <T> loading(oldData: T? = null, message: String? = null): AsyncLoad<T> =
                AsyncLoad(LOADING, oldData, message)

    }

}