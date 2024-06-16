# BACK-Linguagem-de-programacao-III
Backend. Criação de Rede Social estudantil para matéria de LPIII.

# Database Configuration
## 1 Crie o usuário postgres com a senha 123456

## Conecte ao banco
psql -U postgres
senha 123456

CREATE DATABASE socialmedia;
CREATE USER socialmedia_user WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE socialmedia TO socialmedia_user;
\c socialmedia

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Posts table
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Messages table
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    sender_id INT REFERENCES users(id),
    receiver_id INT REFERENCES users(id),
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
