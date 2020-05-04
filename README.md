## Topjava graduation project

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
### For users

##### User work with their profile
- Registration	
    - `Post /rest/profile/register (UserTo) User`
    - `curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/profile/register`
- Get profile  
    - `Get /rest/profile User`
    - `curl -s http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`
- Update profile  
    - `Put /rest/profile (UserTo)`
    - `curl -s -i -X PUT -d '{"name":"newUserName","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`
- Delete profile   
    - `Delete /rest/profile`	
    - `curl -s -X DELETE http://localhost:8080/restaurant/rest/profile --user test@mail.ru:test-password`

##### Work with restaurants		
- Get a list of restaurants (cached)	
    - `Get  /rest/restaurants	List<Restaurant>`
    - `curl -s http://localhost:8080/restaurant/rest/restaurants --user user1@mail.ru:password1`

##### Work with the restaurant menu
- Get all restaurant dishes for the current day	(cached in query cache for 5 hours)
    - `Get  /rest/restaurants/id/dishes	List<Dish>`
    - `curl -s http://localhost:8080/restaurant/rest/restaurants/5/dishes --user user1@mail.ru:password1`
- Get all restaurant dishes for the period	
    - `Get  /rest/restaurants/id/dishes/filter ?startDate=<date>&endDate=<date>	 List<Dish>`
    - `curl -s "http://localhost:8080/restaurant/rest/restaurants/5/dishes/filter?startDate=2020-03-05&endDate=2020-03-06"  --user user1@mail.ru:password1`


##### Voting
- Vote / change vote  
     - `Put  /rest/restaurants/id/votes/ `
     - `curl -s -i -X PUT -d '{ }' http://localhost:8080/restaurant/rest/restaurants/5/votes --user user1@mail.ru:password1`
- Get voting for the current day 
     - `Get  /rest/votes/	Vote`
     - `curl -s http://localhost:8080/restaurant/rest/votes --user user1@mail.ru:password1`
- Get voting history for the period 
     - `Get  /rest/votes/filter ?startDate=<date>&endDate=<date>	List<Vote>`
     - `curl -s "http://localhost:8080/restaurant/rest/votes/filter?startDate=2020-03-05&endDate=2020-03-06" --user user1@mail.ru:password1`
- Delete vote 
      - `Delete  /rest/votes/`
      - `curl -s -X DELETE http://localhost:8080/restaurant/rest/votes --user user1@mail.ru:password1`	
-----------------------------

### For administrators

##### Work with users
- List all users	
    - `Get  /rest/admin/users	List<User>`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
- Single user data 
    - `Get  /rest/admin/users/id	User`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users/1 --user admin@gmail.com:admin`
- Create	
    - `Post  /rest/admin/users (User)	User`
    - `curl -s -i -X POST -d '{"name":"New user", "email":"newUserEmail@mail.ru", "password": "newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
- Update	
    - `Put  /rest/admin/users/id (User)`
    - `curl -s -i -X PUT -d '{"name":"newUser1Name", "email":"user1@mail.ru", "password":"password1", "roles":["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/users/1 --user admin@gmail.com:admin`	
- Delete 	
    - `Delete  /rest/admin/users/id`
    - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/users/2 --user admin@gmail.com:admin`	
- Get user by EMail   (cached in query cache)
    - `Get  /rest/admin/users/by ?email=<Email>  User`
    - `curl -s http://localhost:8080/restaurant/rest/admin/users/by/?email=user1@mail.ru --user admin@gmail.com:admin`
- Change user enabled / disabled
    - `Patch /rest/admin/users/id  ?enabled=<{true/false}>`	
	- `curl -s -i -X PATCH http://localhost:8080/restaurant/rest/admin/users/3/?enabled=false  --user admin@gmail.com:admin`	

##### Work with restaurants
- List of all restaurants (cashed) 
     - `Get  /rest/admin/restaurants  List<Restaurant>`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants --user admin@gmail.com:admin`
- Data about one restaurant	
     - `Get /rest/admin/restaurants/id	Restaurant`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5 --user admin@gmail.com:admin`
- Create	
     - `Post  /rest/admin/restaurants (Restaurant)	Restaurant`
     - `curl -s -i -X POST -d '{"name":"New Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants  --user admin@gmail.com:admin`
- Update	
     - `Put  /rest/ admin/restaurants/id (Restaurant)`
     - `curl -s -i -X PUT -d '{"name":"Restaurant1_updated"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5  --user admin@gmail.com:admin`	
- Delete	
     - `Delete  /rest/admin/restaurants/id`
     - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/restaurants/5 --user admin@gmail.com:admin`	
		
##### Work with the restaurant menu
- List of all restaurant dishes for the current day (cached in query cache for 5 hours) 
     - `Get  /rest/admin/restaurants/restaurantId/dishes	List<Dish>`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes --user admin@gmail.com:admin`
- List of all restaurant dishes for the period	
    - `Get  /rest/admin/restaurants/id/dishes/filter ?startDate=<date>&endDate=<date>	 List<Dish>`
    - `curl -s "http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/filter?startDate=2020-03-05&endDate=2020-03-06"  --user admin@gmail.com:admin`
- One dish data	
     - `Get  /rest/admin/restaurants/restaurantId/dishes/id	Dish`
     - `curl -s http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9 --user admin@gmail.com:admin`
- Create	
     - `Post  /rest/admin/restaurants/restaurantId/dishes (Dish)	Dish`
     - `curl -s -i -X POST -d '{"name":"New dish", "price":15.00, "date":"2020-04-25"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes  --user admin@gmail.com:admin`
- Update	
     - `Post  /rest/admin/restaurants/restaurantId/dishes/id (Dish)`
     - `curl -s -i -X PUT -d '{"name":"New extra dish", "price":20.00}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9  --user admin@gmail.com:admin`	
- Delete	
     - `Delete  /rest/admin/restaurants/restaurantId/dishes/id`
     - `curl -s -X DELETE http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes/9 --user admin@gmail.com:admin`

#### Validate with Error
     - `curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/restaurant/rest/admin/users --user admin@gmail.com:admin`
     - `curl -s -X POST -d '{"name":"" }' -H 'Content-Type: application/json' http://localhost:8080/restaurant/rest/admin/restaurants/5/dishes --user admin@gmail.com:admin`
	
-----------------------------		
### Tools and technologies used
- Maven
- Java 11
- Spring (Data JPA, MVC, Security, Test, Security test)
- Hibernate
- SLF4J, Logback
- HSQLDB
- JUnit (Jupiter)
- Json (Jackson)

