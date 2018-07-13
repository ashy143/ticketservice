# ticketservice
Clone the project and run the below commands from root directory of master branch.

```
# build
mvn clean install
```

```
# run tests
mvn test
```

# Assumptions
- ```SeatHold``` & ```Venue``` are not defined in problem 
- There is no strategy defined for identifying best available seats. Hence will be fetching any available seats from the ```Venue```. Although, with the use of ```Seat``` class you can define any stategy like (ranking of seat).
- Reservation of >1 Seats by a customer doesn't always guarantee adjacent seats if seats are from 2 rows.
- Assuming expiry time as a constant 10 seconds. Although it can be specific to venue and can be defined while initializiing ```Venue```. 
- Assuming there is no role of customer email other than assigning to ```SeatHold```. Since it's not defined if there can be only one ```SeatHold``` per customer email ? I am assuming customer can make any # of holds. If I have to avoid that I can just iterate through ```SeatHold``` and verify the property *customerEmail* and take specific action.
- I am not making some validations like *null email passed* as it doesn't harm in not doing so. Although, I am validating for some null checks for *seatHoldId*
- I am assuming that, I don't have to worry about *seatHoldId* generation will overflow. We can definitely improve here to make sure that you generate uniqueId without crossing the range/overflow Integer value.
- Assuming I can send any *uniqueConfirmation* code after reserving seats. There is no use case defined where this *uniqueConfirmation* code can be used. For ex. if asked to get ```Seat``` reserved by user we can accomodate that by having a property *reservedBy* in ```Seat``` or can even have some other data structure based on use cases.
- Assuming the Venue's capacity is usually a small number. So for every operation if need to iterate through every ```Seat``` of a ```Venue``` doesn't affect the performance.
- Doesn't require any chron job or a schedule to release ```Seat``` after expiry. For every operation which involves to check the state of ```Seat```, I am checking the ```SeatHold``` for expiry and doing appropriate actions.
