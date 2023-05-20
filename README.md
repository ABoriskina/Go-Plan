# Go Plan App
Приложение Go Plan разработано под Android платформу для версии SDK >= 33.
Возможные разрешения: отправка уведомлений.
<br>
## Описание
Go Plan - приложение планер, в функционал которого входит:
1. Создание событий;
2. Управление событиями - удаляйте или делитесь сообщениями со знакомыми;
3. Функция отправки сообщений для привлечения пользователей;
4. Система уведомлений, которая позволяет пользователям поддерживать контакт друг с другом. Система также рассчитана на отправку уведомлений от лица приложения. 
<br>

## Стек технологий
Firebase Authentication, Firebase Realtime Database, Firebase Cloud Messaging, Gradle.
Среда разработки - IntelliJ IDEA с Android Studio Plugin. Язык - Java.
<br>
## Превью

### Примечание:
<br>
Авторизация пользователя реализована с помощью Shared Preferences, что обеспечивает удобство пользования приложением. Достаточно ввести свои данные единожды, 
факт успешной авторизации сохраняется на устройстве до тех пор, пока пользователь не нажмет "Выход".

<br>

### Вход в приложение

<br>
Вход в приложение производится за счет Firebase Authentication. Установленный провайдер - электронная почта. Пароли пользователей не хранятся в прямом виде на стороне сервера, хранятся их хэши, 
при авторизации хэши вычисленный и хранящийся сравниваются.

<br>

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-090757_Go%20Plan.jpg)

<br>

### Регистрация пользвователя

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-090805_Go%20Plan.jpg)

<br>

### Главная страница приложения

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091149_Go%20Plan.jpg)

<br>

### Создание событий

В реализации приложения используется Realtime Database от Firebase. Одна из ключевых особеннстей работы с этой базой данных - модель NoSQL. Данные на сервере хранятся в формате json.
Для обмена данными между клиентом и сервером используются Database References.

<br>

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091237_Go%20Plan.jpg)

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091241_Go%20Plan.jpg)

### Управление событиями

<br>

События можно удалять и отправлять другим пользователям.

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091245_Go%20Plan.jpg)

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091249_Go%20Plan.jpg)

### Отправка приглашения на событие
<br>

Для того, чтобы поделиться событием с другим пользователем, необходимо ввести его электронную почту. 

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091255_Go%20Plan.jpg)

В случае, если такой пользователь найден, текущий пользователь получит уведомление о том, что событие было успешно отправлено:

<br>

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091313_Go%20Plan.jpg)

<br>

В противном случае придет уведомление о том, что такого пользователя не найдено:

<br>

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091814_Go%20Plan.jpg)

### Получение уведомлений

<br>

Просмотреть свои уведомления пользователь может нажав на иконку звоночка. При желании можно добавить событие, которым с Вами поделились, себе.

![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/Screenshot_20230520-091437_Go%20Plan.jpg)

<br>

### Получение запланированных уведомлений
<br>
Реализуется с помощью Cloud Messaging. В личном кабинете разработчика можно настраивать частоту отправления уведомлений на устройства пользователей, а также задавать разовую отправку.
<br>
![Image alt](https://github.com/ABoriskina/Go-Plan/blob/master/pictures/20230520_172804.jpg)
