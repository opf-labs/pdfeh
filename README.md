PDF/Eh?
=======
PDF/Eh? - A JavaFX GUI wrapper for Apache PDFBox Preflight &amp; a Good Excuse(tm) to use Java 7.

<font color="red">**NOTE: this project is currently a proof-of-concept and was coded at a SPRUCE Developers' Workshop.  It is highly likely to do unexpected things/crash/eat your lunch**</font>

Details can be found here: http://www.openplanetsfoundation.org/blogs/2013-03-15-pdf-eh-another-hackathon-tale

Compiling
---------
To compile you need the following installed:

    - Apache Maven
    - Oracle JDK7 (this includes JavaFX) - OpenJDK7 will not work as it does not include JavaFX

On the command line issue the following commands to compile the jar:

    - cd pdfeh/
    - mvn clean jfx:jar

Running
-------
Now that the jar is compiled you can run it by issuing the following commands:

    - cd target/jfx/app
    - java -jar pdfeh3F-0.0.1-SNAPSHOT-jfx.jar
    
    
