# Google Pay Integration Example

This repository contains an example implementation of integrating Google Pay using the DNA Payments SDK. The app is written in kotlin for demonstration purposes, feel free to adapt it for use in your Xamarin project. This example showcases how to:

- Initialize the payment context using `DNAPaymentsGooglePaySdk.init()`.
- Verify if Google Pay is available using `DNAPaymentsGooglePaySdk.isPaymentPossible()`.
- Execute a payment transaction using `DNAPaymentsGooglePaySdk.executeTransaction()`.

Please use this example 
## Features

- **Environment Support**: Supports both TEST and PRODUCTION environments.
- **UI**: Create your own Google Pay button to initiate transaction. Please make sure the Google Pay button adheres to the Google Pay [brand guidelines](https://developers.google.com/pay/api/android/guides/brand-guidelines).
- **Customizable Payment Request**: Allows configuration of payment details, customer information, and order details.
- **Error Handling**: Includes basic error handling for common exceptions.

## DNA Payments SDK (this version of SDK is modified to be used in Xamarin project)

- DNA Payments SDK `dnasdk-release-xamarin-2.0.0-10.aar` file, please find under the `libs` folder in the demo project.

## Support

For any issues or questions, please contact [support@dnapayments.com](mailto:support@dnapayments.com).

