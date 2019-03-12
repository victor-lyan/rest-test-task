-- 123 is the password for test user
INSERT INTO users VALUES (
  1, 'test', '$2a$10$5IGGLE6ICNCirp9ZM.fsZeSERWyvqb/AH7LeS58n8ldp20zxjMPPO', 0, null);
  
INSERT INTO user_roles VALUES (1, 'USER');

INSERT INTO pets ("name", birth_date, gender, user_id) VALUES ('Fluffy', '2012-06-12', 'male', 1);
INSERT INTO pets ("name", birth_date, gender, user_id) VALUES ('Sunny girl', '2015-09-17', 'female', 1);