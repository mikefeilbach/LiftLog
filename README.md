# LiftLog
LiftLog Android App
java codes can be accessed(from Android Studio) in app/java/com.Arnold.LiftLog/



Layout files describe how buttons, action bars and text looks like. They can be accessed(from Android Studio) in app/res/layout/

activity_edit_bubble.xml  layout of edit bubble screen

activity_main.xml     layout of home screen
  in activity_main.xml:
    "include" here stands for the "customized button" element.
    It is extended from "@layout/button_layout"
    the contents of customized button are initialized  in "OnCreate" of activity "MainActivity"
    "include" cannot be set with a OnClick.
    The button listener can only be set by setOnClickListener in "OnCreate" of activity "MainActivity".
    
activity_new_log.xml     layout of new log screen

activity_view_history.xml     layout of view history screen

button_layout.xml isused to layout the customized button


