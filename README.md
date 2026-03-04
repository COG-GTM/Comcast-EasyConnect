# Wi-Fi Easy Connect
[Wi-Fi Easy Connect](https://www.wi-fi.org/discover-wi-fi/wi-fi-easy-connect) was introduced by [the Wi-Fi Alliance (WFA)](https://www.wi-fi.org/) to reduce complexity and enhance the user experience of connecting devices to Wi-Fi networks, while simultaneously incorporating the highest security standards.
 
With Wi-Fi Easy Connect in place, consumers will be able to connect Wi-Fi devices, including those without a screen or an intuitive user interface, to their network by simply scanning a QR code with a smartphone or tablet. Wi-Fi Easy Connect incorporates strong encryption through public key cryptography to ensure networks remain secure as new devices are added.
 
# EasyConnect Reference Kit
Comcast has created a Reference Kit that can be used to demonstrate, understand and help implement Wi-Fi Easy Connect. This reference kit covers Easy Connect configurator discovery and bootstrap delegation to enable Wi-Fi onboarding of a new device using Wi-Fi Easy Connect, by simply scanning a QR code.
 
Setup requires 2 Raspberry Pis and a mobile app. One of the Pis will be configured as a Wi-Fi client (the enrollee) and the other will be configured as an AP (the configurator). A mobile app will be used to scan the QR code from the enrollee and send the bootstrap information to the configurator.
 
Reference kit includes an [iOS app](/mobilesdk/ios), an [android app](/mobilesdk/android) and [patches for Raspberry Pi](/raspberrypi) to make Easy Connect work natively in Raspberry Pi.

## Prerequisites and Setup

### Requirements
- **Java 8 (JDK 1.8)** or higher
- Android Studio 3.5+ (recommended)
- Android SDK with API level 29 (compileSdkVersion 29)
- Gradle (included via the Gradle wrapper)

### How to Build and Run the Android App
1. Clone this repository:
   ```bash
   git clone https://github.com/COG-GTM/Comcast-EasyConnect.git
   ```
2. Open the `mobilesdk/android` directory in Android Studio.
3. Ensure JDK 8 is configured in Android Studio under **File > Project Structure > SDK Location**.
4. Sync Gradle and let dependencies download.
5. Build the project:
   ```bash
   cd mobilesdk/android
   ./gradlew assembleDebug
   ```
6. Run the app on an Android device or emulator (minimum SDK 23 / Android 6.0).

### Java 8 Features Used
The Android codebase leverages the following Java 8 language features (supported via desugaring / `sourceCompatibility 1.8`):
- **Lambda expressions** for concise callback implementations (e.g., click listeners, interceptors, dialog handlers)
- **Method references** for cleaner function passing
- **Try-with-resources** for automatic resource management in I/O operations
- **`@FunctionalInterface`** annotations on single-method interfaces to enable lambda usage
- **Diamond operator** for generic type inference
