CREATE TABLE tb_category(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL
);

INSERT INTO tb_category (id, name) VALUES(1,'Alimentos')