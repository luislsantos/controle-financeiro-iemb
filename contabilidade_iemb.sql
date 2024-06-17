-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 17/06/2024 às 01:54
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `contabilidade_iemb`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `periodo_prestacao`
--

CREATE TABLE `periodo_prestacao` (
  `ano` int(11) NOT NULL,
  `semestre` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `registro_contabil`
--

CREATE TABLE `registro_contabil` (
  `ano_prestacao` int(11) NOT NULL,
  `data` date DEFAULT NULL,
  `semestre_prestacao` int(11) NOT NULL,
  `valor` double NOT NULL,
  `id` bigint(20) NOT NULL,
  `cpf_cnpj` varchar(255) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `num_nota_fiscal` varchar(255) DEFAULT NULL,
  `origem_ou_destinacao` varchar(255) DEFAULT NULL,
  `path_scan_nota_fiscal` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `path_comprovantes` varchar(255) DEFAULT NULL,
  `path_fotos` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `registro_contabil_seq`
--

CREATE TABLE `registro_contabil_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `registro_contabil_seq`
--

INSERT INTO `registro_contabil_seq` (`next_val`) VALUES
(1);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `periodo_prestacao`
--
ALTER TABLE `periodo_prestacao`
  ADD PRIMARY KEY (`ano`,`semestre`);

--
-- Índices de tabela `registro_contabil`
--
ALTER TABLE `registro_contabil`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
