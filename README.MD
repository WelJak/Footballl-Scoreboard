# README

## Introduction
This library provides Live Football World Cup Score Board.

## Pre requisites
* Java installed
* Maven installed
* Gradle installed (optionally)

## Usage
To import as library:
* create new directory for lib e.g. ```mkdir scoreboard```
* clone repository with ```git clone https://github.com/WelJak/Footballl-Scoreboard.git``` in newly created directory
* cd to dir with library and build code with gradle wrapper included in lib with command ```./gradlew clean build```
* publish artifacts to your local Maven Repository with ```./gradlew publishToMavenLocal```
* in your project:
  * if using Gradle -> add ```mavenLocal()``` to repositories declaration then in dependencies section add ```implementation 'com.weljak:scoreboard:1.0'```
  * if using Maven -> add in your dependencies section following code:
  ``` xml<dependency>
    <groupId>com.weljak</groupId>
    <artifactId>scoreboard</artifactId>
    <version>1.0</version>
    </dependency>
  ```

