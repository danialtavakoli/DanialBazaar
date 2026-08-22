# DanialBazaar

DanialBazaar is an Android shopping application for bags and accessories, built with Jetpack Compose and a clean MVVM architecture.  
The app supports authentication, product browsing, product details with comments, cart management, and checkout flow integration.

## Features

- User sign up / sign in
- Home screen with product feed and ad slider
- Category-based product browsing
- Product details and comments
- Shopping cart with quantity management
- User profile and saved location data
- Checkout and payment flow support

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material
- **Architecture:** MVVM
- **Dependency Injection:** Koin (CoKoin)
- **Networking:** Retrofit2, Gson, OkHttp
- **Local Storage:** Room
- **Async:** Kotlin Coroutines
- **Image Loading:** Coil
- **Animation & UI Utilities:** Lottie, Accompanist

## Project Structure

`/app/src/main/java/com/danialtavakoli/danialbazaar`

- `ui/` – Screens, view models, and app theme
- `model/data/` – DTOs and response models
- `model/net/` – API service and auth/network helpers
- `model/repository/` – Repository interfaces and implementations
- `model/db/` – Room database and DAO
- `di/` – Dependency injection modules
- `util/` – Constants, navigation routes, and extensions

## Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK (minSdk 21, target/compileSdk 33)
- JDK 8+

### Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync dependencies.
4. Run the app on an emulator or physical Android device.

### Build from terminal

```bash
./gradlew assembleDebug
```

## Screenshots

<p float="left">
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/open.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/signUp.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/signIn.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/main1.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/main2.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/profile.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/details.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/category.jpg" width="24%" />
  <img src="https://github.com/danialtavakoli/DanialBazaar/blob/master/Screenshots/cart.jpg" width="24%" />
</p>
