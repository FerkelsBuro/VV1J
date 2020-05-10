# Verteilte Verarbeitung 
## Praktikum 1 von 3 –  Reaktive Systeme
In dieser Aufgabe wurde ein  Werkzeug zur Synchronisation von Verzeichnissen erstellt (irgendwas zwischen Dropbox und git).
Dieses Werkzeug funktioniert (theoretisch) über mehrere Rechner hinweg.
Es überwacht ein Quellverzeichnis und synchronisiert die darin befindlichen Dateien mit einem Zielverzeichnis. Das eigentliche Kopieren der Dateien ist nicht programmiert, es wird nur herausgefunden, welche Dateien zu synchronisieren wären.
## Dependencies
Das Projekt nutzt Gradle und JDK 14.