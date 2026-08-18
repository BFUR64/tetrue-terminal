group = "io.github.bfur64"
version = providers.gradleProperty("version").get()

val lanternaVersion: String = providers.gradleProperty("lanternaVersion").get()
val jlineVersion: String = providers.gradleProperty("jlineVersion").get()

plugins {
    `java-library`
    signing
    id("com.github.gmazzo.buildconfig") version "6.0.10"
    id("com.github.spotbugs") version "6.5.8"
    id("com.vanniktech.maven.publish") version "0.37.0"
}

buildConfig {
    className("Versions")
    packageName(group.toString())
    useJavaOutput()
    buildConfigField("String", "TETRUE_TERMINAL", "\"${project.version}\"")
    buildConfigField("String", "LANTERNA", "\"${lanternaVersion}\"")
    buildConfigField("String", "JLINE", "\"${jlineVersion}\"")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(libs.junit)
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("com.googlecode.lanterna:lanterna:${lanternaVersion}")
    implementation("org.jline:jline:$jlineVersion")
    implementation("org.apache.logging.log4j:log4j-api:2.26.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

mavenPublishing {
    coordinates(
        group.toString(),
        "tetrue-terminal",
        project.version.toString()
    )

    pom {
        name = "tetrue-terminal"
        description = "A meta library that combines JLine and Lanterna for my specific purposes"
        url = "https://github.com/BFUR64/tetrue-terminal"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
            }
        }

        developers {
            developer {
                id = "BFUR64"
                name = "Terrance"
                url = "https://github.com/BFUR64/"
            }
        }

        scm {
            url = "https://github.com/BFUR64/tetrue-terminal"
            connection = "scm:git:https://github.com/BFUR64/tetrue-terminal.git"
            developerConnection = "scm:git:ssh://git@github.com/BFUR64/tetrue-terminal.git"
        }
    }

    publishToMavenCentral()
    signAllPublications()
}

signing {
    useInMemoryPgpKeys(
        providers.fileContents(
            layout.projectDirectory.file("signing-key.asc")
        ).asText.get(),
        providers.gradleProperty("signingInMemoryKeyPassword").get()
    )
}
