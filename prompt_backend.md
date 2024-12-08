### Prompt
1. As a Parking Manager, I am responsible for managing three parking lots: ● The Plaza Park (9 parking capacity) ● City Mall Garage (12 parking capacity) ● Office Tower Parking (9 parking capacity) I have employed three Parking Boys to help manage these parking lots, each utilizing a specific parking strategy.
Standard parking boy, that use the sequentially strategy
Smart parking boy, that use the max available strategy
Super Smart parking boy, that use the AvailableRateStrategy
implement A parking manager class, with the above 3 parking lots and 3 parking boys, manager have the following 3 method: - getAllParkingLots(): return all parking lots
park(plate number, strategy): request the correct parking boy to do the parking job and return a ticket
fetch(plate number): fetch the car from the corresponding parking lot and return the car.prompt

2. now please write the unit test for parking manager, you may refer to the following test case for the test for standard parking boy, smart parking boy and super parking boy

3. Now you need to expose 4 APIs to 1. getAllParkingLots, 2. park a new car 3. fetch a car by plateNumber and 4. getAllAvailableParkingStrategy. Below is the design for the APIs, implement the four apis in the ParkingManagerController class. Here is the API design:
paths:
/parkingStrategies:
   get:
   summary: "Get All available parking strategy"
   responses:
   "200":
   description: "successful operation"
   content:
   application/json:
   schema:
   properties:
   content:
   type: "array"
   items:
   type: "string"
   example: "firstAvailable"

/parkingStrategies/{strategy}:
    post:
    summary: "Park a new car to parkingLot using a specified strategy"
    parameters:
    - name: strategy
    in: path
    required: true
    description: "Parking strategy"
    schema:
    type: "string"
    example: "firstAvailable"
    requestBody:
    content:
    application/json:
    type: "object"
    properties:
    plateNumber:
    type: "string"
    example: "ab-1234"
    responses:
    "200":
    description: "successful operation"
    content:
    application/json:
    schema:
    content:
      - type: "array"
      items:
      $ref: "#/components/schemas/ParkingLot"

/parkingLots:
get:
    summary: "Get All Application Registrations"
    responses:
    "200":
    description: "successful operation"
    content:
    application/json:
    schema:
    - type: "object"
    properties:
    content:
    type: "array"
    items:
    $ref: "#/components/schemas/ParkingLot"

/parkingLots/cars/{plateNumber}:
post:
    summary: "Fetch Car by plateNumber"
    parameters:
    - name: plateNumber
    in: path
    required: true
    description: "Car plate number"
    schema:
    type: "string"
    example: "ab-1234"
    responses:
    "200":
    description: "successful operation"
    content:
    application/json:
    schema:
    content:
    - type: "array"
    items:
    $ref: "#/components/schemas/Car"

# dtos:
schemas:
ParkingLot:
    type: "object"
    properties:
    parkingLotId:
    type: "string"
    format: "UUIDv4"
    example: "cf075544-6ef0-4af5-9040-99958e539f68"
    capacity:
    type: "integer"
    example: 10
    ticket:
    type: "array"
    items:
    $ref: "#schemas/Ticket"
Ticket:
    type: "object"
    properties:
    parkingLot:
    type: "integer"
    example: 1
    plateNumber:
    type: "string"
    example: "ab-1234"
    position:
    type: "integer"
    example: 1
Car:
    type: "object"
    properties:
    plateNumber: 
    type: "string"
    example: "ab-1234"

4. given the following APIs, write integration test for ParkingManagerController using following naming convention: "should_return_<something>_when_<something>_given_<something>", you may use the MockMvc and WebApplicationContext in the test
5. please add license Plate Validation for park and fetch function, here is the requirement:  License plates must follow the format standard: two letters + four digits (e.g., “AB-1234”). ○ The system must reject empty or invalid license plate entries.
6. please add unit test for the validateLicensePlate function, please follow the following naming convention: "should_return_<something>_when_<something>_given_<something>"
