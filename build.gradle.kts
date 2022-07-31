plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.5.2"
//    kotlin("plugin.serialization") version "1.7.10"
}

group = "com.yuanzhy.dogcoder"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
//    mavenCentral()
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
}

dependencies {
    implementation("com.alibaba:fastjson:1.2.83")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
//    version.set("IU-221.5080.210")
    version.set("IU-221.5921.22")
//    type.set("IU") // Target IDE Platform

    plugins.set(listOf(
            "JavaScriptLanguage",
            "java",
            "yaml",
            "org.jetbrains.kotlin:221-1.7.10-release-333-IJ5591.52",
            "Pythonid:221.5080.212",
            "org.jetbrains.plugins.go:221.5080.210",
//            "org.jetbrains.plugins.vue:221.5080.169"
    ))
    updateSinceUntilBuild.set(false)
//    buildSearchableOptions.enabled(false)
//    updateSinceUntilBuild = false
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("191")
//        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIde {
        val jvmConfig = project.rootProject.file("jvm.ini")
        if (jvmConfig.exists()) {
            jvmArgs = jvmConfig.readLines()
        }
    }
}
configurations.all {
    resolutionStrategy.cacheChangingModulesFor(24000, "hours")
    resolutionStrategy.cacheDynamicVersionsFor(24000, "hours")
}