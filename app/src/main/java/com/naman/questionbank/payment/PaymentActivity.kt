package com.naman.questionbank.payment

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naman.questionbank.R
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(),PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listener()
    }

    private fun startPayment() {
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Question bank")
            options.put("description", "Previous Question Papers Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            val payment  = "20"
            var total = payment.toDouble()
            total *= 100
            options.put("amount", total)

            val preFill = JSONObject()
            preFill.put("email", " ")
            preFill.put("contact", " ")

            options.put("prefill", preFill)
            co.open(activity, options)
        }catch (e: Exception){
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Congratulations! Payment Successfully", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Oops..Something Went Wrong.", Toast.LENGTH_LONG).show()
    }

    private fun listener(){
        startPayment()
    }

}