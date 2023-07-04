#
GNAME= ul
GSRC= $(GNAME).g

all: grammar compiler

grammar: $(GSRCS)
	java org.antlr.Tool -fo . $(GSRC) 

compiler:
	javac **/*.java
	javac *.java
	


clean:
	rm *.class $(GNAME)*.java $(GNAME).tokens


 
