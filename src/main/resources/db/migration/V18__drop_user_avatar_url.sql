-- O avatar passou a ser gerado dinamicamente a partir do nome do usuário (DiceBear),
-- então a coluna armazenada não é mais usada.
ALTER TABLE users
    DROP COLUMN avatar_url;
