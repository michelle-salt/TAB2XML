# TAB2XML

<p align="center"><img src="https://github.com/michelle-salt/TAB2XML/blob/master/src/main/resources/image_assets/loading_page_background.jpg" alt="TAB2XML"></p>

TAB2XML takes text-based tablature to play, preview and save sheet music.

This application is for anyone who wants to:
- play a given song for guitar, bass or drums.
- preview ASCII tabs as sheet music.
- make reading and playing music more convenient.

## Getting Started

### Prerequisites
This application works on all major operating systems and assumes that you have a recent version of Java 17, or at least Java 11 installed.

### 3 How to Install TAB2XML

#### 3.1 Eclipse
JDK 17.0.2 and Eclipse 2021-12 (4.22.0) were used for the IDE screenshots.

1. Open Eclipse, and click on import projects.
2. In the new window that opens, select Git > Projects from Git. Then click “Next”.
3. Click “Clone URI” then click “Next”.
4. In the “URI” field, enter "https://github.com/michelle-salt/TAB2XML.git".
5. Choose the location you want the program to be stored on your computer then click “Next”.Choose the location you want the program to be stored on your computer then click “Next”.
6. Set the Gradle version to 7.3.3 and the Java home path to your JDK directory. To find the Gradle preferences, click Preferences > Gradle. Select “Specific Gradle version” then 7.3.3.
7. Refresh the Gradle project. If you don’t know how, right-click the project then press Gradle > Refresh Gradle Project.
8. Display the Gradle Tasks window. If you can't find it, press Window > Show View > Other > Gradle > Gradle Tasks, then click "Open". 
9. Press the “build” folder and double-click the green “build” to run Gradle build.

#### 3.2 Command Line Interface

1. Clone the repository to the directory of your choice. To create a new directory use mkdir <directory>. Then use git clone https://github.com/michelle-salt/TAB2XML
2. Change directory to where the project was installed then use ./gradlew build to build the project.
```
  Warning: If permission to build was denied, make sure the ./gradlew is an executable. To change the file permission access of the ./gradlew file use chmod +x ./gradlew	
```

### 4 How to Use TAB2XML
  
#### 4.1 Insert Tablature

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
```
  Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
```
#### 4.2 Convert Tablature to Sheet Music

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
  ```
  Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
  ```
3. Press the “Preview Sheet Music” button. A new window will pop-up with the corresponding Sheet Music.

#### 4.3 Save Sheet Music

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
```
  Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
  ```
3. Press the “Preview Sheet Music” button. A new window will pop-up with the corresponding Sheet Music.
4. Press the “Save as PDF” button at the bottom right of the pop-up to save the Sheet Music as a PDF to your device! (This feature will be implemented at a later time).
  
#### 4.4 Go-to Measure

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
```
  Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
  ```
3. Press the “Preview Sheet Music” button. A new window will pop-up with the corresponding Sheet Music.
4. At the bottom left corner, in the text box beside “Go To Measure”, enter the measure number and click “Go” to navigate to the bar that corresponds to the inputted measure. (This feature will be implemented at a later time.)
  
#### 4.5 Play

1. To run the application in Eclipse, double-click the green “run” item in the application folder. To run the application directly from the command line, use the command ./gradlew run in the program directory.
2. Input your text tab into the application. You can type or copy-paste your text tab into the text box.
```
  Warning: If your input tab has errors, there will be yellow highlights on the text that contains the error.
  ```
3. Make sure your device volume is not muted. Click the “Play Tablature” button on the bottom right corner. You will be able to hear the inputted tablature being played.


[![CC BY-SA 4.0][cc-by-sa-shield]][cc-by-sa]

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg
