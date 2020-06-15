# Verteilte Verarbeitung 
## Praktikum 1 von 3 –  Reaktive Systeme
In dieser Aufgabe wurde ein  Werkzeug zur Synchronisation von Verzeichnissen erstellt (irgendwas zwischen Dropbox und git).
Dieses Werkzeug funktioniert (theoretisch) über mehrere Rechner hinweg.
Es überwacht ein Quellverzeichnis und synchronisiert die darin befindlichen Dateien mit einem Zielverzeichnis. Das eigentliche Kopieren der Dateien ist nicht programmiert, es wird nur herausgefunden, welche Dateien zu synchronisieren wären.
## Dependencies
Das Projekt nutzt Gradle, Jacoco und Sonaqube.
Es wird JDK 14 verwendet.
## Praktikum 2 von 3 – Messaging Konzepte
 Im verteilten System gibt es verschiedene Services mit unterschiedlichen, klar getrennten Aufgaben. 
 
CustomerService: Der Service erstellt zufällige Bestellungen (Orders). Der Service besitzt eine Anbindung an den MessageBus und schreibt die generierten Orders in den Kanal "OpenOrders". 

AccountingService:  Der AccountingService überwacht den Kanal "OpenOrders".  Hat eine Bestellung einen Betrag unter 500€ wird diese freigegeben und in den Kanal "ApprovedOrders" geschrieben. Bei einer freigegebenen Order wird dann das Feld "ApprovedBy" mit dem Namen "Buchhaltung" gefüllt. 
 
TeamLead: Dieses Programm wird vom Teamleiter des Callcenters bedient. Der Service kümmert sich um die Bestätigung von Bestellungen mit hohen Bestellsummen. Der Service liest Bestellungen aus dem Kanal "NeedsApproval" aus. Als "Freigabelogik" wird ein Zufallsalgorithmus verwendet. Hier verzichten wir auf eine komplexere Entscheidungslogik. Der Teamleiter entscheidet per "Bauchgefühl" ob die Bestellung freigegeben werden kann.
## Dependencies
Das Projekt nutzt Gradle, Jacoco und Sonaqube.
Es wird RabbitMQ verwendet.
Es wird JDK 14 verwendet.