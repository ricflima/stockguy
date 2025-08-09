plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // Mantenha antes do KSP
    id("com.google.devtools.ksp")      // Adicione KSP aqui
    // id("kotlin-kapt") // REMOVA esta linha
    id("com.google.dagger.hilt.android") // Se estiver usando Hilt
    // ... outros plugins
}

android {
    namespace = "com.example.stockguy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.stockguy"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    useLibrary("wear-sdk")
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.tiles)
    implementation(libs.tiles.material)
    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)
    implementation(libs.horologist.composables)
    implementation(libs.horologist.compose.layout)

    // Hilt (versão 2.48 para ficar consistente)
    implementation("com.google.dagger:hilt-android:2.48") // Ou a versão compatível que você escolheu
    ksp("com.google.dagger:hilt-compiler:2.48")

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // Room
    val roomVersion = "2.6.1" // Use a versão mais recente compatível com KSP
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Recomendado para Kotlin
    ksp("androidx.room:room-compiler:$roomVersion")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0") // Ou versão mais recente
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // DataStore
    implementation(libs.datastore)

    // Work Manager
    implementation(libs.work.runtime)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Keep annotation
    implementation("androidx.annotation:annotation:1.7.1")

    // Debug tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Testing
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
}
