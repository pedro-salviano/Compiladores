CREATE TABLE pessoa(
    pessoaId SERIAL PRIMARY KEY,
    nomePessoa varchar[60],
    cpf varchar[14],
    altura decimal[3,2],
    peso decimal[5,2]
);

INSERTO INTO PESSOA VALUES
(pessoa1, 123.456.789-00, 1.70, 60.00),
(pessoa2, 001.234.567-89, 1.65, 50.01);

INSERTO INTO PESSOA VALUES
(pessoa3, 111.222.333-44, 1.76, 65.24);

CREATE TABLE curso(
    cod SERIAL PRIMARY KEY,
    nomeCurso varchar[30],
    professorId INT
);

INSERT INTO curso (nomeCurso, professorId)
VALUES 
(compiladores, 1),
(engenharia de software 1, 4);

DROP TABLE curso;