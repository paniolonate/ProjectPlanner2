# README.md

The ProjectPlanner App allows a user to manage a project and visualize a critical path for development. The app allows the user to add project events and their respective durations and dependencies, which the program then uses to calculate the critical path, displaying a list of the critical path events as well as the critical duration. In future, we would like to have a graphical AON display of the project events and a highlighted critical path. However, we were not able to implement it within the time constraint allotted to our team. Come back later for that feature! (Just kidding, don’t).

## Building the Application: 

1) Download Android Studio
2) At the Intro screen choose “Check out project from Version Control” > Github > then choose “password” from the drop-down > Login with your Github credentials >  put this URL into the repository URL field "https://github.com/paniolonate/ProjectPlanner_" > select “clone”
3) Or you can go to github.com and clone GitHub Repository to the Android Studio: "https://github.com/paniolonate/ProjectPlanner_" (Step number 2 worked better for us)
4) Upon opening project in Android Studio it will take a while for the Gradle scripts to build the project, if it fails go to the “Messages Gradle Sync” on the bottom left hand side of the screen and click on any options in the Error field, for example “Install missing platform(s) and sync project showed up when trying to clone the repo to a friend’s machine.  We needed to click on that message and download the “Android SDK Platform 25”.  After that, we also had to click on “Install Build Tools 25.0.3 and sync project” from the Messages Gradle Sync.   And then,  we also then clicked a recommended Gradle update after that.  Another Install Build Tools was downloaded.  Keep doing this until you see BUILD SUCCESSFUL in the console and the Green Play Arrow should appear on the top toolbar after that.
5) Click on Run and then “Create New Virtual Device” and Download the Android Emulator: Nexus 5X, download API 26 if you get an error saying the You need to change the settings in BIOS because of the Intel HAXM thing you will need to > Create new virtual device > instead of x86, select an emulator with “arm” as the first 3 letters and continue setting up the device.
6) Once the emulator is set up, update google play to the latest version (the option might be in the emulator’s grey sidebar “the icon with the 3 dots”)
7) Once everything is up to date. Press the play button to run the application with the Nexus emulator, the emulator should automatically take you to the application

## Running the Application:

1) Once you are on the application there will be two options: “Sign in” and “create account”, click on the create account option. You will need to enter an email and create a password (the password will need to be at least 6 characters long).
2) After the account is created, you should see a welcome message that contains the email you inputted and your User ID in Firebase, next click the back button, then your will see three options: New Project, My Projects, and sign out. Since this is a new project you will need to click on New Project to create a project.
3) You will then enter the New Project's name and select the “create” option. 
4) Once the new project is created, the project name will be displayed on the screen as a heading and beneath it the events which should be empty.
5) Create an event by clicking on the button that is at the bottom of the screen titled “add event”
6) Name the event that will be added to this particular project and click the “create” button.
7) Edit how long you think this particular event might take you. Since this will be the first event, there will be no dependencies to add or remove, therefore you can just click the back button
8) This will bring you back to the main page of this specific project. You will see the event you just added appear.  You can then add new events.
9) After adding events you can edit their duration by selecting them from the scrollable list.
10) You can also add or remove dependencies by clicking on the buttons, and then clicking on the corresponding dependencies.
11) To calculate the critical path and duration of the project go back to the menu with the critical path button (This may crash the application, we have not been able to find the bug related to the cause of this, but if it does, you may need to restart the application a few times and then try the critical path button again, it seems that the critical path generator works with data that was not newly added to the database, please let us know if you guys run into problems with this).
