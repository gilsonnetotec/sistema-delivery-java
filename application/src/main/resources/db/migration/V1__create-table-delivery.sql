CREATE TABLE categories(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(200) NOT NULL
);

INSERT INTO categories (id, name) VALUES(1,'Alimentos')