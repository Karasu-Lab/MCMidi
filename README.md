# MCMidi

MCMidi is a Minecraft Fabric mod that allows you to play MIDI files in-game with high-quality sound and real-time visualization.

## Features
- **MIDI Playback**: Play `.midi` or `.mid` files directly within your Minecraft client.
- **Custom SoundFonts**: Support for `.sf2` SoundFont files to customize your instrument sounds.
- **Real-time Visualization**: A dedicated "Midi Control Center" with multiple visualization tabs:
    - **Detail**: Real-time MIDI message log, BPM, and playback position.
    - **Nodes**: Visual representation of active MIDI nodes. (Not implemented.)
    - **Piano**: A virtual piano roll showing active notes.(Not implemented.)
    - **Waveform**: Dynamic waveform visualization. (Not implemented.)
- **In-Game Configuration**: Integrates with Mod Menu and Cloth Config for seamless setting adjustments.
- **Client-Side Commands**: Quick control over playback using console commands.

## Command Usage
Manage your MIDI playback using the following client-side commands:

- `/midi play <filename>`: Plays a MIDI file from your local library.
- `/midi stop`: Stops the current MIDI playback.

## How to Use
1. **Prepare your files**:
    - Place your `.midi` or `.mid` files in the `midi/musics/` directory within your Minecraft instance folder.
    - (Optional) Place your `.sf2` SoundFont files in the `midi/soundfonts/` directory.
2. **Launch Minecraft**: Open the **Midi Control Center** by pressing `,` (default keybind).
3. **Configure Settings**: Open the Mod Menu from the main menu, find **MCMidi**, and click the gear icon to adjust volume or select a custom SoundFont.

## Implemented Architecture
MCMidi is built with a focus on modularity and maintainability:
- **Dependency Injection**: Uses an abstracted `IConfigManager` to handle configuration without global static state.
- **Abstracted Tab System**: Leverages `KarasunikiLib`'s `ITabBar` and `ITabContent` interfaces to provide a flexible and extensible UI.
- **Decoupled MIDI Engine**: Uses the `IMidiEngine` interface, allowing the mod to support different MIDI drivers or external devices in the future.

## Dependencies
This mod requires the following libraries:
- **Fabric API**
- **KarasunikiLib**
- **Cloth Config API**
- **Mod Menu** (Optional, recommended for configuration UI)

## Contribution
Contributions are welcome! If you have suggestions, bug reports, or would like to submit a pull request, please follow these steps:

1. Fork this repository and create your branch from `main`.
2. Make your changes with clear commit messages.
3. Ensure your code builds and passes any existing checks.
4. Submit a pull request with a description of your changes.

For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License. See [LICENSE](https://github.com/Hashibutogarasu/MCMidi/blob/main/LICENSE) for details.
