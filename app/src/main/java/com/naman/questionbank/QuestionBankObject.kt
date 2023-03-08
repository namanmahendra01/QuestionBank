package com.naman.questionbank

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.constants.EXAM_CATEGORY_KEY
import com.naman.questionbank.constants.EXAM_PDF_PATH
import kotlinx.coroutines.flow.MutableSharedFlow

object QuestionBankObject {
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var firebaseDb: FirebaseDatabase? = null
    var examCategoriesDbRef: DatabaseReference? = null
    val actionPerformerSharedFlow = MutableSharedFlow<Envelope>(replay = 0)
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

}