plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.desailly.backdropsbliss"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.desailly.backdropsbliss"
        minSdk = 24
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
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.airbnb.android:lottie:4.2.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation("com.google.firebase:firebase-database:20.2.2")//base de datos firebase
    implementation("com.google.firebase:firebase-auth:22.1.1")//autenticacion
    implementation ("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.firebaseui:firebase-ui-database:8.0.0")
    implementation ("de.hdodenhof:circleimageview:3.0.1")
    implementation ("com.github.clans:fab:1.6.4")

    implementation("com.squareup.picasso:picasso:2.71828")//gestion imagen
    implementation("androidx.recyclerview:recyclerview:1.3.1")//recyclerview
    implementation("androidx.cardview:cardview:1.0.0")//cardview


}