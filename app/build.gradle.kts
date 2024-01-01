plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("kotlin-android-extensions")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.arincdogansener.finalrecipeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arincdogansener.finalrecipeapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.google.android.material:material:1.11.0")

    //circle image view
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //scalable unit text size
    implementation ("com.intuit.ssp:ssp-android:1.0.6")

    //scalable unit size
    implementation ("com.intuit.sdp:sdp-android:1.0.6")

    //room database
//    implementation ("androidx.room:room-runtime:2.6.1")
//    kapt ("androidx.room:room-compiler:2.2.5")
    implementation ("androidx.room:room-ktx:2.6.1")
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
//    //crop image library
//
//    implementation ("com.theartofdev.edmodo:android-image-cropper:2.8.0")

    //easy permission
//    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("pub.devrel:easypermissions:3.0.0")

    //coroutines core
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    implementation ("androidx.work:work-runtime:2.7.0")
}