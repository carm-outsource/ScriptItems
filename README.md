```text
  ___                              _ ___ _             
 / __|___ _ __  _ __  __ _ _ _  __| |_ _| |_ ___ _ __  
| (__/ _ \ '  \| '  \/ _` | ' \/ _` || ||  _/ -_) '  \ 
 \___\___/_|_|_|_|_|_\__,_|_||_\__,_|___|\__\___|_|_|_|                               
```

# CommandItem

[![workflow](https://github.com/CarmJos/CommandItem/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/CommandItem/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.12--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=CommandItem.readme)

物品指令绑定插件，给予玩家可执行对应指令的消耗物品，基于EasyPlugin实现。

## 插件功能与优势

> 加 * 的功能仍在开发中。

- 物品操作绑定，给予玩家可执行对应操作设置的物品，支持PlaceholderAPI变量。
  - 目前支持的操作有:
    - 控制台执行命令
    - 玩家聊天消息 (加/前缀即为以玩家身份执行命令)
    - 为玩家播放音效
    - 给玩家发送消息 (支持RGB颜色，格式为 `&(#颜色代码)` )
    - 拿取对应物品 (若不配置则物品保留可继续使用)
- **允许限定。** 允许给物品对应的指令组设定“总共可执行次数*”、“每日执行次数*”与“允许使用时间”限定。
- **\*详细记录。**  每个物品均有独立ID，并对使用的玩家与执行结果进行详细记录，便于追踪查询。
- **异步存取。** 数据读取与存储均为异步操作，不影响服务器性能。
- **轻量插件。** 适合小型服务器使用，配置简单方便。
- **规范开发。** 插件架构符合开发规范，适合新手开发者学习。

## 插件依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、 [BukkitAPI](http://bukkit.org/) 实现。
- **[自带]** 插件功能基于 [EasyPlugin](https://github.com/CarmJos/EasyPlugin) 实现。
- **[自带]** 数据功能基于 [EasySQL](https://github.com/CarmJos/EasySQL) 实现。
- **[推荐]** 消息变量基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/timereward/network/dependencies) 。

## 配置文件

### 插件配置文件 ([config.yml](src/main/resources/config.yml))

详见源文件。

### 消息配置文件 ([messages.yml](src/main/java/cc/carm/plugin/commanditem/configuration/PluginMessages.java))

详见代码源文件，将在首次启动时生成配置。

## 插件指令

插件主指令为 `/CommandItem`，所需权限为 `CommandItem.admin`。

```text
# help
- 查看插件帮助

# give <玩家> <物品ID> <数量>
- 给予指定玩家指定数量的物品。

# apply <物品ID>
- 为手中的物品直接绑定一个配置。

# details <物品ID>
- 查看物品的发放与领取情况。

```

## 使用统计

[![bStats](https://bstats.org/signatures/bukkit/CommandItem.svg)](https://bstats.org/plugin/bukkit/CommandItem/14560)

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。

<details>
<summary>关于 GPL 协议</summary>

> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>


