# Wi-Fi Easy Connect
[Wi-Fi Easy Connect](https://www.wi-fi.org/discover-wi-fi/wi-fi-easy-connect) was introduced by [the Wi-Fi Alliance (WFA)](https://www.wi-fi.org/) to reduce complexity and enhance the user experience of connecting devices to Wi-Fi networks, while simultaneously incorporating the highest security standards.

With Wi-Fi Easy Connect in place, consumers will be able to connect Wi-Fi devices, including those without a screen or an intuitive user interface, to their network by simply scanning a QR code with a smartphone or tablet. Wi-Fi Easy Connect incorporates strong encryption through public key cryptography to ensure networks remain secure as new devices are added.

# EasyConnect Reference Kit
Comcast has created a Reference Kit that can be used to demonstrate, understand and help implement Wi-Fi Easy Connect. This reference kit covers Easy Connect configurator discovery and bootstrap delegation to enable Wi-Fi onboarding of a new device using Wi-Fi Easy Connect, by simply scanning a QR code.

Setup requires 2 Raspberry Pis and a mobile app. One of the Pis will be configured as a Wi-Fi client (the enrollee) and the other will be configured as an AP (the configurator). A mobile app will be used to scan the QR code from the enrollee and send the bootstrap information to the configurator.

Reference kit includes an [iOS app](/mobilesdk/ios), an [android app](/mobilesdk/android) and [patches for Raspberry Pi](/raspberrypi) to make Easy Connect work natively in Raspberry Pi.

## Java Version

This project uses **Java 8** (JDK 1.8) for the Android application modules. Both the `app` and `easyconnect` library modules are configured with `sourceCompatibility` and `targetCompatibility` set to Java 8.

### Java 8 Features Used

The codebase leverages the following Java 8 language features and APIs:

- **Lambda expressions**: Used throughout the codebase to replace anonymous inner classes for functional interfaces such as `View.OnClickListener`, `DialogInterface.OnClickListener`, `PopupWindow.OnDismissListener`, and OkHttp `Interceptor`.
- **Method references**: Used where applicable for concise callback registration (e.g., `this::handleResult`).
- **Try-with-resources**: Applied for automatic resource management when reading files (e.g., `InputStream`, `BufferedReader` in `FileUtils.java`).
- **Diamond operator**: Used for type inference in generic instantiation (e.g., `new ArrayList<>()`).
- **Simplified lambda bodies**: Single-expression lambdas are used without braces or explicit `return` statements where possible (e.g., in `DPPRepositoryImpl.java`).

## Setup and Running the Application

### Prerequisites

- **Java 8 (JDK 1.8)** or higher
- **Android Studio** 3.5 or later (with Android SDK 28+)
- **Gradle** (bundled with Android Studio or use the included Gradle wrapper)

### Building the Android App

1. Clone this repository:
   ```bash
   git clone https://github.com/COG-GTM/Comcast-EasyConnect.git
   cd Comcast-EasyConnect/mobilesdk/android
   ```

2. Open the project in Android Studio or build from the command line:
   ```bash
   ./gradlew assembleDebug
   ```

3. Run the unit tests:
   ```bash
   ./gradlew test
   ```

4. Install on a connected device:
   ```bash
   ./gradlew installDebug
   ```

### Hardware Setup

Refer to the [Raspberry Pi README](/raspberrypi/README.md) for instructions on setting up the Raspberry Pi devices as enrollee and configurator.

### API Documentation

Refer to the [Mobile SDK README](/mobilesdk/README.md) for details on the configurator discovery and REST API specifications.
