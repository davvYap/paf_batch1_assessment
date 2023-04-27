CREATE TABLE accounts (
account_id VARCHAR(45) NOT NULL,
name VARCHAR(45) NOT NULL,
balance DECIMAL(10,2) NOT NULL,
PRIMARY KEY (account_id));

insert into accounts(account_id, name, balance)
values
("ABCDE09871","whostheboss",88888.88),
("BSDWQ87652","david",88888.88),
("LKIUH12398","carmen",88888.88),
("LOKHS09621","tomas",88888.88),
("SDHGB08932","yunice",88888.88)
