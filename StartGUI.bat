@echo off
java --module-path ./KMeansGUI/javafx-sdk-21.0.2/lib --add-modules javafx.controls,javafx.fxml -jar "./KMeansGUI/out/artifacts/KMeansGUI_jar/KMeansGUI.jar" localhost 8080
