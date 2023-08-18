package com.naman.questionbank.pdfviewer.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.constants.QUERY_PATH
import com.naman.questionbank.constants.URL_KEY
import com.naman.questionbank.databinding.FragmentPdfViewerBinding
import com.naman.questionbank.payment.PaymentActivity
import com.naman.questionbank.pdfviewer.viewmodels.PdfViewerViewModel
import com.naman.questionbank.ui.snippetData.PdfViewerData
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class PdfViewerFragment : ViewBindingFragment<FragmentPdfViewerBinding>(){
    private var billingClient: BillingClient? = null

    companion object {
        private const val TAG = "com.naman.questionbank.BillingActivity"
        private const val ConsumeProductID = "question_paper"
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            PdfViewerFragment().apply {
                arguments = bundle ?: Bundle()
            }
    }

    private var viewModel: PdfViewerViewModel? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPdfViewerBinding
        get() = FragmentPdfViewerBinding::inflate

    override fun setup() {
        viewModel = ViewModelProvider(this)[PdfViewerViewModel::class.java]
        initView()
        setObservers()
        openPdf()
        getPdfUrl()
        setListeners()
    }

    private fun setListeners() {
        binding.paymentBtn.setOnClickListener {
            initializeBillingClient()
            establishConnection()
        }
    }

    private fun initializeBillingClient() {
        billingClient = this.context?.let {
            BillingClient.newBuilder(it)
                .enablePendingPurchases()
                .setListener { billingResult: BillingResult, list: List<Purchase>? ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                        for (purchase in list) {
                            Log.d(TAG, "Response is OK")
                            handlePurchase(purchase)
                        }
                    } else {
                        Log.d(TAG, "Response NOT OK")
                    }
                }.build()
        }
    }

    private fun establishConnection() {
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Connection established successfully")
                    // Query product details
                    getSingleInAppDetail()
                } else {
                    Log.d(TAG, "Connection failed, retrying...")
                    retryBillingServiceConnection()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing service disconnected, reconnecting...")
                retryBillingServiceConnection()
            }
        })
    }


    private fun retryBillingServiceConnection() {
        val maxRetries = 3
        val retryInterval: Long = 1000 // 1 second
        val retryCount = AtomicInteger(0)
        Thread(Runnable {
            while (retryCount.get() < maxRetries) {
                billingClient!!.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d(TAG, "Connection established successfully")
                            // Query product details
                            getSingleInAppDetail()
                            return   // Connection successful, exit loop
                        } else {
                            Log.d(TAG, "Connection failed, retrying...")
                            retryCount.incrementAndGet()
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        Log.d(TAG, "Billing service disconnected, reconnecting...")
                        // No need to handle reconnection here, onBillingSetupFinished does that
                    }
                })
                try {
                    Thread.sleep(retryInterval)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            if (retryCount.get() >= maxRetries) {
                handleBillingError(BillingClient.BillingResponseCode.ERROR)
            }
        }).start()
    }

    private fun launchPurchaseFlow(skuDetails: SkuDetails) {
        val skuList = ArrayList<String>()
        skuList.add(skuDetails.sku)
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        this.activity?.let { billingClient!!.launchBillingFlow(it, flowParams) }
    }

    private fun getSingleInAppDetail() {
        val skuList = ArrayList<String>()
        skuList.add(ConsumeProductID)
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()
        billingClient!!.querySkuDetailsAsync(
            params
        ) { billingResult: BillingResult, skuDetailsList: List<SkuDetails>? ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                launchPurchaseFlow(skuDetailsList[0])
            } else {
                Log.e(TAG, "Failed to query SKU details")
            }
        }
    }
    private fun handlePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            billingClient!!.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            ) { billingResult: BillingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Purchase acknowledged")
                    consumePurchase(purchase)
                } else {
                    handleBillingError(billingResult.responseCode)
                }
            }
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient!!.consumeAsync(
            params
        ) { billingResult: BillingResult, purchaseToken: String? ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase consumed")
                binding.lockedScreen.visibility = View.GONE
                viewModel?.updatePurchase()
            } else {
                handleBillingError(billingResult.responseCode)
            }
        }
    }

    private fun handleBillingError(responseCode: Int) {
        val errorMessage: String = when (responseCode) {
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> "Billing service is currently unavailable."
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> "An error occurred while processing the request."
            else -> "An unknown error occurred."
        }
        Log.e(TAG, "Billing Error: $errorMessage")
    }


    private fun updateTrialCount() {
        viewModel?.updateTrialCount()
    }

    private fun getPdfUrl(){
        viewModel?.getPdfUrl(arguments?.getString(QUERY_PATH,"") ?: "")
    }

    private fun openPdf() {
        binding.pdfView.setData(PdfViewerData(url = arguments?.getString(URL_KEY,"")))
        updateTrialCount()
    }

    private fun initView() {
        if ((QuestionBankObject.userDetails?.trialCount
                ?: 0) > 3 && QuestionBankObject.userDetails?.isSubscribed == false
        ) {
            binding.lockedScreen.visibility = View.VISIBLE
        } else {
            binding.lockedScreen.visibility = View.GONE

        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun setObservers() {

        viewModel?.pdfUrl?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            binding.pdfView.setData(PdfViewerData(url = it))
        }

        lifecycleScope.launch {
            QuestionBankObject.actionPerformerSharedFlow.collect { envelope ->
                when (envelope.action) {
                    ActionType.CLOSE_PROGRESS_BAR->{
                        binding.progressBar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }
}