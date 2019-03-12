CREATE TABLE IF NOT EXISTS "users" (
	"id" serial NOT NULL PRIMARY KEY,
	"username" varchar(100) NOT NULL UNIQUE,
	"password" varchar(100) NOT NULL,
	"login_tries" int4,
	"last_login_try_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "user_roles" (
  "user_id" int4 NOT NULL,
  "roles" varchar(50) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "pets" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" varchar(100) NOT NULL UNIQUE,
	"birth_date" DATE,
	"gender" varchar(50) NOT NULL,
	"user_id" int4 NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);