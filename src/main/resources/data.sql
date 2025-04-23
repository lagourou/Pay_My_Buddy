DROP TABLE IF EXISTS user_connections;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_balance DECIMAL(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE transaction (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sender INT NOT NULL,
    receiver INT NOT NULL,
    description TEXT,
    amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (sender) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (receiver) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE user_connections (
    user_id INT NOT NULL,
    connection_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (connection_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY (user_id, connection_id)
);

INSERT INTO user (username, email, password, account_balance) VALUES
    ('John Doe', 'jdoe@gmail.com', 'jdoe_password', 200.00),
    ('Jack Will', 'jwill@hotmail.com', 'jwill_password', 500.00),
    ('Sarah Smith', 'Ssmith@gmail.com', 'Ssmith_password', 0.00);

INSERT INTO transaction (sender, receiver, description, amount) VALUES
    (1, 2, 'Paiement du loyer', 300.00),
    (2, 3, 'Remboursement', 50.75),
    (3, 1, 'Paiement pour services', 100.00);

INSERT INTO user_connections (user_id, connection_id) VALUES
    (1, 2),
    (2, 3),
    (3, 1);

SELECT * FROM user;
SELECT * FROM transaction;
SELECT * FROM user_connections;

SELECT t.id, t.description, t.amount
FROM transaction t
WHERE t.sender = 1;

SELECT *
FROM user
JOIN user_connections ON user.id = user_connections.connection_id
WHERE user_connections.user_id = 1;

SELECT t.description, t.amount
FROM transaction t
WHERE t.receiver = 2;
