"Minlog" service
====================
Den nyeste udgave at dette dokument kan findes på TODO

Installationsvejledning
-----------------------
Minlog war artifakten er beregnet til at køre på en jBoss 6 AS

### Configuration
Minlog er konfigureret med standard indstillinger, hvor dele eller hele indstillingen kan 
overskrives med eksterne filer.

De eksterne filer *minlog."brugernavn".properties* og *jdbc."brugernavn".properties*

#### Standard indstillinger
minlog.\*.properties
*minlogCleanup.cron=0 0 \* \* \* ?*  
*sosi.production=0*  

Bemærk at der til *minLogCleanup* bruges Quartz - CronTrigger notation <http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06>

jdbc.\*.properties
*jdbc.url=jdbc:mysql://localhost:3306/minlog*  
*jdbc.username=minlog*  
*jdbc.password=*  

Desuden kræver minlog at der ligger en *log4j-minlog.xml* i server instansens *conf* bibliotek - f.eks. *server/default/conf/log4j-minlog.xml*
denne bruges til at konfigurere minlogs log4j.

### Database
Database opsætningen ligger i *minlogudtraekservice/src/main/resources/db/migration* og skal køres efter versionsnummer.
Scriptsene antager at der i forvejen er oprettet en database ved navn *minlog*


















## Splunk udtræksjob
Scriptet er lavet til at Mappe data fra Splunk til Skema i MySQL. Mapning er som følger:


    Splunk									    MySQL
    ------------------------------------------------------------------
    PersonCivilRegistration 		 			cprNrBorger
    UserIdentification							bruger
    UserIdentificationOnBehalfOf 				ansvarlig
    HealthcareProfessionalOrganization			orgUsingID
    SourceSystemIdentifier 						systemName
    Activity 									handling
    SessionId									sessionId
    "2012....."  								tidspunkt
 
Med følgende Input til Splunk

    2012-06-07T15:26:48.388Z PersonCivilRegistrationIdentifier="100000001" UserIdentifier="0101001000" UserIdentifierOnBehalfOf="0101001000" HealthcareProfessionalOrganization="SOR:12345678" SourceSystemIdentifier="System name" Activity="Sundhedsperson med cpr-nummer 0101001001 har lavet opslag pÂ data." SessionId="urn:uuid:bcf637c1-3032-4cb3-a353-ac36faca3503" 

### Krav til Splunk udtræksjob
For at kunne køre softwaren skal følgende være installeret

* Python 2.7+
* Splunk SDK for Python - <https://github.com/splunk/splunk-sdk-python>
* Python MySQL API (pymysql) - <https://github.com/petehunt/PyMySQL/>

Opsætning

* Installer Python 2.7+
* Hent Splunk SDK herfra: <https://github.com/splunk/splunk-sdk-python>
* Installer Splunk SDK på server med *python setup.py install*
* Hent MySQL API fra <https://github.com/petehunt/PyMySQL/>
* Installer MySQL API på serveren med *python setup.py install*

### Kørsel af Værktøj
Scriptet er beregnet på at blive kørt at udenforstående automatisk kørsel, så som cron eller deamontol.



### Filer
I scriptet kan stien til de filer der bliver produceret rettes.
Følgende filer findes:  
*STATUS_FILE="Splunk2MySQL.status"* - Denne fil indeholder den seneste Index Time som scriptet er nået til at flytte data for.  

*SPLUNK_DATA_FILE="currentData.csv"* - Fil som skrivet skriver med de seneste data hentet fra Splunk og som indsættes i MySQL  

*LOG_FILE="Splunk2MySQL.log"* - Scriptets logfil

Default loglevel er INFO - men for høj debug af actions kan dette rettes til DEBUG ved at rette linje

Fra: 

    logging.basicConfig(filename=LOG\_FILE,level=logging.INFO,format='%(asctime)s, %(funcName)s, %(levelname)s, %(message)s', datefmt='%m/%d/%Y %H:%M:%S')
	
Til:

    logging.basicConfig(filename=LOG\_FILE,level=logging.DEBUG,format='%(asctime)s, %(funcName)s, %(levelname)s, %(message)s', datefmt='%m/%d/%Y %H:%M:%S')

### Scripts opbygning
Scriptet gør følgende: 

* Connecter til Splunk
* Validere Splunk søgningen
* Udføre søgning i Splunk udfra hvor langt man er nået - hvis det er første gang scriptet køre vil det tage op til nu minus 30 sec. Ellers hentes alt der er indexeret siden sidste kørsel til nu minus 30 sec.  
* Gemmer data i CSV fil
* Åbner forbindelse til MySQL
* Læser CSV fil og indsætter data i MySQL
* Skriver den højeste index tid i STATUS_FILE

Hvis nået går galt vil dette blive skrevet ud til LOG_FILE og derfor er det vigtigt at denne overvåges for ERROR entries.

### Antagelser

Scriptet antager at man henter data udfra tidspunktet de er indexeret på i Splunk, da man ikke kan være sikker på at datoen i logs vil være fortløbende - nogle servere kan f.eks. være offline og først aflevere senere og således er den eneste sikre ting at hente data udfra hvornår de er indexeret i Splunk. Derfor bruges denne metode. Tests af scriptet ser ud til at virke i denne hensende. Desuden er der et delay på (default) 30 sekunder der sikre at man ikke henter data op til det sekund som scriptet køre, da der kan nå at komme mere data i de sekund i Splunk skal skal sikres medtages. Derfor er det nu minus 30 sekunder (default) der hentes for i det man kalder scriptet. 

### Usikkerhed
Med den antagelse at man henter data ud af Splunk udfra Indextime og at man dermed får alt data med over i MySQL når scriptet køre. Der er dog ikke udviklet nogen verifikation af at alle data med sikkerhed er flyttet. Det ville være muligt at forsøge validere at der indenfor tidsrammen indextime-x til indextime-y er så mange entries i Splunk og så kigge på om scriptet også har flyttet så mange entries. Dette er nuværende ikke del af scriptet. 



## Driftsvejledning

## Design og Arkitektur beskrivelse

## Guide til anvendere

## Guide til udviklere

## Byg
For bygge skal man have installeret maven og køre "mvn clean install" fra roden af projektet.
Artifakten vil efterfølgende ligge under *minlogudtraekservice/target/minlog.war*

## Test vejledning
Test bliver kørt automatisk når bygger projektet, som beskrevet overstående.

**NB!** Hvis de funktionelle tests bliver afbrudt, er der en risiko for at man ikke kan starte en mysql server efterfølgende, da mysql vil brokke sig over at der er en server instans der ikke er blevet lukket korrekt.
Dette skyldes at de funktionelle tests starter en embedded mysql server instans.


Testrapport til sammenligning
