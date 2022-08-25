#Logs Scanner

## Requisites
- JDK 1.8+
- Maven (Tested on 3.8.5)


#Build Procedure

Build the project first by running

	mvn clean package
	
Dependencies should download then, to run the tool, do;

	java -jar ./target/logscanner-0.0.1-SNAPSHOT.jar <LOG_FILE_PATH>

For example;

	java -jar ./target/logscanner-0.0.1-SNAPSHOT.jar /Users/mwangigikonyo/Documents/Food4Education/InterviewCaseStudy/server.log
	

#Output
There will be a new file created at the root of this project called

	re