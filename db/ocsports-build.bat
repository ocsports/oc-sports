REM ### move to the ingestion root directory where the Maven pom.xml file exists ###
cd C:\Paul\GitHub\oc-sports

REM ### build the latest ingestion app ###
start /wait mvn clean package

REM ## copy output to Apache webapps folder
xcopy "target\oc-sports" "C:\InstalledSoftware\Apache Tomcat 4.1\webapps\oc-sports" /E /Q /Y
