## Fibonacci API ##
API for managing Fibonacci sequences per client, with JWT authentication.

***To run the app***:

``` shell

docker build -t fibonacci-app .
docker run -p 8080:8080 fibonacci-app

```

**<ins>The application should now be available on your localhost</ins>** http://localhost:8080/fibonacci

**SWAGGER Documentation available** : http://localhost:8080/swagger-ui/index.html#

1. Generate JWT Token :  **GET /token?clientId=test-client**
2. Next Fibonacci Number Endpoint : **POST /next
   Authorization: Bearer <token generated at step 1>**
3. Previous Fibonacci Number: **POST /prev
   Authorization:  Bearer <token generated at step 1>**

   ***Note: If there are no previous numbers, the server will return 500 Internal Server Error***
4. List Fibonacci Numbers: GET /fibonacci
   Authorization:  Bearer <token generated at step 1>
