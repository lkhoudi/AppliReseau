CREATE TABLE IF NOT EXISTS user_table(
    id SERIAL PRIMARY KEY ,
    email VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    password VARCHAR(50),
    active BOOLEAN,
    avatar VARCHAR(50)
);

/*
Pour créer la bdd

Installer postgres
Créer une bdd dauphine avec user mydauphine et pasword mydauphine
Donner tous les rivileges au user mydauphie sur la bdd dauphine
Créer la table user_table avec ce script
*/