"Minlog" service
====================
Den nyeste udgave at dette dokument kan findes på <https://github.com/trifork/nsi-minlog/>

En pdf udgave af reporten kan laves med <https://github.com/walle/gimli> ved blot at kører *gimli* fra roden af projektet.

Installationsvejledning
-----------------------


### Krav til miljø

Komponenterne er udviklet og testet JBoss 6.0, og kan deployes i produktion på alle nævnte applikationsservere. Dog er der kun skrevet installationsvejledning for deployment på JBoss 6.0, 
da det er denne platform som bruges på den nationale service platform (NSP).

Applikationsserveren kræver SUN/Oracle Java 6.0 eller højere.

#### Krav til Operativsystem
Der stilles ingen krav til operativsystemet, ud over det åbenlyse krav om at Java er understøttet på operativsystemet. 
Ubuntu Linux bruges som operativsystem på NSP’en. Til udvikling af komponenten er OS X anvendt.

#### Krav til database
Komponenten er testet mod MySQL version 5.5.11. Det er den samme MySQL version som bliver brugt på NSP platformen (NIAB version 1.1.3).

#### Krav til hardware
Der er nogle minimumskrav for at kunne afvikle komponenten fornuftigt til testformål.  

Minimumskravene, for fornuftig performance på et test-setup, er:

	• Intel Core 2 eller lignende CPU
	• 8 GB ram
	• Nødvendig harddisk plads for at kunne håndtere alle logs (80+ GB)

### Configuration
De fleste af konfigurationsfilerne skal ligge i jBoss serverinstansens *conf* bibliotek - f.eks. *server/default/conf/log4j-minlog.xml*.

Desuden er web-applikationen konfigureret med standard indstillinger der kan overskrives ved at ligge *minlog."brugernavn".properties* 
og/eller *jdbc."brugernavn".properties* i *conf*. Hvor "brugernavn" er brugeren der kører web-applikationen - f.eks. *server/default/conf/minlog.nsi.properties*

#### Standard indstillinger
    minlogCleanup.cron=0 0 * * * ?  
    sosi.production=0
    sodi.canSkipSosi=0
    jdbc.url=jdbc:mysql://localhost:3306/minlog
    jdbc.username=minlog
    jdbc.password=

Bemærk at der til *minLogCleanup* bruges Quartz - CronTrigger notation <http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06>

*sosi.production* bestemmer om koden bruger SOSIFederation eller SOSITestFederation.  
*sosi.canSkipSosi* bestemmer om hvorvidt sikkerhedstjekket kan skippes.

#### Logging
Minlog kræver, at der ligger en *log4j-minlog.xml* og en *log4j.dtd* i *conf*, disse bruges til at konfigurere minlogs log4j.

For at SLALog skal fungere skal der ligge en *nspslalog-minlog.properties* i *conf*

*default.properties*, *jdbc.default.properties*, *log4j-minlog.xml*, *log4j.dtd* samt *nspslalog-minlog.properties* 
ligger alle sammen i artifakten under *WEB-INF/classes* eller i repositoriet under *minlogudtraekservice/src/resources*

### Database
Minlog kræver en mysql database og er testet imod MySQL 5.5.11.

Database opsætningen ligger i *minlogudtraekservice/src/main/resources/db/migration* og skal køres efter versionsnummer.
Scriptsene antager at der i forvejen er oprettet en database ved navn *minlog*

### SmokeTest af WebService
Der kan køres en SmokeTest på */jsp/checkall.jsp*, som tjekker interne afhængigheder.
Hvis der returneres andet end http status *200*, betyder det at applikationen ikke virker som den skal.

### Splunk udtræk
Vejledning til opsættelse af splunk udtræksjob kan ses i slutningen af dette dokument.

Driftsvejledning
----------------
WSDLen bliver udstillet på "*/.wsdl*" og kaldende skal fortages til "*/*".

Ved at køre */jsp/checkall.jsp* kontrollerer applikationen at der er forbindelse til database. 
Hvis denne side viser en fejl eller returnerer andet end http status kode *200*, kører applikationen ikke. 
Se den returnerede beskrivelse af fejlen.

## SLA log
SLA loggen kan findes i jBoss serveren instansens *log*, såfremt *nspslalog-minlog.properties* er blevet lagt i *conf*.

### Whitelist
Whitelist databasen tabellen indholder de whitelistede cvr numre, der kan bruge minlog.

Design og Arkitektur beskrivelse
--------------------------------
Minlog består af 2 komponenter

### Splunk udtræk
Et python script som henter splunk data ned i databasen.
Se slutningen af dette dokument for yderligere oplysninger.

### WebService/Oprydningsjob
Webservicens opgave er at søge i logninger, ud fra et given cpr nummer, og evt. et dato interval. 
Alle logninger der opfylder de angivne kriterier returneres.
Webservicen udstiller denne funktionallitet via en soap webservice.

Desuden indholder denne komponent et konfigurerbart job, som undersøger databasen og sletter alle logninger der er ældre end 2 år.

Guide til anvendere
-------------------
Minlog bliver udstillet som en standard soap webservice og alle kald kræver "Den gode webservice 1.0.1".

WSDLen bliver udstillet på "*/.wsdl*" og kald skal fortages til "*/*".

De enkelte parametre kan ses i wsdl.

Guide til udviklere
-------------------
Projektet bør køres på en NSP in a box (NIAB) <https://www.nspop.dk/display/public/NSP+In+A+Box>
Desuden bør man installere Splunk lokalt <http://www.splunk.com/>

For at teste komponenten:
1: Deploy komponenterne på NIAB
2: Opbyg en fil med en række logninger
3: Lad Splunk indexere denne fil og loade den ind i MySql.
4: Man vil nu kunne tilgå MinLog service fra en webservice klient.

### Om koden
Applikationen er en standard DAO-Service-Controller arkitektur, hvor service-delen er undladt, da kodebasen er ret begrænset.

Controller er implementeret i Spring Webservice frameworket <http://static.springsource.org/spring-ws/sites/2.0/>

DAO er implementeret i eBean frameworket <http://www.avaje.org/>

### Byg
For bygge skal man have installeret maven og køre "mvn clean install" fra roden af projektet.
Artifakten vil efterfølgende ligge under *minlogudtraekservice/target/minlog.war*

### SosiIdCardTool
Der ligger et tool til at lave "Den gode webservice 1.0.1" headers, så det er nemmere at teste om servicen virker:

    java -Done-jar.main.class=dk.vaccinationsregister.testtools.SosiIdCardUtil -jar SosiIdCardTool.jar

På OS X kan man pipe resultatet over i clipboardet med pbcopy:
    
    java -Done-jar.main.class=dk.vaccinationsregister.testtools.SosiIdCardUtil -jar SosiIdCardTool.jar | pbcopy

Test vejledning
---------------
Test bliver kørt automatisk når bygger projektet, som beskrevet herover.

### Coverage
For at lave coverage-tests køres *mvn clean install cobertura:cobertura surefire-report:report* fra *minlogudtraekservice/*  
Coverage resultaterne findes i *minlogudtraekservice/target/site/cobertura/index.html*  
Svar på unittests kan ses i *minlogudtraekservice/target/site/surefire-report.html*  

### Funktionelle tests
Formålet med de funktionelle tests er at teste hele vejen gennem komponenten. Fra webserviceklient, igennem webservice, hent data fra MySql, og returner data.


Dette gøres for hver test:
* En ny service og en kontekt til denne.
* En embedded mysql database i en ny process. 
* Der populeres data i databasen.
* Der generes en soap security header med saml.
* Der generes en body, med en forspørgelse på test data.
* Svarets body sammenlignes med et statisk body fra en xml.

**NB!** Hvis de funktionelle tests bliver afbrudt, er der en risiko for at man ikke kan starte en mysql server efterfølgende,  
da mysql vil brokke sig over at der er en server instans der ikke er blevet lukket korrekt. 
Dette er en uhensigtsmæssighed/fejl i MySql ved denne anvendelse.
Problemet løses ved genstart af computer eller ved at rydde op i pid/err i mysqls data-folder.

Testrapport til sammenligning
-----------------------------
Test coverage med unittests:  
<img src="https://github.com/trifork/nsi-minlog/raw/master/doc/coverage.png" width=600>

Performance tests
-----------------
Dette afsnit skal skrives om når vi har lavet nye performance tests.
Der findes *benerator* scripts til at generere tilfældige testdata. Se senere beskrivelse om generering af testdata.

Sitet med af performances-tests ligger under *doc/Performance.zip*


Det er ikke lykkes at få serveren til at gå ned, men i tests med et forventet throughput på 1000 request/sec bliver der kun laves 200 requests/sec.
Dette kan skyldes setup'et eller en indstilling i OS'et.

Denne endurence tests laver 2 requests/sec og er lavet over 2 timer. Grafen spiker kl 12:00,  
dette kan skyldes at computeren har et job der bliver eksekveret kl 12:00.  
Derved får garbage collectoren ikke lov til at lave løbende collection.  
Ved 1GB heap rydder garbage collectoren fint op.  

<img src="https://github.com/trifork/nsi-minlog/raw/master/doc/endurence.png" width=600>

### Opsætning
Disse tests er kørt på 
    
    2GHz Intel core i7
    8 GB ram
    Harddisk med 5400 rpm

Opsætning af mysql:

    innodb_data_file_path = ibdata1:10M:autoextend
    innodb_flush_log_at_trx_commit = 1
    innodb_lock_wait_timeout = 50
    innodb_additional_mem_pool_size=512M
    innodb_buffer_pool_size=4096M
    innodb_log_buffer_size=128M
    innodb_log_file_size=1024M
    read_buffer_size= 128M
    sort_buffer_size=4096M
    tmp_table_size= 1024M

Det antages at databasen *minlog* er oprettet med adgang fra brugeren *minlog*
og at der er minlog er blevet sat op med *sosi.production = 0* og *sosi.canSkipSosi=1*


### Generering af testdata
Til at genere test-data med er *Benerator* blevet brugt <http://databene.org/databene-benerator>

Alle kommandoer skal køres fra */performance*.

Først køres *benerator benerator/cpr.xml* som laver CPR numre i *data/cpr.csv*
Der generes 50.000 tilfældige CPR numre.

Dernæst *benerator benerator/logentries-small.xml* som laver 45.000.000 logs i filen *data/logentries-small.csv*

*import.sql* tilpasses så stien passer til *data/logentries-small.csv* og køres.
NB! skal være absolut, dette skal gøres for at slippe for *local* parameteren til *load data* som laver en kopi af csv filen!.

Dernæst tilpasses *30-cpr.sql* så der laves en fil i *data/cpr.csv*.
Dette finder alle de cpr numre som har højest 3o indgange.


Splunk udtræksjob
=================
Scriptet er skrevet i python og ligger i */splunk*

Scriptet mapper data fra Splunk til Skema i MySQL. Mapning er som følger:


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
Scriptet er beregnet på at blive kørt at udenforstående automatisk kørsel, så som cron eller deamontol

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
* Validerer Splunk søgningen
* Udføre søgning i Splunk udfra hvor langt man er nået - hvis det er første gang scriptet køre vil det tage op til nu minus 30 sec. Ellers hentes alt der er indexeret siden sidste kørsel til nu minus 30 sec.  
* Gemmer data i CSV fil
* Åbner forbindelse til MySQL
* Læser CSV fil og indsætter data i MySQL
* Skriver den højeste index tid i STATUS_FILE

Hvis nået går galt vil dette blive skrevet ud til LOG_FILE og derfor er det vigtigt at denne overvåges for ERROR entries.

### Antagelser

Scriptet antager at man henter data udfra tidspunktet de er indexeret på i Splunk, da man ikke kan være sikker på at datoen i logs vil være fortløbende - nogle servere kan f.eks. være offline og først aflevere senere og således er den eneste sikre ting at hente data udfra hvornår de er indexeret i Splunk. Derfor bruges denne metode. Tests af scriptet ser ud til at virke i denne hensende. Desuden er der et delay på (default) 30 sekunder der sikre at man ikke henter data op til det sekund som scriptet køre, da der kan nå at komme mere data i de sekund i Splunk skal skal sikres medtages. Derfor er det nu minus 30 sekunder (default) der hentes for i det man kalder scriptet. 

### Usikkerhed
Med den antagelse at man henter data ud af Splunk udfra Indextime og at man dermed får alt data med over i MySQL når scriptet kører. 
Der er dog ikke udviklet nogen verifikation af at alle data med sikkerhed er flyttet. Det ville være muligt at forsøge validere at der indenfor tidsrammen indextime-x til indextime-y er så mange entries i Splunk og så kigge på om scriptet også har flyttet så mange entries. 
Dette er nuværende ikke del af scriptet. 
