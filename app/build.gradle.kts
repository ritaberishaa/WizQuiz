plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.wizquiz"
    compileSdk = 35



    defaultConfig {
        applicationId = "com.example.wizquiz"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    android {
        packaging {
            resources {
                excludes.add("META-INF/LICENSE.md")
                excludes.add("META-INF/LICENSE.txt")
                excludes.add("META-INF/NOTICE.md")
                excludes.add("META-INF/NOTICE.txt")
            }
        }
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("org.mindrot:jbcrypt:0.4")
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")

}