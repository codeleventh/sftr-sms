# sftr assigment

Требуется создать веб сервис для валидации сотовых телефонов.  
Дано:

* Веб Сервис будет использоваться бэкендом какой-то системы и не будет доступен
  напрямую из фронта
* Цель веб сервиса - валидировать телефон, при помощи отправки по SMS
  случайного кода и последующей проверки его. Код не должен быть длинным
  (нужно, чтобы он был 4-6 цифр)
* Считаем, что сервису доступен компонент, который умеет отправлять смс. Для
  целей задания достаточно, чтобы этот компонент просто писал текст и адрес в
  консоль
* Сервису не нужно работать с persistent store, достаточно все держать в памяти
  Сервис нужно спроектировать, реализовать и документировать его REST запросы.
  Что хочется от этого сервиса:
* Исключить валидацию телефона пользователем, которому телефон не
  принадлежит. Несмотря на любые его действия.
* Исключить ситуации, когда в результате сделанной им ошибки, пользователь
  вынужден обращаться в службу поддержки и не может более провалидировать
  телефон самостоятельно.
* Минимизировать возможности третьего лица, знающего мой телефон, создать мне
  неудобства при работе с системой.

На что смотрим:

* Выполнение условий, описанных выше
* Четкость и внятность кода
* Оформление в проект, удобный для понимания, сборки, запуска

<details>
  <summary>Solution</summary>

Давайте разбираться.  
По тексту я вижу, что ответственность сервиса — убедиться, что пользователь действительно имеет доступ к номеру. Сервису
не нужно знать про пользователей, права или роли — достаточно лишь отличать их друг от друга. Проблемы, не касающиеся
непосредственно подтверждения, лежат на использующем сервис бэке — например, разрешение ситуации «два юзера подтвердили
один и тот же номер», «юзер потерял доступ к номеру и хочет привязать другой». Это его логика.

Во-первых, мы включаем авторизацию, не давая злоумышленнику слать запросы от лица кого-то другого (просто скрывать адрес
сервера — не защита). Авторизуется именно сервис-потребитель, не пользователь.  
Логин и пароль вхардкожены в конфигу, но их можно унести в проперти, БД, наконфигурить упомянутый на собеседованиии JWT.

У нас есть два метода:

1. `sendCode(phoneNumber, userId)`
2. `verifyCode(code, phoneNumber, userId)`

Сервис-потребитель проставляет в запросы id пользователя, какой бы он они был (и потому он типа ``String``). Id мы
запоминаем, чтобы не дать злоумышленнику выполнить verify вслед за send от подлинного пользователя, потратив его
попытки.

Внутри мапка с номером и информацией о процессе подтверждения (у нас не хайлоад, она может переварить достаточно
пользователей, база в памяти пока не нужна).

У сервиса есть настраиваемое параметром время, которое не дает злоумышленнику флудить SMS-сообщениями на чужой номер, но
и не делает больно пользователю, который просто опечатался при вводе собственного.

Если мы решим интегрироваться с несколькими сервисами сразу, нужно будет различать запросы одного пользователя из разных
сервисов (я могу захотеть прикрепиться везде и сразу без пауз).  
Самый простой разумный способ — ввести дополнительное поле в оба запроса, и делать конкатенацию при работе с мапкой,
использовать в качестве ключа что-то вроде «9139159184_DOSTAVKARU», хэшмапа это стерпит.
</details>