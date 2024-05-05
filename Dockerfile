# Этап сборки
FROM gradle:8.3.0-jdk20 AS build

WORKDIR /app

# Копируем только необходимые файлы для сборки
COPY app/build.gradle .
COPY app/settings.gradle .
COPY app/src ./src

# Собираем проект
RUN ./gradlew build

# Этап запуска
FROM eclipse-temurin:20-jre

WORKDIR /app

# Копируем собранный JAR-файл из этапа сборки
COPY --from=build /app/build/libs/*.jar app.jar

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
