# Simple Makefile for Processing Libraries
# (c) bitcraftlab 2013

NAMESPACE=bitcraftlab
NAME=logo

LIBRARY=library/$(NAME).jar
JARFILE=build/$(NAME).jar
SOURCES=src/$(NAMESPACE)/$(NAME)/*.java
CLASSES=build/$(NAMESPACE)/$(NAME)/*.class
P5_PATH=/Applications/Processing/Processing-1.5.1.app/Contents/Resources/Java
P5_JARS=$(P5_PATH)/core.jar
NL_JARS=assets/app/NetLogo.jar:assets/app/lib
ASSETS=assets/app/*

## NetLogoLite does not work yet.
# NL_JARS=assets/lite/NetLogoLite.jar
# ASSETS=assets/lite/*

install:	$(LIBRARY)
build:		$(JARFILE)
compile:	$(CLASSES)

$(LIBRARY):	$(JARFILE)
	mkdir -p library
	cp $(JARFILE) $(LIBRARY)
	cp -af $(ASSETS) library

$(JARFILE):	$(CLASSES)
	jar -cf $(JARFILE) -C build $(NAMESPACE)

$(CLASSES):	$(SOURCES)
	mkdir -p build
	javac -cp "${P5_JARS}:${NL_JARS}" -d build ${SOURCES}

clean:
	rm -rf build/*

