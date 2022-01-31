CREATE TABLE messages(id serial primary key, name varchar(255), message text);

INSERT INTO messages (name, message) VALUES ('hello', 'Hello, world!');