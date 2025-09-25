# CopperPlus

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-brightgreen.svg)](https://www.minecraft.net/)
[![Paper API](https://img.shields.io/badge/Paper%20API-1.21-blue.svg)](https://papermc.io/)
[![Languages](https://img.shields.io/badge/Languages-11-orange.svg)](#-multi-language-support)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A quality-of-life Paper plugin that revolutionizes copper oxidation mechanics in Minecraft 1.21+ with full multi-language support.

## 📋 Requirements

- **Minecraft Version:** 1.21+
- **Server Type:** Paper/Purpur/Pufferfish (or Paper forks)
- **Java Version:** 21 or higher

## 🎮 Features

### 🌍 Multi-Language Support
**11 Languages Available:**
- English (en_US) - Default
- Chinese Simplified (zh_CN) - 简体中文
- Spanish (es_ES) - Español
- Hindi (hi_IN) - हिन्दी
- Arabic (ar_SA) - العربية
- Portuguese (pt_BR) - Português
- Russian (ru_RU) - Русский
- Japanese (ja_JP) - 日本語
- German (de_DE) - Deutsch
- French (fr_FR) - Français
- Korean (ko_KR) - 한국어

### Accelerated Copper Oxidation
Transform copper blocks through all oxidation stages quickly by creating the right setup:

1. **Place a campfire** (regular or soul campfire)
2. **Place copper block directly above the campfire**
3. **Add water nearby** (within 3x3x3 area around copper)
4. Watch your copper oxidize rapidly!

### Key Features
- **Water Speed Boost:** Each water block increases oxidation speed (25% per block by default)
- **Continuous Oxidation:** Blocks progress through all stages automatically
- **Message Toggle:** Players can toggle oxidation messages with `/coppermsg`
- **Dispenser Support:** Dispensers can place copper blocks that auto-oxidize
- **Visual & Audio Effects:** Customizable particles and sounds
- **Highly Configurable:** Extensive config.yml with 40+ settings

## 🛠️ Installation

1. Download the latest `copper-plus-1.0.jar` from releases
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/copper-plus/config.yml`

## 📝 Commands

All commands use `/copperplus` (or aliases `/cp`, `/copper+`):

| Command | Description | Permission |
|---------|-------------|------------|
| `/copperplus help` | Show all available commands | `copperplus.use` |
| `/copperplus kit` | Get the copper oxidation starter kit | `copperplus.kit` |
| `/copperplus language` | View your current language | `copperplus.use` |
| `/copperplus language set <code>` | Change your language | `copperplus.use` |
| `/copperplus language list` | List all available languages | `copperplus.use` |
| `/copperplus language admin <code>` | Set server default language | `copperplus.admin` |
| `/copperplus togglemsg` | Toggle oxidation messages on/off | `copperplus.use` |
| `/copperplus reload` | Reload plugin configuration | `copperplus.admin` |

## ⚙️ Configuration Overview

The plugin offers extensive configuration through `config.yml`:

### Basic Settings
```yaml
general:
  enabled: true           # Enable/disable entire plugin
  debug: false           # Show debug messages in console

oxidation:
  enabled: true          # Enable oxidation feature
  duration-seconds: 10   # Time per oxidation stage
  water-speed-bonus: 0.25  # Speed multiplier per water block
  max-water-blocks: 26    # Maximum water blocks counted
  require-lit-campfire: true  # Campfire must be lit
  water-check-radius: 1   # Radius to check for water (1 = 3x3x3)
```

### Visual Effects
```yaml
effects:
  particles: true        # Enable particle effects
  particle-type: "WAX_ON"
  particle-count: 10
  sounds: true          # Enable sound effects
  sound-volume: 0.5
  sound-pitch: 1.0
```

### Performance Settings
```yaml
performance:
  check-interval: 20     # Ticks between oxidation checks
  max-per-chunk: 50     # Max oxidations per chunk
```

### Kit Configuration
```yaml
kit:
  items:
    campfire: 4
    copper-block: 64
    water-bucket: 2
    flint-and-steel: 1
  cooldown: 0           # Seconds between kit uses
```

### Customizable Messages
All player messages support color codes (`&`) and can be customized or disabled.

## 🎯 How to Use

### Basic Setup
1. Place a campfire on the ground
2. Place copper blocks directly above the campfire
3. Place water source blocks nearby (within 3 blocks)
4. Oxidation begins automatically!

### Oxidation Stages
The plugin accelerates the natural copper oxidation progression:
- Copper Block → Exposed Copper → Weathered Copper → Oxidized Copper

**Supported Copper Types:**
- Copper Blocks
- Cut Copper (all variants)
- Copper Stairs
- Copper Slabs

**Note:** Waxed copper blocks will not oxidize.

### Tips for Faster Oxidation
- Add more water blocks around the copper (up to 26 blocks counted)
- Keep the campfire lit (if configured)
- Place multiple copper blocks in a tower above one campfire

### Checking Status
- Right-click a copper block to see oxidation progress
- Shows water count and current speed multiplier

## 📊 Default Oxidation Times

With default settings (10 seconds per stage):
- **No water:** No oxidation
- **1 water block:** 10 seconds per stage (base speed)
- **4 water blocks:** 5 seconds per stage (2x speed)
- **8 water blocks:** 3.3 seconds per stage (3x speed)

**Total time from Copper to Oxidized:**
- With 1 water: ~30 seconds
- With 4 water: ~15 seconds
- With 8 water: ~10 seconds

## 🔒 Permissions

- `copperplus.use` - Access to basic commands (default: all)
- `copperplus.kit` - Use the kit command (default: all)
- `copperplus.admin` - Admin commands like reload and setting default language (default: ops)

## 🐛 Troubleshooting

### Oxidation Not Starting
- Check that campfire is placed directly below copper
- Ensure water is within configured radius of copper
- Verify campfire is lit (if required in config)
- Make sure plugin is enabled in config.yml

### No Messages Appearing
- Check if you've toggled messages off with `/coppermsg`
- Verify you're within 16 blocks of oxidizing copper
- Check message configuration isn't empty

### Console Spam
- Set `debug: false` in config.yml to disable console messages

## 📦 Building from Source

```bash
# Clone the repository
git clone https://github.com/yourusername/copper-plus.git
cd copper-plus

# Build the plugin
./gradlew build

# If Paper repository is down, use offline mode:
./gradlew build --offline

# Output JAR will be in build/libs/
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Credits

- **Developer:** Timiliris_420
- **Plugin Prefix:** CopperPlus
- **Version:** 1.0

## 📚 Documentation

For detailed documentation and features, see [PLUGIN_DESCRIPTION.md](PLUGIN_DESCRIPTION.md)

---

*Making copper oxidation fun and fast!* ⚡