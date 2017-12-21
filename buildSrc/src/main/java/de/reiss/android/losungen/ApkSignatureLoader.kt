package de.reiss.android.losungen

import java.io.File
import java.util.*

@Suppress("unused")
class ApkSignatureLoader {

    fun loadApkSignature(propertiesFile: File): ApkSignature? {
        if (propertiesFile.canRead().not()) {
            println("apk_signature.properties not found")
            return null
        }

        val properties = Properties().apply {
            load(propertiesFile.inputStream())

            getProperty("storeFile")?.let { storeFilePath ->
                val storeFile = File(storeFilePath)
                if (storeFile.exists().not()) {
                    println("file $storeFilePath not found")
                    return null
                }

                getProperty("storePassword")?.let { storePassword ->
                    getProperty("keyAlias")?.let { keyAlias ->
                        getProperty("keyPassword")?.let { keyPassword ->

                            return ApkSignature(storeFile, storePassword, keyAlias, keyPassword)
                        }
                    }
                }
            }
        }
        println("Invalid properties $properties")
        return null
    }

}