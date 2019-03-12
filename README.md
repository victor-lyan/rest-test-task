## Тестовое задание для ООО Арт План Софтвер

TestTaskReverseString.jar это файл с заданием разворота строки.

Для задания использовалась база данных Postgresql.
```
dbname: rest_db
username: rest_user
password: rest_password
```
Перед началом работы нужно создать базу данных и пользователя с данными, указанными выше.
Чтобы запустить скрипты для создания таблиц и начальных данных, нужно в файле `application.properties`
раскомментировать строчку
```
# spring.datasource.initialization-mode=always
```
во время первого запуска.