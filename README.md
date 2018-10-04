# JLLAP

## Introduction

This project is an implementation of JLLAP (Java Log Lint and Analysis Plugin) for failure cause localization through standardizing logging practices and analyzing log files with the aid of goal model.
JLLAP guides developers to insert informative logging statements in a convenient manner, and when software system fails, JLLAP could reduce the overhead of locating the cause of the failure.

## Main features

### Log Lint

JLLAP provides on-the-fly check for missing logging statements based on the built-in rules and goal model.

### Log Analysis

JLLAP is able to convert log files into a tree-structured goal model with status for further failure cause localization.

## Getting Started

If you would not like to build this plugin by yourself, you can get the package from the [release page](https://github.com/NJU-LogHelper/NJU-LogHelper/releases).

If you want to build it yourself, you have to make sure the following build environment:

- JDK 1.8 +
- Intellij IDEA 172.4343.14
- Python 2.7
