<?php

use classes\Etudiant;
use service\EtudiantService;

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';
    update();
}

function update() {
    extract($_POST);

    // Vérifier si tous les paramètres sont passés
    if (isset($id) && isset($nom) && isset($prenom) && isset($ville)) {
        $es = new EtudiantService();
        $etudiant = new Etudiant($id, $nom, $prenom, $ville, $sexe);
        $es->update($etudiant); // Appelle la méthode update

        // Chargement de la liste des étudiants sous format JSON
        header('Content-type: application/json');
        echo json_encode($es->findAllApi());
    } else {
        http_response_code(400); // Bad request
        echo json_encode(['message' => 'Paramètres manquants']);
    }
}
