INSERT INTO users (id, username, password, enabled) VALUES
	(1, 'admin', '$2a$04$L7Kz1Ndl8YtUQquwAlYLyeN6t08I7YhFGU6cV3Q6WVGB8JnC2KIPW', true),
	(2, 'user1', '$2a$04$L7Kz1Ndl8YtUQquwAlYLyeN6t08I7YhFGU6cV3Q6WVGB8JnC2KIPW', true),
	(3, 'user2', '$2a$04$L7Kz1Ndl8YtUQquwAlYLyeN6t08I7YhFGU6cV3Q6WVGB8JnC2KIPW', true);

INSERT INTO authorities (id, name) VALUES (1, 'ADMIN'), (2, 'USER');

INSERT INTO users_authorities (user_id, authorities_id)
  VALUES (1, 1), (1, 2), (2, 2), (3, 2);