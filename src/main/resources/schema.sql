CREATE TABLE receipts (
  id INT UNSIGNED AUTO_INCREMENT,
  uploaded TIME DEFAULT CURRENT_TIME(),
  merchant VARCHAR(255),
  amount DECIMAL(12,2),
  receipt_type INT UNSIGNED,

  PRIMARY KEY (id)
);

CREATE TABLE tags (
  id INT UNSIGNED AUTO_INCREMENT,
  tagName VARCHAR(255),

  PRIMARY KEY (id)
);

CREATE TABLE tagsreceipts(
  tagid INT UNSIGNED,
  receiptid INT UNSIGNED,

  PRIMARY KEY (tagid,receiptid)
)