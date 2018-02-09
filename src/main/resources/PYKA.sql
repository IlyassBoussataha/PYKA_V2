CREATE TABLE profil(
	id INTEGER NOT NULL AUTO_INCREMENT,
	civilite VARCHAR(5) COLLATE utf8_unicode_ci NOT NULL,
	nom VARCHAR(30) COLLATE utf8_unicode_ci NOT NULL,
	prenom VARCHAR(30) COLLATE utf8_unicode_ci NOT NULL,
	datedenaissance DATE NULL,
	adresse VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
	codepostale INTEGER(11) NOT NULL,
	ville VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
	email VARCHAR(50) COLLATE utf8_unicode_ci UNIQUE NOT NULL,
	telephone VARCHAR(30) COLLATE utf8_unicode_ci UNIQUE NOT NULL,
	siren VARCHAR(20) COLLATE utf8_unicode_ci UNIQUE,
	rib VARCHAR(50) COLLATE utf8_unicode_ci,
	nom_entreprise VARCHAR(50) COLLATE utf8_unicode_ci,
	parcours VARCHAR(300) COLLATE utf8_unicode_ci DEFAULT NULL,
	dateentree DATE NULL,
	datesortie DATE DEFAULT NULL,
	date_exclusion DATE NULL,
	pieceid VARCHAR(300) DEFAULT NULL,
	pieceidverso VARCHAR(300) DEFAULT NULL,
	kbis VARCHAR(300) DEFAULT NULL,
	permis VARCHAR(300) DEFAULT NULL,
	photo VARCHAR(300) DEFAULT NULL,
	casier_judiciaire VARCHAR(300) DEFAULT NULL,
	vtc VARCHAR(300) DEFAULT NULL,
	nb_commandes int(11),
	last_commandes int(11) DEFAULT '0',
	active tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE login (
	id int(11) NOT NULL  AUTO_INCREMENT,
	profil_id int(11) NOT NULL,
	email varchar(50) COLLATE utf8_unicode_ci UNIQUE NOT NULL,
	password varchar(255) COLLATE utf8_unicode_ci NOT NULL,
	admin tinyint(1) NOT NULL DEFAULT '0',
	limited tinyint(1) NOT NULL DEFAULT '0',
	confirme tinyint(1) NOT NULL DEFAULT '0',
	valide tinyint(1) NOT NULL DEFAULT '0',
	exclu tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (id),
	FOREIGN KEY (profil_id) REFERENCES profil(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE login_roles (
  id int(11) NOT NULL AUTO_INCREMENT,
  login_id int(11) NOT NULL,
  roles varchar(40) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY login_groups_uk (login_id,roles),
  FOREIGN KEY (login_id) REFERENCES login (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE verificationtoken (
  id int(11) NOT NULL AUTO_INCREMENT,
  token VARCHAR(255) COLLATE utf8_unicode_ci,
  expiryDate DATE,
  login_id int(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (login_id) REFERENCES login(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE creneaux (
  id int(11) NOT NULL AUTO_INCREMENT,
  heure_debut TIME NOT NULL,
  heure_fin TIME NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE disponibilite (
  id int(11) NOT NULL AUTO_INCREMENT,
  laveur_id int(11),
  date DATE,
  creneau int(11) NOT NULL,
  attribue tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  FOREIGN KEY (laveur_id) REFERENCES profil(id),
  FOREIGN KEY (creneau) REFERENCES creneaux(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE commande (
  id int(11) NOT NULL AUTO_INCREMENT,
  client_id int(11) NOT NULL,
  laveur_id int(11),
  date DATE,
  creneau int(11) NOT NULL,
  vehicule VARCHAR(50) COLLATE utf8_unicode_ci NOT NULL,
  type_lavage VARCHAR(50) COLLATE utf8_unicode_ci NOT NULL,
  adresse VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  mode_paiement VARCHAR(50) COLLATE utf8_unicode_ci NOT NULL,
  montant FLOAT,
  nouvelle tinyint(1) NOT NULL DEFAULT '0',
  assigner tinyint(1) NOT NULL DEFAULT '0',
  effectuer tinyint(1) NOT NULL DEFAULT '0',
  annuler tinyint(1) NOT NULL DEFAULT '0',
  motif_annuler VARCHAR(100) COLLATE utf8_unicode_ci,
  PRIMARY KEY (id),
  FOREIGN KEY (client_id) REFERENCES profil(id),
  FOREIGN KEY (laveur_id) REFERENCES profil(id),
  FOREIGN KEY (creneau) REFERENCES creneaux(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
