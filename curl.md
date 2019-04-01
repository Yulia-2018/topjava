#Meal
curl http://localhost:8080/topjava/meals/100002
curl -X DELETE http://localhost:8080/topjava/meals/100002

curl -X POST -H "Content-Type: application/json" -d '{"dateTime":"2011-12-03T10:15:30","description":"Ужин","calories":"480"}' http://localhost:8080/topjava/meals
curl -X PUT -H "Content-Type: application/json" -d '{"calories":"80"}' http://localhost:8080/topjava/meals/100003

curl http://localhost:8080/topjava/meals
curl -H "Content-Type: application/json" -d startDateTime=2015-05-29T12:00 -d endDateTime=2015-05-30T22:00 http://localhost:8080/topjava/meals/filter

#Admin User
curl http://localhost:8080/topjava/rest/admin/users
curl http://localhost:8080/topjava/rest/admin/users/100000
curl -X DELETE http://localhost:8080/topjava/rest/admin/users/100001
curl http://localhost:8080/topjava/rest/admin/users/by?email=user@yandex.ru

curl -X POST -H "Content-Type: application/json" -d '{"name":"Valentin","email":"Valentin@gmail.com","password":"12345","roles":["ROLE_USER"]}' http://localhost:8080/topjava/rest/admin/users
curl -X PUT -H "Content-Type: application/json" -d '{"email":"NewEmail@yandex.ru"}' http://localhost:8080/topjava/rest/admin/users/100000

#Profile User
curl http://localhost:8080/topjava/rest/profile
curl -X PUT -H "Content-Type: application/json" -d '{"password":"NewPassword"}' http://localhost:8080/topjava/rest/profile
curl -X DELETE http://localhost:8080/topjava/rest/profile