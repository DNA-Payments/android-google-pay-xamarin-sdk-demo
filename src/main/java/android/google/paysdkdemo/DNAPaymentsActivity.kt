package android.google.paysdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dnapayments.google.paysdk.data.model.AuthTokenRequest
import com.dnapayments.google.paysdk.data.model.PaymentResult
import com.dnapayments.google.paysdk.data.model.request.AccountDetails
import com.dnapayments.google.paysdk.data.model.request.AddressInfo
import com.dnapayments.google.paysdk.data.model.request.CustomerDetails
import com.dnapayments.google.paysdk.data.model.request.DeliveryDetails
import com.dnapayments.google.paysdk.data.model.request.OrderLine
import com.dnapayments.google.paysdk.data.model.request.PaymentRequest
import com.dnapayments.google.paysdk.data.model.request.PaymentSettings
import com.dnapayments.google.paysdk.domain.DNAPaymentsErrorCode
import com.dnapayments.google.paysdk.domain.Environment
import com.dnapayments.google.paysdk.DNAPaymentsGooglePaySdk
import com.dnapayments.google.paysdk.StatusCallback
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton


/**
 * DNAPaymentsActivity
 */
class DNAPaymentsActivity : AppCompatActivity() {

    /**
     * Allow to retrieve to payment status
     *
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Manage Google Pay result
        if (requestCode == DNAPaymentsGooglePaySdk.GOOGLE_PAYMENT_CODE_RESULT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    if (data != null) {
                        // Execute payment
                        DNAPaymentsGooglePaySdk.executeTransaction(
                            data,
                            object : StatusCallback {
                                override fun onResponse(paymentResult: PaymentResult) {
                                    handlePaymentResult(paymentResult)
                                }
                            })
                    } else {
                        handlePaymentResult(
                            PaymentResult(
                                false,
                                DNAPaymentsErrorCode.UNKNOWN_ERROR
                            )
                        )
                    }
                }

                Activity.RESULT_CANCELED -> {
                    handlePaymentResult(
                        PaymentResult(
                            false,
                            DNAPaymentsErrorCode.PAYMENT_CANCELLED_ERROR
                        )
                    )
                }

                AutoResolveHelper.RESULT_ERROR -> {
                    val status = AutoResolveHelper.getStatusFromIntent(data)
                    handlePaymentResult(
                        PaymentResult(
                            false,
                            DNAPaymentsErrorCode.UNKNOWN_ERROR,
                            status?.statusMessage
                        )
                    )
                }
            }
        }
    }

    private lateinit var payBtn: PayButton
    private lateinit var progressBar: ProgressBar
    private lateinit var paymentsClient: PaymentsClient

    /**
     * onCreate method
     * Activity creation
     *
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        payBtn = findViewById(R.id.payBtn)
        progressBar = findViewById(R.id.progressBar)

        paymentsClient = DNAPaymentsGooglePaySdk.init(this, Environment.TEST)

        DNAPaymentsGooglePaySdk.isPaymentPossible(paymentsClient)
            .addOnCompleteListener { task ->
                try {
                    val result = task.getResult(ApiException::class.java)
                    payBtn.isVisible = result
                    if (result) {
                        // show Google Pay as a payment option
                        val paymentMethods =
                            DNAPaymentsGooglePaySdk.getCardPaymentMethods()

                        payBtn.initialize(
                            ButtonOptions.newBuilder()
                                .setAllowedPaymentMethods(paymentMethods)
                                .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                                .setButtonType(ButtonConstants.ButtonType.BUY)
                                .setCornerRadius(100)
                                .build()
                        )
                    } else {
                        Toast.makeText(this, "isPaymentPossible return false", Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: ApiException) {
                    payBtn.isVisible = false
                    Toast.makeText(this, "isPaymentPossible exception catched", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    /**
     * onPayClick method
     * Payment execution
     *
     * @param view View Pay button
     */
    fun onPayClick(view: View) {
        val authTokenResult = AuthTokenRequest(
            grantType = "client_credentials",
            scope = "payment integration_hosted integration_embedded integration_seamless",
            clientId = "Test Merchant",
            clientSecret = "PoF84JqIG8Smv5VpES9bcU31kmfSqLk8Jdo7",
            invoiceId = "1683194969490",
            terminal = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
            amount = 24.4,
            currency = "GBP",
            paymentFormURL = "https://test-pay.dnapayments.com/checkout/"
        )
        val paymentResult = PaymentRequest(
            currency = "GBP",
            paymentMethod = "googlepay",
            description = "Car Service",
            paymentSettings = PaymentSettings(
                terminalId = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
                returnUrl = "https://example.com/return",
                failureReturnUrl = "https://example.com/failure",
                callbackUrl = "https://example.com/callback",
                failureCallbackUrl = "https://example.com/failure-callback"
            ),
            customerDetails = CustomerDetails(
                accountDetails = AccountDetails(
                    accountId = "uuid000001"
                ),
                billingAddress = AddressInfo(
                    firstName = "John",
                    lastName = "Doe",
                    addressLine1 = "123 Main Street",
                    postalCode = "12345",
                    city = "Anytown",
                    country = "GB"
                ),
                deliveryDetails = DeliveryDetails(
                    deliveryAddress = AddressInfo(
                        firstName = "Jane",
                        lastName = "Doe",
                        addressLine1 = "456 Elm Street",
                        postalCode = "54321",
                        city = "Anytown",
                        country = "GB"
                    )
                ),
                email = "aaa@dnapayments.com",
                mobilePhone = "+441234567890"
            ),
            orderLines = listOf(
                OrderLine(
                    name = "Running shoe",
                    quantity = 1,
                    unitPrice = 24,
                    taxRate = 20,
                    totalAmount = 24,
                    totalTaxAmount = 4
                )
            ),
            deliveryType = "service",
            invoiceId = "1683194969490",
            amount = 24.4
        )
        progressBar.visibility = View.VISIBLE
        DNAPaymentsGooglePaySdk.execute(
            activity = this,
            environment = Environment.TEST,
            paymentsClient = paymentsClient,
            paymentRequest = paymentResult,
            authTokenRequest = authTokenResult,
            statusCallback = object : StatusCallback {
                override fun onResponse(paymentResult: PaymentResult) {
                    handlePaymentResult(paymentResult)
                }
            }
        )
    }

    /**
     * Handle payment result
     *
     * @param result PaymentResult
     */
    private fun handlePaymentResult(result: PaymentResult) {
        progressBar.visibility = View.GONE

        if (result.success) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(
            this,
            "Payment failed. errorCode = " + result.errorCode?.name + " and description = " + result.errorDescription,
            Toast.LENGTH_LONG
        ).show()
    }
}