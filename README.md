# Google Pay Integration Example

This repository contains an example implementation of integrating Google Pay into an Xamarin application using the DNA Payments SDK. This example demonstrates how to:

- Initialize the payment context using `DNAPaymentsGooglePaySdk.init()`.
- Verify if Google Pay is available using `DNAPaymentsGooglePaySdk.isPaymentPossible()`.
- Execute a payment transaction using `DNAPaymentsGooglePaySdk.executeTransaction()`.

## Features

- **Environment Support**: Supports both TEST and PRODUCTION environments.
- **UI**: Create your own Google Pay button to initiate transaction. Please make sure the Google Pay button adheres to the Google Pay [brand guidelines](https://developers.google.com/pay/api/android/guides/brand-guidelines).
- **Customizable Payment Request**: Allows configuration of payment details, customer information, and order details.
- **Error Handling**: Includes basic error handling for common exceptions.

## DNA Payments SDK

- DNA Payments SDK `dnasdk-release-xamarin-2.0.0-10.aar` file, please find under the `libs` folder in the demo project.

## Support

For any issues or questions, please contact [support@dnapayments.com](mailto:support@dnapayments.com).

