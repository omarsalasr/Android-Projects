# Music-Service
Android applications that utilizes Android services and Android Interface Definition Languages (AIDL) to mediate the communication between the client and service application

The server application that contains a set of audio clips and defines a service that is both started and bound which exposes an API for clients to use. Since it's running on API 28, the service must utilizes a constant notification so that it can run in the foreground and not be destoyed by the system.

The client application contains all the activities and UI elements to start/stop the service and play/pause/resume the adio clip which handles the binding and unbinding of the service. The application also contains a list of audio clicks to choose from with the proper logic to automatically stop previously selected audio clips from playing which also handles the binding and unbinding of the service.

Both applications use an Android Interface Definition Languages file to help with the communication. The AIDL is implemented in the service application which handles all of the libraries and methods to manipulate the audio clips. Then, the client application is the one to call those methods that are defined in the AIDL to control the audio clips.