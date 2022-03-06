# TAB2XML

<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/master/src/main/resources/image_assets/loading_page_background.jpg" alt="TAB2XML"></p>

TAB2XML takes text-based tablature to play, preview and save sheet music.

This application is for anyone who wants to:
- play the guitar, bass or drum.
- preview ASCII tabs as sheet music.
- make reading and playing music more convenient.

## Getting Started

### 1 Prerequisites
This application works on all major operating systems and assumes that you have a recent version of Java 17, or at least Java 11 installed.

### 2 How to Install TAB2XML

#### 2.1 Eclipse
JDK 17.0.2 and Eclipse 2021-12 (4.22.0) were used for the IDE screenshots.

1. Open Eclipse, and click on import projects.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.07.02%20AM.png" alt="1"></p>

2. In the new window that opens, select Git > Projects from Git. Then click “Next”.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.07.20%20AM.png" alt="2"></p>

3. Click “Clone URI” then click “Next”.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.07.39%20AM.png" alt="3"></p>

4. In the “URI” field, enter "https://github.com/michelle-salt/TAB2XML.git" then click "Next".
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.07.49%20AM.png" alt="5"></p>

5. Choose the location you want the program to be stored on your computer then click “Next”.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.07.59%20AM.png" alt="6"></p>

6. Set the Gradle version to 7.3.3 and the Java home path to your JDK directory. To find the Gradle preferences, click Preferences > Gradle. Select “Specific Gradle version” then 7.3.3.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.08.15%20AM.png" alt="7"></p>

7. Refresh the Gradle project. If you don’t know how, right-click the project then press Gradle > Refresh Gradle Project.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.08.26%20AM.png" alt="8"></p>

8. Display the Gradle Tasks window. If you can't find it, press Window > Show View > Other > Gradle > Gradle Tasks, then click "Open".
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.08.40%20AM.png" alt="4"></p>

9. Press the “build” folder and double-click the green “build” to run Gradle build.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.08.54%20AM.png" alt="9"></p>


#### 2.2 Command Line Interface

1. Clone the repository to the directory of your choice. 
2. Create a new directory and clone the repository.
```
mkdir <filename>
git clone https://github.com/michelle-salt/TAB2XML
```
3. Change directory to where the project was installed then use ./gradlew build to build the project.

```
Warning: If permission to build was denied, make sure the ./gradlew is an executable. To change the file permission access of the ./gradlew file use chmod +x ./gradlew	
```

### 3 How to Use TAB2XML
  
#### 3.1 Insert Tablature

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.30.10%20AM.png" alt="10"></p>

2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.30.34%20AM.png" alt="11"></p>

```
Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
```

#### 3.2 Convert Tablature to Sheet Music

1. Input your text tablature into the application.
2. Press the “Preview Sheet Music” button. A new window will pop-up with the corresponding Sheet Music.
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.52.25%20AM.png" alt="12"></p>

#### 3.3 Save Sheet Music

1. Press the "Preview Sheet Music" button and a new window will open.
2. Press the “Save PDF” button at the bottom right of the pop-up to save the Sheet Music as a PDF to your device! (This feature will be implemented at a later time).
<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/mohammad/src/main/resources/image_assets/Screen%20Shot%202022-03-06%20at%2010.54.53%20AM.png" alt="13"></p>
  
#### 3.4 Go-to Measure

1. In the “Go To Measure” field enter a page number and then click "Go" to navigate to the corresponding page (This feature is partly implemented).

#### 3.5 Play

1. Click “Play Tablature” to play a song.

### 4 Collaborators

- [@Duaaa29](https://github.com/Duaaa29)
- [@jhaniff](https://github.com/jhaniff)
- [@hpaurobally](https://github.com/hpaurobally)
- [@maiv8964](https://github.com/maiv8964)
- [@michelle-salt](https://github.com/michelle-salt)

[![CC BY-SA 4.0][cc-by-sa-shield]][cc-by-sa]

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg
