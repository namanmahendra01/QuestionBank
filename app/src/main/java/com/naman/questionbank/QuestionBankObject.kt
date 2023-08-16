package com.naman.questionbank

import android.provider.Settings
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.constants.EXAM_CATEGORY_KEY
import com.naman.questionbank.constants.EXAM_PDF_PATH
import com.naman.questionbank.constants.PAYMENT_DETAILS_KEY
import com.naman.questionbank.payment.UserDetailsModel
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
    var paymentDbRef: DatabaseReference? = null
    var userDetails: UserDetailsModel? = null
    var uid: String? = null
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

    private fun getUserDetails() {
        val uuidRef = uid?.let { paymentDbRef?.child(it) }
        uuidRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    userDetails = dataSnapshot.getValue(UserDetailsModel::class.java)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getUid() {
        uid= Settings.Secure.getString(applicationContext?.contentResolver, Settings.Secure.ANDROID_ID)
        getUserDetails()
    }

    private fun getFirebaseChildReference() {
        examCategoriesDbRef = firebaseDb?.getReference(EXAM_CATEGORY_KEY)
        paymentDbRef = firebaseDb?.getReference(PAYMENT_DETAILS_KEY)
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