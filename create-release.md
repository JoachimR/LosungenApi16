1. change app version in app_version.gradle
2. add apk_signature.properties in root folder
3. `./gradlew assembleRelease && cp app/build/outputs/apk/release/app-release.apk /Users/joachimreiss/Desktop/. && adb install -r /Users/joachimreiss/Desktop/app-release.apk && adb shell am start -n de.reiss.android.losungen/de.reiss.android.losungen.SplashScreenActivity`