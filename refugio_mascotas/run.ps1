
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8"

mvn clean compile exec:java

Read-Host "Presione Enter para salir..."