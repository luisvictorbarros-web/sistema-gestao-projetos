CREATE DATABASE IF NOT EXISTS gestao_projetos;
USE gestao_projetos;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    cargo VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('administrador', 'gerente', 'colaborador') NOT NULL
);

CREATE TABLE projetos (
    id_projeto INT AUTO_INCREMENT PRIMARY KEY,
    nome_projeto VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_inicio DATE NOT NULL,
    data_termino_prevista DATE NOT NULL,
    status ENUM('planejado', 'em andamento', 'concluido', 'cancelado') NOT NULL DEFAULT 'planejado',
    id_gerente INT NOT NULL,
    FOREIGN KEY (id_gerente) REFERENCES usuarios(id_usuario)
);

CREATE TABLE equipes (
    id_equipe INT AUTO_INCREMENT PRIMARY KEY,
    nome_equipe VARCHAR(100) NOT NULL,
    descricao TEXT
);

CREATE TABLE equipe_usuario (
    id_equipe INT NOT NULL,
    id_usuario INT NOT NULL,
    PRIMARY KEY (id_equipe, id_usuario),
    FOREIGN KEY (id_equipe) REFERENCES equipes(id_equipe) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE projeto_equipe (
    id_projeto INT NOT NULL,
    id_equipe INT NOT NULL,
    PRIMARY KEY (id_projeto, id_equipe),
    FOREIGN KEY (id_projeto) REFERENCES projetos(id_projeto) ON DELETE CASCADE,
    FOREIGN KEY (id_equipe) REFERENCES equipes(id_equipe) ON DELETE CASCADE
);

CREATE TABLE tarefas (
    id_tarefa INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status ENUM('pendente', 'em execução', 'concluída') NOT NULL DEFAULT 'pendente',
    data_inicio_prevista DATE NOT NULL,
    data_fim_prevista DATE NOT NULL,
    data_inicio_real DATE NULL,
    data_fim_real DATE NULL,
    id_projeto INT NOT NULL,
    id_responsavel INT NOT NULL,
    FOREIGN KEY (id_projeto) REFERENCES projetos(id_projeto) ON DELETE CASCADE,
    FOREIGN KEY (id_responsavel) REFERENCES usuarios(id_usuario)
);

INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil) 
VALUES ('Administrador Teste', '123.456.789-00', 'admin@teste.com', 'Diretor', 'admin', '1234', 'administrador');
