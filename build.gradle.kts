plugins {
    id("com.android.application") version "8.0.0" apply false // Use sua versão do Android Gradle Plugin
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false // Adicione esta linha
    id("com.google.dagger.hilt.android") version "2.48" apply false // Exemplo, use sua versão do Hilt
    // ...
}