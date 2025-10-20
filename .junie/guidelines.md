Boomerang — Development Guidelines (Advanced)

This document captures project-specific knowledge needed to build, test, and extend Boomerang effectively. It assumes familiarity with Kotlin Multiplatform (KMP), Gradle, Android/Compose, and common testing practices.

1. Build and configuration

- Toolchain and Gradle
  - Kotlin: 2.2.20 (see gradle/libs.versions.toml)
  - Gradle Android Plugin (AGP): 8.11.1 — requires JDK 17+. Use a current JDK 17 or 21 runtime.
  - Compose Multiplatform plugin: 1.9.1
  - Dokka: 2.1.0 (multi-module docs output under docs/)
  - Publishing: com.vanniktech.maven.publish is applied to library modules

- Project layout and modules
  - Root settings enable Typesafe Project Accessors; modules included via settings.gradle.kts
  - Modules:
    - :core — KMP library (android target via androidLibrary + convention plugin adds desktop/iOS)
    - :compose — KMP library containing Compose utilities
    - :fragment — Android-only fragment support
    - :serialization-kotlinx — KMP library integrating kotlinx.serialization with Boomerang
    - :compose-serialization-kotlinx — KMP library for Compose + kotlinx.serialization
    - :fragment-serialization-kotlinx — Android fragment + kotlinx.serialization integration
    - :app — KMP application showcasing usage; Android, Desktop, and iOS source sets are present
  - Convention plugins (build-logic/) encapsulate common KMP configuration. Expect additional targets (desktop, iOS) to be configured there even if not explicitly declared in the module build scripts.

- Android configuration
  - SDK: compile/target SDK 36, minSdk 21 (see gradle/libs.versions.toml)
  - Ensure local.properties points to a valid Android SDK (sdk.dir=...)
  - Instrumented tests live under app/src/androidInstrumentedTest and rely on Espresso and Compose UI Test.

- Building
  - Build everything: ./gradlew build
  - Build a single module: ./gradlew :core:build (replace :core with the module of interest)
  - IDE import: Use the Gradle wrapper; avoid importing as a plain Kotlin project due to KMP multi-target configuration.

- Documentation (Dokka)
  - Dokka is configured at the root to aggregate module docs into docs/ via Dokka 2.x tasks.
  - Existing API docs are committed under docs/. If you regenerate, prefer running Dokka from the root so cross-module links resolve.
  - Tip: Use Gradle task listing (./gradlew tasks --all | grep -i dokka) to discover the exact Dokka 2.x task names created by the convention plugin.

2. Testing — configuration and execution

- Frameworks and targets
  - KMP unit tests use kotlin("test") across targets. The convention plugin wires common and per-target test tasks.
  - Verified existing tests: :serialization-kotlinx has commonTest powered by kotlin.test. During this session, we executed tests successfully for desktop and iOS simulator targets.
  - Android instrumented UI tests are in :app (androidInstrumentedTest) using JUnit4, Espresso, and Compose UI test APIs.

- Running unit tests (KMP)
  - All tests in a module across enabled targets:
    - Example (serialization-kotlinx):
      - ./gradlew :serialization-kotlinx:allTests
    - Notes:
      - Per-target tasks are also available, e.g. :serialization-kotlinx:desktopTest and :serialization-kotlinx:iosSimulatorArm64Test
      - The exact set of iOS targets depends on the convention plugin and your host OS. On macOS with Xcode installed, simulator tasks are available.
  - Run a single test class/method (where supported by the target’s test runner):
    - ./gradlew :serialization-kotlinx:desktopTest --tests "io.buszi.boomerang.serialization.kotlinx.BoomerangEncoderTest"

- Running Android instrumented tests
  - Requires a running device/emulator with API >= minSdk (21)
  - Command: ./gradlew :app:connectedAndroidTest
  - Useful flags: -Pandroid.testInstrumentationRunnerArguments.class=... to target a specific test class

- Adding a new common test (example)
  - Use an existing module that already declares test dependencies. The :serialization-kotlinx module is preconfigured with kotlin("test") in commonTest.
  - Example file (not committed; created and executed during this session, then removed):
    ```kotlin
    package io.buszi.boomerang.serialization.kotlinx

    import kotlin.test.Test
    import kotlin.test.assertEquals

    class SimpleExampleTest {
        @Test
        fun addition() {
            assertEquals(4, 2 + 2)
        }
    }
    ```
  - After adding the file under serialization-kotlinx/src/commonTest/kotlin/io/buszi/boomerang/serialization/kotlinx/, run:
    - ./gradlew :serialization-kotlinx:allTests
  - If you prefer to add tests under another module (e.g., :core), ensure that module’s build script declares test dependencies:
    ```kotlin
    kotlin {
        sourceSets {
            commonTest.dependencies {
                implementation(kotlin("test"))
            }
        }
    }
    ```

- What we verified in this session
  - Existing test file BoomerangEncoderTest ran and passed on desktop and iosSimulatorArm64
  - A temporary SimpleExampleTest under :serialization-kotlinx/commonTest ran and passed on desktop and iosSimulatorArm64, then was removed to keep the tree clean

3. Additional development information

- Expect/Actual Beta warnings
  - You may see warnings like: 'expect'/'actual' classes are in Beta; consider -Xexpect-actual-classes
  - These are harmless but can be silenced by adding the compiler arg. Prefer doing this in the convention plugin so all modules inherit it. Example snippet (inside kotlin compiler options):
    ```kotlin
    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }
    ```

- Code style and conventions
  - Follow Kotlin idioms used in the codebase; keep public API KDoc in library modules to feed Dokka docs
  - For Compose code, follow unidirectional data flow and hoist state where reasonable; prefer previewable composables for quick inspection
  - Keep multiplatform boundaries clean: expect/actual live in platform-specific source sets; avoid leaking platform types into commonMain

- Module coupling and dependencies
  - :serialization-kotlinx depends on :core; :compose-serialization-kotlinx and :fragment-serialization-kotlinx layer on top of their respective UI/navigation stacks
  - Avoid introducing cyclic dependencies; check settings.gradle.kts and the module build scripts before adding new links

- Testing tips
  - Prefer placing logic in commonMain to maximize test coverage across targets
  - For iOS simulator tests, ensure Xcode and a simulator runtime are installed; you can list available simulators via xcrun simctl list (outside Gradle)
  - For Android UI tests, stabilize with IdlingResources or composeTestRule.waitUntil where necessary

- Documentation
  - When changing public APIs, update KDoc and regenerate docs if needed. Docs are published into docs/; ensure internal packages aren’t accidentally exposed

- Publishing
  - The Vanniktech plugin is present; when you need to publish, configure coordinates and credentials per that plugin’s documentation. Keep versioning centralized via gradle/libs.versions.toml and/or gradle.properties

Notes
- All test examples above were validated during this session using the repository’s existing configuration. Temporary test artifacts created to demonstrate the process were deleted; only this .junie/guidelines.md file was added.
