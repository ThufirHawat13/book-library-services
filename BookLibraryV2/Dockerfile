# Указываем базовый образ для приложения
FROM bellsoft/liberica-openjdk-alpine:17
# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app
# Копируем jar из локальной директории /target в директорию /app внутри контейнера
COPY target/BookLibraryV2-0.0.1-SNAPSHOT.jar app/BookLibraryV2.jar
# Задаем команду, которая будет выполнена при запуске контейнера
CMD ["java", "-jar", "app/BookLibraryV2.jar"]