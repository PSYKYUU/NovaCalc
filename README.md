# NovaCalc 🧮

<p align="center">
  <strong>A modern, high-precision scientific & complex-number calculator for Android.</strong>
</p>

<p align="center">
  Built with <strong>Kotlin</strong>, <strong>Jetpack Compose</strong>, <strong>Material 3</strong>, and <strong>Room</strong> — completely offline and privacy-focused.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform: Android">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Language: Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="UI: Jetpack Compose">
  <img src="https://img.shields.io/badge/Design-Material%203-FF7043" alt="Design: Material 3">
  <img src="https://img.shields.io/badge/Database-Room-009688" alt="Database: Room">
  <img src="https://img.shields.io/badge/Privacy-100%25%20Offline-00C853" alt="Privacy: 100% Offline">
  <img src="https://img.shields.io/badge/License-MIT%20%2F%20Apache%202.0-blue" alt="License">
</p>

---

## 📖 Overview

**NovaCalc** is a modern scientific calculator for Android designed for students, engineers, physicists, programmers, and mathematics enthusiasts.

It provides a powerful mathematical engine capable of handling both **real and complex numbers** without forcing users into separate calculator modes.

NovaCalc combines advanced scientific functionality with a modern Material 3 interface, local calculation history, dynamic variables, extensive theming, and an offline-first architecture.

> **No ads. No trackers. No internet connection required. Your calculations stay on your device.**

---

## ✨ Features

### 🧮 Scientific & Complex Mathematics

* Full real-number and complex-number arithmetic
* Cartesian complex-number representation
* Polar / phasor notation
* Modulus and phase / argument
* Complex conjugation
* Complex-number arithmetic:

  * Addition
  * Subtraction
  * Multiplication
  * Division
  * Powers
* Transcendental functions for complex arguments
* Trigonometric functions
* Hyperbolic functions
* Degrees / radians toggle
* Powers and roots
* Logarithms
* Factorials
* Percentages
* Multi-base exponentiation

### 🔢 Smart Expression Engine

NovaCalc includes an expression parser designed to make mathematical input feel natural.

**Implicit multiplication**

```text
2(3+4)
3i
4π
2x
```

**Smart parentheses**

NovaCalc tracks open parentheses while typing and automatically resolves remaining closures when evaluating an expression.

### 🧠 Dynamic Variables & Constants

Create your own variables and reuse them across calculations.

```text
k = 9e9
v0 = 25
radius = 4.5
```

Variables update reactively across expressions.

NovaCalc also provides commonly used mathematical constants:

| Constant |       Value |
| -------- | ----------: |
| π        | 3.14159265… |
| e        | 2.71828182… |
| φ        | 1.61803398… |
| τ        | 6.28318530… |

### ⚡ Quick Variables

An optional **Quick Variables Bar** provides one-tap access to frequently used variables directly above the keypad.

It can be enabled or disabled from Settings.

---

## 🕘 Calculation History

NovaCalc maintains a local history of calculations using **Room + SQLite**.

Features include:

* Automatic calculation history
* Searchable expressions
* History filtering
* Favorite / bookmark results
* Reuse previous expressions
* Reuse previous results
* One-tap clipboard export
* Reactive history updates
* Local-only storage

Your calculation history never needs to leave your device.

---

## 🎨 Personalization

NovaCalc includes multiple handcrafted dark themes designed for different environments and preferences.

### Available Themes

| Theme               | Description                                         |
| ------------------- | --------------------------------------------------- |
| **Dark Obsidian**   | Midnight slate with deep cyan accents               |
| **OLED True Black** | Pitch-black interface with vivid emerald highlights |
| **Midnight Navy**   | Deep indigo with sky-blue accents                   |
| **Cyber Neon**      | Dark violet with neon-magenta highlights            |
| **Titanium Slate**  | Industrial gunmetal with soft indigo accents        |

### Corner Geometry

NovaCalc also allows the calculator's geometry to be customized.

**Curved**

> Fluid rounded corners with modern Material-style geometry.

**Sharp**

> Minimal 0–2dp corner geometry for a cleaner, more brutalist appearance.

The geometry system affects buttons, cards, tabs, and other UI components throughout the application.

---

## 🔒 Privacy First

NovaCalc is designed around an **offline-first architecture**.

* 🚫 No advertisements
* 🚫 No trackers
* 🚫 No telemetry
* 🚫 No data harvesting
* 🚫 No internet permission required
* ✅ Local calculation history
* ✅ Local variables
* ✅ Local database
* ✅ Calculations remain on the device

Your mathematical expressions and calculation history are yours.

---

## 🏗️ Architecture

NovaCalc is built using modern Android development practices and Google's recommended architecture patterns.

### Technology Stack

| Technology          | Purpose                        |
| ------------------- | ------------------------------ |
| **Kotlin**          | Application language           |
| **Jetpack Compose** | Declarative UI                 |
| **Material 3**      | Design system                  |
| **Room**            | Local SQLite database          |
| **KSP**             | Annotation processing          |
| **Coroutines**      | Asynchronous operations        |
| **StateFlow**       | Reactive state management      |
| **MVI / MVVM**      | Application state architecture |
| **AndroidX**        | Android application framework  |

### Project Structure

```text
app/src/main/java/com/example/
│
├── data/
│   ├── AlexCalcDatabase.kt
│   ├── CalculationHistory.kt
│   ├── VariableEntity.kt
│   └── AlexCalcRepository.kt
│
├── math/
│   ├── Complex.kt
│   └── MathEngine.kt
│
├── ui/
│   ├── components/
│   │   ├── CalcButton.kt
│   │   ├── DisplaySection.kt
│   │   ├── KeypadSection.kt
│   │   ├── VariablesRow.kt
│   │   ├── HistorySheet.kt
│   │   ├── SettingsSheet.kt
│   │   └── LegalDialogs.kt
│   │
│   ├── theme/
│   │   ├── Color.kt
│   │   └── Theme.kt
│   │
│   ├── AlexCalcViewModel.kt
│   └── CalculatorScreen.kt
│
└── MainActivity.kt
```

---

## ⚙️ How It Works

### Mathematical Engine

The mathematical core contains:

* Tokenization
* Expression parsing
* AST-based evaluation
* Implicit multiplication
* Parenthesis handling
* Complex-number operations
* Scientific functions
* Variable resolution

The complex-number implementation overloads the standard mathematical operators for complex values.

```text
Complex(real, imaginary)
```

This allows expressions such as:

```text
(2 + 3i) * (4 - i)
```

to be evaluated naturally.

### Reactive UI

NovaCalc is built entirely with **Jetpack Compose** without legacy XML layouts.

Application state is managed through:

```text
StateFlow
    ↓
ViewModel
    ↓
Composable UI
```

Lifecycle-aware state collection keeps the interface reactive while supporting phones, tablets, foldables, and different window sizes.

### Local Data Layer

NovaCalc uses **AndroidX Room** with **KSP** for local persistence.

The database contains entities for:

* Calculation history
* User variables
* Favorites

Reactive Room queries automatically update the UI when stored data changes.

---

## 📱 Screenshots

> Screenshots coming soon.

Recommended screenshot layout:

```text
[ Main Calculator ]   [ Scientific Mode ]   [ Complex Mode ]

[ History ]           [ Variables ]         [ Themes ]
```

---

## 🚀 Building & Running

### Requirements

* Android Studio **Hedgehog 2023.1.1** or newer
* JDK **17+**
* Android SDK Platform **34**
* Android device or emulator

### Clone the Repository

```bash
git clone https://github.com/your-username/novacalc.git
cd novacalc
```

### Build Debug APK

```bash
./gradlew assembleDebug
```

### Run Unit Tests

```bash
./gradlew testDebugUnitTest
```

### Install on a Connected Device

```bash
./gradlew installDebug
```

---

## 📚 Credits & Attribution

### AlexCalc

NovaCalc's mathematical core, expression evaluation architecture, and complex-number functionality are inspired by and based upon the open-source **AlexCalc** project created by **Alex Barry**.

**Original project:** AlexCalc
**Original author:** Alex Barry
**Original license:** MIT License

We are sincerely grateful to Alex Barry and the contributors of AlexCalc for creating the foundation that made this project possible.

### What NovaCalc Adds

NovaCalc builds upon the original foundation with a substantially modernized Android application layer, including:

* Renamed and redesigned application
* Jetpack Compose UI
* Material 3 design system
* Responsive mobile UI
* Kotlin Coroutines
* StateFlow-based reactive architecture
* Room SQLite calculation history
* Dynamic variables
* Custom scientific and complex-number UI
* Multiple dark themes
* Custom corner geometry
* Quick Variables Bar
* History search and filtering
* Favorites
* Clipboard export
* Local-first privacy architecture
* Modern Android project structure

> NovaCalc would not exist without the original open-source work behind AlexCalc.

Thank you, **Alex Barry**, for making the original project available under the MIT License. 💙

---

## 📜 License

NovaCalc contains code originating from two licensing contexts.

### AlexCalc Mathematical Core

The original AlexCalc-derived mathematical core is licensed under the:

**MIT License**

Copyright © 2020 Alex Barry

### NovaCalc Application Code

The NovaCalc Android application code, including the Compose UI and Material 3 implementation, is licensed under:

**Apache License 2.0**

See the full license texts included in the repository and the application's **Settings → Open Source Licenses** section.

---

## 🤝 Contributing

Contributions, bug reports, feature requests, and improvements are welcome.

If you find a bug or have an idea for improving NovaCalc:

1. Open an issue.
2. Describe the problem or proposed feature.
3. Include reproduction steps when applicable.
4. Provide relevant device / Android version information.
5. Submit a pull request for implementation changes.

---

## ⭐ Acknowledgements

Special thanks to:

* **Alex Barry** — original AlexCalc project and mathematical foundation
* **Android Open Source Project**
* **Jetpack Compose**
* **Material Design**
* **AndroidX Room**
* **Kotlin**
* **Kotlin Coroutines**

---

<p align="center">
  <strong>NovaCalc 🧮</strong><br>
  Modern mathematics. Local by design.
</p>
