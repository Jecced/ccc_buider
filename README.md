# CocosCreator 1.x版本自动编译器

## 描述

- 我们公司的项目使用的是 `CocosCreator 1.10.2`开发原生游戏
- 项目使用语言：`TypeScript`

- 项目非常的庞大, 文件内容非常多

- 每次修改一个`ts`文件, 哪怕只有一行, 都需要会需要`creator`编译接近一分钟的时间才会在浏览器生效

---



## 测试

#### 环境

- OSX 10.14.6
- CocosCreator 1.10.2
- Java 1.8
- TypeScript 3.4.5

#### 脚本类型

- 没有注解的 `ts `脚本
- 有 `@property `注解的 `ts` 脚本

#### 暂未支持

- `windows` 环境尚未测试, 未测试`目录系统`处理和`换行符`是否会出问题
- `js` 脚本尚未监听

---



## 功能

- 自动监听项目目录下的 `assets` 下的所有已有的  `ts`  脚本
- 当脚本有变动会自动更新编译为 `js` 脚本
- 编译完成后会自动覆盖浏览器所使用的 `js` 脚本
- 并不需要返回 `cocos` 客户端等待全量编译刷新
- 直接返回浏览器刷新即可预览到新变动的效果

---

## 效果预览图
![](https://raw.githubusercontent.com/Jecced/ccc_buider/master/image/demo_preview.gif)

![](https://raw.githubusercontent.com/Jecced/ccc_buider/master/image/demo_preview_small.gif)



## 依赖

- TypeScript 环境

```shell
tsc -v
Version 3.4.5
```

- Java 环境

```shell
java -version
java version "1.8.0_201"
Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
```

如果缺少以上环境需要安装 `TypeScript` 和 `Java`

---



## 怎么使用 How to use

```bash
java -jar ccc_builder_xxx.jar <project_path>
```

或者

```bash
java com.againfly.cccbuilder.Main <project_path>
```

参数解释:

- project_path 为cocos项目的根目录, 可以不填该参数, 不填入参数时, 启动会提示询问你的项目目录位置在哪

例如

```bash
java -jar ccc_builder_0.0.4.jar /Users/ankang/git/saisheng/slgrpg 
```



目前脚本支持命令:

- `update` or `u` 通知cocos进行脚本进行全量编译
-  `flash`  or `f` 刷新所有监听的脚本

例如:

```bash
>> java -jar ccc_builder_0.0.4.jar
<< start cmd listener
<< 请输入路径项目路径:
>> /Users/xxx/git/xxx/xxx 
<< listener file flush success, file count:789
<< init success
<< start file listener
>> flush
<< listener file flush success, file count:789
```

- 运行jar包 `java -jar ccc_builder_0.0.4.jar`
- 输入项目路径 `/Users/xxx/git/xxx/xxx`
- 手动刷新监听目录 `flush`



正常运行时: 

```bash
>> java -jar ccc_builder_0.0.4.jar /Users/ankang/git/saisheng/slgrpg 
<< start cmd listener
<< listener file flush success, file count:789
<< init success
<< start file listener
<< /Users/ankang/git/saisheng/slgrpg/assets/script/feature/battle/BattleCtl.ts, file update
<< BattleCtl->编译完成, 耗时:1636ms
```

当监听到ts脚本被保存时, 会打印出相应文件 update

当顺利编译完成时, 会打印 `${文件名}->编译完成, 耗时: ${time}ms`



## TODO

- 加入 `js` 脚本的变动监听, 目前项目开发没有使用`js`, 暂不支持 `js` 脚本的处理
- 自动监听新脚本的处理
- 自动生成新脚本的meta, 脱离新脚本需要 `cocos` 全量更新的卡顿
- windows 环境尚未测试
- 没有进行 `releases` 打包
- 目前项目使用 `java` 语言进行编写, 后期可能会更改编写语言
- ~~目前没有编写使用文档 `how to ues`~~

---



## 更新

### 2020-04-15

- 重新整理项目
- 支持拥有`@property`注解的脚本编译
- 重新加入`http`请求模块
- 命令行加入`u`和`update` 支持直接通知cocos客户端全量编译
- 将文件变动监听和命令行监听改为相互独立的线程



### 2020-04-14

- 初始项目建立
- 支持非注解脚本的自动编译



### 2020-04-20

- 加入依赖更新功能

  需要在 CocosCreator客户端中的模板预览文件 `index.jade`中加入一行:

  `script(type='text/javascript' charset='utf-8' src='http://localhost:8059/deps.js')`

  动态编译出来的脚本依赖信息由工具动态生成, 避免造成动态生成的脚本, 缺少依赖问题



---



## 说明

如果有任何问题, 可以发起 `Issues` 问题

如果这个项目对你产生帮助, 帮我点亮一颗小星星✨



---



## LICENSE

    MIT License
    
    Copyright (c) 2020 Jecced
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
