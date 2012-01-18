# QuantiSense Software Challenge

## RESTful Service

The RESTful service portion of the QuantiSense Software Challenge resides in the qsexam directory.

### Dependencies

The service is written in the Grails framework.
It uses version 2.0.0, which can be downloaded [here](http://grails.org/Download)

### Use
The service can be started by executing

  >grails run-app

By default this will start the service at 'http://localhost:8000/qsexam'.
A JSON representation of Stores can be found at 'stores/show/?q=<Store Name>'.
Additionally, the JSON can be wrapped in a JavaScript callback function with the callback parameter,
e.g. 'http://localhost:8000/qsexam/store/show/?q=Atlanta&callback=callback1'.
This is useful for GWT clients to be able to make cross site requests to a remote server running the service.

## Web Application

The Web Application portion of the QuantiSense Software Challenge resides in the StoreFinder directory.

### Dependencies

The application is written with [Google Web Toolkit](http://code.google.com/webtoolkit).
It is built targetting the GWT 2.4.0 SDK.


### Use

A development server can be started through the Google Plugin for Eclipse.
It will provide a URL with which to view the application in hosted mode.
In order to change the server where the application looks for JSON data, simply change the JSON_URL contant in StoreFinder.java to a URL of your choice.
To use a currently running grails development server, just change it to 'http://localhost:8000/qsexam/store/show/q?='
