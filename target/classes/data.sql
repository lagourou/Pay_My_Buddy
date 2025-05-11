
INSERT INTO user (username, email, password, account_balance) VALUES
    (' TestUser', 'test@example.com', '$2a$10$MBPjr7QhY2SpyO/ugqzO1eV57WhvgaizjuVyR6uBtXrZhlEZzFnuC', 800.00),
    ('John Doe', 'jdoe@gmail.com', '$2a$10$T4FJKpnN2yOI0ECeRsAJdOW7KxLLRePzKCv7nCjW.MEr5EGzgdVIG', 0.00),
    ('Jack Will', 'jwill@hotmail.com', '$2a$10$awfSQApa3fjF1n6Fux0F3uXV0QcUZr05pp060iBq41/mtgxwieLP2', 0.00)
    ('Sarah Smith', 'Ssmith@gmail.com', '$2a$10$wU592yVc1YvlSrXeJekXu.b08gsL3w90Ig2mtb3IUHbOWswQFwzRC', 0.00);
SELECT * FROM user;
INSERT INTO user_connections (user_id, connection_id) VALUES
    (1, 2),
    (1, 3),
    (1, 4);

SELECT * FROM user_connections;
INSERT INTO transaction (id, sender, receiver, description, amount, transaction_date) VALUES
    (1, 1, 4, 'Paiement du loyer', 200.00, '2025-05-11 04:18:39'),
    (2, 1, 3, 'Remboursement', 30.00, '2025-05-11 04:19:05'),
    (3, 1, 2, 'Paiement pour services', 150.00, '2025-05-11 04:19:23');

SELECT * FROM transaction;


