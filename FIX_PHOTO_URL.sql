-- Script para corrigir a coluna photo_url
-- Execute este comando no MySQL Workbench ou no terminal MySQL

USE educapro;

-- Alterar a coluna photo_url para LONGTEXT (suporta até 4GB)
ALTER TABLE users MODIFY COLUMN photo_url LONGTEXT;

-- Verificar se foi alterado corretamente
DESCRIBE users;

