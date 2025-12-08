To run the app: 
``` shell
docker build -t fibonacci-app .
docker run -p 8080:8080 -e JWT_SECRET=my-little-secret fibonacci-app
```
