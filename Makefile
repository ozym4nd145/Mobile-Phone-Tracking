all:
	javac Exchange.java Myset.java MobilePhone.java assn2checker.java
	java assn2checker

clean:
	rm *.class
