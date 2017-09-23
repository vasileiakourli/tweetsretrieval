-- Create and alter table in database
CREATE TABLE IF NOT EXISTS list2(postId bigint primary key , userId bigint, name varchar(20), screenName varchar(50), text varchar(140), emoticon int, komma varchar(20));
ALTER TABLE list CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE list CHANGE text text VARCHAR(250)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- export table to csv
SELECT * INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/my_database_kourli.csv' FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' FROM list;

-- load data from my_database_kourli.csv into table list2
LOAD DATA LOCAL INFILE 'C:/Users/Vasileia/Desktop/my_database_kourli.csv' into table list2 FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n';