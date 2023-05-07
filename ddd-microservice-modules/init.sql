CREATE
USER 'h_hotel_user'@'%' IDENTIFIED BY 'h_hotel_password';
  create
database h_hotel;
  GRANT ALL PRIVILEGES ON h_hotel.* TO
'h_hotel_user'@'%' WITH GRANT OPTION;
  USE
h_hotel;
  CREATE
USER 'h_room_user'@'%' IDENTIFIED BY 'h_room_password';
  create
database h_room;
  GRANT ALL PRIVILEGES ON h_room.* TO
'h_room_user'@'%' WITH GRANT OPTION;
  USE
h_room;
  CREATE
USER 'h_payment_user'@'%' IDENTIFIED BY 'h_payment_password';
  create
database h_payment;
  GRANT ALL PRIVILEGES ON h_payment.* TO
'h_payment_user'@'%' WITH GRANT OPTION;
  USE
h_payment;
  CREATE
USER 'h_order_user'@'%' IDENTIFIED BY 'h_order_password';
  create
database h_order;
  GRANT ALL PRIVILEGES ON h_order.* TO
'h_order_user'@'%' WITH GRANT OPTION;
  USE
h_order;
  CREATE
USER 'h_user_user'@'%' IDENTIFIED BY 'h_user_password';
  create
database h_user;
  GRANT ALL PRIVILEGES ON h_user.* TO
'h_user_user'@'%' WITH GRANT OPTION;
  USE
h_user;
