INSTRUCCIONS PER A CONFIGURAR EL TOMCAT EN SSL

1. Obrir un terminal
2. Arribar al directori on es troba aquest fitxer README 
cd .../SSL/README.txt)

3. Executar les següents comandes:

cp .keystore $TOMCAT/conf
cp server.xml $TOMCAT/conf

on $TOMCAT es la direccio a l'arrel del vostre TOMCAT.


INSTRUCCIONS PER A UTILITZAR EL TOMCAT EN SSL
- El funcionament es el mateix que abans però ara al port 8443 TCP/IP.
Per tant:
https://localhost:8443/DNIe-LectionWeb/index.html
