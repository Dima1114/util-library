import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.jpa") version "1.3.41"
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.40"
    kotlin("plugin.spring") version "1.3.41"
    kotlin("kapt") version "1.3.50"
    `maven-publish`
    maven
}

group = "venus"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-rest:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.1.6.RELEASE")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.querydsl:querydsl-jpa:4.2.1")
    implementation("org.reflections:reflections:0.9.11")
    implementation("io.github.microutils:kotlin-logging:1.6.10")
    
    //query dsl
    kapt ("com.querydsl:querydsl-apt:4.2.1:jpa")
    implementation ("com.querydsl:querydsl-jpa:4.2.1")
    //query dsl tests
    kaptTest ("com.querydsl:querydsl-apt:4.2.1:jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.6.RELEASE")
    testImplementation("com.h2database:h2:1.4.199")
    testImplementation("org.amshove.kluent:kluent:1.49")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.41")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.41")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "venus"
//            artifactId = "util-library"
//            version = "1.0"
//
//            from(components["java"])
//        }
//    }
//}
