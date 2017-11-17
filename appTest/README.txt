Aquesta aplicacio permet fer un test del sistema.
Per al seu correcte funcionament cal que:

1. Configurar $TOMCAT/conf/server.xml (veure més abaix)
1. La conexió VPN estigui oberta (openvpn)
2. L'accés a la Base de Dades estigui obert (ssh)
3. El servidor TOMCAT estigui encès (tomcat)
4. Executar l'aplicacio
5. Tornar a configurar $TOMCAT/conf/server.xml



NOTA: Configurar
Aquesta aplicació no està signada ni posseeix els certificats necessaris
per a executar-se en SSH. Per tant cal obrir a tomcat el port 8080 HTTP.
Per fer-ho només cal descomentar del fitxer $TOMCAT/conf/server.xml les linees:

    <!--- Define a non-SSL HTTP/1.1 Connector on port 8080 -->
    <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />

Obviament, això pot vulnerar la seguretat del sistema així que en acabar cal
tornar a deshabilitar el port 8080 comentant les linees anteriors:

    <!--- Define a non-SSL HTTP/1.1 Connector on port 8080 -->
    <!---
    <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
    -->

