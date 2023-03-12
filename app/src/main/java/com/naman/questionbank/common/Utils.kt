package com.naman.questionbank.common

import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.constants.APP_DIRECTORY
import com.naman.questionbank.constants.EMPTY
import com.naman.questionbank.constants.MEDIA_DIRECTORY
import java.io.File
import java.net.URL

object Utils {
    fun getFileNameFromURL(url: String?): String {
        if (url.isNullOrEmpty()) {
            return EMPTY
        }
        try {
            val resource = URL(url)
            val host = resource.host
            if (host.isNotEmpty() && url.endsWith(host)) {
                return EMPTY
            }
        } catch (e: Exception) {
            return ""
        }

        val startIndex = url.lastIndexOf('/') + 1
        val length = url.length

        // find end index for ?
        var lastQMPos = url.lastIndexOf('?')
        if (lastQMPos == -1) {
            lastQMPos = length
        }

        // find end index for #
        var lastHashPos = url.lastIndexOf('#')
        if (lastHashPos == -1) {
            lastHashPos = length
        }

        // calculate the end index
        val endIndex = lastQMPos.coerceAtMost(lastHashPos)
        return url.substring(startIndex, endIndex)
    }

    private val DOCS_RECEIVED_MIGRATED_PATH =
        QuestionBankObject.applicationContext?.getExternalFilesDir(null)
            .toString() + File.separator + APP_DIRECTORY + File.separator + MEDIA_DIRECTORY + "/QBDocuments/"


    fun docsReceivedFile(url: String): File {
        val rootPath = DOCS_RECEIVED_MIGRATED_PATH

        val f = File(rootPath)
        if (f.exists().not()) {
            f.mkdirs()
        }
        val file = File(rootPath + File.separator)
        file.createNewFile()
        return file
    }
}