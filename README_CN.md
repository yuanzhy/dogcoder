# DogCoder - IntelliJ 插件

代码工具，提供代码片段的自动提示补全和示例代码的创建

1. 代码提示补全：自动提示常用的代码片段，如RSA加解密、HTTP请求等
2. 示例代码创建：项目视图上右键菜单提供了一个菜单“New DogCoder”用来创建示例代码，例如设计模式代码

代码片段和示例代码来自服务器，它可以随时添加新的示例代码

## 安装

支持以下产品，版本 > 2019.1

- IntelliJ IDEA
- IntelliJ IDEA Community Edition
- Android Studio
- GoLand
- PyCharm

**使用IDE插件系统**

- File > Settings > Plugins > Marketplace > 搜索"dogcoder" > Install

## 使用说明

- 代码提示补全
  ![codeCompletion](https://raw.githubusercontent.com/yuanzhy/dogcoder/master/usage/codeCompletion.gif)

- 示例代码创建
  ![new](https://raw.githubusercontent.com/yuanzhy/dogcoder/master/usage/new.gif)

## 维护者

[@yuanzhy](https://github.com/yuanzhy)

## 如何贡献

- 依赖 JDK11+, Intellij IDEA 2021+
- 导入项目到 IntelliJ
- 运行gradle任务runIde

## 使用许可

[MIT](LICENSE) © yuanzhy