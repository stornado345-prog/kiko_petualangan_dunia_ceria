#kiko Petualangan Dunia Ceria 🎮🌱

An interactive Android educational game designed to help children aged 4–6 learn through play. Built with Kotlin and Jetpack Compose, Aku Pintar combines colorful 2D cartoon visuals, engaging animations, interactive questions, and playful learning experiences.

## 📖 About the Project

**Aku Pintar: Petualangan Dunia Ceria** is an educational game concept for preschool and kindergarten children (TK A and TK B). Children explore colorful worlds, solve simple challenges, recognize everyday objects, and practice early numeracy skills with the help of Kiko, a friendly deer mascot.

The project focuses on making early learning enjoyable through interactive gameplay, child-friendly UI/UX, visual feedback, and audio-guided activities.

## ✨ Features

- 🎮 Interactive educational gameplay
- 🔢 Early numeracy and simple addition
- 🍎 Fruit and vegetable recognition
- 🐘 Animal recognition and counting
- 🌱 Plant and nature exploration
- 🌈 Colorful, child-friendly 2D cartoon art direction
- 🎨 Animated UI and gameplay feedback
- 🔊 Audio-guided questions and sound effects
- ⭐ Star rewards and level progression
- 🗺️ Five themed adventure worlds
- 🧩 Planned content of 200 levels
- 📱 Portrait-oriented mobile experience
- 📦 Offline-first gameplay design
- 💾 Local player progress storage

> **Development status:** Features listed above describe the project goals and planned design. Actual implementation, available assets, and tested functionality depend on the current repository version.

## 🌍 Game Worlds

| World | Theme | Level Range |
|---|---|---|
| 1 | Forest of Numbers 🌳 | 1–40 |
| 2 | Happy Fruit Garden 🍎 | 41–80 |
| 3 | Vegetable Farm 🥕 | 81–120 |
| 4 | Animal Kingdom 🐘 | 121–160 |
| 5 | Magical Plant Forest 🌻 | 161–200 |

Each world is designed to introduce age-appropriate learning activities through visual challenges, counting games, matching activities, and simple addition.

## 🕹️ Gameplay Concept

1. Meet Kiko, the friendly deer mascot.
2. Explore an adventure world.
3. Complete interactive educational questions.
4. Receive visual and audio feedback.
5. Collect stars and unlock new levels.
6. Continue learning through progressively varied challenges.

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material 3
- **Architecture:** ViewModel and state-driven UI
- **Asynchronous Programming:** Kotlin Coroutines and StateFlow
- **Local Storage:** DataStore
- **Navigation:** Navigation Compose
- **Animation:** Compose Animation, with Rive or sprite sheets where integrated
- **Audio:** Android audio APIs, with SoundPool or Media3 where appropriate
- **Development Environment:** Android Studio
- **Build System:** Gradle with Kotlin DSL

The exact dependency versions and implemented components should be verified against the current project configuration.

## 🎨 UI/UX Design Goals

The game aims to provide an engaging and accessible learning experience for young children.

- Large, easy-to-tap interactive elements
- Clear typography and simple instructions
- Colorful cartoon illustrations
- Smooth screen transitions
- Animated characters and backgrounds
- Positive feedback for correct answers
- Gentle hints and retry opportunities
- Responsive layouts for different Android phone sizes

## 🧠 Educational Goals

Aku Pintar is designed to support the development of:

- Number recognition
- Counting and early addition
- Object recognition and vocabulary
- Pattern recognition
- Observation and logical thinking
- Basic knowledge of animals, plants, fruits, and vegetables
- Independent exploration and problem-solving

The game is intended as a supplementary learning activity, not a replacement for guided teaching.

## 📂 Project Structure

The intended project structure is organized by responsibility:

```text
app/
└── src/
    └── main/
        ├── java/com/akupintar/game/
        │   ├── core/
        │   │   ├── animation/
        │   │   ├── audio/
        │   │   ├── model/
        │   │   └── utils/
        │   ├── data/
        │   │   ├── levels/
        │   │   ├── questions/
        │   │   └── repository/
        │   ├── domain/
        │   │   ├── engine/
        │   │   ├── progress/
        │   │   └── rewards/
        │   ├── ui/
        │   │   ├── home/
        │   │   ├── worldmap/
        │   │   ├── levelmap/
        │   │   ├── gameplay/
        │   │   ├── result/
        │   │   └── settings/
        │   └── MainActivity.kt
        └── res/
            ├── drawable/
            ├── mipmap/
            ├── raw/
            └── values/
```

This structure is a suggested organization. Adapt folder names to match the actual repository.

## 🚀 Getting Started

### Prerequisites

- Android Studio
- A compatible JDK
- Android SDK required by the project
- Gradle configuration included in the repository
- An Android emulator or physical Android device

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/aku-pintar.git
   ```

2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Connect an Android device or start an emulator.
5. Run the `app` configuration.

Replace `YOUR-USERNAME` and the repository name with your actual GitHub details.

## 🧪 Testing

The project should be tested for:

- Correct answer validation
- Question generation and data integrity
- Level unlocking and progression
- Star and reward calculations
- Persistence of player progress
- Audio playback and settings
- Navigation between screens
- Animation performance
- Layout behavior across screen sizes

Only report tests as passing after they have actually been executed.

## 🔒 Privacy and Child-Friendly Design

The project aims to prioritize children's safety by:

- Avoiding unnecessary collection of personal information
- Supporting offline gameplay
- Avoiding disruptive advertisements
- Using age-appropriate educational content
- Providing gentle, encouraging feedback

Privacy and data-handling claims should be confirmed against the final implementation and any third-party SDKs used.

## 🗺️ Roadmap

- [ ] Complete the animated home screen
- [ ] Implement Kiko's character animations
- [ ] Build the interactive world map
- [ ] Implement the reusable question engine
- [ ] Develop the five themed worlds
- [ ] Add and validate the 200-level content
- [ ] Integrate Indonesian narration and sound effects
- [ ] Implement local progress persistence
- [ ] Add unit tests for core game logic
- [ ] Optimize performance on low- and mid-range devices
- [ ] Test the complete game on physical devices
- [ ] Prepare a release build

## 🤝 Contributions

Suggestions, bug reports, and contributions are welcome. Please open an issue to discuss significant changes before submitting a pull request.

## 📄 License

Choose a license before distributing or accepting contributions. If you want others to use, modify, and redistribute the code, consider an appropriate open-source license such as MIT. Ensure that third-party artwork, music, fonts, and other assets have compatible licenses.

## 👨‍💻 Author

**Satria Tornado**

Android Development | Kotlin | Jetpack Compose | Mobile UI/UX

---

*Aku Pintar — Learn, Play, and Grow!* 🌈
