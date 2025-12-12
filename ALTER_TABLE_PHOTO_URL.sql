-- Script para alterar a coluna photo_url para LONGTEXT
-- Execute este script no MySQL se a coluna não atualizar automaticamente

USE educapro;

ALTER TABLE users MODIFY COLUMN photo_url LONGTEXT;

