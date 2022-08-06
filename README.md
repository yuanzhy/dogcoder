# DogCoder - IntelliJ plugin

A code tool, Providers Code snippet auto completion and sample code creation

1. code completion: Automatically prompt frequently-used code snippets, such as RSA encryption and decryption, HTTP request, etc.
2. Create sample codes: The context menu on project view provides an action group to create new sample code, such as design pattern example code

The code snippet and sample code are from the server, It can add new sample codes at any time

## Install

support following product build version > 2019.1

- IntelliJ IDEA
- IntelliJ IDEA Community Edition
- Android Studio
- GoLand
- PyCharm

**using IDE plugin system**

- File > Settings > Plugins > Marketplace > find"dogcoder" > Install

## Usage

- code completion
  ![codeCompletion](https://raw.githubusercontent.com/yuanzhy/dogcoder/master/usage/codeCompletion.gif)

- sample code creation
  ![new](https://raw.githubusercontent.com/yuanzhy/dogcoder/master/usage/new.gif)

## Maintainers

[@yuanzhy](https://github.com/yuanzhy)

## Contributing

- required JDK11+, Intellij IDEA 2021+
- import the project to IntelliJ
- add a gradle task runIde
- change the code and run the task to see the change

## License

[MIT](LICENSE) © yuanzhy