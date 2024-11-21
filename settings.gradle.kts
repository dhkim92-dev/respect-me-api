plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "respectme"
include("common")
include("member-api")
include("auth-api")
include("group-api")
include("message-api")
