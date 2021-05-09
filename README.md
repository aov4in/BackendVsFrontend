### Для запуска данной сборки необходимо выполнить следующие действия:

1. Запустить Docker.
2. Для выполнения задачи потребуется образ MySQL 8, для скачивания образа в терминале выполняем команду `docker pull mysql:8.0` .
3. Для создания Docker Container создаем файл docker-compose.yml, в нем прописываем создание БД с помощью артефакта ./mysql_artifacts/schema.sql, пользователя, пароля.
4. Создаем Docker Container: в терминале необходимо выполнить команду `docker-compose up -d`
5. Запускаем SUT через терминал с помощью команды `java -jar ./artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://0.0.0.0:3306/app -P:jdbc.user=vasya -P:jdbc.password=qwerty123`
6. Запускаем тесты в терминале с помощью команды `gradlew clean test`