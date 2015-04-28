# LiftLog
LiftLog Android App

Created by: Team 10 - Do You Even Lift? (DYEL)

--
README
--

To see the commits the following link will show all of them:

https://github.com/mikefeilbach/LiftLog/commits/master

--

To the main page of the project, follow the link:

https://github.com/mikefeilbach/LiftLog/

--

Link to clone the code to the Android Studio in your personal computer:

https://github.com/mikefeilbach/LiftLog.git

--

Link to download the Android Studio:

http://developer.android.com/sdk/index.html

--

Link to download GIT:

http://git-scm.com/download/

--
HOW TO CLONE AND RUN THE CODE IN ANDROID STUDIO:

(Please make sure you have GIT installed in your computer)

- In Android Studio menu, go to item VCS;

- In the menu item VCS, go to to option Checkout from Version Control;

- In the Checkout from Version Control, select the option GIT;

- A window will pop-up asking for the Repository URL, which is the following:

    https://github.com/mikefeilbach/LiftLog.git

- Set the Directory Parent where you want to clone the files and a Directory Name;

- Press clone;

- Now the Android Studio will download all the files;

- To execute the program just press the RUN button and select in which Android Virtual Device (AVD) it will run;

- If you do not have an Android Virtual Device created, just follow the instructions in the link below:

    http://developer.android.com/training/basics/firstapp/running-app.html#Emulator

- After configure the AVD, press RUN again, then select the AVD that was created and the app will run.


--

HOW TO RUN BACKEND (DATABASE) TESTS:

Uncomment the following lines in MainActivity.java:

DatabaseTester dbTest = new DatabaseTester(this);

dbTest.testDatabase();


--

HOW TO RUN VIEW HISTORY MONTH CONCATENATION TEST:

Uncomment the following line in ViewHistoryActivity.java:

testViewHistory();

This will create dummy data that will allow the user to see how previous logs are concatenated into months, decluttering the View History Screen.  The dummy logs themselves cannot be clicked on (as they have no information associated with them from the database) so this only test that logs are concatenated into months, and that those month buttons can be clicked on to show the specific logs associated with them.
