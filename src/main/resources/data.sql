
INSERT INTO user (username, email, password, account_balance) VALUES
    ('John Doe', 'jdoe@gmail.com', '$2a$10$RVWa.58B2FHmg0P7lbM9kevj/cOWv3wwT6Jo6FJA3GPdzUn3z8HAe', 200.00),
    ('Jack Will', 'jwill@hotmail.com', '$2a$10$cSxHDlioB3/LDmsUXYUBAOENHLDI2QNzp6SimSGGHJJCTJAzL0GC6', 500.00),
    ('Sarah Smith', 'Ssmith@gmail.com', '$2a$10$egssCWEDOjRhpP.kTmlFyOf5WCk40VV8Cm85QOXglp03ZwAQeUGY', 0.00);
SELECT * FROM user;
INSERT INTO user_connections (user_id, connection_id) VALUES
    (16, 11),
    (11, 16),
    (18, 16),
    (11, 17),
    (11, 18),
    (17, 18);

SELECT * FROM user_connections;
INSERT INTO transaction (id, sender, receiver, description, amount, transaction_date) VALUES
    (4, 11, 16, 'Paiement du loyer', 100.00, '2025-05-07 19:14:07'),
    (5, 11, 17, 'Remboursement', 80.50, '2025-05-07 21:33:48'),
    (6, 11, 18, 'Paiement pour services', 400.00, '2025-05-07 21:34:50');

SELECT * FROM transaction;


