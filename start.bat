gradlew.bat war

if errorlevel 0 (
    java -jar payara-micro-5.182.jar --port {{port}} --deploy build/libs/{{archiveName}} --addlibs /Applications/MATLAB/MATLAB_Runtime/v91/toolbox/javabuilder/jar/javabuilder.jar
)