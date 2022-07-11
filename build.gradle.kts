plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.5.2"
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
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("IU-221.5080.210")
    type.set("IU") // Target IDE Platform

    plugins.set(listOf(
            "JavaScriptLanguage",
            "JavaScriptDebugger",
            "java",
            "org.jetbrains.plugins.vue:221.5080.169"
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
        sinceBuild.set("212")
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
        jvmArgs = listOf(
                "-javaagent:E:\\yuanzhy\\ideatrack\\ja-netfilter-all-2022.1\\ja-netfilter-all\\ja-netfilter.jar=jetbrains"
        )
    }
}
