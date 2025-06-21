-- Point History Table for GreenTable
-- This table tracks all point transactions for users

CREATE TABLE point_history (
    point_history_id    INT             PRIMARY KEY AUTO_INCREMENT,
    user_id            INT             NOT NULL,
    transaction_type   ENUM('EARN', 'USE', 'REFUND', 'EXPIRE') NOT NULL,
    point_amount       INT             NOT NULL,
    description        VARCHAR(255)    NOT NULL,
    transaction_date   DATETIME        NOT NULL DEFAULT NOW(),
    order_no           VARCHAR(50),
    reference_id       VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user_login(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Index for better query performance
CREATE INDEX idx_point_history_user_date ON point_history(user_id, transaction_date DESC);
CREATE INDEX idx_point_history_order ON point_history(order_no);
CREATE INDEX idx_point_history_reference ON point_history(reference_id);
