# Mega tester

Project created for improving skills for writing tests and checking the quality of the knowledge.

## About project

Application - is REST service for:
 - execution order
 - receiving positions based on orders
 - updating account on order execution
 - and notification about events in system
 
 ### Entry points
 - /api/order/
	- **GET** - return all available orders
 	- **POST** - register new order and run workflow for creating position and calculation of the account changes
- /api/position/
	- **GET** - return all available positions for user
- /api/account/
	- / **GET** - receiving logged in user account
	- /{accountId} **GET** - receiving account by account id
	- /{accountId}/deposit/{amountInUSD} - for deposit to user account. Return new balance
	- /{accountId}/deposit/{amountInUSD} - for withdrawal from user account. Return new balance
- /api/notification/
	- /{accountId} **GET** (text/event-stream) - Server Side Events for receiving stream of the events for logged in user
	- /all **GET** (text/event-stream) - Server Side Events for receiving stream of the all events

Note: some entry points have restriction by roles.

### Users
| Username   | Password | Roles      | AccountId |
|------------|----------|------------|-----------|
| admin      | password | ROLE_ADMIN | -1        |
| user1      | user1    | ROLE_USER  | 1         |
| user2      | user2    | ROLE_USER  | 2         |

### Workflow of the application
Orders:
	UI make request for creating new order (POST:/api/orders/). As result position should be opened and account balance - changed.
	Changing account balance made in few stage:
	- open positions
	- update account balance according opened position
	- take fee for opening position
	- notify all about update 
	  

#Tasks:
### Unit tests
- Test core
	- OrderManagementProcessor
	- OrderProcessor
	- PositionProcessor
	- AccountProcessor
	- NotificationProcessor

Recommendation:
- use give-when-then approach
- write some tests with mockito and without
- other test frameworks can be used

### Integration tests
- Test rest points 

Recommendation:
- use REST Assured framework
- automate login in tests (own runner or rules)
- split tests to groups, depend from processor
 	
#//TODO:
- validation
	- parameters
	- balance
- notifications - update