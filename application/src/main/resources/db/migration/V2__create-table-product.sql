CREATE TABLE tb_product(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT DEFAULT NULL,
    price DECIMAL(10,2) DEFAULT NULL,
    imgURL TEXT DEFAULT NULL
);

INSERT INTO tb_product (id, name, description, price, imgURL) VALUES(1, 'Pizza Portuguesa', 'Pizza a moda da casa', 40.0, null);
INSERT INTO tb_product (id, name, description, price, imgURL) VALUES(2, 'Pizza Vegana', 'Pizza sem produtos de origem animal', null, null)