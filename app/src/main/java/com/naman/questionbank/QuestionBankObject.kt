package com.naman.questionbank

import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.constants.EXAM_CATEGORY_KEY
import com.naman.questionbank.constants.EXAM_PDF_PATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object QuestionBankObject {
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var firebaseDb: FirebaseDatabase? = null
    var applicationContext: ApplicationClass? = null
    var examCategoriesDbRef: DatabaseReference? = null
    val actionPerformerSharedFlow = MutableSharedFlow<Envelope>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    var pdfReference: StorageReference? = null


    init {
        initFirebase()
        getFirebaseChildReference()
    }

    private fun getFirebaseChildReference() {
        examCategoriesDbRef = firebaseDb?.getReference(EXAM_CATEGORY_KEY)
        pdfReference = storageReference?.child(EXAM_PDF_PATH)
    }

    private fun initFirebase() {
        firebaseDb = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
    }

    fun emitCloseProgressBarEvent() {
        CoroutineScope(Dispatchers.Default).launch {
            actionPerformerSharedFlow.emit(
                Envelope(ActionType.CLOSE_PROGRESS_BAR,true)
            )
        }
    }

}