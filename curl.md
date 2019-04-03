#Meal
curl http://localhost:8080/topjava/rest/profile/meals
curl http://localhost:8080/topjava/rest/profile/meals/100002
curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100002
curl -X POST -H "Content-Type: application/json" -d '{"dateTime":"2011-12-03T10:15:30","description":"Dinner","calories":480}' http://localhost:8080/topjava/rest/profile/meals
curl -X PUT -H "Content-Type: application/json" -d '{"dateTime":"2015-05-30T07:00","description":"Updated breakfast","calories":80}' http://localhost:8080/topjava/rest/profile/meals/100003
#Filter for two parameter
curl "http://localhost:8080/topjava/rest/profile/meals/filter?startDateTime=2015-05-29T12:00&endDateTime=2015-05-30T22:00"
#Filter for four parameter
curl "http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2015-05-29&startTime=12:00&endDate=2015-05-30&endTime=22:00"

#Admin User
curl http://localhost:8080/topjava/rest/admin/users
curl http://localhost:8080/topjava/rest/admin/users/100000
curl -X DELETE http://localhost:8080/topjava/rest/admin/users/100001
curl -X POST -H "Content-Type: application/json" -d '{"name":"Valentin","email":"Valentin@gmail.com","password":"12345","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/admin/users
curl -X PUT -H "Content-Type: application/json" -d '{"name":"User","email":"user@yandex.ru","password":"password123","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/admin/users/100000
curl http://localhost:8080/topjava/rest/admin/users/by?email=user@yandex.ru

#Profile User
curl http://localhost:8080/topjava/rest/profile
curl -X PUT -H "Content-Type: application/json" -d '{"name":"User","email":"NewEmail@gmail.com","password":"password","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/profile
curl -X DELETE http://localhost:8080/topjava/rest/profile