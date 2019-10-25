/**
 * Find which updates are available by running
 *     `$ ./gradlew syncLibs`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
	const val appcompat: String = "1.1.0-rc01" // exceed the version found: 1.0.2

	const val cardview: String = "1.0.0"

	const val constraintlayout: String = "2.0.0-beta2" // exceed the version found: 1.1.3

	const val legacy_support_v4: String = "1.0.0"

	const val lifecycle_extensions: String = "2.0.0"

	const val palette: String = "1.0.0"

	const val recyclerview: String = "1.1.0-beta01" // exceed the version found: 1.0.0

	const val espresso_core: String = "3.3.0-alpha01" // exceed the version found: 3.2.0

	const val androidx_test: String = "1.3.0-alpha01" // exceed the version found: 1.2.0

	const val aapt2: String = "3.4.2-5326820"

	const val com_android_tools_build_gradle: String = "3.4.2"

	const val lint_gradle: String = "26.4.2"

	const val crashlytics: String = "2.10.1"

	const val com_github_bumptech_glide: String = "4.9.0"

	const val oss_licenses_plugin: String = "0.9.5"

	const val play_services_oss_licenses: String = "17.0.0"

	const val material: String = "1.1.0-alpha08" // exceed the version found: 1.0.0

	const val firebase_core: String = "17.0.1"

	const val firebase_ml_vision_image_label_model: String = "18.0.0"

	const val firebase_ml_vision: String = "22.0.0"

	const val firebase_analytics: String = "17.2.0"

	const val firebase_messaging: String = "20.0.0"

	const val google_services: String = "4.3.0"

	const val com_readystatesoftware_chuck: String = "1.1.0"

	const val leakcanary_android: String = "2.0-alpha-2" // exceed the version found: 1.6.3

	const val com_squareup_retrofit2: String = "2.6.0"

	const val io_fabric_tools_gradle: String = "1.30.0"

	const val jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin: String = "0.2.6"

	const val junit: String = "4.12"

	const val commons_text: String = "1.7"

	const val org_gradle_kotlin_kotlin_dsl_gradle_plugin: String = "1.2.8" //available: "1.2.9"

	const val kotlin_android_extensions_runtime: String = "1.3.41"

	const val kotlin_android_extensions: String = "1.3.41"

	const val kotlin_gradle_plugin: String = "1.3.41"

	const val kotlin_reflect: String = "1.3.31" //available: "1.3.41"

	const val kotlin_sam_with_receiver: String = "1.3.41"

	const val kotlin_scripting_compiler_embeddable: String = "1.3.41"

	const val kotlin_stdlib_jdk8: String = "1.3.31" //available: "1.3.41"

	/**
	 *
	 *   To update Gradle, edit the wrapper file at path:
	 *      ./gradle/wrapper/gradle-wrapper.properties
	 */
	object Gradle {
		const val runningVersion: String = "5.5.1"

		const val currentVersion: String = "5.5.1"

		const val nightlyVersion: String = "5.6-20190714220053+0000"

		const val releaseCandidate: String = ""
	}
}
