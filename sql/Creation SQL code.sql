-- Create the schema
CREATE SCHEMA socialmedia;

-- Create the users table
CREATE TABLE socialmedia.users (
    id INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    image BLOB, -- New field for user image
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- Create the posts table
CREATE TABLE socialmedia.posts (
    id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    image BLOB, -- New field for post image
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT posts_pkey PRIMARY KEY (id),
    CONSTRAINT posts_user_id_fkey FOREIGN KEY (user_id) REFERENCES socialmedia.users(id)
);

-- Create the messages table
CREATE TABLE socialmedia.messages (
    id INT AUTO_INCREMENT NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message TEXT NOT NULL,
    image BLOB, -- New field for message image
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT messages_pkey PRIMARY KEY (id),
    CONSTRAINT messages_receiver_id_fkey FOREIGN KEY (receiver_id) REFERENCES socialmedia.users(id),
    CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES socialmedia.users(id)
);

-- Create the interactions table
CREATE TABLE socialmedia.interactions (
    id INT AUTO_INCREMENT NOT NULL,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment TEXT,
    interaction VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT interactions_pkey PRIMARY KEY (id),
    CONSTRAINT interactions_post_id_fkey FOREIGN KEY (post_id) REFERENCES socialmedia.posts(id),
    CONSTRAINT interactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES socialmedia.users(id)
);

-- Create the friends table
CREATE TABLE socialmedia.friends (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id) REFERENCES socialmedia.users(id),
    CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES socialmedia.users(id),
    CONSTRAINT friends_pkey PRIMARY KEY (user_id, friend_id)
);

-- Create the events table
CREATE TABLE socialmedia.events (
    id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL, -- The creator of the event
    name VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT events_pkey PRIMARY KEY (id),
    CONSTRAINT events_user_id_fkey FOREIGN KEY (user_id) REFERENCES socialmedia.users(id)
);

-- Create the event_signups table to track user signups for events
CREATE TABLE socialmedia.event_signups (
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    signed_up_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT event_signups_event_id_fkey FOREIGN KEY (event_id) REFERENCES socialmedia.events(id),
    CONSTRAINT event_signups_user_id_fkey FOREIGN KEY (user_id) REFERENCES socialmedia.users(id),
    CONSTRAINT event_signups_pkey PRIMARY KEY (event_id, user_id)
);
