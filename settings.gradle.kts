import org.ajoberstar.reckon.core.Scope.PATCH
import org.ajoberstar.reckon.gradle.ReckonExtension
import java.util.Optional

rootProject.name = "okio-extras"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.12.6"
    id("org.ajoberstar.reckon.settings") version "0.17.0-beta.4"
}

configure<ReckonExtension> {
    /*
     * Only two stages are used, `snapshot` and `final`, `snapshot` being the
     * default one.
     */
    snapshots()

    /*
     * The stage is calculated from the `reckon.stage` property.
     */
    setStageCalc(calcStageFromProp())

    /*-
     * How the version is incremented.
     *
     * MINOR: 1.0.0 -> 1.1.0-SNAPSHOT
     * PATCH: 1.0.0 -> 1.0.1-SNAPSHOT
     */
    setScopeCalc {
        Optional.of(PATCH)
    }

    /*
     * Run `./gradlew -Preckon.stage=final reckonTagCreate` when the Git
     * repository is clean.
     */
    setTagWriter { version ->
        "v$version"
    }
}

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}
