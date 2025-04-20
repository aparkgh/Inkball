# Inkball
A recreation of the classic Inkball game by Microsoft in Java, developed using the Gradle build system and the Processing library.

## How to run
* Install **Gradle 8.10.2** 🐘 or higher
* Open the project using any **Gradle-compatible IDE** (e.g. VSC, IntelliJ IDEA, Eclipse).
* Run the project by executing the following command in the terminal:
```
gradle run
```

### Screenshots
Level 1 | Paused | Level Won
:-:|:-:|:-:
<img src="https://github.com/user-attachments/assets/c5856684-4db4-46ac-864a-149ddae47e9e" width="320"/> | <img src="https://github.com/user-attachments/assets/adafa839-7eeb-462c-903c-36be8ee342ea" width="320"/> | <img src="https://github.com/user-attachments/assets/e809a6fd-892a-41c9-bdfb-87a3d9139d09" width="320"/>

## What is Inkball? And how do I play?
**Inkball** is a classic puzzle game originally developed by Microsoft, where players draw lines to guide balls into designated holes.

Balls **spawn** periodically from spawner tiles throughout the level. The goal is to **direct** colored balls into matching holes by strategically creating barriers and pathways with ink.

Score will only increase[^1] if the colour of a ball **matches** the colour of the hole[^2]. If a ball enters a **mismatched** coloured hole, score is deducted[^1], and the ball will be added back to the list of balls to be spawned for that level.

You can use **Left Click** to draw lines to guide the balls, **Right Click** or **Control + Left Click** to get rid of unwanted lines, **Space** to pause and **R** to restart any level at any time.

## What is Gradle?
**Gradle** is a powerful build automation tool used for managing dependencies, compiling code, and running tasks in software projects. It is particularly popular in Java development for automating the build process, handling large-scale projects efficiently, and integrating with IDEs. Gradle allows developers to write flexible build scripts and use plugins to simplify tasks like testing, packaging, and running applications. Its ability to handle complex project structures and its performance optimizations make it a go-to tool for modern development workflows.

[^1]: Based on a score integer multiplied by a score modifier which is individual to each level
[^2]: Provided that the ball or hole isn't grey; grey balls can enter any coloured holes and grey holes accept any coloured balls.
