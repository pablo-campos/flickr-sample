// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
	`kotlin-dsl`
	id("jmfayard.github.io.gradle-kotlin-dsl-libs") version "0.2.6"
}

buildscript {

	repositories {

		google()
		maven {
			url = uri("https://maven.fabric.io/public")
		}
		jcenter()
		mavenCentral()
	}

	dependencies {

		classpath (Libs.com_android_tools_build_gradle)
		classpath (Libs.google_services)
		classpath (Libs.io_fabric_tools_gradle)
		classpath (Libs.kotlin_gradle_plugin)

		// Open Source Notices
		classpath (Libs.oss_licenses_plugin)
	}
}

allprojects {
	repositories {
		google()
		jcenter()
	}
}