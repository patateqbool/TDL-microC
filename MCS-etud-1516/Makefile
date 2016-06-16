#--------------------------------------------------------
# la grammaire (voir src)
XLANG=MCS
ALANG=AASM
#--------------------------------------------------------
# repertoires contenant egg
EDIR=.
# les jars associes
GJAR=$(EDIR)/eggc-5.3.1.jar:.
#--------------------------------------------------------
# java, javac, jar
#JDIR=/usr/local/jdk1.6/bin
JDIR=/usr/bin
#--------------------------------------------------------
all : src att class

src: lang asm

lang:
	(cd mcs ; $(JDIR)/java -cp ../$(GJAR) mg.egg.eggc.compiler.EGGC $(XLANG).egg)

asm:
	(cd mcs ; $(JDIR)/java -cp ../$(GJAR) mg.egg.eggc.compiler.EGGC $(ALANG).egg)

att :
	$(JDIR)/javac -classpath $(GJAR) mcs/symtab/*.java
	$(JDIR)/javac -classpath $(GJAR) mcs/gc/*.java
	$(JDIR)/javac -classpath $(GJAR) mcs/compiler/*.java
	$(JDIR)/javac -classpath $(GJAR) mcs/obj/*.java
	$(JDIR)/javac -classpath $(GJAR) mcs/util/*.java

class :
	$(JDIR)/javac -classpath $(GJAR) mcs/egg/*.java

clean :
	rm -rf $(PACKAGE)
	rm -f mcs/symtab/*.class
	rm -f mcs/obj/*.class
	rm -f mcs/gc/*.class
	rm -f mcs/compiler/*.class
	rm -f mcs/util/*.class
	rm -f mcs/tests/*.class
	rm -rf mcs/egg/*

