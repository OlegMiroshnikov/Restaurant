## Выпускной проект Topjava

----
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.

-----------------------------

## Documentation

### API Description and curl samples (application deployed in application context `restaurant`)

**Entities**

*	User: id, name, email, password, enabled, registered, roles;
*	UserTo: id, name, email, password (DTO);
*	Restaurant: id, name;
*	Dish: id, name, date, price, restaurant;
*	Vote: id, date, user, restaurant.
 
> For all requests except registration, authorization is required.
> Basic authorization supported: email, password.

-----------------------------
### Для пользователей

##### Работа пользователя со своим профилем
- Регистрация	
    - `Post /rest/profile/register (UserTo) User`
    - `curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/profile/register`
- Получить профиль  
    - `Get /rest/profile User`
    - `curl -s http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`
- Изменить профиль  
    - `Put /rest/profile (UserTo)`
    - `curl -s -i -X PUT -d '{"name":"newUserName","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`
- Удалить профиль   
    - `Delete /rest/profile`	
    - `curl -s -X DELETE http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`

##### Работа с ресторанами		
- Получить список ресторанов	
    - `Get  /rest/restaurants	List<Restaurant>`
    - `curl -s http://localhost:8080/restaurant/rest/restaurants --user user1@mail.ru:password1`

##### Работа с меню ресторана
- Получить все блюда ресторана за текущий день	
    - `Get  /rest/restaurants/id/dishes	List<Dish>`
    - `curl -s http://localhost:8080/restaurant/rest/restaurants/5/dishes --user user1@mail.ru:password1`
- Получить все блюда ресторана за период	
    - `Get  /rest/restaurants/id/dishes/filter ?startDate=<date>&endDate=<date>	 List<Dish>`
    - `curl -s "http://localhost:8080/restaurant/rest/restaurants/5/dishes/filter?startDate=2020-03-05&endDate=2020-03-06"  --user user1@mail.ru:password1`


##### Голосование
- Проголосовать / изменить голосование  
     - `Put  /rest/restaurants/id/votes/ `
     - `curl -s -i -X PUT -d '{ }' http://localhost:8080/restaurant/rest/restaurants/5/votes --user user1@mail.ru:password1`
- Получить голосование за текущий день 
     - `Get  /rest/votes/	Vote`
     - `curl -s http://localhost:8080/restaurant/rest/votes --user user1@mail.ru:password1`
- Получить историю голосования за период 
     - `Get  /rest/votes/filter ?startDate=<date>&endDate=<date>	List<Vote>`
     - `curl -s "http://localhost:8080/restaurant/rest/votes/filter?startDate=2020-03-05&endDate=2020-03-06" --user user1@mail.ru:password1`
- Удалить голосование 
      - `Delete  /rest/votes/`
      - `curl -s -X DELETE http://localhost:8080/restaurant/rest/votes --user user1@mail.ru:password1`	
-----------------------------

### Для администраторов

##### Работа с пользователями
- Список всех пользователей	
    - `Get  /rest/admin/users	List<User>`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
- Данные об одном пользователе 
    - `Get  /rest/admin/users/id	User`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users/1 --user admin@gmail.com:admin`
- Создать	
    - `Post  /rest/admin/users (User)	User`
    - `curl -s -i -X POST -d '{"name":"New user", "email":"newUserEmail@mail.ru", "password": "newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
- Изменить	
    - `Put  /rest/admin/users/id (User)`
    - `curl -s -i -X PUT -d '{"name":"newUser1Name", "email":"user1@mail.ru", "password":"password1", "roles":["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/users/1 --user admin@gmail.com:admin`	
- Удалить 	
    - `Delete  /rest/admin/users/id`
    - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/users/2 --user admin@gmail.com:admin`	
- Получить пользователя по EMail   
    - `Get  /rest/admin/users/by ?email=<Email>  User`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users/by/?email=user1@mail.ru --user admin@gmail.com:admin`
- Изменить enabled / disabled пользователя
    - `Patch /rest/admin/users/id  ?enabled=<{true/false}>`	
	- `curl -s -i -X PATCH http://localhost:8080/restaurant/rest/admin/users/3/?enabled=false  --user admin@gmail.com:admin`	

##### Работа с ресторанами
- Список всех ресторанов 
     - `Get  /rest/admin/restaurants  List<Restaurant>`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants --user admin@gmail.com:admin`
- Данные об одном ресторане	
     - `Get /rest/admin/restaurants/id	Restaurant`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5 --user admin@gmail.com:admin`
- Создать	
     - `Post  /rest/admin/restaurants (Restaurant)	Restaurant`
     - `curl -s -i -X POST -d '{"name":"New Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants  --user admin@gmail.com:admin`
- Изменить	
     - `Put  /rest/ admin/restaurants/id (Restaurant)`
     - `curl -s -i -X PUT -d '{"name":"Restaurant1_updated"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5  --user admin@gmail.com:admin`	
- Удалить	
     - `Delete  /rest/admin/restaurants/id`
     - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/restaurants/5 --user admin@gmail.com:admin`	
		
##### Работа с меню ресторана
- Список всех блюд ресторана за текущий день 
     - `Get  /rest/admin/restaurants/restaurantId/dishes	List<Dish>`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes --user admin@gmail.com:admin`
- Список всех блюд ресторана за период	
    - `Get  /rest/admin/restaurants/id/dishes/filter ?startDate=<date>&endDate=<date>	 List<Dish>`
    - `curl -s "http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/filter?startDate=2020-03-05&endDate=2020-03-06"  --user admin@gmail.com:admin`
- Данные об одном блюде	
     - `Get  /rest/admin/restaurants/restaurantId/dishes/id	Dish`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9 --user admin@gmail.com:admin`
- Создать	
     - `Post  /rest/admin/restaurants/restaurantId/dishes (Dish)	Dish`
     - `curl -s -i -X POST -d '{"name":"New dish", "price":15.00, "date":"2020-04-25"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes  --user admin@gmail.com:admin`
- Изменить	
     - `Post  /rest/admin/restaurants/restaurantId/dishes/id (Dish)`
     - `curl -s -i -X PUT -d '{"name":"New extra dish", "price":20.00}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9  --user admin@gmail.com:admin`	
- Удалить	
     - `Delete  /rest/admin/restaurants/restaurantId/dishes/id`
     - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9 --user admin@gmail.com:admin`

#### validate with Error
     - `curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
     - `curl -s -X POST -d '{"name":"" }' -H 'Content-Type: application/json' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes --user admin@gmail.com:admin`
	
-----------------------------		
### Используемые инструменты и технологии
- Maven
- Java 11
- Spring (Data JPA, MVC, Security, Test, Security test)
- Hibernate
- SLF4J, Logback
- HSQLDB
- JUnit (Jupiter)
- Json (Jackson)

