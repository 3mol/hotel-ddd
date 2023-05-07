mvn -pl base-service-client,hotel-service-client,order-service-client,payment-service-client,room-service-client,user-service-client,base-service install
mvn -pl hotel-service,order-service,payment-service,room-service,user-service package docker:build

