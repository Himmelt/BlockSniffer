# Block Sniffer

### Description
This is a Scenter-like mod. It can show you a way to the target block(s).

### Features
1. Optimize the scanning rule,by default,it will skip air block.
2. Use command to add/remove target block(s) or change work mode.
3. No need to find the name(id) of the block,you can add the block in-hand or looking at (with meta).
4. Multi thread to scan the world,no lag.
5. Customizable work mode/range.
6. Supports several formats of colors. Hex Value(#abcdef),Minecraft Color(&5/&e),Web Color(blue,magenta),MapColor.
7. Add Gamma settings command.
8. Full command tabCompletions support.

### Attentions
1. This mod only can be used in client side.
2. Java: JDK 1.8  Language Level: 8
3. Don't use this mod in multi-player servers to cheat !

### Commands

|command|function|
|---|---|
|`/sniffer`|main command, simplify `/sf`|
|`/sf g/gamma [0-15]`|get/set Gamma|
|`/sf reload`|reload config|
|`/sf reset`|reset target|
|`/sf setblock look│{<x> <y> <z>} <id│name>`|set block (client side)|
|||
|`/sf t/target i/info`|show information of current target|
|`/sf t/target m/mode [{0│1}]`|get/set work mode|
|`/sf t/target h/hrange [{0-15}]`|get/set horizon range(chunk)|
|`/sf t/target v/vrange [{0-255}]`|get/set vertical range(block)|
|`/sf t/target d/depth [{0-255}] [{0-255}]`|get/set vertical depth range(0-255)|
|`/sf t/target c/color [{#abcdef}│{&e}│{blue}│map]`|get/set particle color|
|`/sf t/target rm/remove`|remove current target|
|`/sf t/target cla/clear`|clear all targets|
|`/sf t/target add <{name│id}│hold│look> [meta] [{0-15}]`|add a new target|
|`/sf t/target add {name│id}`|add the block named `name` or id=`id` as a new target(ignore meta)|
|`/sf t/target add hold meta`|add the held block as a new target,using it's meta|
|`/sf t/target add look meta {0-15}`|add the looking block as a new target with the specific meta(0-15)|
|`/sf t/target skip`|skip current target block which can not be harvested.(client set block to air)|
|||
|`/sf sub l/list`|show the sub-blocks of current target|
|`/sf sub add <{name│id}│hold│look> [meta] [{0-15}]`|add sub block to current target|
|`/sf sub rm/remove <{uid}>`|remove sub block with the uid `uid`|

### Default values
|option|default value|
|---|---|
|work mode|`0` fixed range, using`depth`|
|horizon range|`1`chunk|
|vertical range|`16`block|
|depth range (y)|`0`level-`64`level|
|particle color|using `MapColor`|
|default target|`minecraft:diamond_ore/0`,meta`0`|

---

# 方块探测 (Block Sniffer)

### 简介
此Mod是 矿物追踪 (Scenter) 的改进增强版, 主要功能也是探测方块.

### 特性
1. 优化扫描规则, 默认跳过空气方块, 加快探测速度.
2. 可通过指令热添加/删除探测目标, 并设置工作模式和参数.
3. 不必再查方块的注册名, 可直接添加手持方块或光标所在的方块.
4. 子线程探测, 大范围探测时不会导致游戏进程卡顿.
5. 高度自定义探测范围、探测模式, 水平最大探测半径15区块.
6. 支持多种颜色格式, 十六进制(#abcdef)、Minecraft颜色(&5、&e)、Web标准色(blue、magenta)、地图色(MapColor).
7. 添加Gamma设置指令.
8. 完全支持的命令补全.

### 注意
1. 该Mod只能用于客户端, 不会影响其他与服务端匹配的Mod；~~如果将本Mod用于服务端可能导致崩溃~~.
2. 编译环境为JDK 1.8 语言级别 8 ，所以java7及以下可能无法正常运行.如有需要可自行修改并编译源码.
3. 请不要在多人联机时使用本Mod作弊.

### 指令
|符号|意义|
|---|---|
|`x/xx`|相同的指令|
|`x│xx`|选择其中一个|
|`xxx`|指令(没有`{}`修饰表示该字段是一个指令, 需要照写)|
|`<xxx>`|必填项|
|`[xxx]`|选填项|
|`{xxx}`|参数|
|`x-y`|范围|
|`[{x│y}]`|选填其中一个参数|
|`<{x│y}>`|必填其中一个参数|
|`[{x-y}]`|选填范围内参数|

|指令|功能|
|---|---|
|`/sniffer`|总指令, 简写`/sf`|
|`/sf g/gamma [0-15]`|查看/设置 Gamma 值|
|`/sf reload`|重载配置|
|`/sf reset`|复位探测器|
|`/sf setblock look│{<x> <y> <z>} <id│name>`|设置目标位置为指定方块(客户端)|
|||
|`/sf t/target i/info`|查看当前探测目标信息|
|`/sf t/target m/mode [{0│1}]`|查看/设置探测模式|
|`/sf t/target h/hrange [{0-15}]`|查看/设置水平探测半径(区块)|
|`/sf t/target v/vrange [{0-255}]`|查看/设置垂直探测半径(方块)|
|`/sf t/target d/depth [{0-255}] [{0-255}]`|查看/设置探测深度范围|
|`/sf t/target c/color [{#abcdef}│{&e}│{blue}│map]`|查看/设置粒子颜色|
|`/sf t/target rm/remove`|移除当前探测目标|
|`/sf t/target cla/clear`|清除所有探测目标|
|`/sf t/target add <{name│id}│hold│look> [meta] [{0-15}]`|添加探测目标|
|`/sf t/target add {name│id}`|添加名为`name`或id为`id`的方块到新的探测目标, 将无视元数据|
|`/sf t/target add hold meta`|添加手持方块到新的探测目标, 并使用当前元数据|
|`/sf t/target add look meta {0-15}`|添加光标处方块到新的探测目标, 并使用指定的元数据|
|`/sf t/target skip`|跳过当前探测到得目标位置,以跳过无法挖掘的方块(客户端设置为空气)|
|||
|`/sf sub l/list`|显示当前目标包含的子目标|
|`/sf sub add <{name│id}│hold│look> [meta] [{0-15}]`|添加子目标|
|`/sf sub rm/remove <{uid}>`|移除当前目标下id为`uid`的子目标|

### 默认值
|内容|默认值|
|---|---|
|探测模式|`0`固定深度探测, 使用`depth`|
|水平探测半径|`1`区块|
|垂直探测半径|`16`方块|
|探测深度范围|`0`层-`64`层|
|粒子颜色|使用`MapColor`|
|默认探测目标|钻石原矿,元数据`0`|
