# LoadApp
LoadApp is an app that demonstrates downloading a file from Internet and displaying a progress indicator as the file is being downloaded. When the user touches the custom-built button it animates a loading bar across the width of the button in addition to a circular progress animation. The text of the button also changes dynamically as the state of the download changes.

This app also demonstrates posting notifications. A notification is posted when the selected file is downloaded. If the user selects a notification they are presented with a detail fragment and the notification is dismissed automatically. In the detail fragment the status of the download is displayed.

[The final look of the app](https://gph.is/g/Zywmnre)

### Dependencies

```
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.core:core-ktx:1.3.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation "android.arch.navigation:navigation-fragment-ktx:1.0.0"
    implementation "android.arch.navigation:navigation-ui-ktx:1.0.0"
    implementation 'androidx.test.ext:junit-ktx:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
    implementation 'androidx.core:core-ktx:1.7.0'
```

### Installation

To get the project running on your local machine, you need to follow these steps:

```
1.Clone the repo
2. Open Android Studio Application
3. Choose "Open an existing Android Studio Project"
4. In the opened finder find `nd940-c3-advanced-android-programming-project-starter` folder
5. Click on the folder and select `starter` folder and click on "Open" button
6. Once the project is opened in Android studio, go to File -> Sync Project with gradle files
7. Click on "Run" button in Android Studio to install the project on the phone or emulator
```

## Built With

* [Android Studio](https://developer.android.com/studio) - Default IDE used to build android apps
* [Kotlin](https://kotlinlang.org/) - Default language used to build this project

## License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)
