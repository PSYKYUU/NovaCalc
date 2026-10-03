# NovaCalc 🧮

<p align="center">
  <b>A modern, high-precision Scientific & Complex Number Calculator built for Android.</b><br>
  Crafted with Jetpack Compose, Material Design 3, Room Database, and an offline-first architecture.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform: Android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Language: Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="UI: Jetpack Compose" />
  <img src="https://img.shields.io/badge/Design-Material%203-FF7043" alt="Design: Material 3" />
  <img src="https://img.shields.io/badge/Database-Room%20(SQLite)-009688" alt="Database: Room" />
  <img src="https://img.shields.io/badge/Privacy-100%25%20Offline-00C853" alt="Privacy: 100% Offline" />
  <img src="https://img.shields.io/badge/License-MIT%20%2F%20Apache%202.0-blue" alt="License" />
</p>

---

## 🌟 What It Does

NovaCalc is engineered for students, engineers, physicists, and mathematics enthusiasts who need a powerful scientific calculator that seamlessly handles **both real ($\mathbb{R}$) and complex ($\mathbb{C}$) numbers** without clunky secondary modes or mode penalties.

### ✨ Key Features

- **Full Complex Number Arithmetic ($\mathbb{C}$)**:
  - Native representation of complex numbers in Cartesian form ($a + bi$) and Polar phasor notation ($r \angle \theta$).
  - Modulus ($|z| = \text{abs}(z)$), phase/argument ($\text{arg}(z)$), and complex conjugate ($z^* = \text{conj}(z)$).
  - Transcendental functions on complex arguments: $e^z$, $\ln(z)$, $\sin(z)$, $\cos(z)$, $\sqrt{z}$, etc.

- **Scientific & Engineering Evaluation**:
  - Trigonometric and hyperbolic functions ($\sin, \cos, \tan, \arcsin, \sinh, \cosh, \tanh$).
  - Instant angle toggle between **Degrees (DEG)** and **Radians (RAD)**.
  - Multi-base powers ($x^y$, $e^x$, $10^x$), roots ($\sqrt{x}$, $\sqrt[n]{x}$), logarithms ($\ln$, $\log_{10}$), factorials ($n!$), and percentage.

- **Dynamic Variable Memory & Constants**:
  - Create and store custom variables (e.g. `k = 9e9`, `v0 = 25`, `radius = 4.5`) that update live across expressions.
  - Built-in fundamental mathematical constants: $\pi$ ($3.14159\dots$), $e$ ($2.71828\dots$), $i$ ($\sqrt{-1}$), and golden ratio $\phi$ ($1.61803\dots$).
  - Optional 1-tap **Quick Variables Bar** directly above the keypad (can be toggled on/off in Settings).

- **Rich Local Calculation History**:
  - Automatically records every computation into an encrypted, isolated **SQLite Room Database**.
  - Favorite/bookmark important results.
  - Filter and search past expressions.
  - Reuse past formulas or results into the active display with a single tap.
  - One-tap **Export to Clipboard** to share logs or study notes.

- **Visual Personalization & Theming**:
  - **5 Handcrafted Dark Palettes**:
    - **Dark Obsidian**: Midnight slate with deep cyan accents.
    - **OLED True Black**: 100% pitch black with vivid emerald highlights (battery saver on AMOLED).
    - **Midnight Navy**: Cosmic deep indigo and sky blue.
    - **Cyber Neon**: Synthwave dark violet with neon magenta keys.
    - **Titanium Slate**: Industrial gunmetal with soft indigo keys.
  - **Dynamic Corner Geometry**:
    - **Curved**: Fluid, modern rounded corners (16–24dp).
    - **Sharp**: Clean, brutalist square geometry (0–2dp) across every button, card, and tab.

- **Zero Ads, Zero Trackers, 100% Offline**:
  - No internet permission. No data harvesting. No telemetry. Your calculations stay strictly on your device.

---

## ⚙️ How It Does It (Architecture & Under the Hood)

NovaCalc is built using modern Android best practices and Google's recommended architecture:

```
app/src/main/java/com/example/
├── data/                    # Data Layer
│   ├── AlexCalcDatabase.kt  # Room SQLite Database
│   ├── CalculationHistory.kt# History Entity & DAO
│   ├── VariableEntity.kt    # Dynamic Variables Entity & DAO
│   └── AlexCalcRepository.kt# Clean Architecture Repository
├── math/                    # Mathematical Core
│   ├── Complex.kt           # IEEE-754 Complex Number Engine
│   └── MathEngine.kt        # Tokenizer, AST Parser & Evaluator
├── ui/                      # Presentation Layer (Jetpack Compose)
│   ├── components/          # Modular Compose UI Components
│   │   ├── CalcButton.kt    # Responsive Haptic Animated Key
│   │   ├── DisplaySection.kt# Scrollable Interactive Display & Angle Mode
│   │   ├── KeypadSection.kt # Multi-mode (Basic, Scientific, Complex) Keypad
│   │   ├── VariablesRow.kt  # Quick 1-tap Variable Chips
│   │   ├── HistorySheet.kt  # Searchable History Bottom Sheet
│   │   ├── SettingsSheet.kt # Visual Theme Mockups & Preferences
│   │   └── LegalDialogs.kt  # Full License & Credits Dialogs
│   ├── theme/               # Dynamic Theming & Shape System
│   │   ├── Color.kt         # Color Palettes & Color Schemes
│   │   └── Theme.kt         # CompositionLocal Providers (Colors & Shapes)
│   ├── AlexCalcViewModel.kt # StateFlow & MVI/MVVM ViewModel
│   └── CalculatorScreen.kt  # Top-level Screen Scaffold
└── MainActivity.kt          # Edge-to-Edge Single Activity Entrypoint
```

### 1. Mathematical Parser Engine
- **Implicit Multiplication**: Handles notations like `2(3+4)`, `3i`, `4π`, and `2x` automatically.
- **Smart Parentheses**: Tracks unclosed parentheses badges in real time and automatically resolves open closures on evaluation.
- **Complex Numbers**: Overloads all standard mathematical operators (`+`, `-`, `*`, `/`, `^`) for `Complex(real, imag)`.

### 2. Reactive UI Architecture
- Built **100% in Jetpack Compose** without legacy XML layouts.
- Employs `MutableStateFlow` and `collectAsStateWithLifecycle()` to provide 60/120 FPS buttery-smooth frame rates.
- Responsive to foldables, tablets, and phones with adaptive WindowInsets handling.

### 3. Data Persistence
- Uses **AndroidX Room** with **KSP** (Kotlin Symbol Processing).
- Reactive `Flow<List<CalculationHistoryEntity>>` queries automatically refresh the UI as soon as new computations occur.

---

## 📖 Source & Attribution

### Credits To The Owner

NovaCalc's mathematical core, expression evaluation architecture, and complex arithmetic logic is inspired by and based upon the open-source **[AlexCalc](https://github.com/alexbarry/AlexCalc)** project created by **Alex Barry**.

- **Original Project**: [AlexCalc](https://github.com/alexbarry/AlexCalc)
- **Author**: Alex Barry
- **License**: MIT License

We express our sincere gratitude and highest credit to Alex Barry and all contributors to the original AlexCalc repository for their foundational work in open-source mobile scientific computation.

---

## 💖 Made with Love & Respect for the Original Creator

All love, appreciation, and credit belong first and foremost to the original creator, **[Alex Barry](https://github.com/alexbarry)**, who built the remarkable foundation and mathematical core of **[AlexCalc](https://github.com/alexbarry/AlexCalc)**.

What we did in NovaCalc is simply stand on the shoulders of Alex Barry's exceptional work:
- **Renamed it**: Transformed the package presentation to NovaCalc.
- **Revamped it**: Re-architected the entire interface into 100% declarative **Jetpack Compose** and **Material Design 3**, replacing legacy layouts with fluid animations and responsive mobile ergonomics.
- **Modernized it**: Added reactive Kotlin Coroutines/StateFlow architecture, **Room SQLite** local calculation history, dynamic variables system, custom dark theme engine with interactive visual mockups, brutalist/curved corner geometry toggles, and seamless export utilities.

Without Alex Barry's original open-source vision, passion for mathematics, and generous MIT licensing, this modern iteration would not exist. All credit for the underlying computational magic goes to him. Thank you, Alex! 💙

---

## 🚀 Building & Running

### Prerequisites
- **Android Studio** (Hedgehog 2023.1.1 or newer recommended)
- **JDK 17+**
- **Android SDK Platform 34**

### Clone & Build
```bash
# Clone the repository
git clone https://github.com/your-username/novacalc.git

# Navigate to project root
cd novacalc

# Build Debug APK
./gradlew assembleDebug

# Run Unit & Robolectric Tests
./gradlew testDebugUnitTest
```

---

## 📜 License

This project is open-source software:
- The core mathematical engine is licensed under the **[MIT License](https://opensource.org/licenses/MIT)** (Copyright © 2020 Alex Barry).
- The Android application codebase, Jetpack Compose UI, and Material 3 design system are licensed under the **[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)**.

Full license texts are accessible directly inside the app under **Settings → Open Source Licenses**.
