------------------------------------------------
CREATE TABLE library_user
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL
);

INSERT INTO library_user (name, surname)
VALUES ('John', 'Doe');
------------------------------------------------

------------------------------------------------
CREATE TABLE book
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_name       VARCHAR(50) UNIQUE NOT NULL,
    author          VARCHAR(50)        NOT NULL,
    year_of_writing NUMBER(4)          NOT NULL,
    book_holder_id  BIGINT             REFERENCES library_user (id) ON DELETE SET NULL
);

INSERT INTO book (book_name, author, year_of_writing)
VALUES ('Java Core Guide', 'Frank Foe', 2022);
------------------------------------------------

------------------------------------------------
CREATE TABLE user_entity
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    username         VARCHAR(50) UNIQUE NOT NULL,
    password         VARCHAR(200)       NOT NULL,
    role             VARCHAR(16)        NOT NULL
);

INSERT INTO user_entity (username, password, role)
VALUES ('user', '$2a$12$OveaPkOk6QEIwRno62QPVubis1gE8cWn3DXTGzTTEWgySRQm0328u', 'USER');
--password: user

INSERT INTO user_entity (username, password, role)
VALUES ('admin', '$2a$12$YU5bo90RuEH8lOgKXPhoXOI3hjQd2CsKwMsQ0IvSrCVVYEDXPV/Xi', 'ADMIN');
--passwrod: admin
------------------------------------------------

------------------------------------------------
CREATE TABLE refresh_token
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(200),
    user_id BIGINT REFERENCES user_entity (id)
);
