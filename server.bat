@echo off

rem Cool development server downloader for quickly spinning up and running servers by Mellorj/RDPolarity
rem 
rem jq is required for this script to work (https://stedolan.github.io/jq/download/)
rem 
rem usage: ./server.bat <version>
rem 
rem Running this script will:
rem - download the latest version of paper if it hasn't already
rem - create a new folder under ./servers/{version}/server.jar
rem - quickly check your eula for you ;)
rem - give the specified player (%uuid% and %username%) operator
rem - BAM!! instant development server up and running

set version=%1
set api=https://papermc.io/api/v2
set uuid=cf9e8121-b946-43d4-8a14-3ce6b66b3616
set username=mellorj

if "%version%" == "" (
 echo please specify a version! eg. server.bat 1.16.4
 exit
)

rem get the latest build number for the specified %version% using jq
for /F "Delims=" %%A in ('"curl -sS https://papermc.io/api/v2/projects/paper/versions/%version%/builds | jq ".builds [-1].build""') do (
 set build=%%A
)

rem rem check if a build for that version exists
if %build% == null (
 echo [i] No builds found for %version%, enter a valid version number
 exit
)

rem check if the servers folder exists, otherwise create it
if exist servers\ (
 echo [i] servers folder found
) else (
 echo [+] creating ./servers/ folder
 mkdir "servers"
)

rem check if the version file exists, otherwise create it
if exist servers\%version% (
	echo [+] %version% folder found
) else (
	echo [+] creating ./servers/%version%/ folder 
	mkdir "servers/%version%"
)

rem check to see if the server,jar file exists, otherwise download it 
if exist servers\%version%\server.jar (
	echo [i] found existing server.jar
) else (
 echo [+] no server.jar found, downloading the latest version of paper for %version%
	call :downloadVersion
)

echo [i] running %version% server!

call :runServer

rem ======= FUNCTIONS =======

goto:eof

:runServer
 cd servers/%version%
 java -Xmx1G -jar server.jar --nogui
goto :eof

:downloadVersion  
 rem build the download URL using infomation gathered from %build% and %version%
 set download_url=%api%/projects/paper/versions/%version%/builds/%build%/downloads/paper-%version%-%build%.jar
 echo %download_url%
 
 rem download the server jar in the respective version directory
 cd servers/%version%
 curl -o ./server.jar %download_url%

 rem cheeky eula true setter
 (echo eula=true) > eula.txt
 
 rem op the specified %username% and %uuid% on the server
  (
   echo [
   echo {
   echo  "uuid": "%uuid%",
   echo  "name": "%username%",
   echo  "level": 4
   echo }
   echo ]
  ) > ops.json

 rem chanrem change the motd to something more developer friendly (doesn't seem to work)
 rem (echo 'motd=\u00A7c[%version%] Development Server') > server.properties
goto :eof