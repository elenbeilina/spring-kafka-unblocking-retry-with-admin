printf " -> Producing message \n\n"

curl -X PUT \
 -H "Content-Type: application/json"  \
 http://localhost:8080/produce