# CPU5007_OOM_1st_sem

## **Prerequisites**
<br>
Ensure the following are installed:<br>

Java Development Kit (JDK) (version 11)<br>
Maven for dependency management<br>
An Integrated Development Environment (IDE) like IntelliJ IDEA<br>
JavaFX SDK - Version: 19.0.2<br><br>
## **How to Configure JavaFX**
<br>
Step-by-Step Guide<br><br>
01.Copy the following command and save it in your notepad:<br><br>
--module-path "replace path" --add-modules javafx.controls,javafx.fxml<br><br>
02.Open the folder where your JavaFX SDK is stored.<br><br>
03.Right-click on the lib folder inside the SDK directory and copy its path.<br><br>
04.Replace "replace path" with your lib folder path:<br><br>
Example updated path:<br>
--module-path "C:\FX\openjfx-19.0.2.1_windows-x64_bin-sdk\javafx-sdk-19.0.2.1\lib" --add-modules javafx.controls,javafx.fxml<br><br>
05.Open the "Edit Configuration" tab in IntelliJ IDEA:<br><br>
06.Click the dropdown next to the application runner in IntelliJ IDEA.<br><br>
07.Select "Edit Configuration".<br><br>
08.Click "Modify Options".<br><br>
09.Select "Add VM Options".<br><br>
10.Paste your path into the "VM Options" field:<br><br>
Copy the path saved in your notepad and paste it into the "VM Options" field.<br>
11.Click "Apply" and then "OK".<br><br>
12.Run your project to ensure the configuration is successful.<br>
