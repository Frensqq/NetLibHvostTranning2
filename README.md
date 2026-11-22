# NetLib «Хвост»

Сетевая библиотека для Android 14+ и сервера записи питомцев к ветеринару. Модуль `:netlib` реализует все 16 REST-операций спринта А. Модуль `:app` подключает библиотеку как зависимость.

## Сборка и запуск

Откройте корень проекта в Android Studio с Android SDK 37 и JDK 25. Из корня выполните:

```sh
./gradlew :netlib:assembleRelease :app:assembleDebug :netlib:testDebugUnitTest
```

Запустите конфигурацию app на Android 14 или новее. Тест реального SharedPreferences запускается командой `./gradlew :netlib:connectedDebugAndroidTest` при подключённом устройстве.

## Подключение

Подключите каталог модуля в settings.gradle.kts через `include(":netlib")`. Если библиотека находится в соседнем проекте, укажите путь `project(":netlib").projectDir = file("../NetLibHvostTranning2/netlib")`. В build.gradle.kts приложения добавьте `implementation(project(":netlib"))`. Для подключения исходного модуля используйте плагины и version catalog из этого проекта.

Запустите сервер из комплекта «Хвост». Базовый адрес из стандартного эмулятора Android — `http://10.0.2.2:8090/api/`, с физического устройства — `http://<IP компьютера>:8090/api/`. Для локального HTTP отладочный вариант приложения разрешает открытый трафик.

```kotlin
val net = HvostNetLib(applicationContext, "http://10.0.2.2:8090/api/")
// В корутине
val result = net.catalogRepository.getService()
when (result) {
    is NetworkResult.Success -> println(result.data.items.size)
    is NetworkResult.Error -> println(result.errorResponse.message)
    is NetworkResult.ErrorSerialization -> println(result.message)
    NetworkResult.NoInternet -> println("Нет сети")
    NetworkResult.Timeout -> println("Время ожидания истекло")
}
// Владелец закрывает клиент при завершении работы с библиотекой.
net.close()
```

## Контракты

- AuthRepository отвечает за регистрацию, вход и обновление токена. Стандартное хранилище использует SharedPreferences. В HvostNetLib можно передать собственный TokenStorage.
- UserRepository читает профиль и обновляет отдельные поля или аватар.
- CatalogRepository возвращает услуги, клиники и врачей. Метод doctorsForService принимает ID услуги.
- PetRepository читает список, создаёт, изменяет и удаляет питомца. Фото передаётся как UploadFiles.
- AppointmentRepository загружает слоты, создаёт и отменяет записи. Метод appointmentsFor принимает период и параметры страницы. В expand приходят связанные данные для карточки записи.

Публичные модели и интерфейсы находятся в domain. Слой data использует Ktor Client 2.3.12 с движком OkHttp, kotlinx.serialization для JSON и Kotlin coroutines. HvostNetLib собирает зависимости и управляет HTTP-клиентом.

## Проверка на сервере

Запустите отдельный экземпляр сервера с тестовой базой. Команда ниже создаёт временную учётную запись, изменяет профиль, загружает фото, создаёт и отменяет запись, затем удаляет питомца. Учётная запись остаётся в тестовой базе. Адрес должен указывать на localhost или 127.0.0.1.

```sh
HVOST_TEST_URL=http://127.0.0.1:8090/api/ ./gradlew :netlib:testDebugUnitTest --tests '*HvostLiveTest' --rerun-tasks
```

Без переменной HVOST_TEST_URL Gradle пропускает серверный сценарий. Остальные JVM-тесты выполняются автономно.
