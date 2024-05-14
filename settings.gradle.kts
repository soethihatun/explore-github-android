pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Explore GitHub Android"
include(":app")
include(":core:model")
include(":core:domain")
include(":core:data")
include(":core:repository")
include(":feature:home")
include(":core:network")
include(":core:common")
include(":core:ui")
include(":core:testing")
