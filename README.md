# inventi-homework

# TODO:
HouseKeeping
- [ ] Instructions how to run a project + migrations
- [ ] API documentation
- [ ] Tests
- [ ] Remove intial load and try adding/importing new transactions

Business logic
- [ ] How will the income be parsed for given account? Do I need to have parse each line for two accounts (Double the input)? Does it only for the one, if so should I allow negative values to indicate credit/debit. 
- [ ] import from CSV
    - [x] Import & save
    - [ ] What happens if format doesn't match in lines?
- [ ] export to CSV
    - [x] simple export all existing data in database
    - [ ] filter export by params, date
    - [ ] pageination? or limiting the number of entries? perform? 
- [ ] endpoint for account balance
    - [ ] Simple plain fetch all and add numbers
    - [ ] Filter by account
    - [ ] Consider beneficieries? Depends on the approach of how i deal with credit/debit. 
    - [ ] make account balance calcs to perform fast