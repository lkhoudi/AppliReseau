CREATE TABLE IF NOT EXISTS user_table(
    id SERIAL PRIMARY KEY ,
    email VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    password VARCHAR(50),
    active BOOLEAN
);