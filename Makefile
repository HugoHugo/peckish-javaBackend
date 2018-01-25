#Makefile for Backend server

.SUFFIXES: .class .java

.java.class:
	javac $*.java

default: Backend.class Sender.class Worker.class ManipulateDatabase.class Message.class Ingredients.class Librarian.class

javadoc:
	cd doc; javadoc -author -package *.java

javadoc-local:
	cp Backend.java doc/Backen
	cp Message.java doc/Message.java
	cp Worker.java doc/Worker.java
	cp Sender.java doc/Sender.java
	cd doc; javadoc -author -package *.java

setup:
	wget http://repo1.maven.org/maven2/com/squareup/okhttp3/okhttp/3.9.1/okhttp-3.9.1.jar
