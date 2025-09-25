# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Paper plugin for Minecraft 1.21 that adds Quality of Life (QoL) features. It's built with Java 21 and uses the Paper API.

## Essential Commands

### Build and Development
```bash
# Build the plugin JAR
./gradlew build

# Build with offline mode (use when Paper repository is down)
./gradlew build --offline

# Run a test Paper server with the plugin loaded (Minecraft 1.21)
./gradlew runServer

# Run test server offline (when repository is unavailable)
./gradlew runServer --offline

# Clean build artifacts
./gradlew clean

# Clean and build offline
./gradlew clean build --offline

# Generate JAR without tests
./gradlew jar

# Compile Java sources
./gradlew compileJava
```

**Important**: If you encounter "502 Bad Gateway" errors from the Paper repository, use the `--offline` flag. This requires having the dependencies cached locally from a previous successful build.

### Paper-Specific Commands
```bash
# Clear cached Paper server JARs
./gradlew cleanPaperCache

# Clear cached Paper plugin JARs
./gradlew cleanPaperPluginsCache
```

## Architecture

### Plugin Structure
- **Main Class**: `src/main/java/extra/qol/copperPlus/CopperPlus.java` - Extends `JavaPlugin` and serves as the entry point
- **Plugin Metadata**: `src/main/resources/plugin.yml` - Defines plugin name, version, author, and API version
- **Package Structure**: `extra.qol.copperPlus` - All plugin code should be under this package

### Paper Plugin Development Pattern
When implementing features:
1. Event listeners should be registered in `onEnable()` method
2. Commands should be defined in `plugin.yml` and handled via command executors
3. Use Paper API (not Bukkit) for better performance features
4. The plugin targets Paper 1.21.8-R0.1-SNAPSHOT

### Build Configuration
- Uses Gradle 8.8 with the `xyz.jpenilla.run-paper` plugin for development
- Java 21 is required for compilation and runtime
- The built JAR will be in `build/libs/` after running `./gradlew build`
- `processResources` task automatically substitutes version variables in `plugin.yml`

## Key Development Notes
- The plugin prefix "CopperPlus" is used for console output and logging
- When testing, `./gradlew runServer` automatically downloads and runs a Paper 1.21 server
- The project uses IntelliJ IDEA configuration (.idea directory present)