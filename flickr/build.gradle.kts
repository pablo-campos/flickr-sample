
plugins {
	id("com.android.application")
	id("io.fabric")
	id("com.google.android.gms.oss-licenses-plugin")        // Open Source Notices
	id("com.google.gms.google-services")
	kotlin("android")
	kotlin("android.extensions")
}

android {

	compileSdkVersion (28)

	defaultConfig {
		applicationId = "com.pablocampos.flickrsample"
		minSdkVersion (21)
		targetSdkVersion (28)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	compileOptions {
		setSourceCompatibility(JavaVersion.VERSION_1_8)
		setTargetCompatibility(JavaVersion.VERSION_1_8)
	}

	buildTypes {

		getByName("release") {
			isDebuggable = false
			isMinifyEnabled = true
			isZipAlignEnabled = true
			isShrinkResources = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
			// Can't be enabled b/c we use Resources#getIdentifier to look-up resources dynamically by name which confuses this tool
		}

		getByName("debug") {
			isDebuggable = true
			isJniDebuggable = true
			isMinifyEnabled = false
		}
	}

	sourceSets {
		val main by getting
		main.java.srcDirs("src/main/kotlin")
	}
}

dependencies {
	implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
	implementation(Libs.appcompat)
	implementation(Libs.recyclerview)
	implementation(Libs.cardview)
	implementation(Libs.material)
	implementation(Libs.palette)
	implementation(Libs.constraintlayout)
	implementation(Libs.legacy_support_v4)
	testImplementation(Libs.junit)
	androidTestImplementation(Libs.androidx_test_runner)
	androidTestImplementation(Libs.espresso_core)

	// Apache Commons Utils
	implementation(Libs.commons_text)

	// Glide
	implementation(Libs.glide)
	annotationProcessor(Libs.com_github_bumptech_glide_compiler)

	// Retrofit / GSON
	implementation(Libs.retrofit)
	implementation(Libs.converter_gson)

	// ViewModel and LiveData
	implementation (Libs.lifecycle_extensions)

	// Open Source Notices
	implementation(Libs.play_services_oss_licenses)
	androidTestImplementation(Libs.androidx_test_rules)

	// ML Kit Android libraries
	implementation(Libs.firebase_core)
	implementation(Libs.firebase_ml_vision)
	implementation(Libs.firebase_ml_vision_image_label_model)
	implementation(Libs.firebase_analytics)
	implementation(Libs.firebase_messaging)

	// Crashlytics
	implementation(Libs.crashlytics)

	// LeakCanary. Imported as debugImplementation because LeakCanary should only run in debug builds.
	debugImplementation(Libs.leakcanary_android)

	// Chuck
	debugImplementation (Libs.library)
	releaseImplementation(Libs.library_no_op)

}