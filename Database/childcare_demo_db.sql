-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 12, 2018 at 02:15 PM
-- Server version: 10.3.7-MariaDB-1:10.3.7+maria~bionic
-- PHP Version: 7.2.5-0ubuntu0.18.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `childcare_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `Addetti`
--

CREATE TABLE `Addetti` (
  `Persona_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Addetti`
--

INSERT INTO `Addetti` (`Persona_ID`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8);

-- --------------------------------------------------------

--
-- Table structure for table `Bambini`
--

CREATE TABLE `Bambini` (
  `Gruppo_FK` int(11) DEFAULT NULL,
  `Persona_ID` int(11) NOT NULL,
  `Pediatra_FK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Bambini`
--

INSERT INTO `Bambini` (`Gruppo_FK`, `Persona_ID`, `Pediatra_FK`) VALUES
(1, 22, 1),
(2, 23, 1),
(3, 24, 1),
(1, 25, 1),
(2, 26, 2),
(3, 27, 1),
(1, 28, 1),
(2, 29, 2),
(3, 30, 1),
(1, 31, 2),
(2, 32, 1),
(3, 33, 2),
(1, 34, 2),
(2, 35, 2),
(3, 36, 1),
(1, 37, 1),
(2, 38, 2),
(3, 39, 1),
(1, 40, 1),
(2, 41, 1),
(3, 42, 1),
(1, 43, 1),
(2, 44, 1),
(3, 45, 2),
(1, 46, 1),
(2, 47, 2),
(3, 48, 2),
(1, 49, 2),
(2, 50, 1),
(3, 51, 1),
(1, 52, 1),
(2, 53, 2),
(3, 54, 1),
(1, 55, 1),
(2, 56, 1),
(3, 57, 1),
(1, 58, 2),
(2, 59, 2),
(3, 60, 1),
(1, 61, 2);

-- --------------------------------------------------------

--
-- Table structure for table `Contatti`
--

CREATE TABLE `Contatti` (
  `Pediatra` char(1) NOT NULL,
  `ID` int(11) NOT NULL,
  `Cognome` varchar(25) NOT NULL,
  `Descrizione` varchar(250) DEFAULT NULL,
  `Indirizzo` varchar(75) NOT NULL,
  `Nome` varchar(25) NOT NULL,
  `telefoni` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Contatti`
--

INSERT INTO `Contatti` (`Pediatra`, `ID`, `Cognome`, `Descrizione`, `Indirizzo`, `Nome`, `telefoni`) VALUES
('1', 1, 'Lettiere', 'TEST2912493', 'via Katia Mazzi 42, Crotone KR, Italia', 'Pippo', '+395579675900\n'),
('1', 2, 'Pallino', 'TEST1570291', 'via Katia Mazzi 58, Cuneo CN, Italia', 'Jacopo', '+395007988928\n'),
('0', 3, 'Loggia', 'TEST5099768', 'via Pippo Trevisani 34, Trieste TS, Italia', 'Sara', '+392603722652\n+390121385455\n'),
('0', 4, 'Gaetano', 'TEST9891408', 'via Marcello Rossi 94, Fermo FM, Italia', 'Kevin', '+396353286290\n+398589983857\n'),
('0', 5, 'Enrico', 'TEST1237811', 'via Luigi Trevisani 189, Gorizia GO, Italia', 'Marco', '+393636628426\n'),
('0', 6, 'Sabbatini', 'TEST3693048', 'via Simone Pinto 26, Agrigento AG, Italia', 'Marzia', '+398871057894\n'),
('0', 7, 'Monaldo', 'TEST6017344', 'via Lucia Monaldo 123, Bergamo BG, Italia', 'Mirco', '+399450534039\n'),
('0', 8, 'Trevisani', 'TEST2665162', 'via Elia Mazzi 94, Rieti RI, Italia', 'Marcello', '+394985724339\n'),
('0', 9, 'Lettiere', 'TEST1217713', 'via Giacobbe Rossi 100, Messina ME, Italia', 'Marzia', '+390561416721\n+390981805837\n'),
('0', 10, 'Rossi', 'TEST3253547', 'via Davide Lettiere 79, Reggio Calabria RC, Italia', 'Luigi', '+392914433748\n'),
('0', 11, 'Loggia', 'TEST4037738', 'via Elia Bucco 123, Asti AT, Italia', 'Mirco', '+396877486777\n'),
('0', 12, 'Sabbatini', 'TEST8715112', 'via Alberto Lorenzo 125, Ravenna RA, Italia', 'Marzia', '+396595921530\n'),
('0', 13, 'Lettiere', 'TEST6548149', 'via Pinco Bucco 109, Modena MO, Italia', 'Antonio', '+392042230528\n'),
('0', 14, 'Lettiere', 'TEST9882316', 'via Edoardo Castiglione 105, Pescara PE, Italia', 'Lucia', '+398005290391\n'),
('0', 15, 'Sabbatini', 'TEST9345135', 'via Luigi Lorenzo 124, Udine UD, Italia', 'Giacobbe', '+399450534039\n'),
('0', 16, 'Pallino', 'TEST8300328', 'via Mirco Toscani 199, Ferrara FE, Italia', 'Pinco', '+399914130427\n'),
('0', 17, 'Toscani', 'TEST2057380', 'via Mirco Lucciano 134, Nuoro NU, Italia', 'Simone', '+390609083697\n'),
('0', 18, 'Sabbatini', 'TEST3016766', 'via Lucia Lorenzo 6, Pistoia PT, Italia', 'Maria', '+397267134196\n'),
('0', 19, 'Mazzi', 'TEST5574858', 'via Elia Bellucci 37, Udine UD, Italia', 'Lucia', '+398999822275\n'),
('0', 20, 'Ladislao', 'TEST4800441', 'via Elia Pallino 197, Isernia IS, Italia', 'Anna', '+395027556015\n+390609083697\n'),
('0', 21, 'Enrico', 'TEST8609509', 'via Maria Pancrazio 55, Milano MI, Italia', 'Davide', '+394483098401\n'),
('0', 22, 'Gaetano', 'TEST2172090', 'via Pinco Gaetano 138, Ancona AN, Italia', 'Elia', '+394200434925\n+399355749424\n'),
('0', 23, 'Bucco', 'TEST1744918', 'via Kevin Trevisani 111, Cuneo CN, Italia', 'Alberto', '+391781574811\n'),
('0', 24, 'Toscani', 'TEST4720519', 'via Antonio Rossi 120, Crotone KR, Italia', 'Antonio', '+395031435189\n'),
('0', 25, 'Trevisani', 'TEST3946687', 'via Marta Castiglione 180, Latina LT, Italia', 'Jacopo', '+392603722652\n'),
('0', 26, 'Sabbatini', 'TEST4896765', 'via Edoardo Bellucci 14, Ancona AN, Italia', 'Marta', '+399984509576\n'),
('0', 27, 'Ladislao', 'TEST4662214', 'via Luigi Ladislao 39, Palermo PA, Italia', 'Katia', '+393636628426\n'),
('0', 28, 'Lettiere', 'TEST5006467', 'via Pinco Pancrazio 152, Cosenza CS, Italia', 'Lucia', '+396353286290\n'),
('0', 29, 'Trevisani', 'TEST3292124', 'via Maria Pancrazio 6, Bari BA, Italia', 'Edoardo', '+398026026209\n'),
('0', 30, 'Fiorentino', 'TEST3849388', 'via Davide Mazzi 193, Lecco LC, Italia', 'Pinco', '+399355749424\n'),
('0', 31, 'Loggia', 'TEST9463216', 'via Alberto Toscani 15, Reggio Emilia RE, Italia', 'Katia', '+395970108432\n'),
('0', 32, 'Mazzi', 'TEST2229282', 'via Sara Bellucci 152, Cagliari CA, Italia', 'Anna', '+394985724339\n+395187274883\n'),
('0', 33, 'Bucco', 'TEST7796047', 'via Mirko Pancrazio 136, Genova GE, Italia', 'Maria', '+391783415856\n'),
('0', 34, 'Gaetano', 'TEST8233847', 'via Pino Rossi 91, Savona SV, Italia', 'Anna', '+390121385455\n'),
('0', 35, 'Trevisani', 'TEST2883797', 'via Sara Loggia 63, Milano MI, Italia', 'Maria', '+399049241385\n'),
('0', 36, 'Toscani', 'TEST3586187', 'via Marta Pallino 168, Como CO, Italia', 'Luigi', '+398026026209\n'),
('0', 37, 'Monaldo', 'TEST3925251', 'via Pino Pinto 106, Lodi LO, Italia', 'Antonio', '+390561416721\n'),
('0', 38, 'Castiglione', 'TEST2494743', 'via Mirco Mazzi 115, Sassari SS, Italia', 'Edoardo', '+398005290391\n'),
('0', 39, 'Fiorentino', 'TEST6973260', 'via Luigi Pancrazio 169, Perugia PG, Italia', 'Pino', '+394200434925\n'),
('0', 40, 'Gaetano', 'TEST7977029', 'via Maria Gaetano 41, Grosseto GR, Italia', 'Marta', '+398589983857\n'),
('0', 41, 'Enrico', 'TEST3175213', 'via Antonio Toscani 143, Catanzaro CZ, Italia', 'Giacobbe', '+395187274883\n'),
('0', 42, 'Belloni', 'TEST5475250', 'via Giacobbe Rossi 1, Brescia BS, Italia', 'Katia', '+398005290391\n+398026026209\n'),
('0', 43, 'Pancrazio', 'TEST3328717', 'via Edoardo Monaldo 130, Caltanissetta CL, Italia', 'Marco', '+398251938816\n'),
('0', 44, 'Mazzi', 'TEST5500297', 'via Pinco Pinto 177, Terni TR, Italia', 'Simone', '+390609083697\n'),
('0', 45, 'Lettiere', 'TEST8969903', 'via Alberto Monaldo 152, Alessandria AL, Italia', 'Marco', '+399450534039\n'),
('0', 46, 'Pinto', 'TEST2725689', 'via Antonio Trevisani 194, Firenze FI, Italia', 'Marcello', '+392914433748\n'),
('0', 47, 'Fiorentino', 'TEST6790399', 'via Mirko Castiglione 172, Caltanissetta CL, Italia', 'Luigi', '+396877486777\n'),
('0', 48, 'Loggia', 'TEST9229171', 'via Marcello Enrico 56, Gorizia GO, Italia', 'Davide', '+399914130427\n'),
('0', 49, 'Lucciano', 'TEST7690203', 'via Luigi Trevisani 121, Palermo PA, Italia', 'Elia', '+390561416721\n'),
('0', 50, 'Bellucci', 'TEST2342625', 'via Anna Lettiere 26, Massa Carrara MS, Italia', 'Antonio', '+398026026209\n+395187274883\n'),
('0', 51, 'Lorenzo', 'TEST7184226', 'via Mirko Lucciano 34, Fermo FM, Italia', 'Giacobbe', '+390176008329\n');

-- --------------------------------------------------------

--
-- Table structure for table `Diagnosi`
--

CREATE TABLE `Diagnosi` (
  `ID` int(11) NOT NULL,
  `Allergia` bit(1) DEFAULT NULL,
  `Persona_FK` int(11) NOT NULL,
  `ReazioneAvversa_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Diagnosi`
--

INSERT INTO `Diagnosi` (`ID`, `Allergia`, `Persona_FK`, `ReazioneAvversa_FK`) VALUES
(1, b'0', 2, 2),
(2, b'0', 21, 39),
(3, b'0', 28, 3),
(4, b'1', 34, 14),
(5, b'0', 57, 3);

-- --------------------------------------------------------

--
-- Table structure for table `Fornitori`
--

CREATE TABLE `Fornitori` (
  `ID` int(11) NOT NULL,
  `Email` varchar(30) DEFAULT NULL,
  `IBAN` varchar(27) DEFAULT NULL,
  `NumeroRegistroImprese` varchar(15) NOT NULL,
  `PartitaIVA` varchar(11) NOT NULL,
  `RagioneSociale` varchar(50) NOT NULL,
  `SedeLegale` varchar(125) NOT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `telefoni` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Fornitori`
--

INSERT INTO `Fornitori` (`ID`, `Email`, `IBAN`, `NumeroRegistroImprese`, `PartitaIVA`, `RagioneSociale`, `SedeLegale`, `fax`, `telefoni`) VALUES
(1, 'test196@childcare.com', 'ITA0000086358849', '81705271', 'TEST5191958', 'SimonePancrazioS.p.A.', 'via Marco Pallino 62, Udine UD, Italia', '+391271013097\n', '+398455149936\n+399517887949\n'),
(2, 'test863@childcare.com', 'ITA0000038390023', '58421238', 'TEST4472070', 'MariaMazziS.r.l.', 'via Simone Sabbatini 4, Varese VA, Italia', '+398822706929\n', NULL),
(3, 'test724@childcare.com', 'ITA0000051495086', '44989204', 'TEST1332910', 'PincoRossiOrg.', 'via Marta Pinto 92, Potenza PZ, Italia', NULL, '+390570389982\n+394450550524\n'),
(4, 'test142@childcare.com', 'ITA0000097357527', '99027786', 'TEST4395609', 'KatiaLorenzoS.r.l.', 'via Luigi Castiglione 128, Piacenza PC, Italia', '+394450550524\n', '+392224062517\n'),
(5, 'test972@childcare.com', 'ITA0000067138191', '57543675', 'TEST9488756', 'PincoMazziS.p.p.', 'via Katia Lettiere 89, Parma PR, Italia', NULL, '+394450550524\n'),
(6, 'test332@childcare.com', 'ITA0000074400643', '38176517', 'TEST2521620', 'KatiaLuccianoS.p.p.', 'via Elia Sabbatini 151, Cuneo CN, Italia', NULL, '+395827334671\n'),
(7, 'test709@childcare.com', 'ITA0000020325585', '96114593', 'TEST5940455', 'MariaPallinoInc.', 'via Federico Pallino 195, Asti AT, Italia', NULL, '+395827334671\n'),
(8, 'test216@childcare.com', 'ITA0000049975603', '12025043', 'TEST9044953', 'LuciaToscaniInc.', 'via Kevin Loggia 12, Brindisi BR, Italia', NULL, '+399341190292\n'),
(9, 'test144@childcare.com', 'ITA0000075200122', '40991301', 'TEST7222063', 'MirkoLorenzoS.p.p.', 'via Marco Lorenzo 13, Oristano OR, Italia', '+390637257056\n', '+391271013097\n+396529522306\n'),
(10, 'test758@childcare.com', 'ITA0000056506522', '40641970', 'TEST6775914', 'AnnaMonaldoOrg.', 'via Marta Rossi 112, Siena SI, Italia', '+392224062517\n', '+394450550524\n');

-- --------------------------------------------------------

--
-- Table structure for table `Genitori`
--

CREATE TABLE `Genitori` (
  `Persona_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Genitori`
--

INSERT INTO `Genitori` (`Persona_ID`) VALUES
(9),
(10),
(11),
(12),
(13),
(14),
(15),
(16),
(17),
(18),
(19),
(20),
(21);

-- --------------------------------------------------------

--
-- Table structure for table `Gite`
--

CREATE TABLE `Gite` (
  `ID` int(11) NOT NULL,
  `Costo` int(11) NOT NULL,
  `DataFine` date NOT NULL,
  `DataInizio` date NOT NULL,
  `Luogo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Gite`
--

INSERT INTO `Gite` (`ID`, `Costo`, `DataFine`, `DataInizio`, `Luogo`) VALUES
(1, 0, '2018-06-13', '2018-06-13', 'Politecnico di Milano'),
(2, 15, '2018-07-18', '2018-07-18', 'Parco delle Cornelle');

-- --------------------------------------------------------

--
-- Table structure for table `Gruppi`
--

CREATE TABLE `Gruppi` (
  `ID` int(11) NOT NULL,
  `sorvergliante_Persona_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Gruppi`
--

INSERT INTO `Gruppi` (`ID`, `sorvergliante_Persona_ID`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Table structure for table `Menu`
--

CREATE TABLE `Menu` (
  `ID` int(11) NOT NULL,
  `Nome` varchar(255) NOT NULL,
  `Ricorrenza` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Menu`
--

INSERT INTO `Menu` (`ID`, `Nome`, `Ricorrenza`) VALUES
(1, 'Menu Mediterraneo', 17),
(2, 'Pietanze di Mare', 10),
(3, 'Menu in Bianco', 36),
(4, 'Menu Pane e Prosciutto', 0);

-- --------------------------------------------------------

--
-- Table structure for table `MenuPasti`
--

CREATE TABLE `MenuPasti` (
  `Menu_FK` int(11) NOT NULL,
  `Pasto_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MenuPasti`
--

INSERT INTO `MenuPasti` (`Menu_FK`, `Pasto_FK`) VALUES
(1, 8),
(1, 9),
(1, 21),
(2, 6),
(2, 13),
(2, 20),
(3, 10),
(3, 11),
(4, 12),
(4, 18);

-- --------------------------------------------------------

--
-- Table structure for table `MezziDiTrasporto`
--

CREATE TABLE `MezziDiTrasporto` (
  `ID` int(11) NOT NULL,
  `Capienza` int(11) NOT NULL,
  `CostoOrario` int(11) NOT NULL,
  `NumeroIdentificativo` int(11) NOT NULL,
  `Targa` varchar(7) NOT NULL,
  `Fornitore_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MezziDiTrasporto`
--

INSERT INTO `MezziDiTrasporto` (`ID`, `Capienza`, `CostoOrario`, `NumeroIdentificativo`, `Targa`, `Fornitore_FK`) VALUES
(1, 102, 9, 9111908, 'AY408PN', 1),
(2, 81, 9, 10666089, 'TO427KV', 2),
(3, 241, 9, 8126863, 'OS883AF', 5),
(4, 238, 4, 9826951, 'CH500FU', 6),
(5, 211, 7, 4425431, 'XS355GJ', 8),
(6, 111, 8, 2335169, 'GV215FN', 8),
(7, 148, 12, 8457984, 'WF625HM', 1),
(8, 94, 5, 4622878, 'VS328KK', 5);

-- --------------------------------------------------------

--
-- Table structure for table `Pasti`
--

CREATE TABLE `Pasti` (
  `ID` int(11) NOT NULL,
  `Descrizione` varchar(250) DEFAULT NULL,
  `Nome` varchar(65) NOT NULL,
  `Fornitore_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Pasti`
--

INSERT INTO `Pasti` (`ID`, `Descrizione`, `Nome`, `Fornitore_FK`) VALUES
(1, 'Test -723587464', 'Pane', 2),
(2, 'Test -1111371642', 'Caviale', 2),
(3, 'Test 1293494365', 'Pasta in Bianco', 3),
(4, 'Test -357804105', 'Caviale', 8),
(5, 'Test -1522296871', 'Riso in bianco', 5),
(6, 'Test 2112022738', 'Caviale', 1),
(7, 'Test 564394194', 'Pasta in Bianco', 5),
(8, 'Test 1673427656', 'Prosciutto Crudo', 3),
(9, 'Test 1707321619', 'Pane', 10),
(10, 'Test 226411466', 'Pasta in Bianco', 5),
(11, 'Test 381272466', 'Riso in bianco', 2),
(12, 'Test 1527890431', 'Cavolfiori', 1),
(13, 'Test 1831740717', 'Scampi', 6),
(14, 'Test -27623470', 'Scampi', 7),
(15, 'Test -696917325', 'Scampi', 7),
(16, 'Test 1123364973', 'Pasto con Ragu', 1),
(17, 'Test -1575272214', 'Cotoletta', 2),
(18, 'Test -1379417423', 'Pasto con Ragu', 8),
(19, 'Test -1532418636', 'Pasta in Bianco', 5),
(20, 'Test -1035682736', 'Zuppa di Pesce', 6),
(21, 'Pietanza favolosa', 'Pasta con Ragù', 3);

-- --------------------------------------------------------

--
-- Table structure for table `Persone`
--

CREATE TABLE `Persone` (
  `ID` int(11) NOT NULL,
  `Cittadinanza` varchar(20) NOT NULL,
  `CodiceFiscale` varchar(16) NOT NULL,
  `Cognome` varchar(20) NOT NULL,
  `Comune` varchar(45) NOT NULL,
  `DataNascita` date NOT NULL,
  `Nome` varchar(20) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  `Residenza` varchar(50) NOT NULL,
  `Sesso` int(11) NOT NULL,
  `Stato` varchar(20) NOT NULL,
  `telefoni` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Persone`
--

INSERT INTO `Persone` (`ID`, `Cittadinanza`, `CodiceFiscale`, `Cognome`, `Comune`, `DataNascita`, `Nome`, `Provincia`, `Residenza`, `Sesso`, `Stato`, `telefoni`) VALUES
(1, 'Italiana', 'TEST4042690', 'Mazzi', 'Biella', '2001-12-22', 'Luigi', 'BI', 'via Antonio Pallino 120', 1, 'Italia', '+394081335250\n'),
(2, 'Italiana', 'TEST5198316', 'Monaldo', 'Pesaro Urbino', '1970-07-10', 'Sara', 'PU', 'via Maria Monaldo 88', 1, 'Italia', '+392252826429\n'),
(3, 'Italiana', 'TEST6871704', 'Belloni', 'Sassari', '1973-02-18', 'Pino', 'SS', 'via Pino Sabbatini 193', 2, 'Italia', '+394949718601\n'),
(4, 'Italiana', 'TEST8900353', 'Trevisani', 'Lodi', '1986-06-27', 'Marco', 'LO', 'via Mirko Lorenzo 74', 2, 'Italia', '+394990110139\n'),
(5, 'Italiana', 'TEST4846690', 'Bellucci', 'Napoli', '1978-01-18', 'Kevin', 'NA', 'via Marzia Gaetano 55', 2, 'Italia', '+399452363808\n'),
(6, 'Italiana', 'TEST4404958', 'Pinto', 'Forli Cesena', '1987-11-27', 'Marco', 'FC', 'via Jacopo Rossi 159', 2, 'Italia', '+390166956626\n'),
(7, 'Italiana', 'TEST2682253', 'Gaetano', 'Latina', '2005-06-24', 'Anna', 'LT', 'via Marta Gaetano 21', 0, 'Italia', '+399050298890\n+397000522610\n'),
(8, 'Italiana', 'TEST2810743', 'Toscani', 'Isernia', '1979-12-13', 'Maria', 'IS', 'via Mirco Trevisani 44', 0, 'Italia', '+390680509592\n'),
(9, 'Italiana', 'TEST9120628', 'Rossi', 'Macerata', '1992-06-11', 'Giacobbe', 'MC', 'via Simone Lettiere 98', 2, 'Italia', '+397686197401\n'),
(10, 'Italiana', 'TEST8488268', 'Belloni', 'Palermo', '1969-04-21', 'Anna', 'PA', 'via Elia Bellucci 180', 1, 'Italia', '+398371604250\n'),
(11, 'Italiana', 'TEST1794568', 'Pallino', 'Pescara', '2012-08-20', 'Davide', 'PE', 'via Luigi Bellucci 65', 0, 'Italia', '+390062589312\n'),
(12, 'Italiana', 'TEST4932400', 'Castiglione', 'Trapani', '2004-04-09', 'Antonio', 'TP', 'via Maria Rossi 168', 0, 'Italia', '+391109045079\n'),
(13, 'Italiana', 'TEST4361041', 'Bellucci', 'Alessandria', '2015-08-18', 'Elia', 'AL', 'via Giacobbe Ladislao 22', 0, 'Italia', '+392646645996\n'),
(14, 'Italiana', 'TEST6888667', 'Pancrazio', 'Reggio Calabria', '2001-08-20', 'Edoardo', 'RC', 'via Elia Castiglione 49', 2, 'Italia', '+398371604250\n'),
(15, 'Italiana', 'TEST7788009', 'Belloni', 'Mantova', '1970-02-28', 'Federico', 'MN', 'via Marta Trevisani 98', 2, 'Italia', '+391612821298\n'),
(16, 'Italiana', 'TEST3951198', 'Gaetano', 'Palermo', '2018-05-03', 'Edoardo', 'PA', 'via Maria Pinto 131', 1, 'Italia', '+395400566624\n'),
(17, 'Italiana', 'TEST7235081', 'Enrico', 'Caltanissetta', '1981-03-19', 'Mirko', 'CL', 'via Davide Pancrazio 41', 0, 'Italia', '+393259523914\n'),
(18, 'Italiana', 'TEST8156752', 'Castiglione', 'Enna', '1996-09-07', 'Marzia', 'EN', 'via Marcello Bucco 25', 1, 'Italia', '+393081386154\n'),
(19, 'Italiana', 'TEST4383024', 'Ladislao', 'Aosta', '2012-05-21', 'Pippo', 'AO', 'via Marzia Gaetano 131', 2, 'Italia', '+396574692480\n'),
(20, 'Italiana', 'TEST2162533', 'Monaldo', 'Gorizia', '2010-07-29', 'Marta', 'GO', 'via Pinco Lorenzo 120', 0, 'Italia', '+395216715369\n'),
(21, 'Italiana', 'TEST6269313', 'Loggia', 'Brindisi', '1970-09-20', 'Lucia', 'BR', 'via Luigi Bucco 80', 1, 'Italia', '+395812406634\n'),
(22, 'Italiana', 'TEST2922308', 'Rossi', 'Rieti', '1973-06-16', 'Sara', 'RI', 'via Giacobbe Fiorentino 151', 1, 'Italia', NULL),
(23, 'Italiana', 'TEST9771872', 'Castiglione', 'Prato', '2018-05-19', 'Katia', 'PO', 'via Antonio Enrico 38', 1, 'Italia', NULL),
(24, 'Italiana', 'TEST8609507', 'Monaldo', 'Cagliari', '1976-02-13', 'Alberto', 'CA', 'via Marzia Pallino 175', 2, 'Italia', NULL),
(25, 'Italiana', 'TEST2344187', 'Belloni', 'Trento', '1993-02-04', 'Sara', 'TN', 'via Marta Pinto 47', 1, 'Italia', NULL),
(26, 'Italiana', 'TEST8591922', 'Gaetano', 'Varese', '2008-03-04', 'Pinco', 'VA', 'via Mirco Lucciano 114', 2, 'Italia', NULL),
(27, 'Italiana', 'TEST7125439', 'Ladislao', 'Foggia', '1989-03-04', 'Federico', 'FG', 'via Marzia Pancrazio 15', 0, 'Italia', NULL),
(28, 'Italiana', 'TEST7678914', 'Monaldo', 'Pesaro Urbino', '1990-08-24', 'Marcello', 'PU', 'via Katia Sabbatini 130', 0, 'Italia', NULL),
(29, 'Italiana', 'TEST6929827', 'Monaldo', 'Matera', '2015-03-23', 'Katia', 'MT', 'via Marco Rossi 84', 1, 'Italia', NULL),
(30, 'Italiana', 'TEST4880702', 'Enrico', 'Latina', '1997-02-14', 'Mirco', 'LT', 'via Sara Pancrazio 139', 1, 'Italia', NULL),
(31, 'Italiana', 'TEST5629172', 'Pallino', 'Nuoro', '2017-02-18', 'Mirko', 'NU', 'via Marzia Lorenzo 25', 2, 'Italia', NULL),
(32, 'Italiana', 'TEST5943310', 'Enrico', 'Milano', '2013-12-25', 'Davide', 'MI', 'via Marzia Pancrazio 133', 0, 'Italia', NULL),
(33, 'Italiana', 'TEST8529666', 'Pancrazio', 'Cuneo', '1977-02-02', 'Mirko', 'CN', 'via Elia Fiorentino 151', 0, 'Italia', NULL),
(34, 'Italiana', 'TEST2044900', 'Mazzi', 'Foggia', '1996-04-13', 'Marco', 'FG', 'via Marco Trevisani 165', 1, 'Italia', NULL),
(35, 'Italiana', 'TEST9797850', 'Castiglione', 'Cremona', '1978-09-24', 'Simone', 'CR', 'via Lucia Loggia 146', 2, 'Italia', NULL),
(36, 'Italiana', 'TEST7160596', 'Mazzi', 'Pistoia', '2002-03-14', 'Elia', 'PT', 'via Marzia Rossi 196', 0, 'Italia', NULL),
(37, 'Italiana', 'TEST1561622', 'Enrico', 'Cosenza', '1972-04-29', 'Marco', 'CS', 'via Marco Sabbatini 23', 1, 'Italia', NULL),
(38, 'Italiana', 'TEST9456613', 'Belloni', 'Ragusa', '1983-03-30', 'Sara', 'RG', 'via Lucia Toscani 163', 1, 'Italia', NULL),
(39, 'Italiana', 'TEST9069873', 'Pallino', 'Arezzo', '2002-03-01', 'Mirco', 'AR', 'via Elia Mazzi 47', 0, 'Italia', NULL),
(40, 'Italiana', 'TEST5813966', 'Sabbatini', 'Foggia', '1979-01-18', 'Lucia', 'FG', 'via Pino Pancrazio 26', 2, 'Italia', NULL),
(41, 'Italiana', 'TEST9642867', 'Gaetano', 'Lecce', '1981-02-05', 'Marcello', 'LE', 'via Marcello Loggia 139', 2, 'Italia', NULL),
(42, 'Italiana', 'TEST7960361', 'Loggia', 'Fermo', '1968-08-16', 'Pinco', 'FM', 'via Jacopo Pallino 36', 0, 'Italia', NULL),
(43, 'Italiana', 'TEST5949182', 'Lettiere', 'Lecce', '2011-09-13', 'Pippo', 'LE', 'via Pino Monaldo 107', 2, 'Italia', NULL),
(44, 'Italiana', 'TEST4471046', 'Lettiere', 'Enna', '1993-02-04', 'Lucia', 'EN', 'via Giacobbe Trevisani 183', 2, 'Italia', NULL),
(45, 'Italiana', 'TEST8051152', 'Lorenzo', 'Reggio Calabria', '2006-10-13', 'Maria', 'RC', 'via Katia Ladislao 174', 0, 'Italia', NULL),
(46, 'Italiana', 'TEST4998504', 'Pinto', 'Brescia', '1982-08-01', 'Davide', 'BS', 'via Katia Rossi 44', 1, 'Italia', NULL),
(47, 'Italiana', 'TEST3264137', 'Pallino', 'Trieste', '1979-06-12', 'Marco', 'TS', 'via Marta Sabbatini 143', 0, 'Italia', NULL),
(48, 'Italiana', 'TEST9406257', 'Toscani', 'Campobasso', '1977-09-19', 'Marta', 'CB', 'via Federico Bucco 112', 0, 'Italia', '+394109425599\n'),
(49, 'Italiana', 'TEST8862903', 'Pallino', 'Ascoli Piceno', '2005-02-11', 'Luigi', 'AP', 'via Edoardo Gaetano 145', 1, 'Italia', NULL),
(50, 'Italiana', 'TEST3412861', 'Bellucci', 'Ferrara', '2011-07-30', 'Mirco', 'FE', 'via Edoardo Lucciano 186', 2, 'Italia', NULL),
(51, 'Italiana', 'TEST5749731', 'Pancrazio', 'Alessandria', '2015-05-24', 'Luigi', 'AL', 'via Marzia Fiorentino 160', 0, 'Italia', NULL),
(52, 'Italiana', 'TEST3813751', 'Fiorentino', 'Ancona', '1986-12-31', 'Marco', 'AN', 'via Davide Pallino 72', 2, 'Italia', NULL),
(53, 'Italiana', 'TEST7639873', 'Loggia', 'Biella', '1980-06-18', 'Kevin', 'BI', 'via Marta Fiorentino 39', 2, 'Italia', NULL),
(54, 'Italiana', 'TEST7640160', 'Rossi', 'Parma', '1973-07-09', 'Maria', 'PR', 'via Anna Loggia 96', 0, 'Italia', NULL),
(55, 'Italiana', 'TEST4084510', 'Belloni', 'Arezzo', '1971-02-15', 'Kevin', 'AR', 'via Sara Monaldo 83', 1, 'Italia', NULL),
(56, 'Italiana', 'TEST3006097', 'Sabbatini', 'La Spezia', '1982-11-09', 'Marco', 'SP', 'via Marcello Toscani 78', 0, 'Italia', NULL),
(57, 'Italiana', 'TEST5140015', 'Castiglione', 'Pistoia', '2017-03-21', 'Mirko', 'PT', 'via Sara Lettiere 77', 1, 'Italia', NULL),
(58, 'Italiana', 'TEST4800993', 'Toscani', 'Pescara', '2011-03-04', 'Pino', 'PE', 'via Federico Sabbatini 130', 0, 'Italia', NULL),
(59, 'Italiana', 'TEST5228390', 'Fiorentino', 'Bologna', '1998-12-12', 'Marcello', 'BO', 'via Maria Castiglione 50', 0, 'Italia', NULL),
(60, 'Italiana', 'TEST7300371', 'Lorenzo', 'Reggio Calabria', '1990-03-10', 'Pinco', 'RC', 'via Marta Pinto 144', 0, 'Italia', NULL),
(61, 'Italiana', 'TEST2714595', 'Sabbatini', 'Perugia', '2004-04-01', 'Edoardo', 'PG', 'via Kevin Toscani 184', 0, 'Italia', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `PianiViaggi`
--

CREATE TABLE `PianiViaggi` (
  `ID` int(11) NOT NULL,
  `Gruppo_FK` int(11) DEFAULT NULL,
  `Gita_FK` int(11) NOT NULL,
  `Mezzo_FK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PianiViaggi`
--

INSERT INTO `PianiViaggi` (`ID`, `Gruppo_FK`, `Gita_FK`, `Mezzo_FK`) VALUES
(1, 1, 1, 1),
(2, 3, 1, 1),
(3, 2, 1, 1),
(4, 1, 2, 3),
(5, 3, 2, 4),
(6, 2, 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `ReazioneAvversa_Pasto`
--

CREATE TABLE `ReazioneAvversa_Pasto` (
  `Pasto_FK` int(11) NOT NULL,
  `ReazioneAvversa_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ReazioneAvversa_Pasto`
--

INSERT INTO `ReazioneAvversa_Pasto` (`Pasto_FK`, `ReazioneAvversa_FK`) VALUES
(1, 3),
(1, 4),
(1, 5),
(1, 7),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(1, 23),
(1, 25),
(1, 26),
(1, 27),
(1, 29),
(1, 31),
(1, 35),
(1, 39),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 9),
(2, 10),
(2, 15),
(2, 16),
(2, 17),
(2, 20),
(2, 26),
(2, 27),
(2, 31),
(2, 32),
(2, 34),
(2, 36),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 6),
(3, 10),
(3, 11),
(3, 12),
(3, 13),
(3, 16),
(3, 18),
(3, 21),
(3, 23),
(3, 25),
(3, 26),
(3, 27),
(3, 35),
(3, 37),
(4, 5),
(4, 6),
(4, 7),
(4, 9),
(4, 13),
(4, 14),
(4, 22),
(4, 24),
(4, 25),
(4, 30),
(4, 31),
(4, 35),
(5, 1),
(5, 3),
(5, 4),
(5, 5),
(5, 7),
(5, 9),
(5, 10),
(5, 11),
(5, 12),
(5, 13),
(5, 14),
(5, 15),
(5, 16),
(5, 17),
(5, 18),
(5, 19),
(5, 20),
(5, 23),
(5, 31),
(5, 32),
(5, 34),
(5, 35),
(5, 36),
(5, 37),
(5, 39),
(6, 2),
(6, 3),
(6, 9),
(6, 15),
(6, 17),
(6, 19),
(6, 20),
(6, 23),
(6, 25),
(6, 29),
(6, 30),
(6, 35),
(6, 36),
(6, 38),
(7, 11),
(8, 1),
(8, 2),
(8, 3),
(8, 5),
(8, 8),
(8, 12),
(8, 13),
(8, 16),
(8, 17),
(8, 18),
(8, 21),
(8, 22),
(8, 25),
(8, 28),
(8, 30),
(8, 31),
(8, 32),
(8, 34),
(8, 37),
(9, 1),
(9, 6),
(9, 9),
(9, 19),
(9, 21),
(9, 22),
(9, 23),
(9, 25),
(9, 26),
(10, 2),
(10, 6),
(10, 7),
(10, 12),
(10, 13),
(10, 15),
(10, 19),
(10, 20),
(10, 23),
(10, 27),
(10, 34),
(10, 35),
(10, 37),
(11, 3),
(11, 9),
(11, 12),
(11, 18),
(11, 21),
(11, 24),
(11, 25),
(11, 27),
(11, 29),
(11, 32),
(11, 33),
(11, 34),
(11, 35),
(11, 38),
(12, 1),
(12, 2),
(12, 4),
(12, 5),
(12, 8),
(12, 13),
(12, 15),
(12, 20),
(12, 21),
(12, 22),
(12, 23),
(12, 24),
(12, 25),
(12, 26),
(12, 27),
(12, 29),
(12, 30),
(12, 31),
(12, 35),
(12, 36),
(12, 39),
(13, 1),
(13, 3),
(13, 5),
(13, 6),
(13, 7),
(13, 9),
(13, 10),
(13, 11),
(13, 12),
(13, 13),
(13, 14),
(13, 16),
(13, 19),
(13, 22),
(13, 24),
(13, 25),
(13, 26),
(13, 27),
(13, 28),
(13, 29),
(13, 30),
(13, 33),
(13, 38),
(14, 6),
(14, 9),
(14, 10),
(14, 13),
(14, 15),
(14, 17),
(14, 22),
(14, 23),
(14, 24),
(14, 26),
(14, 28),
(14, 29),
(14, 32),
(14, 37),
(15, 1),
(15, 2),
(15, 4),
(15, 5),
(15, 7),
(15, 9),
(15, 12),
(15, 17),
(15, 19),
(15, 20),
(15, 21),
(15, 28),
(15, 29),
(15, 30),
(15, 35),
(15, 36),
(15, 38),
(15, 39),
(16, 8),
(16, 18),
(16, 32),
(16, 33),
(16, 36),
(17, 9),
(17, 11),
(17, 12),
(17, 17),
(17, 22),
(17, 24),
(17, 29),
(17, 30),
(17, 33),
(17, 36),
(18, 22),
(18, 23),
(18, 28),
(19, 1),
(19, 5),
(19, 6),
(19, 9),
(19, 13),
(19, 14),
(19, 18),
(19, 20),
(19, 22),
(19, 23),
(19, 25),
(19, 29),
(19, 32),
(19, 33),
(19, 34),
(19, 35),
(19, 37),
(20, 1),
(20, 4),
(20, 5),
(20, 7),
(20, 8),
(20, 10),
(20, 11),
(20, 12),
(20, 13),
(20, 14),
(20, 15),
(20, 16),
(20, 17),
(20, 19),
(20, 21),
(20, 24),
(20, 29),
(20, 30),
(20, 31),
(20, 33),
(20, 34),
(20, 35),
(20, 36),
(20, 39),
(21, 3),
(21, 12),
(21, 34);

-- --------------------------------------------------------

--
-- Table structure for table `ReazioniAvverse`
--

CREATE TABLE `ReazioniAvverse` (
  `ID` int(11) NOT NULL,
  `Descrizione` varchar(250) DEFAULT NULL,
  `Nome` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ReazioniAvverse`
--

INSERT INTO `ReazioniAvverse` (`ID`, `Descrizione`, `Nome`) VALUES
(1, 'CROSS-REATTIVITÀ DOCUMENTATA: Patata, carota, polline di betulla', 'Mela'),
(2, 'CROSS-REATTIVITÀ DOCUMENTATA: Sedano, anice, mela, patata, segale, frumento, ananas, avocado, polline di betulla', 'Carota'),
(3, 'CROSS-REATTIVITÀ DOCUMENTATA: Frumento, segale, orzo, avena, granoturco, riso, polline di graminacee, corrispondenti pollini', 'Cereali'),
(4, 'CROSS-REATTIVITÀ DOCUMENTATA: Anguilla, sgombro, salmone, trota, tonno', 'Merluzzo'),
(5, 'CROSS-REATTIVITÀ DOCUMENTATA: Latte d\'asina, capra, di altri animali simili', 'Latte di mucca'),
(6, 'CROSS-REATTIVITÀ DOCUMENTATA: Albume, lisozima, tuorlo, ovoalbumina, ovomucoide', 'Uova'),
(7, 'CROSS-REATTIVITÀ DOCUMENTATA: Cipolla, asparago', 'Aglio'),
(8, 'CROSS-REATTIVITÀ DOCUMENTATA: Contaminazione da polline di composite', 'Miele'),
(9, 'CROSS-REATTIVITÀ DOCUMENTATA: Lenticchie, liquirizia, semi di soia, fagioli bianchi, noccioline americane, finocchio', 'Piselli'),
(10, 'CROSS-REATTIVITÀ DOCUMENTATA: Albicocca, prugna, banana, guava', 'Pesca'),
(11, 'CROSS-REATTIVITÀ DOCUMENTATA: Noccioline, noce, noce brasiliana', 'Noce americana'),
(12, 'CROSS-REATTIVITÀ DOCUMENTATA: Cereali, granoturco, polline di segale', 'Riso'),
(13, 'CROSS-REATTIVITÀ DOCUMENTATA: Granchio comune, aragosta, calamaro, gambero, acari', 'Gamberetto'),
(14, 'All\'interno della famiglia: cavolo (verza, capuccio, rapa, cruciferae fiore, broccolo di bruxelles), rapa, colza, ravizzone e loro olii', 'Brassicacea'),
(15, 'asteraceae - All\'interno della famiglia: camomilla, carciofo, cicoria, lattuga, girasole (semi ed olio) dragoncello e con i corrispondenti pollini', 'Compositae'),
(16, 'All\'interno della famiglia: zucchino, zucca, melone, anguria, cetriolo, e con il polline di Gramineae e con il pomodoro (fam. solanaceae)', 'Cucurbitace'),
(17, 'poaceae (fam. solanaceae) - All\'interno della famiglia: frumento, mais, segale, orzo, riso, avena, con il polline di Gramineae e con il pomodoro', 'Gramineae'),
(18, 'papilionaceae - All\'interno della famiglia: fagioli, soia, arachidi, piselli, lenticchie, liquerizia, gomme', 'Leguminosea'),
(19, 'All\'interno della famiglia: asparago, porro, cipolla, aglio, ecc.', 'Liliaceae'),
(20, 'All\'interno della famiglia: patata, melanzana, peperone, pomodoro e con le graminaceae', 'Solanaceae'),
(21, 'All\'interno della famiglia: limone, mandarino, pompelmo, arancia, cedro e con il vischio (fam. Loranthaceae)', 'Rutaceae'),
(22, 'All\'interno della famiglia: mandorle, mela, albicocca, pesca, susina, ciliegia, prugna, fragola e con il polline di betulla', 'Rosaceae'),
(23, 'apiaceae - All\'interno della famiglia: anice, carota, finocchio, sedano, prezzemolo e con il polline di artemisia', 'Umbellifera'),
(24, 'segale - Papaina, bromelina, e polline di betulla', 'Grano'),
(25, 'castagna, kiwi, avocado - Tra di loro,con il lattice e il ficus beniamina', 'Banana'),
(26, 'Melone e polline di Compositeae', 'Banana'),
(27, 'Lattuga, sedano, anice, mela, patata, segale, frumento, ananas, avocado, e polline di betulla', 'Carota'),
(28, 'Patata, carota, sedano, e con il polline di betulla', 'Mela'),
(29, 'Fra di loro (noce, noce americana, nocciola, mandorla) e con l\'arachide (fam. leguminoseae)', 'Semi e noci'),
(30, 'Carota, cumino, anice, finocchio, coriandolo, pepe, noce moscata, zenzero, cannella', 'Sedano'),
(31, 'Segale, semi di sesamo, kiwi, semi di papavero', 'Nocciole'),
(32, 'Fra di loro (latte di mucca, capra, ecc.)', 'Latte'),
(33, 'Singole proteine, ovoalbumina, ovomucoide, e con le piume ed il siero di volatili', 'Uova'),
(34, 'Fra di loro (carne di maiale, di bue, di coniglio, ecc.) e fra carne di bovino e latte', 'Carni'),
(35, 'All\'interno della famiglia: gambero, aragosta, granchio, calamaro ecc.', 'Crustacea'),
(36, 'Acari', 'Gasteropodi'),
(37, 'Tra di loro ( mitili, vongole, ostriche, ecc.)', 'Molluschi'),
(38, 'Tra di loro (merluzzo, sgombro, salmone, trota, tonno, ecc.)', 'Pesci'),
(39, 'Merluzzo', 'Surimi');

-- --------------------------------------------------------

--
-- Table structure for table `RegistroPresenze`
--

CREATE TABLE `RegistroPresenze` (
  `ID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Ora` smallint(6) NOT NULL,
  `Stato` int(11) NOT NULL,
  `TimeStamp` datetime(6) NOT NULL,
  `Bambino_FK` int(11) NOT NULL,
  `Gita_FK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `RegistroPresenze`
--

INSERT INTO `RegistroPresenze` (`ID`, `Date`, `Ora`, `Stato`, `TimeStamp`, `Bambino_FK`, `Gita_FK`) VALUES
(1, '2018-06-12', 11, 0, '2018-06-12 11:47:09.271000', 22, NULL),
(2, '2018-06-12', 11, 0, '2018-06-12 11:47:11.262000', 24, NULL),
(3, '2018-06-12', 11, 2, '2018-06-12 11:47:13.287000', 25, NULL),
(4, '2018-06-12', 11, 0, '2018-06-12 11:47:16.702000', 26, NULL),
(5, '2018-06-12', 11, 0, '2018-06-12 11:47:17.955000', 27, NULL),
(6, '2018-06-12', 11, 3, '2018-06-12 11:47:18.860000', 27, NULL),
(7, '2018-06-12', 11, 0, '2018-06-12 11:47:20.221000', 28, NULL),
(8, '2018-06-12', 11, 0, '2018-06-12 11:47:21.108000', 29, NULL),
(9, '2018-06-12', 11, 0, '2018-06-12 11:47:22.711000', 31, NULL),
(10, '2018-06-12', 11, 2, '2018-06-12 11:47:24.224000', 32, NULL),
(11, '2018-06-12', 11, 0, '2018-06-12 11:47:27.330000', 33, NULL),
(12, '2018-06-12', 11, 0, '2018-06-12 11:47:28.877000', 34, NULL),
(13, '2018-06-12', 11, 0, '2018-06-12 11:47:30.348000', 35, NULL),
(14, '2018-06-12', 11, 2, '2018-06-12 11:47:31.994000', 36, NULL),
(15, '2018-06-12', 11, 0, '2018-06-12 11:47:35.371000', 37, NULL),
(16, '2018-06-12', 11, 0, '2018-06-12 11:47:36.801000', 38, NULL),
(17, '2018-06-12', 11, 0, '2018-06-12 11:47:38.030000', 39, NULL),
(18, '2018-06-12', 11, 0, '2018-06-12 11:47:40.375000', 41, NULL),
(19, '2018-06-12', 11, 3, '2018-06-12 11:47:41.513000', 41, NULL),
(20, '2018-06-12', 11, 0, '2018-06-12 11:47:42.515000', 42, NULL),
(21, '2018-06-12', 11, 0, '2018-06-12 11:47:43.833000', 43, NULL),
(22, '2018-06-12', 11, 0, '2018-06-12 11:47:45.181000', 44, NULL),
(23, '2018-06-12', 11, 2, '2018-06-12 11:47:46.963000', 45, NULL),
(24, '2018-06-12', 11, 0, '2018-06-12 11:47:50.052000', 47, NULL),
(25, '2018-06-12', 11, 0, '2018-06-12 11:47:51.218000', 48, NULL),
(26, '2018-06-12', 11, 0, '2018-06-12 11:47:52.388000', 49, NULL),
(27, '2018-06-12', 11, 0, '2018-06-12 11:47:53.678000', 50, NULL),
(28, '2018-06-12', 11, 0, '2018-06-12 11:47:55.302000', 51, NULL),
(29, '2018-06-12', 11, 0, '2018-06-12 11:47:56.943000', 52, NULL),
(30, '2018-06-12', 11, 2, '2018-06-12 11:47:58.365000', 53, NULL),
(31, '2018-06-12', 11, 0, '2018-06-12 11:48:02.664000', 55, NULL),
(32, '2018-06-12', 11, 0, '2018-06-12 11:48:03.892000', 56, NULL),
(33, '2018-06-12', 11, 0, '2018-06-12 11:48:05.394000', 57, NULL),
(34, '2018-06-12', 11, 0, '2018-06-12 11:48:07.314000', 58, NULL),
(35, '2018-06-12', 11, 2, '2018-06-12 11:48:08.747000', 59, NULL),
(36, '2018-06-12', 11, 2, '2018-06-12 11:48:15.536000', 60, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Riferimenti`
--

CREATE TABLE `Riferimenti` (
  `Contatto_FK` int(11) NOT NULL,
  `Bambino_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Riferimenti`
--

INSERT INTO `Riferimenti` (`Contatto_FK`, `Bambino_FK`) VALUES
(3, 22),
(3, 54),
(3, 60),
(4, 22),
(4, 24),
(5, 23),
(5, 55),
(6, 23),
(6, 42),
(6, 47),
(6, 59),
(7, 24),
(7, 42),
(8, 25),
(8, 31),
(8, 43),
(8, 44),
(8, 51),
(9, 25),
(9, 37),
(9, 42),
(9, 53),
(10, 26),
(10, 42),
(11, 26),
(11, 48),
(11, 52),
(12, 27),
(12, 35),
(12, 61),
(13, 27),
(13, 41),
(13, 46),
(14, 28),
(15, 28),
(15, 57),
(16, 29),
(16, 61),
(17, 29),
(18, 30),
(18, 33),
(18, 58),
(19, 30),
(19, 35),
(19, 46),
(19, 60),
(20, 31),
(20, 59),
(21, 31),
(21, 32),
(21, 47),
(21, 57),
(22, 31),
(23, 32),
(23, 41),
(23, 45),
(23, 50),
(24, 33),
(25, 34),
(25, 37),
(26, 34),
(27, 36),
(28, 36),
(29, 36),
(29, 46),
(29, 52),
(29, 59),
(30, 36),
(31, 37),
(31, 40),
(31, 41),
(32, 37),
(32, 58),
(33, 38),
(33, 40),
(34, 38),
(35, 39),
(35, 45),
(35, 52),
(36, 39),
(37, 41),
(37, 49),
(37, 52),
(38, 43),
(38, 56),
(39, 44),
(40, 44),
(40, 46),
(41, 44),
(42, 48),
(43, 49),
(44, 50),
(45, 51),
(46, 53),
(47, 54),
(48, 55),
(48, 56),
(49, 56),
(50, 56),
(51, 59);

-- --------------------------------------------------------

--
-- Table structure for table `Tappe`
--

CREATE TABLE `Tappe` (
  `ID` int(11) NOT NULL,
  `Luogo` varchar(100) NOT NULL,
  `Gita_FK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Tappe`
--

INSERT INTO `Tappe` (`ID`, `Luogo`, `Gita_FK`) VALUES
(1, 'Poilitecnico di Milano (Cremona)', 1);

-- --------------------------------------------------------

--
-- Table structure for table `TutoriLegali`
--

CREATE TABLE `TutoriLegali` (
  `Bambino_FK` int(11) NOT NULL,
  `Genitore_FK` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `TutoriLegali`
--

INSERT INTO `TutoriLegali` (`Bambino_FK`, `Genitore_FK`) VALUES
(22, 9),
(22, 10),
(23, 11),
(23, 12),
(24, 13),
(24, 14),
(25, 15),
(25, 16),
(26, 9),
(27, 17),
(27, 18),
(28, 10),
(28, 16),
(29, 10),
(29, 11),
(30, 13),
(30, 14),
(31, 16),
(31, 19),
(32, 10),
(32, 12),
(33, 13),
(33, 14),
(34, 14),
(34, 18),
(35, 9),
(35, 16),
(36, 12),
(36, 16),
(37, 18),
(37, 19),
(38, 14),
(39, 13),
(39, 20),
(40, 9),
(40, 16),
(41, 12),
(41, 16),
(42, 9),
(42, 16),
(43, 16),
(43, 19),
(44, 16),
(44, 20),
(45, 9),
(45, 14),
(46, 12),
(46, 18),
(47, 14),
(48, 11),
(49, 19),
(49, 20),
(50, 13),
(51, 13),
(51, 15),
(52, 13),
(52, 21),
(53, 10),
(54, 11),
(54, 12),
(55, 13),
(55, 19),
(56, 17),
(57, 10),
(57, 15),
(58, 11),
(58, 16),
(59, 13),
(59, 14),
(60, 13),
(60, 15),
(61, 11),
(61, 21);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Addetti`
--
ALTER TABLE `Addetti`
  ADD PRIMARY KEY (`Persona_ID`);

--
-- Indexes for table `Bambini`
--
ALTER TABLE `Bambini`
  ADD PRIMARY KEY (`Persona_ID`),
  ADD KEY `FKr27p1o55k7o09ws6rl5adathl` (`Gruppo_FK`),
  ADD KEY `FKe7pjeupmuv8yclcj0p623osg2` (`Pediatra_FK`);

--
-- Indexes for table `Contatti`
--
ALTER TABLE `Contatti`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Diagnosi`
--
ALTER TABLE `Diagnosi`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKfxn5d5ysiwx457odakpwsq35h` (`Persona_FK`),
  ADD KEY `FKr45g3wla2kshgk7wqm8o9081k` (`ReazioneAvversa_FK`);

--
-- Indexes for table `Fornitori`
--
ALTER TABLE `Fornitori`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Genitori`
--
ALTER TABLE `Genitori`
  ADD PRIMARY KEY (`Persona_ID`);

--
-- Indexes for table `Gite`
--
ALTER TABLE `Gite`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Gruppi`
--
ALTER TABLE `Gruppi`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `UK_rk433h2p5k8unwseyk4reg8x2` (`sorvergliante_Persona_ID`);

--
-- Indexes for table `Menu`
--
ALTER TABLE `Menu`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `MenuPasti`
--
ALTER TABLE `MenuPasti`
  ADD PRIMARY KEY (`Menu_FK`,`Pasto_FK`),
  ADD KEY `FKjjha8qvspryvwiahf2lngo19c` (`Pasto_FK`);

--
-- Indexes for table `MezziDiTrasporto`
--
ALTER TABLE `MezziDiTrasporto`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK3jv9wt6at3sljjulsl6dn0x01` (`Fornitore_FK`);

--
-- Indexes for table `Pasti`
--
ALTER TABLE `Pasti`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKcg1xf1qgtngs32o14qwqkw1n1` (`Fornitore_FK`);

--
-- Indexes for table `Persone`
--
ALTER TABLE `Persone`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `PianiViaggi`
--
ALTER TABLE `PianiViaggi`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK71dtgkc4spfwesx2ypl05m9ll` (`Gita_FK`),
  ADD KEY `FK3m7gcyonyj0mgexfpdhhprfm9` (`Gruppo_FK`),
  ADD KEY `FK9rfl7aoe7vtugig0augpjmgve` (`Mezzo_FK`);

--
-- Indexes for table `ReazioneAvversa_Pasto`
--
ALTER TABLE `ReazioneAvversa_Pasto`
  ADD PRIMARY KEY (`Pasto_FK`,`ReazioneAvversa_FK`),
  ADD KEY `FK3wi3isl20r1doo5b4cret630v` (`ReazioneAvversa_FK`);

--
-- Indexes for table `ReazioniAvverse`
--
ALTER TABLE `ReazioniAvverse`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `RegistroPresenze`
--
ALTER TABLE `RegistroPresenze`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK938hgappvymqm3jkdbqcvrcsp` (`Bambino_FK`),
  ADD KEY `FKlx1kaxi95tik18ent90pym6tm` (`Gita_FK`);

--
-- Indexes for table `Riferimenti`
--
ALTER TABLE `Riferimenti`
  ADD PRIMARY KEY (`Contatto_FK`,`Bambino_FK`),
  ADD KEY `FKadxw8x4mee8f3bkpe8wunk1om` (`Bambino_FK`);

--
-- Indexes for table `Tappe`
--
ALTER TABLE `Tappe`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKlw8di7q61q8g9id7w4tmi2fw4` (`Gita_FK`);

--
-- Indexes for table `TutoriLegali`
--
ALTER TABLE `TutoriLegali`
  ADD PRIMARY KEY (`Bambino_FK`,`Genitore_FK`),
  ADD KEY `FK9s0inxdi4vothvn2w84wtkcoi` (`Genitore_FK`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Contatti`
--
ALTER TABLE `Contatti`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;
--
-- AUTO_INCREMENT for table `Diagnosi`
--
ALTER TABLE `Diagnosi`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `Fornitori`
--
ALTER TABLE `Fornitori`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `Gite`
--
ALTER TABLE `Gite`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `Menu`
--
ALTER TABLE `Menu`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `MezziDiTrasporto`
--
ALTER TABLE `MezziDiTrasporto`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `Pasti`
--
ALTER TABLE `Pasti`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `Persone`
--
ALTER TABLE `Persone`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
--
-- AUTO_INCREMENT for table `PianiViaggi`
--
ALTER TABLE `PianiViaggi`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `ReazioniAvverse`
--
ALTER TABLE `ReazioniAvverse`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT for table `RegistroPresenze`
--
ALTER TABLE `RegistroPresenze`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `Tappe`
--
ALTER TABLE `Tappe`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `Addetti`
--
ALTER TABLE `Addetti`
  ADD CONSTRAINT `FKqmnvqxxe3thw7ut4x77ckrj14` FOREIGN KEY (`Persona_ID`) REFERENCES `Persone` (`ID`);

--
-- Constraints for table `Bambini`
--
ALTER TABLE `Bambini`
  ADD CONSTRAINT `FK67q0oqu6irtafp0nea9l1b2v3` FOREIGN KEY (`Persona_ID`) REFERENCES `Persone` (`ID`),
  ADD CONSTRAINT `FKe7pjeupmuv8yclcj0p623osg2` FOREIGN KEY (`Pediatra_FK`) REFERENCES `Contatti` (`ID`),
  ADD CONSTRAINT `FKr27p1o55k7o09ws6rl5adathl` FOREIGN KEY (`Gruppo_FK`) REFERENCES `Gruppi` (`ID`);

--
-- Constraints for table `Diagnosi`
--
ALTER TABLE `Diagnosi`
  ADD CONSTRAINT `FKfxn5d5ysiwx457odakpwsq35h` FOREIGN KEY (`Persona_FK`) REFERENCES `Persone` (`ID`),
  ADD CONSTRAINT `FKr45g3wla2kshgk7wqm8o9081k` FOREIGN KEY (`ReazioneAvversa_FK`) REFERENCES `ReazioniAvverse` (`ID`);

--
-- Constraints for table `Genitori`
--
ALTER TABLE `Genitori`
  ADD CONSTRAINT `FKf1d7uycackgpax178scne3hw3` FOREIGN KEY (`Persona_ID`) REFERENCES `Persone` (`ID`);

--
-- Constraints for table `Gruppi`
--
ALTER TABLE `Gruppi`
  ADD CONSTRAINT `FKg9r3kujkk104h5cxnujjr9q08` FOREIGN KEY (`sorvergliante_Persona_ID`) REFERENCES `Addetti` (`Persona_ID`);

--
-- Constraints for table `MenuPasti`
--
ALTER TABLE `MenuPasti`
  ADD CONSTRAINT `FKjjha8qvspryvwiahf2lngo19c` FOREIGN KEY (`Pasto_FK`) REFERENCES `Pasti` (`ID`),
  ADD CONSTRAINT `FKk839dmt0qkflkqpplt8g0qsao` FOREIGN KEY (`Menu_FK`) REFERENCES `Menu` (`ID`);

--
-- Constraints for table `MezziDiTrasporto`
--
ALTER TABLE `MezziDiTrasporto`
  ADD CONSTRAINT `FK3jv9wt6at3sljjulsl6dn0x01` FOREIGN KEY (`Fornitore_FK`) REFERENCES `Fornitori` (`ID`);

--
-- Constraints for table `Pasti`
--
ALTER TABLE `Pasti`
  ADD CONSTRAINT `FKcg1xf1qgtngs32o14qwqkw1n1` FOREIGN KEY (`Fornitore_FK`) REFERENCES `Fornitori` (`ID`);

--
-- Constraints for table `PianiViaggi`
--
ALTER TABLE `PianiViaggi`
  ADD CONSTRAINT `FK3m7gcyonyj0mgexfpdhhprfm9` FOREIGN KEY (`Gruppo_FK`) REFERENCES `Gruppi` (`ID`),
  ADD CONSTRAINT `FK71dtgkc4spfwesx2ypl05m9ll` FOREIGN KEY (`Gita_FK`) REFERENCES `Gite` (`ID`),
  ADD CONSTRAINT `FK9rfl7aoe7vtugig0augpjmgve` FOREIGN KEY (`Mezzo_FK`) REFERENCES `MezziDiTrasporto` (`ID`);

--
-- Constraints for table `ReazioneAvversa_Pasto`
--
ALTER TABLE `ReazioneAvversa_Pasto`
  ADD CONSTRAINT `FK3wi3isl20r1doo5b4cret630v` FOREIGN KEY (`ReazioneAvversa_FK`) REFERENCES `ReazioniAvverse` (`ID`),
  ADD CONSTRAINT `FKc923t3ecnw1t4b1e4q738ph8m` FOREIGN KEY (`Pasto_FK`) REFERENCES `Pasti` (`ID`);

--
-- Constraints for table `RegistroPresenze`
--
ALTER TABLE `RegistroPresenze`
  ADD CONSTRAINT `FK938hgappvymqm3jkdbqcvrcsp` FOREIGN KEY (`Bambino_FK`) REFERENCES `Bambini` (`Persona_ID`),
  ADD CONSTRAINT `FKlx1kaxi95tik18ent90pym6tm` FOREIGN KEY (`Gita_FK`) REFERENCES `Gite` (`ID`);

--
-- Constraints for table `Riferimenti`
--
ALTER TABLE `Riferimenti`
  ADD CONSTRAINT `FK1f44hfcb5lqkh30oil1t5vo6k` FOREIGN KEY (`Contatto_FK`) REFERENCES `Contatti` (`ID`),
  ADD CONSTRAINT `FKadxw8x4mee8f3bkpe8wunk1om` FOREIGN KEY (`Bambino_FK`) REFERENCES `Bambini` (`Persona_ID`);

--
-- Constraints for table `Tappe`
--
ALTER TABLE `Tappe`
  ADD CONSTRAINT `FKlw8di7q61q8g9id7w4tmi2fw4` FOREIGN KEY (`Gita_FK`) REFERENCES `Gite` (`ID`);

--
-- Constraints for table `TutoriLegali`
--
ALTER TABLE `TutoriLegali`
  ADD CONSTRAINT `FK9s0inxdi4vothvn2w84wtkcoi` FOREIGN KEY (`Genitore_FK`) REFERENCES `Genitori` (`Persona_ID`),
  ADD CONSTRAINT `FKp5l9reo67jabp3xrpj4cuc1xl` FOREIGN KEY (`Bambino_FK`) REFERENCES `Bambini` (`Persona_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
