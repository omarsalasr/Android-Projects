# Phone-List-Reloaded
A set of Android applications that utilize more advanced topics to show a list of phones.

First, application 1, 2, and 3 all utilize a custom dangerous level permission so that only these applications can listen to a special broadcast.
App 1 must have permission to use app 2, and app both app 1 and 2 must have permission to use app 3.

Once permissions have been granted, app 3 contains a list of phones using fragments.
In portrait mode, these phones are in a list. Once clicked, a new fragment appears with the image of the phone selected.
The fragments are retained across device configuration changes like screen rotation so that views aren't being rebuilt every rotation.

Finally, app 3 utilizes a custom action bar with an options menu that contains two options.
1. Broadcast a single ordered intent to start app 1 and 2. App 2 displays a toast message, then app 1 will display the web page of the smart phone selected.
2. Exit app 3