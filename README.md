### Compilar Parte1:

- Para crear los .class: (desde _src/parte1/_)

    javac -d bin/ *.java

- Para ejecutar el __SERVIDOR__: (desde _src/_)

    java -cp "parte1/bin" parte1.Servidor

- Para ejecutar el __CLIENTE__: (desde _src/_)
    
    java -cp "parte1/bin" parte1.Cliente
    

cd src/parte1
javac -d bin/ *.java
cd ../
java -cp "parte1/bin" parte1.Servidor


java -cp "parte1/bin" parte1.Cliente
