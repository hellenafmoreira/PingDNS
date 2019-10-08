# PingDNS
O código tem como função fazer a comunicação entre dois clientes, usando um servidor DNS que possui menor tempo de resposta.
Deve ser iniciado fazendo um start na classe ServidorDNS. Deve ser executado o código em dois hosts diferentes,para rodar dois servidores, tornando assim possível comparar o tempo de resposta de cada um. 
Após os servidores estarem ativados, é necessário iniciar a classe RMIClient em ambos hosts. Dessa forma o pingClient irá pingar 10 vezes nos dois servidores ativos, e retornará o ip do host com menor tempo de resposta. Assim a comunição será estabelecida por meio do servidor DNS mais rápido, tornando possível a troca de mensagens entre os clientes.
