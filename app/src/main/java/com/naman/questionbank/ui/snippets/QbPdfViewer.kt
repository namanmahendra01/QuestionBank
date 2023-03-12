package com.naman.questionbank.ui.snippets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.common.Utils
import com.naman.questionbank.databinding.QbPdfViewerLayoutBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.snippetData.PdfViewerData


class QbPdfViewer : ConstraintLayout, ISnippetSetData<PdfViewerData> {
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
    }

    private val binding: QbPdfViewerLayoutBinding =
        QbPdfViewerLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private var currentData: PdfViewerData? = null

    var dirPath: String? = null

    override fun setData(t: PdfViewerData?) {
        t ?: return
        currentData = t
        dirPath = currentData?.url?.let { Utils.docsReceivedFile(it).absolutePath }

        val fileName = Utils.getFileNameFromURL(currentData?.url)
        PRDownloader.download(currentData?.url, dirPath, fileName)
            .build()
            .setOnStartOrResumeListener {}
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener { }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    checkStatusAndShowPdf(fileName)
                }

                override fun onError(error: Error?) {}
            })
    }

    private fun checkStatusAndShowPdf(fileName: String) {
        dirPath?.let { binding.pdfView.fromFile("$it/$fileName") }
        binding.pdfView.show()
        binding.pdfView.postDelayed( {
            QuestionBankObject.emitCloseProgressBarEvent()
        },1000)
    }
}
