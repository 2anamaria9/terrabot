# Tema 1 - TerraBot

## 1.Arhitectura Sistemului

Proiectul a fost structurat modular pentru a separa responsabilitatile, fiind 
astfel impartit in doua pachete principale:

    * **main.entities**
    Contine pachetele Air, Animal, Plant, Soil si Water ale caror continut
    defineste ierarhia de clase, asigurandu-se astfel o structura logica. Am
    utilizat o interfata comuna 'Entity' pentru a permite polimorfismul in
    gestionarea inventarului robotului. Clasele de baza abstracte functioneaza
    ca un sablon pentru subclase, asigurand ca fiecare tip de entitate sa isi
    gestioneze propria logica matematica. Pentru evitarea folosirii instanceof,
    am introdus metoda 'addSpecificFields' in clasele de baza pentru ca fiecare
    subclasa sa isi puna in nodul Json atributul specific.

    * **main.Commands**
    Toate actiunile robotului sau ale mediului sunt incapsulate in clase
    utilitare statice. Astfel se separa logica de informatii si se faciliteaza
    testarea si intretinerea fiecarei comenzi izolat.

Pe langa entitati si comenzi, arhitectura se bazeaza pe un set de clase care
gestioneaza starea globala si fluxul de date:

    * **Simulation**
    Actioneaza ca un container de stare care serveste pentru initializarea si
    popularea hartii prin metoda populateMap, logica de update a ecosistemului
    si mai ales pastrarea referintelor catre robot si mapa.

    * **TerraBot**
    Contine starea interna a robotului. Clasa gestioneaza coordonatele actuale,
    coordonatele urmatoare datorate miscarii, energia in momentul consumului/
    reincarcarii, inventarul care stocheaza obiectele colectate si baza de date
    ce curpinde informatii despre obiecte, implementandu-se astfel metode
    aferente.

    * **SimulationMap & Cell**
    Ele constituie structura spatiala. Harta este modelata ca o matrice de
    obiecte de tip celula. Clasa Cell contine simultan referinte catre toate
    tipurile de entitati la o anumita coordonata, fapt ce faciliteaza accesul
    la orice element de pe harta.

## 2.Flow-ul Simularii

Motorul simularii se afla in 'Main.java'. Deoarece comenzile din input nu au
intotdeauna timpestamp-uri consecutive, se calculeaza diferentele de timp
(currentTimestamp - previousTimestamp) si se ruleaza bucla de update (metoda 
updateEnvironment din 'Simulation.java') de atatea ori cat este necesar pentru 
a simula trecerea timpului.

    **updateEnvironment**
    Gestioneaza ciclul de viata al ecosistemului intr-o ordine stricta:
        * Actualizare conditii meteo;
        * Stergerea entitatilor moarte/ lipsite de resurse;
        * Interactiuni statice;
        * Logica de hranire si ulterior de miscare a animalelor.

## 3.Structuri de date si algoritmi

    * LinkedHashMap: Am utilizat pentru baza de date a robotului pentru a pastra
    ordinea de inserare a topic-urilor.

    * IdentityHashMap (Set):  Deoarece masa animalelor se modifica in timpul 
    hranirii, hashcode-ul lor se schimba, iar un HashSet standard nu putea sa 
    reecunoasca animalul in lista celor deja procesate, cauzand procesarea 
    multipla. Astfel, am utilizat IdentityHashMap pentru a identifica animalele
    dupa referinta lor in memorie, garantandu-se astfel unicitatea procesarii
    indiferent de ce modificari de stare au loc (aceasta a fost principala
    problema pe care am intampinat-o pe parcursul rezolvarii temei, solutia
    gasita fiind cea enuntata mai sus).

    * ArrayList: Am utilizat pentru inventarul robotului si in principal in
    logica de miscare a animalelor unde m-am folosit de un set de liste 
    auxiliare pentru a filtra vecinii in functie de resursele disponibile.

## 4.Utilizare LLM-uri

In rezolvarea acestei teme m-am folosit de LLM-uri in doua situatii in care am
intampinat mici dificultati:

    * Gestionarea colectiilor: mai precis pentru clarificarea conceptelor de
    Hashing in Java. Deoarece nu stapaneam indeajuns 'LinkedHashMap' sau 
    'IdentityHashMap' am apelat la ajutorul LLM-urilor pentru a putea intelege
    si aplica ceea ce am inteles in rezolvarea problemei identitatii animalelor
    si a ordinii datelor in baza de date.

    * Eliminarea Magic Numbers: pentru a transforma valorile in constante
    statice cu nume descriptive in vederea respectarii regulilor de coding 
    style. Aceasta abordare a accelerat semnificativ procesul de refactorizare,
    evitand blocajele legate de alegerea numelor.
