## Алгоритм работы

Сервер может работать в трех состояниях:
* FOLLOWER. Принимает heartbeat от лидера
* CANDIDATE. Рассылает vote запросы другим нодам
* LEADER. Рассылает heartbeat запросы другим нодам.

На сервере запущены два вида таймеров:
* ElectionTimer. Для запуска голосования. Сбрасывается при получении heartbeat от сервера. Настраивается с помощью 
  параметра -e, --election-timeout
* HearbeatTimer. Для отправки heartbeat запроса фоловерам. Настраивается с помощью параметра -h, --heartbeat-timeout

Если сервер не получает heartbeat от лидера, то он инициирует выборы. Для того повышает номер раунда, и рассылает 
запросы на голосование. Если он соберет необходимое число голосов (кворум), то он становится лидером и начинает
рассылать heartbeat запросы.
Кворум определяется как ((число сервисов)/2)+1

## Запуска с помощью docker-compose
```shell
docker-compose up --build
```

## Запуска без docker-compose
В application.properties в services прописать ноды в кластере (host и port для каждого сервера) 
после чего собрать приложение и запустить

### Сборка
```shell
mvn org.apache.maven.plugins:maven-assembly-plugin:assembly
```

### Запуск
```shell
java -jar ./destributedService-1.0-SNAPSHOT.jar -p 9090 -h distrib-1 -election 10 -heartbeat 5 -n 1
java -jar ./destributedService-1.0-SNAPSHOT.jar -p 9091 -h distrib-2 -election 10 -heartbeat 5 -n 2
java -jar ./destributedService-1.0-SNAPSHOT.jar -p 9092 -h distrib-3 -election 10 -heartbeat 5 -n 3
```

### Тестовый сценарий:
#### Предусловие.
После того как все экземпляры поднимуться они будут находиться в состоянии FOLLOWER и по истечении срока election-timer 
будут произведены выборы. В случае удачного завершения выборов, кто-то из кандидитов станет LEADER

#### Тест:
Отключить лидера с помощью команды

```shell
docker-compose stop <service-name>
```
<service-name> - Лидер-нода. необходимо определить по логам приложений по строке "Current state: LEADER".

#### Результат:
Произведены новые выборы и определен новый лидер.

#### Тест:
Вернуть отключенную ноду в кластер

#### Результат:
Нода снова в кластере

## Параметры запуска
```text
-e,--election-timeout <arg>    Election timeout 
-h,--heartbeat-timeout <arg>   Heartbeat timeout
-host,--host <arg>             service host
-n,--node-id <arg>             ID Node
-p,--port <arg>                service port
```

