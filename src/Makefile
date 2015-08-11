#MakeFile for Assignment_1
#Tinotenda Chemvura
#10 August 2015

JFLAGS = -g
JAVAC = javac

SOURCES = Main.java \
		Filter.java \
		FileUtil.java \
		MeanFilter.java \
		Tests.java

.SUFFIXES: .java .class

CLS= $(SOURCES:.java=.class)

all: $(CLS)

.java.class:
	$(JAVAC) $(JFLAGS) $<

clean:
	@rm -f  $(SOURCES:.java=.class)
