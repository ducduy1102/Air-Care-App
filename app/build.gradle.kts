plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.aircareapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.aircareapp"
        minSdk = 33
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // load img from url
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // circle imageview
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // integration google sign in
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    // SSLHandle
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Retrofix
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Volley
    implementation ("com.android.volley:volley:1.2.1")

    // GoogleMap
    implementation ("com.google.android.gms:play-services-maps:17.0.1")

    // Chart Pilot
    implementation ("com.androidplot:androidplot-core:1.5.10")

    // AppIntro
    implementation("com.github.AppIntro:AppIntro:6.3.1")

    implementation ("androidx.cardview:cardview:1.0.0")

}