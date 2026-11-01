### Facilities and Resources:

##### 

##### This part of the project contains the facility classes used in the Mars Colony system.

##### 

##### 

#### 1\. Facility:

##### 

##### \-"Facility" is an abstract parent class for all facilities in the colony.

##### 

##### \-It stores common information such as the facility name, location, capacity, and operational status.

##### 

##### \-Each facility manages its own 7 resources, including oxygen, water, food, fuel, battery, medical supplies, and spare parts. Resources can be added, removed, or checked using the available methods.

##### 

##### \-Each facility can also store up to 10 equipment items, Every equipment item has a name and a working status.

##### 

##### \-The "getType()" method is abstract, so each child class provides its own facility type.

##### 

##### 

#### 2\. Habitat:

##### 

##### \-"Habitat" extends the "Facility" class and represents the living areas in the colony.

##### 

##### \-It includes methods to start and repair an oxygen leak, Oxygen is reduced only when a leak is active. The amount removed cannot exceed the available oxygen.

##### 

##### 

#### 3\. Laboratory:

##### 

##### "Laboratory" extends the "Facility" class and represents the laboratories in the colony.

##### 

##### It keeps track of whether a fire is active. When a fire starts, the laboratory becomes unavailable. Stopping the fire does not automatically make the laboratory operational again.

##### 

##### 

#### 4\. StorageFacility:

##### 

##### "StorageFacility" extends the "Facility" class and represents the facilities used to store resources and other items.

##### 

#### 5\. PowerStation:

##### 

##### "PowerStation" extends the "Facility" class and represents the power stations in the colony.

##### 

#### 6\. CommunicationStation:

##### 

##### "CommunicationStation" extends the "Facility" class and represents the communication stations used in the colony.

##### 

#### \# Main OOP Concepts Used: 

##### 

##### \- Inheritance: All facility types inherit common properties and methods from "Facility".

##### \- Encapsulation: Class fields are private and accessed through public methods.

##### \- Abstraction: "Facility" is an abstract class that defines the "getType()" method.

##### \- Polymorphism: Different facility objects can be handled using a "Facility" reference, while each object returns its own type through the overridden "getType()" method.

##### 

#### \# Design Notes :

##### 

##### \- Each facility manages its resources separately.

##### \- A facility can hold a maximum of 10 equipment items.

##### \- Resources cannot be removed if the requested amount is unavailable.

##### \- Only working equipment is considered available by the "hasEquipment()" method.

