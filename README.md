# ATM application

## Описание
Это приложение предназначено для симуляции банкомата

## Запуск
Необходимо запустить класс `Main.Java`.

## Запуск .bat файла 
Перед тем как запускать `runAtmApp.bat` необходимо запустить команду `mvn package` для того, чтобы сгенерировался jar файл.

## Взаимодействие с программой
Инетерфейс построен на вводе пользователем чисел, за которые отвечают определённые команды и после выбора команд будут запрошены необходимые данные для ввода.
1. Выбор создать новую карту или зайти в существующую либо выйти из банкомата, что приведёт к сохранению данных.
2. При выборе создания карты будет запрошен номер карты и её pin-код.
3. При выборе существующей карты необходимо ввести номер и pin-код карты и после успешной авторизации будет доступно просмотр баланса, поплнение либо снятие со счёта, а так же выход (достать карту) что приведёт к сохранению данных.
4. После создании карты или взаимодействия с существующей необходимо выбрать команду **выход** либо **выход (вытащить карту)**  для того чтобы все новые данные записались в файл `cards.txt`.

## Пояснение классов
+ Card - для представления и управления информацией о банковской карте
+ CardRepository - для хранения и управления различными данными карт
+ BankService - реализация бизнес логики для карт
+ AtmConsole - логика для управления вводом пользователя и отработкой комманд
+ ConsoleUI - для хранения текстового представления комманд в консоли
+ Main - запуск приложения, в нём происходит создание экземпляров основных классов
