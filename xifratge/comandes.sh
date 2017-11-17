# Generar clau privada RSA de 4096 bits, xifrada amb AES de 256 bits
# La contrasenya és: uQwb7BbXG4P/.MedgQNDRwkTcI
openssl genrsa -aes256 -out server_private.pem 4096

# Convertir la clau a format PKCS#8 per a que Java ho llegeixi
# Atenció: server_private.der no estarà xifrat amb AES
openssl pkcs8 -topk8 -inform PEM -outform DER -in server_private.pem -out server_private.der -nocrypt

# Generar clau pública de la clau RSA
openssl rsa -in server_private.pem -pubout -out server_public.pem

# Generar clau pública en format DER
openssl rsa -in server_private.pem -pubout -outform DER -out server_public.der

# Posar les claus al applet
cp *der ../DNIe-LectionWeb/appletVotacio/
