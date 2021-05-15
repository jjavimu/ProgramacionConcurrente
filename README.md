PARTE 1: 

SERVIDOR:
cd src/parte1
javac -d bin/ *.java
cd ../
java -cp "parte1/bin"  parte1.Servidor

CLIENTE
java -cp "parte1/bin"  parte1.Cliente
