# Verteilte Verarbeitung 
## Praktikum 1 von 3 –  Reaktive Systeme
In dieser Aufgabe wurde ein  Werkzeug zur Synchronisation von Verzeichnissen erstellt (irgendwas zwischen Dropbox und git).
Dieses Werkzeug funktioniert (theoretisch) über mehrere Rechner hinweg.
Es überwacht ein Quellverzeichnis und synchronisiert die darin befindlichen Dateien mit einem Zielverzeichnis. Das eigentliche Kopieren der Dateien ist nicht programmiert, es wird nur herausgefunden, welche Dateien zu synchronisieren wären.
### Dependencies
Das Projekt nutzt Gradle, Jacoco und Sonaqube.
Es wird JDK 14 verwendet.
## Praktikum 2 von 3 – Messaging Konzepte
 Im verteilten System gibt es verschiedene Services mit unterschiedlichen, klar getrennten Aufgaben. 
 
CustomerService: Der Service erstellt zufällige Bestellungen (Orders). Der Service besitzt eine Anbindung an den MessageBus und schreibt die generierten Orders in den Kanal "OpenOrders". 

AccountingService:  Der AccountingService überwacht den Kanal "OpenOrders".  Hat eine Bestellung einen Betrag unter 500€ wird diese freigegeben und in den Kanal "ApprovedOrders" geschrieben. Bei einer freigegebenen Order wird dann das Feld "ApprovedBy" mit dem Namen "Buchhaltung" gefüllt. 
 
TeamLead: Dieses Programm wird vom Teamleiter des Callcenters bedient. Der Service kümmert sich um die Bestätigung von Bestellungen mit hohen Bestellsummen. Der Service liest Bestellungen aus dem Kanal "NeedsApproval" aus. Als "Freigabelogik" wird ein Zufallsalgorithmus verwendet. Hier verzichten wir auf eine komplexere Entscheidungslogik. Der Teamleiter entscheidet per "Bauchgefühl" ob die Bestellung freigegeben werden kann.
### Dependencies
Das Projekt nutzt Gradle, Jacoco und Sonaqube.
Es wird RabbitMQ verwendet.
Es wird JDK 14 verwendet.

## Praktikum 3 von 3 – Schnittstellen-Konzepte
Im verteilten System gibt es verschiedene Services mit unterschiedlichen, klar getrennten Aufgaben.

Szenario: Das aus Aufgabe 2 bekannte Szenario soll nun im Kontext der Kundenzufriedenheit erweitert
werden.

OrderApprovalService: Hat eine Anbindung an eine Datenbank. In dieser werden die Entitäten„Customer“, „Order“ und „Payment“ gespeichert. Der Service besitzt eine Anbindung an den MessageBus. Der Service prüft bei eingehenden Bestellungen, ob ein Kunde noch offene Posten besitzt. Überschreiten diese den Betrag von 1000€ wird eine Bestellung nicht freigegeben und der Kunde in den MessageBus Kanal „DeclinedCustomers“ geschrieben. Bei Freigabe einer Bestellung wird der Bestellbetrag auf den offenen Betrag eines Kunden addiert und der Kunde in den MessageBus Kanal „ApprovedCustomers“ geschrieben. Der OrderApprovalService bietet eine REST API an.

MarketingService: Besitzt eine Anbindung an den MessageBus um mit dem OrderApprovalService zu kommunizieren. Er liest aus den Kanalen „ApprovedCustomers“ und „DeclinedCustomers“ die Kunden aus. Die Marketing Abteilung arbeitet mit einem externen Mail-Anbieter zusammen.
### Dependencies
Das Projekt nutzt Gradle.
Es wird RabbitMQ verwendet.
Es wird JDK 14 verwendet.
Es wird Spring Boot und Hibernate verwendet