package com.naman.questionbank

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import java.util.concurrent.atomic.AtomicInteger


class BillingActivity : AppCompatActivity() {
    private var billingClient: BillingClient? = null
    private var tvStatus: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

    }

    override fun onResume() {
        super.onResume()
        setUpListeners()
    }

    private fun setUpListeners() {
        val button = findViewById<Button>(R.id.unlock)
        button.setOnClickListener {
            initializeBillingClient()
            establishConnection()
        }
    }


    private fun initializeBillingClient() {
        billingClient = BillingClient.newBuilder(this)
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
        billingClient!!.launchBillingFlow(this, flowParams)
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
                tvStatus!!.text = "Product Consumed"
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

    companion object {
        private const val TAG = "com.naman.questionbank.BillingActivity"
        private const val ConsumeProductID = "question_paper"
    }
}