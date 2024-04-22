## Train tracking Java app
* Copyright the owner of this GitHub account, A-YT. Use for review purposes is allowed.
* Tracks trains between "Leppävaara" and "Helsinki Asema".
* Also tested between "Jyväskylä" and "Äänekoski".
* The two axes represent distance (represented by stations) and time, with the thick green vertical line indicating time now.
* Only shows trains whose timetable entries are within 4 hours of current time.

### Running
* Runnable with the Windows Command promt / Linux shell.
* Go to directory "train-tracking" and run the folloing command 
  - java -jar train-tracking.jar

### Compiling
* To compile the project, you need to add file "lib\json-simple-1.1.1.jar" to the project. Other libraries should be standard libraries.
* Use "javac -classpath lib\json-simple-1.1.1.jar "

### Notes and TODOs
* Code would benefit from refactoring. Ran out of time initially.
* Train graph lines and station labels now take into account actual station distances.
* Actual train times are combined with scheduled times, since given API gives out very few actual times.
* API doesn't seem to give train names, only train numbers, so train names could not be implemented.
* Hover over line showing train name not implemented. Not even tried, as API seems to lack train names requested. Possibly later with something like GeneralPath.
* Button to switch axes no implemented due to lack of time. (Not sure what the added value in changing axes is either)
* Current implementation is not realtime, i.e. app doesn't update graph. Possible future improvement.
* Hasn't been tested if used is outside Finnish timezone. Probably doesn't work.
* Train schedule seconds are not used for the graph. This is likely not noticeable, but could in theory in some cases cause something like a 1 pixel distortion. Easy to add seconds if needed.
* Train colors are random. Restart the application of colors are hard to see (pending a refresh button).
* For further notes, see source code.


### Example of software running
![Train graph](example.jpg)