#!/bin/bash
javac Life.java
mkdir docs
mkdir -p edu/rpi/cs/cs4963/u19/wardz2/hw02/gol_gui
mv *.class edu/rpi/cs/cs4963/u19/wardz2/hw02/gol_gui
javadoc -private -d ./docs *.java
java edu.rpi.cs.cs4963.u19.wardz2.hw02.gol_gui.Life
