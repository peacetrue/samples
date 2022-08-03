#!/usr/bin/env sh

java -jar build/libs/runnable-jar-1.0.0-SNAPSHOT-all.jar &

scp build/libs/runnable-jar-1.0.0-SNAPSHOT-all.jar root@192.168.150.29:/root
scp /Users/xiayx/Downloads/jdk-8u191-linux-x64.tar.gz root@192.168.150.29:/root
#export JAVA_HOME="/root/jdk1.8.0_191"
#export PATH="$JAVA_HOME/bin:$PATH"
ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.150.29
scp root@192.168.150.29:/root/pmap.txt src/test/resources
scp root@192.168.150.29:/root/nmt.txt src/test/resources
