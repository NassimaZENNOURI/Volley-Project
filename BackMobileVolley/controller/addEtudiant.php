<?php

namespace controller;
use classes\Etudiant;
use service\EtudiantService;

include_once '../racine.php';
include_once RACINE.'/service/EtudiantService.php';
extract($_GET);
$es = new EtudiantService();
$es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe));
header("location:../index.php"); 