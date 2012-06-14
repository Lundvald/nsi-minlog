#!/usr/bin/env python
#
#  
#

import sys, os
import csv
import time
from time import localtime,strftime,sleep
from datetime import datetime
import pymysql
import logging
import uuid

from splunklib.binding import HTTPError
import splunklib.client as client

#
# Variables
#

# Splunk Connection Details
SPLUNK_SERVER="localhost"
SPLUNK_PORT="8089"
SPLUNK_USER="admin"
SPLUNK_PASSWORD="abekat"
SPLUNK_SCHEMA="http" # http eller https

#MySQL Connection Details
MYSQL_SERVER="localhost"
MYSQL_PORT=3306
MYSQL_USER="jvl"
MYSQL_PASSWORD="abekat"
MYSQL_DATABASE="minlog"
MYSQL_INSERT_STATEMENT="INSERT INTO LogEntry (regKode, cprNrBorger, bruger, ansvarlig, orgUsingID, systemName, handling, sessionid, tidspunkt) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s');" # The Insert statement to use for the MySQL database

# Splunk Search Details
SPLUNK_RETURN_FIELDS="_indextime, _time, PersonCivilRegistrationIdentifier, UserIdentifier, UserIdentifierOnBehalfOf, HealthcareProfessionalOrganization, SourceSystemIdentifier, Activity, SessionId" # Fields that will be selected from Splunk and returned
SPLUNK_SEARCH_INDEX_TIME_DELAY_SEC=30 # The number og sec to go back for the _indextime search. To make sure that all events that will be indexed that second have been written 
SPLUNK_SEARCH_INDEX_TIME=int(time.time()-SPLUNK_SEARCH_INDEX_TIME_DELAY_SEC) # Calculate max _indextime
SPLUNK_SEARCH='search index=main sourcetype=minlog (_indextime < ' + str(SPLUNK_SEARCH_INDEX_TIME) + ' AND _indextime > %s) | fields ' + SPLUNK_RETURN_FIELDS + ' | sort by _indextime asc' # Contruct the Splunk Search.

# Helper files Details
STATUS_FILE="Splunk2MySQL.status" # File with the latest _indextime moved
SPLUNK_DATA_FILE="currentData.csv" # File to write data to when fetched from Splunk 
LOG_FILE="Splunk2MySQL.log"

#
# Setup Logging
#
# Logging setup to log in 24 hour format and to default from from INFO level
#
logging.basicConfig(filename=LOG_FILE,level=logging.INFO,format='%(asctime)s, %(funcName)s, %(levelname)s, %(message)s', datefmt='%m/%d/%Y %H:%M:%S')

#
# Get a connection to Splunk and return the service object
#
def connectSplunk():
    try:
        logging.debug('Connecting to SplunkServer=\"'+str(SPLUNK_SERVER)+'\"')
        service = client.connect(host=SPLUNK_SERVER, port=SPLUNK_PORT, schema=SPLUNK_SCHEMA, username=SPLUNK_USER, password=SPLUNK_PASSWORD)
        logging.debug('Returning service for SplunkServer=\"'+str(SPLUNK_SERVER)+'\"')
        return service
    except Exception as e:
        logging.error('Unable to connect to SplunkServer=\"'+str(SPLUNK_SERVER)+'\" ExceptionType=\"'+str(type(e))+'\" Message=\"'+str(e)+'\"')

#
# Debug function to call to debug Splunk connection 
#
def testSplunkConn(service):
    content = service.info
    for key in sorted(content.keys()):
        value = content[key]
        if isinstance(value, list):
            print "%s:" % key
            for item in value: print "    %s" % item
        else:
            print "%s: %s" % (key, value)

    print "Settings:"
    content = service.settings.content
    for key in sorted(content.keys()):
        value = content[key]
        print "    %s: %s" % (key, value)

#
# Debug function to call to debug the Splunk Search configured
#
def testSplunkSearch(service):
    try:
        logging.debug('Parsing Splunk Search=\"'+str(SPLUNK_SEARCH)+'\"')
        service.parse(SPLUNK_SEARCH, parse_only=True)
        logging.debug('Done Parsing for SplunkSearch=\"'+str(SPLUNK_SEARCH)+'\"')
        return 0
    except HTTPError as e:
        logging.error('Unable to parse SplunkSearch=\"'+str(SPLUNK_SEARCH)+'\" ExceptionType=\"'+str(type(e))+ '\" Message=\"'+str(str(e))+'\"')
        return 1

#
# Call the Splunk search and write SPLUNK_DATA_FILE 
#
def searchSplunk(service):
	LAST_INDEX_TIME=readStatusFile()
	SEARCH=SPLUNK_SEARCH %(LAST_INDEX_TIME)
	logging.debug('Submitting Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	job = service.jobs.create(SEARCH)
	logging.debug('Done Submitting Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
    
	logging.debug('Checking Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	while True:
		stats = job.refresh()(
			'isDone',
			'doneProgress',
			'scanCount',
			'eventCount',
			'resultCount')
		progress = float(stats['doneProgress'])*100
		scanned = int(stats['scanCount'])
		matched = int(stats['eventCount'])
		results = int(stats['resultCount'])
		if stats['isDone'] == '1':
			break
		sleep(2)
	logging.debug('Done Checking Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	logging.debug('Reading Results for Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	results = job.results(output_mode="csv", field_list=SPLUNK_RETURN_FIELDS, count=0)
	COUNT=job.content.resultCount
	logging.info('Start Moving COUNT=\"'+job.content.resultCount+'\" from Splunk to MySQL')
	logging.debug('Done Reading Results for Search JOB=\"'+str(SEARCH)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	logging.debug('Writing CSV for Search JOB=\"'+str(SEARCH)+'\" to CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	csvFile = open(SPLUNK_DATA_FILE,mode='w')
	while True:
		content = results.read(1024)
		if len(content) == 0: break
		csvFile.write(content)
	csvFile.close()
	job.cancel()
	logging.debug('Done Writing CSV for Search JOB=\"'+str(SEARCH)+'\" to CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
	return COUNT

#
# Insert data into MySQL and write new status to STATUS_FILE
#
def writeToMySQL(COUNT):
    try:
        logging.debug('Connecting to MySQLServer=\"'+str(MYSQL_SERVER)+'\"')
        conn = pymysql.connect(host=MYSQL_SERVER, port=MYSQL_PORT, user=MYSQL_USER, passwd=MYSQL_PASSWORD, db=MYSQL_DATABASE, charset='utf8')
        logging.debug('Done Connecting to MySQLServer=\"'+str(MYSQL_SERVER)+'\"')
        cur = conn.cursor()

        INDEX_TIME=readStatusFile()
        OLD_INDEX_TIME=INDEX_TIME

        logging.debug('Opening CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
        csvFile=file(SPLUNK_DATA_FILE, mode="rb")
        logging.debug('Done Opening CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')

        csvFile.readline() # Skip first entry in file, this is the CSV headers

        logging.debug('Reading CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
        csvReader= csv.reader(csvFile)
        for data in csvReader:
            try:
                _indextime, _time, PersonCivilRegistrationIdentifier, UserIdentifier, UserIdentifierOnBehalfOf, HealthcareProfessionalOrganization, SourceSystemIdentifier, Activity, SessionId = data
                SQL_STATEMENT = MYSQL_INSERT_STATEMENT %(uuid.uuid4(),PersonCivilRegistrationIdentifier,UserIdentifier,UserIdentifierOnBehalfOf,HealthcareProfessionalOrganization,SourceSystemIdentifier,Activity,SessionId,_time)
                INDEX_TIME=_indextime
                logging.debug('Insert data into MySQL PersonCivilRegistrationIdentifier=\"'+str(PersonCivilRegistrationIdentifier)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
                cur.execute(SQL_STATEMENT)
                logging.debug('Done Insert data into MySQL PersonCivilRegistrationIdentifier=\"'+str(PersonCivilRegistrationIdentifier)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
            except Exception as e:
                logging.error('Unable Write to MySQLServer=\"'+str(MYSQL_SERVER)+'\" ExceptionType=\"'+str(type(e))+ '\" Message=\"'+str(str(e))+'\"')
                logging.error('Rolling Back Database Changes')
                conn.rollback()
                logging.error('Done Rolling Back Database Changes')
		logging.debug('Done Reading CSVFILE=\"'+str(SPLUNK_DATA_FILE)+'\" LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
	logging.info('Inserted data into MySQL OLD_LAST_INDEX_TIME=\"'+str(OLD_INDEX_TIME)+'\" NEW_LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
	writeStatusFile(INDEX_TIME)
	logging.debug('Closing Connection to MySQLServer=\"'+str(MYSQL_SERVER)+'\" OLD_LAST_INDEX_TIME=\"'+str(OLD_INDEX_TIME)+'\" NEW_LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
	conn.commit()
	logging.info('Moved COUNT=\"'+COUNT+'\" from Splunk to MySQL')
        logging.debug('Done Connection to MySQLServer=\"'+str(MYSQL_SERVER)+'\" OLD_LAST_INDEX_TIME=\"'+str(OLD_INDEX_TIME)+'\" NEW_LAST_INDEX_TIME=\"'+str(INDEX_TIME)+'\"')
        csvFile.close()

    except Exception, e:
        logging.error('Unable To connect to MySQLServer=\"'+str(MYSQL_SERVER)+'\" ExceptionType=\"'+str(type(e))+ '\" Message=\"'+str(str(e))+'\"')
        sys.exit(2)

    finally:
        logging.debug('Closing Connection to MySQLServer=\"'+str(MYSQL_SERVER)+'\"')
        cur.close()
        conn.close()
        logging.debug('Done Closing Connection to MySQLServer=\"'+str(MYSQL_SERVER)+'\"')

#
# Read STATUS_FILE and return LAST_INDEX_TIME
#
def readStatusFile():
    # Open the Status file that tells where we are in the indexing time
    LAST_INDEX_TIME = 0;
    if os.path.isfile(STATUS_FILE):
        logging.debug('LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" Existed')
        logging.debug('Reading LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\"')
        try:
            LAST_INDEX_TIME_FILE = open(STATUS_FILE,'r')
            LAST_INDEX_TIME = long(LAST_INDEX_TIME_FILE.readline())

        except IOError as e:
            logging.error('Unable To Read LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" ExceptionType=\"'+str(type(e))+ '\" Message=\"'+str(str(e))+'\"')
            sys.exit(2)
        finally:
            logging.debug('Done Reading LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\"')
            LAST_INDEX_TIME_FILE.close()
    else:
        logging.info('STATUS_FILE=\"'+str(STATUS_FILE)+'\" did not exist! Starting from zero')
    return LAST_INDEX_TIME

#
# Write new LAST_INDEX_TIME to STATUS_FILE
#
def writeStatusFile(LAST_INDEX_TIME):
    if LAST_INDEX_TIME > 0:
        try:
            logging.debug('Opening LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" for writing and reading OLD_LAST_INDEX_TIME')
            OLD_LAST_INDEX_TIME=readStatusFile()
            LAST_INDEX_TIME_FILE = open(STATUS_FILE,'w')
            logging.debug('Writing to LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" OLD_LAST_INDEX_TIME=\"'+str(OLD_LAST_INDEX_TIME)+'\" NEW_LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
            LAST_INDEX_TIME_FILE.write(str(LAST_INDEX_TIME))
            logging.debug('Done Writing to LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" OLD_LAST_INDEX_TIME=\"'+str(OLD_LAST_INDEX_TIME)+'\" NEW_LAST_INDEX_TIME=\"'+str(LAST_INDEX_TIME)+'\"')
            LAST_INDEX_TIME_FILE.close()
            logging.debug('Done Opening LAST_INDEX_TIME_FILE=\"'+str(STATUS_FILE)+'\" for writing and reading OLD_LAST_INDEX_TIME')
        except IOError as e:
            logging.error('Error writing LAST_INDEX_TIME=\"' + str(LAST_INDEX_TIME) + '\" to LAST_INDEX_TIME_FILE=\"' + LAST_INDEX_TIME_FILE + '\"')
            sys.exit(2)
#
# Define Main Flow
#		
def main(argv):
	try:
		logging.info('Starting Run at TIME=\"'+str(datetime.now().isoformat(' '))+'\"')
		service = connectSplunk()
		testSplunkSearch(service)
		COUNT = searchSplunk(service)
		writeToMySQL(COUNT)
	except Exception as e:
		logging.debug('Exception under Run - ExceptionType=\"'+str(type(e))+ '\" Message=\"'+str(str(e))+'\"')
	finally:
		logging.info('Finished Run at TIME=\"'+str(datetime.now().isoformat(' '))+'\"')

if __name__ == "__main__":
    main(sys.argv[1:])
