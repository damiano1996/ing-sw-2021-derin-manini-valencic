# Software Engineering - Course Project

## Team Members

| Surname   | Name      | Contact Info                       |
|:----------|:----------|:-----------------------------------|
| Derin     | Damiano   | damiano.derin@mail.polimi.it       |
| Manini    | Andrea    | andrea3.manini@mail.polimi.it      |
| Valencic  | Jas       | jas.valencic@mail.polimi.it        |

#Implemented functionalities
* Simple rules + CLI + Socket
* GUI
* FA: Multiple matches
* FA: Persistence
* FA: Resilience to disconnections

#Advanced functionalities: small explanation
###Multiple matches
The Server can handle more than one match simultaneously.

###Persistence
If the Server shuts down, you will be able to restore matches that have been interrupted this way.

###Resilience to disconnections
If you loose the connection while playing, you'll be able to restore the match that have been interrupted this way.

#Where to find JARs
Since the JARs weight more that 100MB, they have been uploaded in Dropbox (as permitted by Tutors on Slack).  
You can download them at the following link:  
INSERIRE LINK DROPBOX FUNZIONANTE CON I JAR  

Once downloaded, save them in your favourite directory.

#How to run JARs
##Running the Server
#####WARNING!!
Do not open more than one Server at a time on the same machine: if you do this, the same Socket port will be used by all the Servers and will lead to a crash of the Servers.  
.  

In order to run the Server JAR, a terminal is required.  
For example, you can use Windows terminal or Unix-like terminals.

In order to run the Server JAR follow these steps:

* Browse with the terminal in the directory where the server.jar file is saved
* To run the server.jar use this command: java -jar server.jar

At this point the Server JAR should be running.  
To turn the Server off, enter the ctrl+c key combination in the terminal.

##Running the Client: CLI version
####WARNING!!
The CLI version of the game uses ANSI escape codes.  
Windows terminal can't render them properly: in order to have a playable version of the CLI, a 
Unix-like terminal is required.  
If you are on Windows, you must use a WSL terminal to run the CLI JAR.  
A nice WSL terminal can be downloaded from the Windows store for free at the following link:
https://www.microsoft.com/store/productId/9NBLGGH4MSV6  
Lastly, if you use a WSL terminal to run the CLI JAR, you won't be able to see the PDF rules: use the GUI version instead.  
.  

In order to run the CLI JAR follow these steps:
* Browse with the terminal in the directory where the client.jar file is saved
* To run the CLI version of the client.jar, use this command: java -jar client.jar "-playingViewMode=cli"

At this point the CLI version of the Client JAR should be running.  
To turn the CLI off, you can select the "exit" command in the initial menu of the game.  
Another way to turn the CLI off is to enter the ctrl+c key combination in the terminal.

##Running the Client: GUI version
In order to run the GUI version of the client.jar, simply double click on the client.jar icon.  
At this point the GUI version of the Client JAR should be running.  

To turn the GUI off, you can use the "exit" button in the initial menu.  
Another way to turn the GUI off is to press the "view board" button while playing and then close the game window by clicking the "X" in the top corner of the window (as for any other program).  
An additional way is to kill the GUI directly in the task manager: use this method only if unable to perform the two above.
