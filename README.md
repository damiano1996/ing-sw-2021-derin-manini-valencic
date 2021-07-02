# Software Engineering - Course Project

# Table of Contents

- [Team Members](#team-members)
- [Implemented Functionalities](#implemented-functionalities)
- [Requirements](#requirements)
- [Usage](#usage)
    - [Step 1: Download the JAR files](#usage-step-1)
    - [Step 2: Running the Server](#usage-step-2)
    - [Step 3: Running the Client](#usage-step-3)
        - [CLI Version](#cli-version)
        - [GUI Version](#gui-version)

<a name="team-members"></a>

## Team Members

In alphabetic order:

| Surname   | Name      | Contact Info                       |
|:----------|:----------|:-----------------------------------|
| Derin     | Damiano   | damiano.derin@mail.polimi.it       |
| Manini    | Andrea    | andrea3.manini@mail.polimi.it      |
| Valencic  | Jas       | jas.valencic@mail.polimi.it        |

<a name="implemented-functionalities"></a>

## Implemented Functionalities

| Requirements                               |
|:-------------------------------------------|
| Complete rules + CLI + GUI + Socket + 3 FA |

| n° | Advanced Functionality        | Short Explanation                                                                                         |
|:---|:------------------------------|:--------------------------------------------------------------------------------------------------------- |
| 1  | Multiple Matches              | The Server can handle more than one match simultaneously.                                                 |
| 2  | Persistence                   | If the Server shuts down, you will be able to restore matches that have been interrupted this way.        |
| 3  | Resilience to Disconnections  | If you lose the connection while playing, you'll be able to restore the match that have been interrupted. |

<a name="requirements"></a>

## Requirements

- java >= 11
- junit == 4.11
- com.google.code.gson == 2.8.5
- org.openjfx (javafx-controls) == 15.0.1
- org.openjfx (javafx-fxml) == 11.0.2
- org.openjfx (javafx-media) == 11

<a name="usage"></a>

## Usage

<a name="usage-step-1"></a>

### Step 1: Download JAR files

Since JARs weight more than 100 MB, they have been uploaded on Dropbox (as permitted by Tutors on Slack).  
You can download them at the following link:  
TODO: INSERIRE LINK DROPBOX FUNZIONANTE CON I JAR

Once downloaded, save them in your favourite directory.

<a name="usage-step-2"></a>

### Step 2: Running the Server

- The ```server.jar``` can be executed by double click or with the following command in the terminal.

  We suggest executing it by the terminal to stop it easily, otherwise by double click it will run in background and to
  kill the application you should use the task manager.

- Browse with the terminal in the directory where the ```server.jar``` is saved. Then run:

  ```bash
    $ java -jar server.jar
  ```

  At this point the ```server.jar``` should be running.
- To turn off the server, enter the CTRL+C key combination in the terminal.

<div style="background: rgba(0, 0, 0, 0.5); border-radius: 10px; padding: 10px">
<h6 style="color: #eca400">WARNING!</h6>
<p>
Do not run more than one server at a time on the same machine:
if you do this, the same socket port will be used by all the servers causing problems.
</p>
</div>

<a name="usage-step-3"></a>

### Running the Client

<a name="cli-version"></a>

#### CLI Version

<div style="background: rgba(0, 0, 0, 0.5); border-radius: 10px; padding: 10px">
<h6 style="color: #eca400">WARNING!</h6>
<p>
The CLI version of the game uses ANSI escape codes.<br>
Windows terminal can't render them properly:
in order to have a playable version of the CLI, a Unix-like terminal is required.<br>
If you are on Windows, you must use a WSL terminal to run the CLI JAR.<br>
A nice WSL terminal can be downloaded from the Windows store for free at the following
<a href="https://www.microsoft.com/store/productId/9NBLGGH4MSV6">link</a>.<br>
Lastly, if you use a WSL terminal to run the client.jar (CLI), you won't be able to see the PDF rules,
please use the GUI version instead.  
</p>
</div>

In order to run the ```client.jar``` in CLI mode follow these steps:

- Browse with the terminal in the directory where the ```client.jar``` is saved.

- To run the CLI version use this command:
  ```bash
    $ java -jar client.jar "-playingViewMode=cli"
  ```

  At this point the CLI version of the Client JAR should be running.

- To turn off the CLI, you can select the "exit" command in the initial menu of the game.  
  Another way to turn off the CLI is to enter the CTRL+C key combination in the terminal.

<a name="gui-version"></a>

#### GUI Version

In order to run the GUI version of the ```client.jar```, simply double-click on the icon of the ```client.jar```.  
At this point the GUI version of the Client JAR should be running.

To turn the GUI off, you can use the "exit" button in the menu.  
Another way to turn the GUI off is to press the "View Board" button while playing and then close the window as usual by
click on the top red X.

Another option is to stop the GUI by the task manager: use this method only if unable to perform the two above.
