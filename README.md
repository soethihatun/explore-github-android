# Explore GitHub Android
A simple Android App to demonstrate the use of modern android development. It makes use of GitHub Apis to explore GitHub users.

## Features
* User search screen with pagination
* User repository screen with pagination
* Recent searches
* Custom Tabs to show GitHub Repository

## Screenshots
<img width="1488" alt="v0 1 0_screenshot" src="https://github.com/soethihatun/explore-github-android/assets/5600819/dc059f98-6cc6-4698-8a20-45ae8e59d892">

## How to run the project
- Please add [secrets.properties](secrets.properties) file in project's root directory.
- Get `YOUR TOKEN` from [GitHub API](https://docs.github.com/en).
```
GITHUB_ACCESS_TOKEN="YOUR TOKEN"
GITHUB_BASE_URL="https://api.github.com/"
```

## What's under the hood

### Technologies used
* Kotlin
* Jetpack Compose
* MVVM with clean architecture
* Modularization
* Kotlin Coroutine
* Dagger Hilt
* Retrofit
* OkHttp
* Kotlinx serialization
* Material 3
* Room database
* Gradle Kotlin DSL
* Version catalog
* Unit tests
* JUnit
* Truth assertion library
* MockK
* Instrumented tests
* UI tests
* Espresso

### Todos
* Gradle convention plugin
* Tests on datbase
* Refine UI
