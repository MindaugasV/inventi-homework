# inventi-homework

# TODO:
HouseKeeping
- [ ] Instructions how to run a project + migrations
- [ ] API documentation
- [ ] Tests
- [ ] Remove intial load and try adding/importing new transactions
- [ ] Consider refactoring where needed
- [ ] Consider changing to persistant DB
- [ ] Check any DEBUG flags before ready to prod
 
Business logic
- [ ] How will the income be parsed for given account? Do I need to have parse each line for two accounts (Double the input)? Does it only for the one, if so should I allow negative values to indicate credit/debit. 
- [ ] import from CSV
    - [x] Import & save
    - [ ] What happens if format doesn't match in lines?
- [ ] export to CSV
    - [x] simple export all existing data in database
    - [x] filter export by params, date
    - [ ] Sort? 
    - [ ] pageination? or limiting the number of entries? perform? 
- [ ] endpoint for account balance
    - [x] Simple plain fetch all and add numbers
    - [x] Filter by account
    - [x] Filter by date
    - [ ] Add currency consideration. 
    - [ ] Consider beneficieries? Depends on the approach of how i deal with credit/debit. 
    - [ ] make account balance calcs to perform fast