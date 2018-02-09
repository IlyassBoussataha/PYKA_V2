-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Client :  127.0.0.1
-- Généré le :  Dim 07 Janvier 2018 à 16:44
-- Version du serveur :  5.7.14
-- Version de PHP :  5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `pyka`
--

-- --------------------------------------------------------

--
-- Structure de la table `chat`
--

CREATE TABLE `chat` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `chat`
--

INSERT INTO `chat` (`id`) VALUES
(1),
(2),
(3),
(4);

-- --------------------------------------------------------

--
-- Structure de la table `chat_line`
--

CREATE TABLE `chat_line` (
  `id` bigint(20) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `chat_id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `message` longtext COLLATE utf8_unicode_ci,
  `vu` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `chat_line`
--

INSERT INTO `chat_line` (`id`, `sender_id`, `receiver_id`, `chat_id`, `date`, `message`, `vu`) VALUES
(1, 5, 3, 1, '2017-11-08', '                                        test message        ', 1),
(3, 5, 6, 3, '2017-11-08', 'test abd', 1),
(4, 1, 5, 4, '2017-11-09', 'test de message à l\'admin', 1),
(5, 1, 5, 4, '2017-11-09', 'aweeeee', 0),
(6, 5, 1, 4, '2017-11-09', 'Thankkkks', 1);

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `laveur_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `creneau` int(11) NOT NULL,
  `vehicule` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `type_lavage` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `adresse` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `mode_paiement` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `montant` float DEFAULT NULL,
  `nouvelle` tinyint(1) NOT NULL DEFAULT '0',
  `assigner` tinyint(1) NOT NULL DEFAULT '0',
  `effectuer` tinyint(1) NOT NULL DEFAULT '0',
  `annuler` tinyint(1) NOT NULL DEFAULT '0',
  `motif_annuler` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `commande`
--

INSERT INTO `commande` (`id`, `client_id`, `laveur_id`, `date`, `creneau`, `vehicule`, `type_lavage`, `adresse`, `mode_paiement`, `montant`, `nouvelle`, `assigner`, `effectuer`, `annuler`, `motif_annuler`) VALUES
(1, 1, 3, '2017-11-03', 2, 'Berline', 'Interieur + Exterieur', '37', 'Cash', NULL, 0, 0, 1, 0, NULL),
(2, 1, NULL, '2017-11-16', 2, 'Berline', 'Exterieur', '37', 'CB', NULL, 1, 0, 0, 0, NULL),
(3, 2, 3, '2017-11-02', 2, 'Break', 'Exterieur', '37 avenue de la marne', 'CB', NULL, 0, 1, 1, 0, NULL),
(4, 2, NULL, '2017-06-12', 2, 'Citadine', 'Interieur + Exterieur', '37', 'CB', NULL, 1, 0, 0, 0, NULL),
(5, 1, 3, '2017-11-09', 2, 'SUV', 'Interieur', 'surenses', 'Cash', NULL, 0, 0, 1, 0, NULL),
(6, 1, 3, '2017-11-10', 2, 'Break', 'Exterieur', '999999', 'Cash', NULL, 0, 1, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `creneaux`
--

CREATE TABLE `creneaux` (
  `id` int(11) NOT NULL,
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `creneaux`
--

INSERT INTO `creneaux` (`id`, `heure_debut`, `heure_fin`) VALUES
(2, '10:00:00', '14:00:00'),
(3, '09:00:00', '10:00:00');

-- --------------------------------------------------------

--
-- Structure de la table `disponibilite`
--

CREATE TABLE `disponibilite` (
  `id` int(11) NOT NULL,
  `laveur_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `creneau` int(11) NOT NULL,
  `attribue` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `disponibilite`
--

INSERT INTO `disponibilite` (`id`, `laveur_id`, `date`, `creneau`, `attribue`) VALUES
(1, 3, '2017-11-22', 2, 1),
(2, 3, '2017-11-24', 2, 1),
(3, 3, '2017-11-24', 3, 1);

-- --------------------------------------------------------

--
-- Structure de la table `login`
--

CREATE TABLE `login` (
  `id` int(11) NOT NULL,
  `profil_id` int(11) NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `limited` tinyint(1) NOT NULL DEFAULT '0',
  `confirme` tinyint(1) NOT NULL DEFAULT '0',
  `valide` tinyint(1) NOT NULL DEFAULT '0',
  `exclu` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `login`
--

INSERT INTO `login` (`id`, `profil_id`, `email`, `password`, `admin`, `limited`, `confirme`, `valide`, `exclu`) VALUES
(1, 1, 'fdgfdg@gmail.com', '$2a$10$1f7RrShHHjZ8cHgNsez0A.U10Zzx8w3bZiCnDyO0rzrTOOq2iPMo6', 0, 0, 1, 1, 0),
(2, 2, 'fdgfdgf@gmail.com', '$2a$10$Y92JdNu.4vPZx3.awkh0GuBcxEmmWYHDk1jXZ7mKFCXh1obJxNftS', 0, 0, 1, 1, 0),
(3, 3, 'fdgfdgff@gmail.com', '$2a$10$mB7qnJIwGiRDdBsr3s0sIuygZoaKNaFWkO7MDvcbyWzUxaTWAbxmO', 0, 0, 1, 1, 0),
(4, 5, 'ilyass.bou@gmail.com', '$2a$10$nrS/7JP0k1LVXqppNuDcROVZvvIMpQkN99.hRJtLDzFj1/AGXF/q2', 1, 0, 1, 1, 0),
(5, 6, 'abd@gmail.com', '$2a$10$tEgrApIsNhg550GBOPydeuZsrO.DSBk4PkKuIGqE9gRBqmhGwdM4y', 0, 0, 1, 1, 0);

-- --------------------------------------------------------

--
-- Structure de la table `login_roles`
--

CREATE TABLE `login_roles` (
  `id` int(11) NOT NULL,
  `login_id` int(11) NOT NULL,
  `roles` varchar(40) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `login_roles`
--

INSERT INTO `login_roles` (`id`, `login_id`, `roles`) VALUES
(1, 1, 'CLIENT'),
(2, 2, 'CLIENT'),
(3, 3, 'LAVEUR'),
(4, 4, 'ADMIN'),
(5, 4, 'SUPER_ADMIN'),
(6, 5, 'LAVEUR');

-- --------------------------------------------------------

--
-- Structure de la table `profil`
--

CREATE TABLE `profil` (
  `id` int(11) NOT NULL,
  `civilite` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `nom` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `prenom` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `datedenaissance` date DEFAULT NULL,
  `adresse` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `codepostale` int(11) NOT NULL,
  `ville` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `telephone` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `siren` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rib` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nom_entreprise` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parcours` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dateentree` date DEFAULT NULL,
  `datesortie` date DEFAULT NULL,
  `date_exclusion` date DEFAULT NULL,
  `pieceid` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pieceidverso` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `kbis` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `permis` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `photo` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `casier_judiciaire` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vtc` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nb_commandes` int(11) DEFAULT NULL,
  `last_commandes` int(11) DEFAULT '0',
  `active` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `profil`
--

INSERT INTO `profil` (`id`, `civilite`, `nom`, `prenom`, `datedenaissance`, `adresse`, `codepostale`, `ville`, `email`, `telephone`, `siren`, `rib`, `nom_entreprise`, `parcours`, `dateentree`, `datesortie`, `date_exclusion`, `pieceid`, `pieceidverso`, `kbis`, `permis`, `photo`, `casier_judiciaire`, `vtc`, `nb_commandes`, `last_commandes`, `active`) VALUES
(1, 'Mr', 'thgtrh', 'fdgfdg', NULL, 'fdgfdg', 75630, 'nancy', 'fdgfdg@gmail.com', '0764350022', NULL, NULL, 'fdgfdgsociete', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cfb3378971f01a979f83b57067b27099.jpg', NULL, 'cfb3378971f01a979f83b57067b27099.jpg', NULL, NULL, 0),
(2, 'Mr', 'sdf', 'fdsgdsfs', '2017-10-27', 'fdsg', 75000, 'Paris', 'fdgfdgf@gmail.com', '0624620426', '552100554', NULL, '', NULL, '2017-10-27', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 1),
(3, 'Mme', 'gfhgfh', 'gfhgfhgfh', '2017-10-17', 'ghjghj', 54000, 'Nancy', 'fdgfdgff@gmail.com', '0620455859', '379158322', 'FR76 3000 3041 2300 0509 4405 264', NULL, NULL, NULL, NULL, NULL, 'e60181aae984a57cd30f2dfeb653eb19.jpg', NULL, 'f8459d42677c55eb4059eed0fcc6e96b.jpg', 'e60181aae984a57cd30f2dfeb653eb19.jpg', 'e60181aae984a57cd30f2dfeb653eb19.jpg', 'c36a11a6d0294bba5f142d6294d233fc.jpg', NULL, NULL, 1, 0),
(5, 'Mme', 'gfdgfd', 'gfdsgfds', '2017-09-30', '02 SQUARE DU LIMOUSIN', 80000, 'Amiens', 'ilyass.bou@gmail.com', '0607080905', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cd8437f8dd218414f86453c27994f144.jpg', NULL, NULL, NULL, NULL, 0),
(6, 'Mme', 'laveur', 'test', '2017-11-06', '99 adr', 75011, 'Paris', 'abd@gmail.com', '0764350023', '519464267', 'FR76 3000 3031 2500 0503 0857 104', NULL, NULL, '2017-11-06', NULL, NULL, 'a1d12da42d4302e53d510954344ad164.jpg', NULL, 'c36a11a6d0294bba5f142d6294d233fc.jpg', 'c36a11a6d0294bba5f142d6294d233fc.jpg', 'a1d12da42d4302e53d510954344ad164.jpg', 'a1d12da42d4302e53d510954344ad164.jpg', NULL, NULL, 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `verificationtoken`
--

CREATE TABLE `verificationtoken` (
  `id` int(11) NOT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `expiryDate` date DEFAULT NULL,
  `login_id` int(11) NOT NULL,
  `expiry_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `verificationtoken`
--

INSERT INTO `verificationtoken` (`id`, `token`, `expiryDate`, `login_id`, `expiry_date`) VALUES
(1, '7caee1de-0039-4b9a-ad9a-ba38a6541884', NULL, 4, '2017-10-28'),
(2, 'b7d22d6e-147f-446f-b238-56bb402453ca', NULL, 5, '2017-11-07');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `chat_line`
--
ALTER TABLE `chat_line`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4ic1ofe4m9c4nd7pimpgg08tx` (`chat_id`),
  ADD KEY `FKbro2r0jbs1oqb6g4y8mg169rv` (`receiver_id`),
  ADD KEY `FKhm581xnwd1e91562ca10vrv3l` (`sender_id`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqsd4gq12ja9l53w2l1v1040v4` (`client_id`),
  ADD KEY `FKp0uyretcglfjryc4iyv6305a4` (`creneau`),
  ADD KEY `FK3fg3rpgguhmoikuyesy6w0xja` (`laveur_id`);

--
-- Index pour la table `creneaux`
--
ALTER TABLE `creneaux`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `disponibilite`
--
ALTER TABLE `disponibilite`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKo6iu3tfwlkty07i4tkmtwp5p4` (`creneau`),
  ADD KEY `FKmr1m0s4gbu3tnfrekmrmsvodj` (`laveur_id`);

--
-- Index pour la table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `FKio4yeeec2niega2ow9r7scj6h` (`profil_id`);

--
-- Index pour la table `login_roles`
--
ALTER TABLE `login_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login_groups_uk` (`login_id`,`roles`);

--
-- Index pour la table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `telephone` (`telephone`),
  ADD UNIQUE KEY `siren` (`siren`);

--
-- Index pour la table `verificationtoken`
--
ALTER TABLE `verificationtoken`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaxm7a2dvqmxbfhja7i1snkq06` (`login_id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `chat`
--
ALTER TABLE `chat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT pour la table `chat_line`
--
ALTER TABLE `chat_line`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `creneaux`
--
ALTER TABLE `creneaux`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT pour la table `disponibilite`
--
ALTER TABLE `disponibilite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT pour la table `login`
--
ALTER TABLE `login`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT pour la table `login_roles`
--
ALTER TABLE `login_roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `profil`
--
ALTER TABLE `profil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `verificationtoken`
--
ALTER TABLE `verificationtoken`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `chat_line`
--
ALTER TABLE `chat_line`
  ADD CONSTRAINT `FK4ic1ofe4m9c4nd7pimpgg08tx` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`),
  ADD CONSTRAINT `FKbro2r0jbs1oqb6g4y8mg169rv` FOREIGN KEY (`receiver_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `FKhm581xnwd1e91562ca10vrv3l` FOREIGN KEY (`sender_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `chat_line_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `chat_line_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `chat_line_ibfk_3` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`);

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `FK3fg3rpgguhmoikuyesy6w0xja` FOREIGN KEY (`laveur_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `FKp0uyretcglfjryc4iyv6305a4` FOREIGN KEY (`creneau`) REFERENCES `creneaux` (`id`),
  ADD CONSTRAINT `FKqsd4gq12ja9l53w2l1v1040v4` FOREIGN KEY (`client_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `commande_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `commande_ibfk_2` FOREIGN KEY (`laveur_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `commande_ibfk_3` FOREIGN KEY (`creneau`) REFERENCES `creneaux` (`id`);

--
-- Contraintes pour la table `disponibilite`
--
ALTER TABLE `disponibilite`
  ADD CONSTRAINT `FKmr1m0s4gbu3tnfrekmrmsvodj` FOREIGN KEY (`laveur_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `FKo6iu3tfwlkty07i4tkmtwp5p4` FOREIGN KEY (`creneau`) REFERENCES `creneaux` (`id`),
  ADD CONSTRAINT `disponibilite_ibfk_1` FOREIGN KEY (`laveur_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `disponibilite_ibfk_2` FOREIGN KEY (`creneau`) REFERENCES `creneaux` (`id`);

--
-- Contraintes pour la table `login`
--
ALTER TABLE `login`
  ADD CONSTRAINT `FKio4yeeec2niega2ow9r7scj6h` FOREIGN KEY (`profil_id`) REFERENCES `profil` (`id`),
  ADD CONSTRAINT `login_ibfk_1` FOREIGN KEY (`profil_id`) REFERENCES `profil` (`id`);

--
-- Contraintes pour la table `login_roles`
--
ALTER TABLE `login_roles`
  ADD CONSTRAINT `FK6urcevowvshout8aa2ar87k3v` FOREIGN KEY (`login_id`) REFERENCES `login` (`id`),
  ADD CONSTRAINT `login_roles_ibfk_1` FOREIGN KEY (`login_id`) REFERENCES `login` (`id`);

--
-- Contraintes pour la table `verificationtoken`
--
ALTER TABLE `verificationtoken`
  ADD CONSTRAINT `FKaxm7a2dvqmxbfhja7i1snkq06` FOREIGN KEY (`login_id`) REFERENCES `login` (`id`),
  ADD CONSTRAINT `verificationtoken_ibfk_1` FOREIGN KEY (`login_id`) REFERENCES `login` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
