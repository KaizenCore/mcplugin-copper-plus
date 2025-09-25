# CopperPlus - Advanced Copper Oxidation Plugin

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-brightgreen.svg)](https://www.minecraft.net/)
[![Paper API](https://img.shields.io/badge/Paper%20API-1.21-blue.svg)](https://papermc.io/)
[![Languages](https://img.shields.io/badge/Languages-11-orange.svg)](#supported-languages)

## 🎮 Overview

CopperPlus is a quality-of-life plugin for Minecraft 1.21+ that revolutionizes copper oxidation mechanics. Transform the slow, natural oxidation process into a rapid, controlled system using campfires and water - perfect for builders and redstone engineers who need oxidized copper quickly!

## ✨ Key Features

### ⚡ Accelerated Oxidation System
- **Rapid Oxidation**: Place copper blocks above lit campfires with water nearby to speed up oxidation dramatically
- **Smart Detection**: Automatically detects valid setups and begins the oxidation process
- **Water Speed Multiplier**: More water blocks = faster oxidation (configurable bonus per water block)
- **Visual Feedback**: Particle effects and sound notifications during oxidation
- **Progress Tracking**: Real-time oxidation progress messages

### 🌍 Multi-Language Support (11 Languages!)
- **English** (en_US) - Default
- **Chinese Simplified** (zh_CN) - 简体中文
- **Spanish** (es_ES) - Español
- **Hindi** (hi_IN) - हिन्दी
- **Arabic** (ar_SA) - العربية
- **Portuguese** (pt_BR) - Português
- **Russian** (ru_RU) - Русский
- **Japanese** (ja_JP) - 日本語
- **German** (de_DE) - Deutsch
- **French** (fr_FR) - Français
- **Korean** (ko_KR) - 한국어

### 🛠️ Additional Features
- **Starter Kit**: Quick-start kit with campfire, copper blocks, water bucket, and flint & steel
- **Dispenser Support**: Dispensers can place copper blocks that automatically start oxidizing
- **Message Toggle**: Players can enable/disable oxidation notifications
- **Smart Chunk Management**: Efficient processing with configurable limits per chunk
- **Fully Configurable**: Extensive configuration options for all features

## 📋 Commands

All commands use the main `/copperplus` command (aliases: `/cp`, `/copper+`):

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

## 🔧 How It Works

### Basic Setup
1. Place a **Campfire** (regular or soul campfire)
2. Place a **Copper Block** directly above the campfire
3. Place **Water** blocks nearby (within 1 block radius)
4. The campfire must be **lit** for oxidation to occur
5. Watch as your copper oxidizes in seconds instead of hours!

### Oxidation Speed Formula
- **Base Speed**: Configurable duration (default: 60 seconds for full oxidation)
- **Water Bonus**: Each water block adds a speed multiplier
- **Maximum**: Up to 26 water blocks can affect oxidation speed
- **Formula**: `Speed = 1.0 + (water_blocks × water_speed_bonus)`

### Oxidation Stages
The plugin supports all vanilla copper oxidation stages:
- Copper Block → Exposed Copper
- Exposed Copper → Weathered Copper
- Weathered Copper → Oxidized Copper

Works with all copper variants: blocks, stairs, slabs, and cut copper!

## ⚙️ Configuration

### Key Configuration Options

```yaml
general:
  enabled: true                    # Enable/disable the plugin
  debug: false                      # Debug mode for troubleshooting

oxidation:
  duration-seconds: 60              # Base time for one oxidation stage
  water-speed-bonus: 0.25           # Speed multiplier per water block
  max-water-blocks: 26              # Maximum water blocks counted
  require-lit-campfire: true        # Campfire must be lit
  water-check-radius: 1             # Radius to check for water

effects:
  particles: true                   # Enable particle effects
  particle-type: "WAX_ON"          # Particle type to use
  sounds: true                      # Enable sound effects

performance:
  check-interval: 20                # Ticks between oxidation checks
  max-per-chunk: 50                 # Max oxidizing blocks per chunk

kit:
  items:
    campfire: 4                     # Number of campfires in kit
    copper-block: 64                # Number of copper blocks
    water-bucket: 2                 # Number of water buckets
    flint-and-steel: 1              # Flint and steel for lighting
```

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `copperplus.use` | Access to basic commands | All players |
| `copperplus.kit` | Use the `/copperplus kit` command | All players |
| `copperplus.admin` | Admin commands (reload, set default language) | Operators |

## 📦 Installation

1. **Requirements**:
   - Paper server 1.21 or higher
   - Java 21 or higher

2. **Installation Steps**:
   - Download the latest `copper-plus-1.0.jar` from releases
   - Place the JAR file in your server's `plugins` folder
   - Restart your server
   - Configure settings in `plugins/CopperPlus/config.yml`
   - (Optional) Customize language files in `plugins/CopperPlus/languages/`

## 🎯 Use Cases

### For Builders
- Quickly obtain oxidized copper for builds without waiting hours
- Create gradient effects with different oxidation stages
- Mass-produce weathered copper for roofing and decoration

### For Redstone Engineers
- Rapidly test copper bulb oxidation states
- Create controlled oxidation farms
- Experiment with different oxidation patterns

### For Server Admins
- Provide quality-of-life improvement for players
- Reduce server load compared to tick acceleration methods
- Full control over oxidation speed and mechanics

## 🌟 Unique Features

### Why CopperPlus?

1. **Realistic Mechanism**: Uses campfire heat and water (like real oxidation chemistry!)
2. **Performance Optimized**: Efficient chunk-based processing
3. **Fully Integrated**: Works with dispensers, all copper types, and vanilla mechanics
4. **Player Choice**: Optional notifications, per-player language preferences
5. **Admin Friendly**: Extensive configuration and reload without restart

## 🔄 Update Roadmap

### Planned Features
- [ ] Custom oxidation patterns and templates
- [ ] Waxing automation system
- [ ] Oxidation reversal with lightning integration
- [ ] GUI for language and settings management
- [ ] PlaceholderAPI support
- [ ] Economy integration for kit purchases
- [ ] Custom crafting recipes for oxidized variants

## 📊 Performance

- **Lightweight**: Minimal server impact with smart check intervals
- **Scalable**: Configurable limits prevent performance issues
- **Efficient**: Only processes active oxidation setups
- **Clean**: Automatic cleanup when chunks unload

## 🐛 Troubleshooting

### Common Issues

**Oxidation not starting:**
- Ensure campfire is lit (not just placed)
- Check water is within 1 block radius
- Verify copper block is directly above campfire
- Make sure the plugin is enabled in config

**Messages not showing:**
- Check if you've toggled messages off with `/copperplus togglemsg`
- Verify your language file exists and is valid
- Ensure you're within 16 blocks of oxidizing copper

**Performance issues:**
- Reduce `max-per-chunk` in config
- Increase `check-interval` for less frequent updates
- Enable `clear-on-unload` for better memory management

## 💬 Support

- **Bug Reports**: Create an issue on our GitHub repository
- **Feature Requests**: Suggest features through GitHub discussions
- **Discord**: Join our community server for live support
- **Wiki**: Detailed documentation available on GitHub Wiki

## 📜 License

This plugin is released under the **MIT License**. You are free to:
- Use the plugin on any server (commercial or non-commercial)
- Modify the source code for your needs
- Distribute the plugin
- Include it in modpacks or server packs

The only requirement is to include the original copyright notice and license.

## 👨‍💻 Author

**Timiliris_420** - Lead Developer

## 🙏 Credits

- Paper team for the excellent API
- Minecraft community for inspiration
- All contributors and testers

---

*CopperPlus - Making copper oxidation fun and fast!* 🎮⚡