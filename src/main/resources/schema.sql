-- for postman tests
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS film_directors CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS review_likes CASCADE;
DROP TABLE IF EXISTS event_types CASCADE;
DROP TABLE IF EXISTS operations CASCADE;
DROP TABLE IF EXISTS events CASCADE;

CREATE TABLE IF NOT EXISTS ratings (
  id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
  id INTEGER PRIMARY KEY,
  name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS directors (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(200),
    release_date date NOT NULL,
    duration INTEGER NOT NULL,
    rating_id INTEGER REFERENCES ratings(id),
    CHECK(duration > 0)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER REFERENCES films(id) NOT NULL,
    genre_id INTEGER REFERENCES genres(id) NOT NULL,
    UNIQUE(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_directors (
    film_id INTEGER REFERENCES films(id) NOT NULL,
    director_id INTEGER REFERENCES genres(id) NOT NULL,
    UNIQUE(film_id, director_id)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login VARCHAR(64) NOT NULL,
    name VARCHAR(64),
    email VARCHAR(64) NOT NULL,
    birthday DATE, CHECK(birthday <= CAST(now() AS DATE))
);

CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER REFERENCES users(id) NOT NULL,
    friend_id INTEGER REFERENCES users(id) NOT NULL,
    UNIQUE(user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER REFERENCES films(id) NOT NULL,
    user_id INTEGER REFERENCES users(id) NOT NULL,
    UNIQUE(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id INTEGER  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content VARCHAR(256) NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id INTEGER  NOT NULL,
    film_id INTEGER  NOT NULL,
    CONSTRAINT reviews_film_id_fk
        FOREIGN KEY (film_id)
        REFERENCES films(id)
        ON DELETE CASCADE,
    CONSTRAINT reviews_user_id_fk
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review_likes (
    review_id INTEGER not null,
    user_id INTEGER not null,
    useful INTEGER not null,
    CONSTRAINT review_likes_pk
        PRIMARY KEY (review_id, user_id),
    CONSTRAINT review_likes_review_id_fk
        FOREIGN KEY (review_id)
        REFERENCES reviews(id)
        ON DELETE CASCADE,
    CONSTRAINT review_likes_user_id_fk
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_types (
    id INTEGER PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS operations (
    id INTEGER PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    event_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    user_id INTEGER REFERENCES users(id) NOT NULL,
    event_type_id INTEGER REFERENCES event_types(id),
    operation_id INTEGER REFERENCES operations(id),
    entity_id INTEGER
);
