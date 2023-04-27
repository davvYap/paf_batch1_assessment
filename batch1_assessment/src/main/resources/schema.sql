CREATE TABLE `acme_bank`.'accounts' (
  'account_id' INT NOT NULL,
  'name' VARCHAR(45) NOT NULL,
  'balance' DECIMAL(6,2) NOT NULL,
  PRIMARY KEY ('account_id'));

insert into accounts(account_id, name, balance)
values
(102595,"whostheboss",88888.88);