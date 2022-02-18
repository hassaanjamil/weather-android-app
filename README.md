# Articles Sample App

## Overview

The sample is based on technology assessment requirement of a company, in which, I have implemented a list of most popular news articles using:

- MVVM (Repository Pattern)
- Navigation Component
- Dagger-hilt (for dependency injection)
- Navigation Drawer component
- Retrofit, Secrets Gradle Plugin
- Unit test / Instrumented Test with converage (using Android Studio built-in feature)

## Setup

Simply clone or download the zip project and open it to Android Studio IDE.

I have developed it using Android Studio Bumblebee (2021.1.1)

## Project Structure

Project structure is generic and simple followed MVVM Repository Pattern guidelines. The package name or ApplicationId of the application is 'com.nytimes.app'

In this base package, there are:

- **data package**

  Data package holds the implementation/classes related to remote data. In which I have setup retrofit library to communicate with the Articles API.

- **di package** (Dependency Injection Package)

  The package holds the Dagger Hilt Modules in which provides have been defined for retrofit and adapter dependencies which we needs to inject to our views.

- **ui package**

  The package holds our injected view classes and supporting classes like adapters, view models etc.

- **utils package**

  The package holds utility classes/models e.g. kotlin extensions, to support additional and reusable functions to our business logic.

## Unit Tests and coverage

I have wrote basic unit tests (exists in androidTest/test packages under src package/directory) which is convering stats below:
Package: all classes

| Classes %     | Methods %      | Lines %         |
| ------------- | -------------- | --------------- |
| 27.8% (10/36) | 33.3% (42/126) | 	38.8% (83/214) |

I have used Android Studio's built-in coverage feature to generate the report which you can also generate using the method mentioned below:

1. The very first thing you need to do is to run all the unit tests combine or at once for what you need to define a run configuration for it you can follow the guidelines mentioned [here](https://stackoverflow.com/a/69453681/3459944), You can also run a test file by right clicking it and run the tests, you will get results in console.
2. After selecting the configuration you can hit the Android Studio option "Run with Converage", very right to Run and Debug options.
3. After hitting it, all our unit test will be executed by gradle and will open in a Coverage pane in which you can see the stats of our report, you can also generate this report using "Generate converage report" option of this pane.

Good Luck!