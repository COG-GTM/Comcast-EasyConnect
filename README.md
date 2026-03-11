# Wi-Fi Easy Connect
[Wi-Fi Easy Connect](https://www.wi-fi.org/discover-wi-fi/wi-fi-easy-connect) was introduced by [the Wi-Fi Alliance (WFA)](https://www.wi-fi.org/) to reduce complexity and enhance the user experience of connecting devices to Wi-Fi networks, while simultaneously incorporating the highest security standards.
 
With Wi-Fi Easy Connect in place, consumers will be able to connect Wi-Fi devices, including those without a screen or an intuitive user interface, to their network by simply scanning a QR code with a smartphone or tablet. Wi-Fi Easy Connect incorporates strong encryption through public key cryptography to ensure networks remain secure as new devices are added.
 
# EasyConnect Reference Kit
Comcast has created a Reference Kit that can be used to demonstrate, understand and help implement Wi-Fi Easy Connect. This reference kit covers Easy Connect configurator discovery and bootstrap delegation to enable Wi-Fi onboarding of a new device using Wi-Fi Easy Connect, by simply scanning a QR code.
 
Setup requires 2 Raspberry Pis and a mobile app. One of the Pis will be configured as a Wi-Fi client (the enrollee) and the other will be configured as an AP (the configurator). A mobile app will be used to scan the QR code from the enrollee and send the bootstrap information to the configurator.
 
Reference kit includes an [iOS app](/mobilesdk/ios), an [android app](/mobilesdk/android) and [patches for Raspberry Pi](/raspberrypi) to make Easy Connect work natively in Raspberry Pi.

# Android App - Setup and Build Instructions

## Prerequisites
- **Java 8 (JDK 1.8)** or higher
- Android Studio 3.5+ (recommended)
- Android SDK with compileSdkVersion 28+ and buildToolsVersion 29.0.0+

## Java 8 Language Features
The Android app uses Java 8 language features enabled via `compileOptions` in the Gradle build files:

```groovy
compileOptions {
    sourceCompatibility = '1.8'
    targetCompatibility = '1.8'
}
```

The codebase leverages the following Java 8 features:
- **Lambda expressions** for concise callback and listener implementations
- **Method references** (e.g., `dppService::getDPPUrii`) for cleaner delegation
- **Try-with-resources** for automatic resource management of I/O streams
- **Type inference** for `findViewById` calls (no explicit casting needed)
- **Diamond operator** (`<>`) for reduced generic type verbosity

## How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/COG-GTM/Comcast-EasyConnect.git
   ```

2. Open the `mobilesdk/android` directory in Android Studio.

3. Let Android Studio sync the Gradle project and download dependencies.

4. Build the project:
   ```bash
   cd mobilesdk/android
   ./gradlew assembleDebug
   ```

5. Run the tests:
   ```bash
   ./gradlew test
   ```

6. Install on a connected device:
   ```bash
   ./gradlew installDebug
   ```

## Project Structure
- `app/` - Main application module containing the UI (MainActivity, QRCodeActivity)
- `easyconnect/` - Library module with networking, mDNS discovery, QR code scanning, and utility classes
