# inventi-homework

## How to run
To run execute command in project root:
```
./mvnw spring-boot:run
```

Runs with Java8 and Maven

# API Docs
## Import CSV
```
POST /transactions/import
```
Accepts CSV as `form-data/multipart`

Allowed CSV format example:
```
accountNumber,date,beneficiary,comment,amount,currency
LT12312,2020-02-20 02:12:54.934,LT1231,transfer,2.00,LTL
LT12312,2020-02-20 20:07:54.934,LT1231,transfer,2.00,LTL
```

## Export CSV
```
GET /transactions/export
```
Outputs CSV file

__Parameteres__
User can specify date params to URL as a request param. Example `/transactions/export?from=2020-02-20&to=2020-02-22` 
__from__ - Optional, Date. Tells to include transactions only from given date. Format yyyy-MM-dd
__to__ - Optional, Date. Tells to include transactions only before given date. Format yyyy-MM-dd

Output example:
```
accountNumber,date,beneficiary,comment,amount,currency
LT12312,2020-02-23 18:10:01.8,LT1231,transfer,2.00,LTL
LT12312,2020-02-23 12:18:15.901,LT123213,transfer,3.12,LTL
```

## Balance for account
```
GET /account/{accountNumber}/balance
```
Returns balance for requested accountNumber

__Parameters__
__accountNumber__ - URL param. Required. Account number for which we want to know account balance.
User can specify date params to URL as a request param. Example `/account/{accountNumber}/balance?from=2020-02-20&to=2020-02-22` 
__from__ - Optional, Date. Tells to include transactions only from given date. Format yyyy-MM-dd
__to__ - Optional, Date. Tells to include transactions only before given date. Format yyyy-MM-dd

Ouput example:
```
{
    "accountNumber": "LT12312",
    "balances": {
        "EUR": 3.39,
        "LTL": 25.60
    }
}
```

## Extra Get transactions
```
GET /transactions
```
Return all transactions in JSON

Response example:
```
[
    {
        "id": 1,
        "accountNumber": "LT12312",
        "date": "2020-02-23 15:34:24.968",
        "beneficiary": "LT1231",
        "comment": "transfer",
        "amount": 2.00,
        "currency": "LTL"
    },
    {
        "id": 2,
        "accountNumber": "LT12312",
        "date": "2020-02-23 15:34:25.039",
        "beneficiary": "LT123213",
        "comment": "transfer",
        "amount": 3.12,
        "currency": "LTL"
    }
]
```

## Extra POST transactions
```
POST /transactions
```
Crate a new transaction form given data. 

POST Body example:
```
{
	"accountNumber": "Post:123",
    "date": "2020-02-22 15:11:10.793",
    "beneficiary": "Post:456",
    "comment": "Well thats about it",
    "amount": 22.12,
    "currency": "LTL"
}
```

# TODO:
## HouseKeeping
- [x] Instructions how to run a project
- [x] API documentation
- [ ] Tests
- [ ] Remove intial load and try adding/importing new transactions
- [ ] Consider refactoring where needed
- [ ] Consider changing to persistant DB
- [ ] Check any DEBUG flags before ready to prod
 
## Business logic
- I assume that credit/debit operations are indicated by the amount being negative/postive. Would not check beneficiery field to calc balance.
- I choose that balance will be calculated seperatly for each currency and will return seperate line for each. 
- [ ] import from CSV
    - [x] Import & save
    - [ ] Test optional comment
    - [ ] What happens if format doesn't match in lines?
    - [ ] Double check negative amount values.
- [ ] export to CSV
    - [x] simple export all existing data in database
    - [x] filter export by params, date
    - [ ] Sort? 
    - [ ] pageination? or limiting the number of entries? perform? 
- [ ] endpoint for account balance
    - [x] Simple plain fetch all and add numbers
    - [x] Filter by account
    - [x] Filter by date
    - [x] Add currency consideration. Update docs.
    - [ ] Double check negative amount values.
    - [ ] Consider beneficieries? Depends on the approach of how i deal with credit/debit. 
