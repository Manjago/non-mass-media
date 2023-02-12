# Трансляция https://снми.рф/ в телеграм-канал

Использование:

Через крон настроить периодический запуск

```shell
java -jar non-mass-media-1.0.0.jar /home/user/nmm.properties
```

Пример содержимого /home/user/nmm.properties

```properties
next.article.id.provider.data.file=/home/user/nnm.data.txt
worker.sleep.interval=5000
telegram.poster.bot.token=xxx:yyy
telegram.poster.bot.channel.id=-111111
telegram.poster.bot.admin.id=111111
```

| Параметр                           | Смысл                                                                                                                        |
|------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| next.article.id.provider.data.file | Текстовый файл с данными, в котором лежит номер последней отосланной статьи. В самом начале работу там должно быть -1        |
| worker.sleep.interval              | Интервал между постингом статей в канал, в миллисекундах (при работе приложение пытается запостить все статьи, которые были) |
| telegram.poster.bot.token          | Секретный токен бота                                                                                                         |
| telegram.poster.bot.channel.id     | Идентификатор канала, в который постим статьи                                                                                |
| telegram.poster.bot.admin.id       | Идентификатор админа, которому жалуемся на исключения                                                                        |
