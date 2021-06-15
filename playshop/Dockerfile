FROM ubuntu:18.04

ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8

RUN apt-get clean
RUN apt-get update
RUN apt-get install -y wget vim zip unzip curl gnupg

RUN apt-get install -y openjdk-8-jdk

RUN wget https://downloads.lightbend.com/scala/2.12.12/scala-2.12.12.deb
RUN dpkg -i scala-2.12.12.deb
RUN rm scala-2.12.12.deb

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get update && apt-get install -y sbt

EXPOSE 9000
EXPOSE 8080

RUN useradd -ms /bin/bash user 
RUN usermod -aG sudo user 
USER user 

WORKDIR /home/user/

RUN git clone https://github.com/t0biash/e-business.git
WORKDIR /home/user/e-business/playshop

RUN sbt compile 

CMD sbt run
