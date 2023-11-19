import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cn.gionrose.facered"
version = "1.0-SNAPSHOT"
val vertxVersion = "4.4.6"
val mainClassName = "cn.gionrose.facered.Gionrose"

repositories {
    mavenLocal ()
    mavenCentral()
}

dependencies {
    //web开发
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web")
    testImplementation("io.vertx:vertx-junit5")

}

tasks.withType<ShadowJar> {
    archiveClassifier.set("1-full")
   dependencies {
       include(dependency("io.vertx:vertx-web"))
            include(dependency("io.vertx:vertx-web-client"))
            include(dependency("io.vertx:vertx-junit5"))
        }
        manifest {
            attributes(mapOf("Main-Class" to mainClassName))
        }
        mergeServiceFiles()
}





tasks.test {
    useJUnitPlatform()
}


application {
    mainClass.set(mainClassName)
}