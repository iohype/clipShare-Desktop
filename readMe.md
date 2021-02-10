# Clipshare

Clipshare is a java maven project that enables auto syncing of
clipboards of connected devices. <br>  
![title](sampleImages/homePage.png)

# Description

The app connects various devices through a HTTP server and automatically
shares their clipboard. Therefore when a text is copied on one device,
the text is available for pasting on the other device connected device.

The java clipboard API makes it possible to monitor the clipboard on the
device, therefore once the app is running it listens for copied clips.
Once a text is copied, it gets the text and makes a PUT request to the
running server. Documentation for the java clipboard API can be found at  
[https://docs.oracle.com/javase/8/docs/api/java/awt/datatransfer/Clipboard.html]()

The HTTP server is developed with an external library named Javalin. The
documentation for Javalin can be found at [https://javalin.io/](). Using
this library, three (3) endpoints were setup for the server. <br>

<ul>
<li><strong>GET /</strong> - which returns the text in the server </li>
<li><strong>PUT /</strong> - which allows the text to be sent to the server </li>
<li><strong>GET /lastupdated</strong> - which returns the last timestamp the server was updated</li>
</ul>

### Features

<ul>
<li>Preferences on what port the server should run can be set, but by default already set to 9090 </li>
<li>Dark theme can also be applied </li>
</ul>

# Installation

Semantic versions of the app release can be found under releases. <br>
Clipshare was built using JDK 8 (with JavaFX) and maven.<br> To build
Clipshare. <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ensure a JDK and
maven is downloaded and installed. <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Clone this git repository.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To build execute the command `mvn
build`<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To run the demo, execute
the following command `mvn jfx:run `

# RoadMap
Several ideas such as will also be developed:
<ul>
<li>Syncing of copied files such as images, music etc.</li>
<li>Development of a mobile app to sync with this desktop app is ongoing</li>
</ul>

# Contribution

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.
