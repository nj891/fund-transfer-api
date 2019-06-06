### Technologies used
- Java 8
- Spark Framework
- Maven
- concurrency utilities to handle multiple requests
- JUnit 5
- Apache HttpClient for unit testing
- Logback
- Lombok
- google/gson


### To run
- java -jar ".\target\fund-transfer-1.0-SNAPSHOT.jar"

### Services

1. To fetch account holders
- http://localhost:4567/accountholders?limit=10

Example: GET - http://localhost:4567/accountholders?limit=10

2. To fetch single account holder
- http://localhost:4567/accountholders/1

Example: GET - http://localhost:4567/accountholders/1

3. To fetch all accounts of account holder
- http://localhost:4567/accountholders/1/accounts

Example: GET - http://localhost:4567/accountholders/1/accounts

4. To fetch accounts
- http://localhost:4567/accounts?limit=10

Example: GET - http://localhost:4567/accounts?limit=10

5. To fetch single account
- http://localhost:4567/accounts/1

Example: GET - http://localhost:4567/accounts/1

6. To fetch all transactions
- http://localhost:4567/transactions?limit=100

Example: GET - http://localhost:4567/transactions?limit=100

7. To fetch all transaction of particular account
- http://localhost:4567/accounts/1/transactions?limit=100

Example: GET - http://localhost:4567/accounts/1/transactions?limit=100


8. To transfer money from one account to another account

- http://localhost:4567/transactions

Example: POST - http://localhost:4567/transactions

param sample:

{
    "debitAccountNumber": "3",
    "creditAccountNumber": "6",
    "amount": "10"
}
