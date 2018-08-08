#!/bin/bash
./gradlew war
if [ $? -eq 0 ]
then
    java -jar payara-micro-5.182.jar --port 8080  --deploy build/libs/matlab-webified-1.0.0.war --addlibs "/Applications/MATLAB/MATLAB_Runtime/v91/toolbox/javabuilder/jar/javabuilder.jar" 
fi