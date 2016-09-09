all:
	javac RoutingMapTree.java Myset.java assn3checker.java Exceptions.java
	java assn3checker

clean:
	rm *.class
