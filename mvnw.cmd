@ECHO OFF
SETLOCAL
SET MAVEN_WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
SET BASEDIR=%~dp0.
SET MAVEN_USER_HOME=%~dp0.mvn\cache
IF NOT DEFINED JAVA_HOME GOTO execute
SET JAVA_EXEC=%JAVA_HOME%\bin\java.exe
GOTO init
:execute
SET JAVA_EXEC=java.exe
:init
"%JAVA_EXEC%" "-Dmaven.multiModuleProjectDirectory=%BASEDIR%" -classpath "%BASEDIR%\.mvn\wrapper\maven-wrapper.jar" %MAVEN_WRAPPER_LAUNCHER% %*
ENDLOCAL
