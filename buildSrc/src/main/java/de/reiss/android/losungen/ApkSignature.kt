package de.reiss.android.losungen

import java.io.File

data class ApkSignature(val storeFile: File,
                        val storePassword: String,
                        val keyAlias: String,
                        val keyPassword: String)